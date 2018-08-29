/**
 * 
 */
package com.flyover.kube.operator.mariadb.storage.clc;

import com.flyover.kube.tools.connector.Kubernetes;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

/**
 * @author mramach
 *
 */
public class ClcVolume {

	private Kubernetes kube;
	private ClcVolumeModel model = new ClcVolumeModel();
	
	public ClcVolume(Kubernetes kube) {
		this.kube = kube;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public ClcVolume merge() {
		
		ClcVolumeModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model);
		} else {
			this.model = kube.update(found, this.model);
		}
		
		return this;
		
	}
	
	public ClcVolume find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public ClcVolumeModel model() {
		return this.model;
	}

}
