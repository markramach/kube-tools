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
	
	public Service merge(Callback<ServiceModel> c) {
		
		ServiceModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model, c);
		} else {
			this.model = kube.update(found, this.model, c);
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
		
		public ServiceSpec clusterIP(String clusterIP) {
			this.model.setClusterIP(clusterIP);
			return this;
		}

		public Map<String, String> selectors() {
			return this.model.getSelector();
		}

		public ServiceSpec udpPort(int port) {
			return udpPort("port-" + String.valueOf(model.getPorts().size()), port);
		}

		public ServiceSpec udpPort(int port, int nodeport) {
			return udpPort("port-" + String.valueOf(model.getPorts().size()), port, nodeport);
		}

		public ServiceSpec udpwithTargetPort(int port, int targetPort) {
			return udpWithTargetPort("port-" + String.valueOf(model.getPorts().size()), port, targetPort);
		}

		public ServiceSpec udpPort(String name, int port) {
			PortTargetModel p = new PortTargetModel();
			p.setName(name);
			p.setProtocol("UDP");
			p.setPort(port);
			p.setTargetPort(port);
			model.getPorts().add(p);

			return this;
		}

		public ServiceSpec udpPort(String name, int port, int nodeport) {
			PortTargetModel p = new PortTargetModel();
			p.setName(name);
			p.setProtocol("UDP");
			p.setPort(port);
			p.setTargetPort(port);
			p.setNodePort(nodeport);

			model.getPorts().add(p);

			return this;
		}

		public ServiceSpec udpWithTargetPort(String name, int port, int targetPort) {
			PortTargetModel p = new PortTargetModel();
			p.setName(name);
			p.setProtocol("UDP");
			p.setPort(port);
			p.setTargetPort(targetPort);
			model.getPorts().add(p);

			return this;
		}

		public ServiceSpec tcpPort(int port) {
			
			return tcpPort("port-" + String.valueOf(model.getPorts().size()), port);
			
		}
		
		public ServiceSpec tcpPort(int port, int nodeport) {
			
			return tcpPort("port-" + String.valueOf(model.getPorts().size()), port, nodeport);
			
		}

		public ServiceSpec tcpWithTargetPort(int port, int targetPort) {

			return tcpWithTargetPort("port-" + String.valueOf(model.getPorts().size()), port, targetPort);

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
		
		public ServiceSpec tcpPort(String name, int port, int nodeport) {

			PortTargetModel p = new PortTargetModel();
			p.setName(name);
			p.setProtocol("TCP");
			p.setPort(port);
			p.setTargetPort(port);
			p.setNodePort(nodeport);

			model.getPorts().add(p);

			return this;

		}

		public ServiceSpec tcpWithTargetPort(String name, int port, int targetPort) {

			PortTargetModel p = new PortTargetModel();
			p.setName(name);
			p.setProtocol("TCP");
			p.setPort(port);
			p.setTargetPort(targetPort);

			model.getPorts().add(p);

			return this;

		}
		
		public ServiceSpec type(String type) {
			this.model.setType(type);
			return this;
		}
		
	}

}
