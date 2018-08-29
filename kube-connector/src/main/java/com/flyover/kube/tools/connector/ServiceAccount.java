/**
 * 
 */
package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.ServiceAccountModel;

/**
 * @author mramach
 *
 */
public class ServiceAccount {

	private Kubernetes kube;
	private ServiceAccountModel model = new ServiceAccountModel();
	
	public ServiceAccount(Kubernetes kube) {
		this.kube = kube;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public ServiceAccount findOrCreate() {
		
		ServiceAccountModel found = kube.find(this.model);
		
		this.model = found != null ? found : kube.create(this.model); 
		
		return this;
		
	}

}
