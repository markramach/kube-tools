/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.Map;

import com.flyover.kube.tools.connector.PodSpec.SecurityContext;
import com.flyover.kube.tools.connector.model.DaemonsetModel;
import com.flyover.kube.tools.connector.model.DaemonsetSpecModel;
import com.flyover.kube.tools.connector.model.DaemonsetTemplateModel;
import com.flyover.kube.tools.connector.model.DeploymentMetadataModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

/**
 * @author mramach
 *
 */
public class Daemonset {
	
	private Kubernetes kube;
	private DaemonsetModel model;

	public Daemonset(Kubernetes kube) {
		this(kube, new DaemonsetModel());
	}
	
	public Daemonset(Kubernetes kube, DaemonsetModel model) {
		this.kube = kube;
		this.model = model;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public DaemonsetSpec spec() {
		return new DaemonsetSpec(this.model.getSpec());
	}
	
	public Daemonset containers(Container c) {
		this.spec().template().podSpec().containers(c);
		return this;
	}
	
	public Daemonset initContainers(Container c) {
		this.spec().template().podSpec().initContainers(c);
		return this;
	}
	
	public Daemonset volumes(Volume v) {
		this.spec().template().podSpec().volumes(v);
		return this;
	}

	public Daemonset hostNetwork() {
		this.spec().template().podSpec().hostNetwork();
		return this;
	}
	
	public Daemonset hostPID() {
		this.spec().template().podSpec().hostPID();
		return this;
	}
	
	public Daemonset serviceAccount(String sa) {
		this.spec().template().podSpec().serviceAccount(sa);
		return this;
	}

	public SecurityContext securityContext() {
		return this.spec().template().podSpec().securityContext();
	}
	
	public Daemonset nodeSelector(Map<String, String> selectors) {
		this.spec().template().podSpec().nodeSelector(selectors);
		return this;
	}
	
	public Container containers(String name) {
		
		return this.spec().template().podSpec().containers().stream()
			.filter(c -> name.equals(c.name()))
				.findFirst().get();
		
	}
	
	public Container initContainers(String name) {
		
		return this.spec().template().podSpec().initContainers().stream()
			.filter(c -> name.equals(c.name()))
				.findFirst().get();
		
	}
	
	public Daemonset imagePullSecret(Secret s) {
		this.spec().template().podSpec().imagePullSecret(s);
		return this;
	}
	
	public DaemonsetModel model() {
		return this.model;
	}
	
	public Daemonset merge() {
		
		DaemonsetModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model);
		} else {
			this.model = kube.update(found, this.model);
		}
		
		return this;
		
	}
	
	public Daemonset merge(Callback<DaemonsetModel> c) {
		
		DaemonsetModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model, c);
		} else {
			this.model = kube.update(found, this.model, c);
		}
		
		return this;
		
	}
	
	public Daemonset find() {
	
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public void delete() {
		
		kube.delete(this.model);
		
	}
	
	public static class DaemonsetSpec {
		
		private DaemonsetSpecModel model;

		public DaemonsetSpec(DaemonsetSpecModel model) {
			this.model = model;
		}

		public DaemonsetTemplate template() {
			return new DaemonsetTemplate(model.getTemplate());
		}
		
	}
	
	public static class DaemonsetTemplate {
		
		private DaemonsetTemplateModel model;

		public DaemonsetTemplate(DaemonsetTemplateModel model) {
			this.model = model;
		}

		public PodSpec podSpec() {
			return new PodSpec(model.getSpec());
		}

		public DeploymentMetadataModel metadata() {
			return this.model.getMetadata();
		}
		
	}
	
}
