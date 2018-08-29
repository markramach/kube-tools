/**
 * 
 */
package com.flyover.kube.tools.connector;

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
	
	public String exec(String command) {
		
		return kube.exec(model, command);
		
	}
	
}
