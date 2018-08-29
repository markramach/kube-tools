/**
 * 
 */
package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.VolumeModel;

/**
 * @author mramach
 *
 */
public class Volume {
	
	private VolumeModel model;

	public Volume(VolumeModel model) {
		this.model = model;
	}
	
	public String name() {
		return this.model.getName();
	}
	
	protected VolumeModel model() {
		return this.model;
	}
	
}
