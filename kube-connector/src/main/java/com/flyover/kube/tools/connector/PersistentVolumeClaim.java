/**
 * 
 */
package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.PersistentVolumeClaimModel;
import com.flyover.kube.tools.connector.model.PersistentVolumeClaimModel.PersistentVolumeClaimSpecModel;

/**
 * @author mramach
 *
 */
public class PersistentVolumeClaim {

	private Kubernetes kube;
	private PersistentVolumeClaimModel model = new PersistentVolumeClaimModel();
	
	public PersistentVolumeClaim(Kubernetes kube) {
		this(kube, new PersistentVolumeClaimModel());
	}
	
	public PersistentVolumeClaim(Kubernetes kube, PersistentVolumeClaimModel model) {
		this.kube = kube;
		this.model = model;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public PersistentVolumeClaimSpecModel spec() {
		return this.model.getSpec();
	}
	
	public PersistentVolumeClaim spec(PersistentVolumeClaimSpecModel spec) {
		this.model.setSpec(spec); 
		return this;
	}

	public PersistentVolumeClaim find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public PersistentVolumeClaim create() {
		
		this.model = kube.create(this.model);
		
		return this;
		
	}
	
	public PersistentVolumeClaim merge() {

		PersistentVolumeClaimModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model);
		} else {
			this.model = kube.update(found, this.model);
		}
		
		return this;
		
	}

	public void delete() {
	    kube.delete(this.model);
    }
	
	public PersistentVolumeClaimModel model() {
		return this.model;
	}

}
