/**
 * 
 */
package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.IngressModel;
import com.flyover.kube.tools.connector.model.IngressSpecModel;
import com.flyover.kube.tools.connector.model.IngressSpecModel.BackendModel;
import com.flyover.kube.tools.connector.model.IngressSpecModel.Http;
import com.flyover.kube.tools.connector.model.IngressSpecModel.Path;
import com.flyover.kube.tools.connector.model.IngressSpecModel.Rule;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

/**
 * @author mramach
 *
 */
public class Ingress {

	private Kubernetes kube;
	private IngressModel model = new IngressModel();
	
	public Ingress(Kubernetes kube) {
		this.kube = kube;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public IngressSpec spec() {
		return new IngressSpec(this.model.getSpec());
	}
	
	public Ingress merge() {
		
		IngressModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model);
		} else {
			this.model = kube.update(found, this.model);
		}
		
		return this;
		
	}
	
	public Ingress find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public static class IngressSpec {
		
		private IngressSpecModel model;

		public IngressSpec(IngressSpecModel model) {
			this.model = model;
		}
		
		public RuleSpec rule(String host) {
			
			Rule r = new Rule();
			r.setHost(host);
			r.setHttp(new Http());
			
			this.model.getRules().add(r);
			
			return new RuleSpec(r);
			
		}
		
	}
	
	public static class RuleSpec {
		
		private Rule model;

		public RuleSpec(Rule model) {
			this.model = model;
		}
		
		public RuleSpec path(String path, String serviceName, int servicePort) {
			
			BackendModel backend = new BackendModel();
			backend.setServiceName(serviceName);
			backend.setServicePort(servicePort);
			
			Path p = new Path();
			p.setBackend(backend);
			p.setPath(path);
			
			this.model.getHttp().getPaths().add(p);
			
			return this;
			
		}
		
	}

}
