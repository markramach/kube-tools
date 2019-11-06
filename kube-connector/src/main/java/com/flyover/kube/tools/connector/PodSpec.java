/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.flyover.kube.tools.connector.model.ContainerModel;
import com.flyover.kube.tools.connector.model.PodSpecModel;
import com.flyover.kube.tools.connector.model.PodSpecModel.AffinityModel;
import com.flyover.kube.tools.connector.model.PodSpecModel.ImagePullSecretModel;
import com.flyover.kube.tools.connector.model.PodSpecModel.NodeAffinityModel;
import com.flyover.kube.tools.connector.model.PodSpecModel.NodeSelectorTermsModel;
import com.flyover.kube.tools.connector.model.PodSpecModel.RequiredDuringSchedulingIgnoredDuringExecutionModel;
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
	
	public PodSpec hostname(String hostname) {
		
		this.model.setHostname(hostname);
		
		return this;
		
	}
	
	public static class Builders {

		public static Container container(String name) {
			return new Container(new ContainerModel()).name(name);
		}
		
		public static Match match(String label) {
			return new Match(label);
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
		
		public SecurityContext fsGroup(int fsGroup) {
			this.model.setFsGroup(fsGroup);
			return this;
		}
		
	}

	public Affinity affinity() {
		
		if(this.model.getAffinity() == null) {
			this.model.setAffinity(new AffinityModel());
		}
		
		return new Affinity(this.model.getAffinity());
		
	}
	
	public static class Affinity {
		
		private AffinityModel model;

		public Affinity(AffinityModel model) {
			this.model = model;
		}

		public NodeAffinity nodeAffinity() {

			if(this.model.getNodeAffinity() == null) {
				this.model.setNodeAffinity(new NodeAffinityModel());
			}
			
			return new NodeAffinity(this.model.getNodeAffinity());
			
		}
		
	}
	
	public static class NodeAffinity {
		
		private NodeAffinityModel model;

		public NodeAffinity(NodeAffinityModel model) {
			this.model = model;
		}
		
		public NodeAffinityRequired required() {
			
			if(this.model.getRequiredDuringSchedulingIgnoredDuringExecution() == null) {
				this.model.setRequiredDuringSchedulingIgnoredDuringExecution(new RequiredDuringSchedulingIgnoredDuringExecutionModel());
			}
			
			return new NodeAffinityRequired(this.model.getRequiredDuringSchedulingIgnoredDuringExecution());
			
		}
		
	}
	
	public static class NodeAffinityRequired {
		
		private RequiredDuringSchedulingIgnoredDuringExecutionModel model;

		public NodeAffinityRequired(RequiredDuringSchedulingIgnoredDuringExecutionModel model) {
			this.model = model;
		}

		public void nodeSelectorTerms(Match matchExpression) {
			
			NodeSelectorTermsModel term = new NodeSelectorTermsModel();
			term.getMatchExpressions().add(matchExpression.build());
			
			this.model.getNodeSelectorTerms().add(term);
			
		}
		
	}
	
	public static class Match {
		
		private String label;
		private MatchOperator operator;
		private List<String> values;

		public Match(String label) {
			this.label = label;
		}
		
		public Match in(List<String> values) {
			
			this.operator = MatchOperator.IN;
			this.values = values;
			
			return this;
			
		}
		
		public Map<String, Object> build() {

			Map<String, Object> exp = new LinkedHashMap<>();
			exp.put("key", this.label);
			exp.put("operator", this.operator.code());
			
			if(this.values != null) {
				exp.put("values", this.values);
			}
			
			return exp;
			
		}
		
	}
	
	public static enum MatchOperator {
		
		IN("In");
		
		private String code;

		private MatchOperator(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
		
	}

}
