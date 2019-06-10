/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author mramach
 *
 */
public class PodSpecModel extends Model {

	private String serviceAccount;
	private List<ContainerModel> containers = new LinkedList<>();
	@JsonInclude(Include.NON_NULL)
	private List<ContainerModel> initContainers = new LinkedList<>();
	private List<VolumeModel> volumes = new LinkedList<>();
	private List<ImagePullSecretModel> imagePullSecrets = new LinkedList<>();
	private Map<String, String> nodeSelector;
	private boolean hostNetwork = false;
	private boolean hostPID = false;
	private String dnsPolicy = "ClusterFirst";
	@JsonInclude(Include.NON_NULL)
	private SecurityContextModel securityContext;

	public List<ContainerModel> getContainers() {
		return containers;
	}

	public void setContainers(List<ContainerModel> containers) {
		this.containers = containers;
	}
	
	public List<ContainerModel> getInitContainers() {
		return initContainers;
	}

	public void setInitContainers(List<ContainerModel> initContainers) {
		this.initContainers = initContainers;
	}

	public List<VolumeModel> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<VolumeModel> volumes) {
		this.volumes = volumes;
	}

	public List<ImagePullSecretModel> getImagePullSecrets() {
		return imagePullSecrets;
	}

	public void setImagePullSecrets(List<ImagePullSecretModel> imagePullSecrets) {
		this.imagePullSecrets = imagePullSecrets;
	}

	public Map<String, String> getNodeSelector() {
		return nodeSelector;
	}

	public void setNodeSelector(Map<String, String> nodeSelector) {
		this.nodeSelector = nodeSelector;
	}

	public String getServiceAccount() {
		return serviceAccount;
	}

	public void setServiceAccount(String serviceAccount) {
		this.serviceAccount = serviceAccount;
	}

	public boolean isHostNetwork() {
		return hostNetwork;
	}

	public void setHostNetwork(boolean hostNetwork) {
		this.hostNetwork = hostNetwork;
	}

	public boolean isHostPID() {
		return hostPID;
	}

	public void setHostPID(boolean hostPID) {
		this.hostPID = hostPID;
	}

	public SecurityContextModel getSecurityContext() {
		return securityContext;
	}

	public void setSecurityContext(SecurityContextModel securityContext) {
		this.securityContext = securityContext;
	}

	public String getDnsPolicy() {
		return dnsPolicy;
	}

	public void setDnsPolicy(String dnsPolicy) {
		this.dnsPolicy = dnsPolicy;
	}

	public static class ImagePullSecretModel extends Model {
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}

	@JsonInclude(Include.NON_NULL)
	public static class SecurityContextModel extends Model {
		
		private SeLinuxOptions seLinuxOptions;
		private Integer runAsUser;
		private Integer fsGroup;

		public SeLinuxOptions getSeLinuxOptions() {
			return seLinuxOptions;
		}

		public void setSeLinuxOptions(SeLinuxOptions seLinuxOptions) {
			this.seLinuxOptions = seLinuxOptions;
		}

		public Integer getRunAsUser() {
			return runAsUser;
		}

		public void setRunAsUser(Integer runAsUser) {
			this.runAsUser = runAsUser;
		}

		public Integer getFsGroup() {
			return fsGroup;
		}

		public void setFsGroup(Integer fsGroup) {
			this.fsGroup = fsGroup;
		}
		
	}
	
	public static class SeLinuxOptions extends Model {
		
	}
	
}
