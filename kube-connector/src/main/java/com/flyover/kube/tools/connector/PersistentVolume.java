/**
 * 
 */
package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.PersistentVolumeModel;
import com.flyover.kube.tools.connector.model.PersistentVolumeModel.PersistentVolumeSpecModel;

/**
 * @author mramach
 *
 */
public class PersistentVolume {

	private Kubernetes kube;
	private PersistentVolumeModel model = new PersistentVolumeModel();
	
	public PersistentVolume(Kubernetes kube) {
		this(kube, new PersistentVolumeModel());
	}
	
	public PersistentVolume(Kubernetes kube, PersistentVolumeModel model) {
		this.kube = kube;
		this.model = model;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public PersistentVolumeSpecModel spec() {
		return this.model.getSpec();
	}
	
	public PersistentVolume spec(PersistentVolumeSpecModel spec) {
		this.model.setSpec(spec); 
		return this;
	}

	public PersistentVolume find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public PersistentVolume create() {
		
		this.model = kube.create(this.model);
		
		return this;
		
	}
	
	public PersistentVolume merge() {

		PersistentVolumeModel found = kube.find(this.model);
		
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
	
	public PersistentVolumeModel model() {
		return this.model;
	}

}
