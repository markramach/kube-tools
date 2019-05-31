/**
 * 
 */
package com.flyover.kube.tools.connector.storage.model;

import com.flyover.kube.tools.connector.model.Model;
import com.flyover.kube.tools.connector.model.VolumeModel;

/**
 * @author mramach
 *
 */
public class PersistentVolumeClaimVolumeModel extends VolumeModel {
	
	private ClaimModel persistentVolumeClaim;

	public ClaimModel getPersistentVolumeClaim() {
		return persistentVolumeClaim;
	}

	public void setPersistentVolumeClaim(ClaimModel persistentVolumeClaim) {
		this.persistentVolumeClaim = persistentVolumeClaim;
	}
	
	public static class ClaimModel extends Model {
		
		private String claimName;

		public String getClaimName() {
			return claimName;
		}

		public void setClaimName(String claimName) {
			this.claimName = claimName;
		}
		
	}

}
