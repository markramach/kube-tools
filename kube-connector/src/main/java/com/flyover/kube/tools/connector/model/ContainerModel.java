/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author mramach
 *
 */
public class ContainerModel extends Model {
	
	private String name;
	private String image;
	private String imagePullPolicy;
	private List<PortModel> ports = new LinkedList<>();
	private List<EnvModel> env = new LinkedList<>();
	private List<VolumeMountModel> volumeMounts = new LinkedList<>();
	private List<String> command = new LinkedList<>();
	private Collection<String> args = new HashSet<>();
	private ProbeModel readinessProbe;
	private ResourceModel resources;
	@JsonInclude(Include.NON_NULL)
	private SecurityContextModel securityContext;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<PortModel> getPorts() {
		return ports;
	}

	public void setPorts(List<PortModel> ports) {
		this.ports = ports;
	}

	public List<EnvModel> getEnv() {
		return env;
	}

	public void setEnv(List<EnvModel> env) {
		this.env = env;
	}

	public String getImagePullPolicy() {
		return imagePullPolicy;
	}

	public void setImagePullPolicy(String imagePullPolicy) {
		this.imagePullPolicy = imagePullPolicy;
	}

	public List<String> getCommand() {
		return command;
	}

	public void setCommand(List<String> command) {
		this.command = command;
	}

	public Collection<String> getArgs() {
		return args;
	}

	public void setArgs(Collection<String> args) {
		this.args = new LinkedList<>(args);
	}

	public List<VolumeMountModel> getVolumeMounts() {
		return volumeMounts;
	}

	public void setVolumeMounts(List<VolumeMountModel> volumeMounts) {
		this.volumeMounts = volumeMounts;
	}

	public ProbeModel getReadinessProbe() {
		return readinessProbe;
	}

	public void setReadinessProbe(ProbeModel readinessProbe) {
		this.readinessProbe = readinessProbe;
	}
	
	public ResourceModel getResources() {
		return resources;
	}

	public void setResources(ResourceModel resources) {
		this.resources = resources;
	}

	public SecurityContextModel getSecurityContext() {
		return securityContext;
	}

	public void setSecurityContext(SecurityContextModel securityContext) {
		this.securityContext = securityContext;
	}

	public static class ResourceModel extends Model {
	
		private Map<String, Object> limits = new LinkedHashMap<>();
		private Map<String, Object> requests = new LinkedHashMap<>();
		
		public Map<String, Object> getLimits() {
			return limits;
		}
		
		public void setLimits(Map<String, Object> limits) {
			this.limits = limits;
		}

		public Map<String, Object> getRequests() {
			return requests;
		}

		public void setRequests(Map<String, Object> requests) {
			this.requests = requests;
		}
		
	}
	
	public static class SecurityContextModel extends Model {
		
		private boolean privileged;

		public boolean isPrivileged() {
			return privileged;
		}

		public void setPrivileged(boolean privileged) {
			this.privileged = privileged;
		}
		
	}

}
