/**
 * 
 */
package com.flyover.kube.operator.mariadb;

import static com.flyover.kube.tools.connector.PodSpec.Builders.*;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.flyover.kube.operator.mariadb.model.DatabaseModel;
import com.flyover.kube.operator.mariadb.storage.clc.ClcStorageProvider;
import com.flyover.kube.tools.connector.Deployment;
import com.flyover.kube.tools.connector.InsecureSslPolicy;
import com.flyover.kube.tools.connector.Kubernetes;
import com.flyover.kube.tools.connector.KubernetesConfig;
import com.flyover.kube.tools.connector.Namespace;
import com.flyover.kube.tools.connector.Role;
import com.flyover.kube.tools.connector.Secret;
import com.flyover.kube.tools.connector.Service;
import com.flyover.kube.tools.connector.ServiceAccount;
import com.flyover.kube.tools.connector.ServiceAccountKubernetesAuthenticator;
import com.flyover.kube.tools.connector.Volume;
import com.flyover.kube.tools.connector.model.NamespaceModel;
import com.flyover.kube.tools.connector.spring.EnableKubernetes;
import com.flyover.kube.tools.connector.storage.StorageProvider;

/**
 * @author mramach
 *
 */
@Configuration
@EnableKubernetes
@EnableScheduling
public class Application {

	private static final String BOOTSTRAP_FLAG = "mariadb.bootstrap";
	private static final String CLUSTER_ID = "mariadb.cluster";
	
	@Value("${kubernetes.endpoint:https://192.168.253.1}")
	private String endpoint;
	@Value("${kubernetes.token:/var/run/secrets/kubernetes.io/serviceaccount/token}")
	private String token;
	@Value("${location:ca1}")
	private String location;
	@Autowired
	private Kubernetes kube;
	
	@Scheduled(fixedDelay = 5000)
	public void controlLoop() {
		
		System.out.println("control loop");
		
		kube.list(new NamespaceModel(), Collections.emptyMap()).stream()
			.forEach(this::processNamespace);
		
	}
	
	private void processNamespace(NamespaceModel ns) {
		
		DatabaseModel db = new DatabaseModel();
		db.getMetadata().setNamespace(ns.getMetadata().getName());
		
		kube.list(db, Collections.singletonMap("location", location)).stream()
			.forEach(this::processDatabase);
		
	}
	
	private void processDatabase(DatabaseModel db) {
		
		String namespace = db.getMetadata().getNamespace();
		String name = db.getMetadata().getName();
		String clusterId = db.getSpec().getClusterId();
		boolean bootstrap = db.getSpec().isBootstrap();
		int replicas = db.getSpec().getReplicas();
		int idx = 1;
		
		Namespace ns = kube.namespace(namespace).findOrCreate();
		
		// generate root password and create secret.
		String rootSecretName = String.format("%s-root", clusterId);
		Secret rootSecret = ns.secret(rootSecretName).find();
		
		if(rootSecret == null) {
			
			rootSecret = ns.secret(rootSecretName)
				.data("username", "root")
				.data("password", generateToken(36, ALPHA_CAPS + ALPHA + NUMERIC))
					.create();
			
		}
		
		ServiceAccount serviceAccount = ns.serviceAccount("operator-mariadb-companion")
			.findOrCreate();
		
		Role role = ns.role("operator-mariadb-companion")
			.rule(
				Arrays.asList("mariadb.operator.flyover.com"),
				Arrays.asList("databaseusers"),
				Arrays.asList("get", "list", "watch", "create", "update", "patch", "delete")
			)
			.rule(
				Arrays.asList(""),
				Arrays.asList("namespaces", "secrets"),
				Arrays.asList("get", "list", "watch", "create", "update", "patch", "delete")
			).findOrCreate();
		
		ns.roleBinding("operator-mariadb-companion")
			.roleRef(role)
			.subject(serviceAccount)
				.findOrCreate();
		
		List<Deployment> deployments = ns.deployments(
				Collections.singletonMap(CLUSTER_ID, clusterId));
		
		if(bootstrap) {
		
			if(deployments.isEmpty()) {
				
				String deploymentName = String.format("%s-%s-node-%s", clusterId, name, idx);
				Volume data = kube.volume(namespace, deploymentName, "data");
				
				Deployment d = ns.deployment(deploymentName)
					.serviceAccount(serviceAccount.metadata().getName())
					.nodeSelector(db.getSpec().getNodeSelector())
					.volumes(data)
					.containers(
						container("mariadb")
							.image("mariadb:10.2")
							.tcpPort(3306)
							.tcpPort(4567)
							.tcpPort(4444)
							.readinessProbeTcp(3306, 15, 15, 10)
							.env("MYSQL_ROOT_PASSWORD", rootSecret.data("password"))
							.volumeMount(data, "/var/lib/mysql")
							.args(
								"--wsrep-new-cluster",
								"--wsrep_on=ON",
								"--wsrep_cluster_address=gcomm://127.0.0.1:4567",
								"--wsrep_provider=/usr/lib/galera/libgalera_smm.so",
								"--binlog_format=row",
								"--default_storage_engine=InnoDB",
								"--innodb_autoinc_lock_mode=2",
								"--bind-address=0.0.0.0",
								"--wsrep_slave_threads=4",
								"--innodb_flush_log_at_trx_commit=0",
								String.format("--wsrep_sst_auth=root:%s", rootSecret.data("password"))
							))
					.containers(
						container("companion")
							.image("portr.ctnr.ctl.io/flyover/kube-operator-mariadb-companion:0.0.1-SNAPSHOT")
							.imagePullPolicy("IfNotPresent")
							.args(
								String.format("--spring.datasource.password=%s", rootSecret.data("password")),
								String.format("--namespace=%s", namespace),
								String.format("--clusterId=%s", clusterId)
							));
				d.spec().selector().getMatchLabels().put(CLUSTER_ID, clusterId);
				d.spec().selector().getMatchLabels().put(BOOTSTRAP_FLAG, "true");
				d.spec().template().metadata().getLabels().put(CLUSTER_ID, clusterId);
				d.spec().template().metadata().getLabels().put(BOOTSTRAP_FLAG, "true");
				d.metadata().getLabels().put(CLUSTER_ID, clusterId);
				d.metadata().getLabels().put(BOOTSTRAP_FLAG, "true");
				// update deployment
				d.merge();
				// log it
				System.out.println(String.format("waiting for deployment %s to become ready", deploymentName));
				// wait for pods to become ready
				d.ready(120, TimeUnit.SECONDS);
				// log it
				System.out.println(String.format("deployment %s is ready", deploymentName));
				
				return;
				
			}
			
			idx++;
			
		}
		
		Service service = ns.service(clusterId).find();
		
		if(service == null) {

			service = ns.service(clusterId);
			service.spec().tcpPort(3306);
			service.spec().tcpPort(4567);
			service.spec().tcpPort(4444);
			service.spec().selectors().put(CLUSTER_ID, clusterId);
			
			service.merge();
			
			System.out.println(String.format("service %s created", service.metadata().getName()));
			
		}
		
		if(!bootstrap && getDeployments(ns, clusterId).isEmpty()) {
			
			System.out.println(String.format("no existing deployments found for cluster %s", clusterId));
			
			return;
			
		}
		
		final Secret rs = rootSecret;
		
		IntStream.range(idx, replicas + 1).forEach(i -> {
			
			String deploymentName = String.format("%s-%s-node-%s", clusterId, name, i);
			Deployment d2 = ns.deployment(deploymentName).find();
			
			if(d2 == null) {
				
				Volume data = kube.volume(namespace, deploymentName, "data");
				
				d2 = ns.deployment(deploymentName)
					.nodeSelector(db.getSpec().getNodeSelector())
					.volumes(data)
					.containers(
						container("mariadb")
							.image("mariadb:10.2")
							.tcpPort(3306)
							.tcpPort(4567)
							.tcpPort(4444)
							.readinessProbeTcp(3306, 15, 15, 10)
							.env("MYSQL_ROOT_PASSWORD", rs.data("password"))
							.volumeMount(data, "/var/lib/mysql")
							.args(
								"--wsrep_on=ON",
								String.format("--wsrep_cluster_address=gcomm://%s", getFullClusterAddress(ns, clusterId)),
								"--wsrep_provider=/usr/lib/galera/libgalera_smm.so",
								"--binlog_format=row",
								"--default_storage_engine=InnoDB",
								"--innodb_autoinc_lock_mode=2",
								"--bind-address=0.0.0.0",
								"--wsrep_slave_threads=4",
								"--innodb_flush_log_at_trx_commit=0",
								String.format("--wsrep_sst_auth=root:%s", rs.data("password"))
							));
				d2.spec().selector().getMatchLabels().put(CLUSTER_ID, clusterId);
				d2.spec().selector().getMatchLabels().put(BOOTSTRAP_FLAG, "false");
				d2.spec().template().metadata().getLabels().put(CLUSTER_ID, clusterId);
				d2.spec().template().metadata().getLabels().put(BOOTSTRAP_FLAG, "false");
				d2.metadata().getLabels().put(CLUSTER_ID, clusterId);
				d2.metadata().getLabels().put(BOOTSTRAP_FLAG, "false");
				// update deployment
				d2.merge();
				
			} else {
				
				System.out.println(String.format("deployment %s already exists", deploymentName));
				
			}
			
			// log it
			System.out.println(String.format("waiting for deployment %s to become ready", deploymentName));
			// wait for deployment to become ready
			d2.ready(120, TimeUnit.SECONDS);
			
			System.out.println(String.format("deployment %s is ready", deploymentName));
			
		});
		
		if(bootstrap) {
		
			// ensure the bootstrap node is updated to remove the bootstrap argument
			Deployment bootstrapDeployment = getBootstrapDeployment(ns, clusterId);
			
			bootstrapDeployment.containers("mariadb")
				.args(
					"--wsrep_on=ON",
					String.format("--wsrep_cluster_address=gcomm://%s", getClusterAddress(ns, clusterId)),
					"--wsrep_provider=/usr/lib/galera/libgalera_smm.so",
					"--binlog_format=row",
					"--default_storage_engine=InnoDB",
					"--innodb_autoinc_lock_mode=2",
					"--bind-address=0.0.0.0",
					"--wsrep_slave_threads=4",
					"--innodb_flush_log_at_trx_commit=0",
					String.format("--wsrep_sst_auth=root:%s", rs.data("password"))
				);
			
			bootstrapDeployment.merge();
			
		}
		
	}
	
	private String getClusterAddress(Namespace ns, String name) {
		
		return getNonBootstrapDeployments(ns, name).stream()
			.map(d -> d.pods())
			.map(pods -> pods.stream().map(p -> String.format("%s:4567", p.podIP())).collect(Collectors.joining(",")))
			.collect(Collectors.joining(","));
		
	}
	
	private String getFullClusterAddress(Namespace ns, String name) {
		
		return getDeployments(ns, name).stream()
			.map(d -> d.pods())
			.map(pods -> pods.stream().map(p -> String.format("%s:4567", p.podIP())).collect(Collectors.joining(",")))
			.collect(Collectors.joining(","));
		
	}

	private List<Deployment> getDeployments(Namespace ns, String name) {
		
		Map<String, String> selectors = new LinkedHashMap<>();
		selectors.put(CLUSTER_ID, name);
		
		return ns.deployments(selectors);
		
	}
	
	private Deployment getBootstrapDeployment(Namespace ns, String name) {
		
		Map<String, String> selectors = new LinkedHashMap<>();
		selectors.put(CLUSTER_ID, name);
		selectors.put(BOOTSTRAP_FLAG, "true");
		
		Deployment bootstrap = ns.deployments(selectors).stream().findFirst().get();
		
		return bootstrap;
		
	}
	
	private List<Deployment> getNonBootstrapDeployments(Namespace ns, String name) {
		
		Map<String, String> selectors = new LinkedHashMap<>();
		selectors.put(CLUSTER_ID, name);
		selectors.put(BOOTSTRAP_FLAG, "false");
		
		return ns.deployments(selectors);
		
	}
	
	@Bean
	public StorageProvider clcStorageProvider() {
		
		ClcStorageProvider provider = new ClcStorageProvider(
				Arrays.asList(
					"10.50.216.15:6789",
					"10.50.216.16:6789",
					"10.50.216.18:6789",
					"10.50.216.19:6789"
				), "rbd", location, 10);
		
		return provider;
		
	}
	
	@Bean
	public KubernetesConfig config(StorageProvider provider) {
		
		KubernetesConfig c = new KubernetesConfig();
		c.setEndpoint(endpoint);
		c.setSslPolicy(new InsecureSslPolicy());
		c.setAuthenticator(new ServiceAccountKubernetesAuthenticator(token));
		c.setStorageProvider(provider);
		
		return c;
		
	}
	
	private static final SecureRandom RANDONM = new SecureRandom();
	private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMERIC = "0123456789";
	
    private String generateToken(int len, String dic) {
		String result = "";
		for (int i = 0; i < len; i++) {
			int index = RANDONM.nextInt(dic.length());
			result += dic.charAt(index);
		}
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
