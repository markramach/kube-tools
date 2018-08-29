/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.Map;

import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.PortTargetModel;
import com.flyover.kube.tools.connector.model.ServiceModel;
import com.flyover.kube.tools.connector.model.ServiceSpecModel;

/**
 * @author mramach
 *
 */
public class Service {

	private Kubernetes kube;
	private ServiceModel model = new ServiceModel();
	
	public Service(Kubernetes kube) {
		this.kube = kube;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public ServiceSpec spec() {
		return new ServiceSpec(this.model.getSpec());
	}
	
	public Service merge() {
		
		ServiceModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model);
		} else {
			this.model = kube.update(found, this.model);
		}
		
		return this;
		
	}
	
	public Service find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public void delete() {
		
		kube.delete(this.model);
		
	}
	
	public static class ServiceSpec {
		
		private ServiceSpecModel model;

		public ServiceSpec(ServiceSpecModel model) {
			this.model = model;
		}
		
		public String clusterIP() {
			return this.model.getClusterIP();
		}

		public Map<String, String> selectors() {
			return this.model.getSelector();
		}

		public ServiceSpec tcpPort(int port) {
			
			return tcpPort("port-" + String.valueOf(model.getPorts().size()), port);
			
		}
		
		public ServiceSpec tcpPort(String name, int port) {
			
			PortTargetModel p = new PortTargetModel();
			p.setName(name);
			p.setProtocol("TCP");
			p.setPort(port);
			p.setTargetPort(port);
			
			model.getPorts().add(p);
			
			return this;
			
		}
		
	}

}
