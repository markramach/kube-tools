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
public class SecretVolumeModel extends VolumeModel {

	private SecretModel secret;
	
	public SecretModel getSecret() {
		return secret;
	}

	public void setSecretModel(SecretModel secret) {
		this.secret = secret;
	}
	
	public static class SecretModel extends Model {
		
		private String secretName;

		public String getSecretName() {
			return secretName;
		}

		public void setSecretName(String secretName) {
			this.secretName = secretName;
		}
		
	}

}
