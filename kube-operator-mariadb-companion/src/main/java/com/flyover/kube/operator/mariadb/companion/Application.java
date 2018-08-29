/**
 * 
 */
package com.flyover.kube.operator.mariadb.companion;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.flyover.kube.operator.mariadb.companion.model.DatabaseUserModel;
import com.flyover.kube.operator.mariadb.companion.model.DatabaseUserSpecModel.GrantsModel;
import com.flyover.kube.tools.connector.InsecureSslPolicy;
import com.flyover.kube.tools.connector.Kubernetes;
import com.flyover.kube.tools.connector.KubernetesConfig;
import com.flyover.kube.tools.connector.Namespace;
import com.flyover.kube.tools.connector.Secret;
import com.flyover.kube.tools.connector.ServiceAccountKubernetesAuthenticator;
import com.flyover.kube.tools.connector.spring.EnableKubernetes;

/**
 * @author mramach
 *
 */
@Configuration
@EnableKubernetes
@EnableScheduling
@SpringBootApplication
public class Application {
	
	private static final SecureRandom RANDONM = new SecureRandom();
	private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMERIC = "0123456789";
	
	@Value("${kubernetes.endpoint:https://192.168.253.1}")
	private String endpoint;
	@Value("${kubernetes.token:/var/run/secrets/kubernetes.io/serviceaccount/token}")
	private String token;
	@Autowired
	private Kubernetes kube;
	@Value("${clusterId}")
	private String clusterId;
	@Value("${namespace}")
	private String namespace;
	@Autowired
	private JdbcTemplate jdbc;
	
	@Scheduled(fixedDelay = 5000)
	public void controlLoop() {
		
		System.out.println("control loop");
		
		DatabaseUserModel model = new DatabaseUserModel();
		model.getMetadata().setNamespace(namespace);
		
		kube.list(model, Collections.emptyMap()).stream()
			.filter(du -> clusterId.equals(du.getSpec().getClusterId()))
			.forEach(this::processDatabaseUser);
		
	}
	
	private void processDatabaseUser(DatabaseUserModel du) {
		
		PreparedStatementCallback<Boolean> callback = (ps) -> {
			
			ps.setString(1, du.getSpec().getUser().getName());
			
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {
				return false;
			}
			
			return rs.getBoolean("user_exists");
			
		};
		
		boolean userExists = jdbc.execute(
				"select "
			  + "exists(select true "
			  + "		from mysql.user "
			  + "		WHERE user = ?) as user_exists", callback);

		
		String username = du.getSpec().getUser().getName();
		String password = generateToken(36, ALPHA_CAPS + ALPHA + NUMERIC);
		
		if(!userExists) {
			
			Namespace ns = kube.namespace(namespace).findOrCreate();
			
			// generate root password and create secret.
			String secretName = String.format("%s-%s", clusterId, username);
			Secret secret = ns.secret(secretName).find();
			
			if(secret == null) {
				
				secret = ns.secret(secretName)
					.data("username", username)
					.data("password", password)
						.create();
				
			}
			
			jdbc.execute("create user '" + username + "'@'%' identified by '" + password + "'");
			
		}
		
		du.getSpec().getUser().getGrants().stream()
			.forEach(g -> processGrants(du, g));
		
	}
	
	private void processGrants(DatabaseUserModel u, GrantsModel g) {
		
		PreparedStatementCallback<Boolean> callback = (ps) -> {
			
			ps.setString(1, g.getSchema());
			
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {
				return false;
			}
			
			return rs.getBoolean("schema_exists");
			
		};
		
		boolean schemaExists = jdbc.execute(
				"select "
			  + "exists(select true "
			  + "		from information_schema.schemata "
			  + "		WHERE schema_name = ?) as schema_exists", callback);
		
		if(!schemaExists) {
			jdbc.execute("create schema if not exists " + g.getSchema());
		}

		PreparedStatementCallback<Set<String>> privCallback = (ps) -> {
			
			ps.setString(1, "'" + u.getSpec().getUser().getName() + "'@'%'");
			ps.setString(2, g.getSchema());
			ResultSet rs = ps.executeQuery();
			Set<String> privileges = new LinkedHashSet<>();
			
			while(rs.next()) {
				privileges.add(rs.getString("privilege_type").toLowerCase());
			}
			
			return privileges;
			
		};
		
		// determine missing grants.
		Set<String> privileges = jdbc.execute(
				"select * "
			  + "from information_schema.schema_privileges "
			  + "where grantee = ?"
			  + "	and table_schema = ?", privCallback);
		
		List<String> missing = g.getPrivileges().stream()
			.map(p -> p.toLowerCase())
			.filter(p -> !privileges.contains(p))
				.collect(Collectors.toList());
		
		missing.stream().forEach(p -> {
			
			if("all".equals(p)) {
				
				if(privileges.size() < 18) {
					jdbc.execute("grant all on " + g.getSchema() + ".* to '" + u.getSpec().getUser().getName() + "'@'%'");
				}
				
			} else {
				
				jdbc.execute("grant " + p + " on " + g.getSchema() + ".* to '" + u.getSpec().getUser().getName() + "'@'%'");
				
			}
			
		});
		
	}
	
	private String generateToken(int len, String dic) {
		String result = "";
		for (int i = 0; i < len; i++) {
			int index = RANDONM.nextInt(dic.length());
			result += dic.charAt(index);
		}
		return result;
	}

	@Bean
	public KubernetesConfig config() {
		
		KubernetesConfig c = new KubernetesConfig();
		c.setEndpoint(endpoint);
		c.setSslPolicy(new InsecureSslPolicy());
		c.setAuthenticator(new ServiceAccountKubernetesAuthenticator(token));
		
		return c;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
