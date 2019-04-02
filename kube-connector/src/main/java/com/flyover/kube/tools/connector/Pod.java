/**
 * 
 */
package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.EvictionModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.PodModel;

/**
 * @author mramach
 *
 */
public class Pod {

	private Kubernetes kube;
	private PodModel model = new PodModel();
	
	public Pod(Kubernetes kube) {
		this.kube = kube;
	}
	
	public Pod(Kubernetes kube, PodModel model) {
		this.kube = kube;
		this.model = model;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}

	public String podIP() {
		return this.model.getStatus().getPodIP();
	}
	
	public Pod find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public String exec(String command) {
		
		return kube.exec(model, command);
		
	}
	
	public Pod evict() {
		
		EvictionModel ev = new EvictionModel();
		ev.getMetadata().setNamespace(metadata().getNamespace());
		ev.getMetadata().setName(metadata().getName());
		
		kube.create(ev);
		
		return find();
		
	}
	
}
