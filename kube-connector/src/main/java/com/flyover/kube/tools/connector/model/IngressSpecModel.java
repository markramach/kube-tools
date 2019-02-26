/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.util.LinkedList;
import java.util.List;


/**
 * @author mramach
 *
 */
public class IngressSpecModel extends Model {
	
	private List<Rule> rules = new LinkedList<>();
	private List<Tls> tls = new LinkedList<>();
	
	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public List<Tls> getTls() {
		return tls;
	}

	public void setTls(List<Tls> tls) {
		this.tls = tls;
	}

	public static class Rule extends Model {
		
		private String host;
		private Http http;
		
		public String getHost() {
			return host;
		}
		
		public void setHost(String host) {
			this.host = host;
		}
		
		public Http getHttp() {
			return http;
		}
		
		public void setHttp(Http http) {
			this.http = http;
		}
		
	}
	
	public static class Http extends Model {
		
		private List<Path> paths = new LinkedList<>();

		public List<Path> getPaths() {
			return paths;
		}

		public void setPaths(List<Path> paths) {
			this.paths = paths;
		}
		
	}
	
	public static class Path extends Model {
		
		private String path;
		private BackendModel backend;
		
		public String getPath() {
			return path;
		}
		
		public void setPath(String path) {
			this.path = path;
		}
		
		public BackendModel getBackend() {
			return backend;
		}
		
		public void setBackend(BackendModel backend) {
			this.backend = backend;
		}
		
	}
	
	public static class BackendModel extends Model {
		
		private String serviceName;
		private Integer servicePort;
		
		public String getServiceName() {
			return serviceName;
		}
		
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		
		public Integer getServicePort() {
			return servicePort;
		}
		
		public void setServicePort(Integer servicePort) {
			this.servicePort = servicePort;
		}
		
	}
	
	public static class Tls extends Model {
		
		private List<String> hosts = new LinkedList<>();
		private String secretName;
		
		public List<String> getHosts() {
			return hosts;
		}
		
		public void setHosts(List<String> hosts) {
			this.hosts = hosts;
		}

		public String getSecretName() {
			return secretName;
		}

		public void setSecretName(String secretName) {
			this.secretName = secretName;
		}
		
	}

}
