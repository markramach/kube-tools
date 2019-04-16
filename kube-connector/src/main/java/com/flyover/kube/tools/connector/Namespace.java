/**
 * 
 */
package com.flyover.kube.tools.connector;

import static com.flyover.kube.tools.connector.PodSpec.Builders.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.flyover.kube.tools.connector.model.DeploymentModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.NamespaceModel;

/**
 * @author mramach
 *
 */
public class Namespace {
	
	private Kubernetes kube;
	private NamespaceModel model;

	public Namespace(Kubernetes kube) {
		this.kube = kube;
		this.model = new NamespaceModel();
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	protected Kubernetes kube() {
		return kube;
	}

	public Namespace create() {
		
		this.model = kube.create(this.model);
		
		return this;
		
	}
	
	public Namespace findOrCreate() {
		
		NamespaceModel found = kube.find(this.model);
		
		this.model = found != null ? found : kube.create(this.model); 
		
		return this;
		
	}
	
	public void delete() {
		
		kube.delete(model);
		
	}

	public Deployment deployment(String name, String image, int port) {
		
		Deployment d = new Deployment(kube);
		d.metadata().setNamespace(this.model.getMetadata().getName());
		d.metadata().setName(name);
		d.spec().replicas(1);
		d.spec().selector().getMatchLabels().put("key", name);
		d.spec().template().metadata().getLabels().put("key", name);
		d.containers(container(name).image(image).tcpPort(port));
		
		return d;
		
	}

	public Deployment deployment(String name) {
		
		Deployment d = new Deployment(kube);
		d.metadata().setNamespace(this.model.getMetadata().getName());
		d.metadata().setName(name);
		d.spec().replicas(1);
		d.spec().selector().getMatchLabels().put("key", name);
		d.spec().template().metadata().getLabels().put("key", name);
		
		return d;
		
	}
	
	public List<Deployment> deployments(Map<String, String> selectors) {
		
		DeploymentModel m = new DeploymentModel();
		m.getMetadata().setNamespace(metadata().getName());
		
		return kube.list(m, selectors).stream()
			.map(d -> new Deployment(kube, d))
				.collect(Collectors.toList());
		
	}
	
	public Service service(String name) {
		
		Service service = new Service(kube);
		service.metadata().setNamespace(metadata().getName());
		service.metadata().setName(name);
		
		return service;
		
	}
	
	public Ingress ingress(String name) {
		
		Ingress ingress = new Ingress(kube);
		ingress.metadata().setNamespace(metadata().getName());
		ingress.metadata().setName(name);
		
		return ingress;
		
	}
	
	public Secret secret(String name) {
		
		Secret s = new Secret(kube);
		s.metadata().setNamespace(this.model.getMetadata().getName());
		s.metadata().setName(name);
		
		return s;
		
	}
	
	public ServiceAccount serviceAccount(String name) {
		
		ServiceAccount sa = new ServiceAccount(kube);
		sa.metadata().setNamespace(metadata().getName());
		sa.metadata().setName(name);
		
		return sa;
		
	}
	
	public Role role(String name) {
		
		Role role = new Role(kube);
		role.metadata().setNamespace(metadata().getName());
		role.metadata().setName(name);
		
		return role;
		
	}
	
	public RoleBinding roleBinding(String name) {
		
		RoleBinding roleBinding = new RoleBinding(kube);
		roleBinding.metadata().setNamespace(metadata().getName());
		roleBinding.metadata().setName(name);
		
		return roleBinding;
		
	}

	public ClusterRole clusterRole(String name) {
		ClusterRole clusterRole = new ClusterRole(kube);
		clusterRole.metadata().setNamespace(metadata().getName());
		clusterRole.metadata().setName(name);

		return clusterRole;
	}

	public ClusterRoleBinding clusterRoleBinding(String name) {
		ClusterRoleBinding clusterRoleBinding = new ClusterRoleBinding(kube);
		clusterRoleBinding.metadata().setNamespace(metadata().getName());
		clusterRoleBinding.metadata().setName(name);

		return clusterRoleBinding;
	}

	public NetworkPolicy networkPolicy(String name) {
		NetworkPolicy networkPolicy = new NetworkPolicy(kube);
		networkPolicy.metadata().setNamespace(metadata().getName());
		networkPolicy.metadata().setName(name);

		return networkPolicy;
	}

	public ConfigMap configMap(String name) {
		ConfigMap configMap = new ConfigMap(kube);

		configMap.metadata().setNamespace(metadata().getName());
		configMap.metadata().setName(name);

		return configMap;
	}

}
