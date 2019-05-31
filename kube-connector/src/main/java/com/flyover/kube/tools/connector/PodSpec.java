/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.flyover.kube.tools.connector.model.ContainerModel;
import com.flyover.kube.tools.connector.model.PodSpecModel;
import com.flyover.kube.tools.connector.model.PodSpecModel.ImagePullSecretModel;
import com.flyover.kube.tools.connector.model.PodSpecModel.SeLinuxOptions;
import com.flyover.kube.tools.connector.model.PodSpecModel.SecurityContextModel;

/**
 * @author mramach
 *
 */
public class PodSpec {
	
	private PodSpecModel model;

	public PodSpec(PodSpecModel model) {
		this.model = model;
	}

	public Container container(String name) {
		
		return model.getContainers().stream()
			.filter(c -> name.equals(c.getName()))
			.map(Container::new)
				.findFirst()
					.orElse(null);
		
	}

	public PodSpec containers(Container c) {
		
		this.model.getContainers().add(c.model());
		
		return this;
		
	}
	
	public PodSpec initContainers(Container c) {
		
		this.model.getInitContainers().add(c.model());
		
		return this;
		
	}
	
	public PodSpec volumes(Volume v) {
		
		this.model.getVolumes().add(v.model());
		
		return this;
		
	}
	
	public PodSpec hostNetwork() {
		
		this.model.setHostNetwork(true);
		
		return this;
		
	}
	
	public PodSpec hostPID() {
		
		this.model.setHostPID(true);
		
		return this;
		
	}
	
	public PodSpec serviceAccount(String sa) {
		
		this.model.setServiceAccount(sa);
		
		return this;
		
	}
	
	public PodSpec dnsPolicy(String policy) {
		
		this.model.setDnsPolicy(policy);
		
		return this;
		
	}
	
	public List<Container> containers() {
		
		return this.model.getContainers().stream()
			.map(c -> new Container(c))
				.collect(Collectors.toList());
		
	}
	
	public List<Container> initContainers() {
		
		return this.model.getInitContainers().stream()
			.map(c -> new Container(c))
				.collect(Collectors.toList());
		
	}

	public PodSpec imagePullSecret(Secret s) {

		PodSpecModel.ImagePullSecretModel secret = new ImagePullSecretModel();
		secret.setName(s.metadata().getName());
		
		this.model.getImagePullSecrets().add(secret);
		
		return this;
		
	}
	
	public PodSpec nodeSelector(Map<String, String> selectors) {
		
		if(selectors != null && !selectors.isEmpty()) {
			this.model.setNodeSelector(selectors);
		}
		
		return this;
		
	}
	
	public SecurityContext securityContext() {
		
		if(model.getSecurityContext() == null) {
			model.setSecurityContext(new SecurityContextModel());
		}
		
		return new SecurityContext(model.getSecurityContext());
		
	}
	
	public static class Builders {

		public static Container container(String name) {
			return new Container(new ContainerModel()).name(name);
		}
		
	}

	public static class SecurityContext {
		
		private SecurityContextModel model;

		public SecurityContext(SecurityContextModel model) {
			this.model = model;
		}
		
		public SecurityContext seLinuxOptions(SeLinuxOptions options) {
			this.model.setSeLinuxOptions(options);
			return this;
		}
		
		public SecurityContext runAsUser(int runAsUser) {
			this.model.setRunAsUser(runAsUser);
			return this;
		}
		
	}
	
}
