/**
 * 
 */
package com.flyover.kube.tools.connector.storage.model;

import java.util.LinkedList;
import java.util.List;

import com.flyover.kube.tools.connector.model.Model;
import com.flyover.kube.tools.connector.model.VolumeModel;

/**
 * @author mramach
 *
 */
public class RbdVolumeModel extends VolumeModel {

	private RbdModel rbd = new RbdModel();
	
	public RbdModel getRbd() {
		return rbd;
	}

	public void setRbd(RbdModel rbd) {
		this.rbd = rbd;
	}

	public static class RbdModel extends Model {
		
		private String user;
		private String image;
		private String pool;
		private List<String> monitors = new LinkedList<>();
		private SecretRef secretRef = new SecretRef();
		
		public String getUser() {
			return user;
		}
		
		public void setUser(String user) {
			this.user = user;
		}
		
		public String getImage() {
			return image;
		}
		
		public void setImage(String image) {
			this.image = image;
		}
		
		public String getPool() {
			return pool;
		}
		
		public void setPool(String pool) {
			this.pool = pool;
		}
		
		public List<String> getMonitors() {
			return monitors;
		}
		
		public void setMonitors(List<String> monitors) {
			this.monitors = monitors;
		}

		public SecretRef getSecretRef() {
			return secretRef;
		}

		public void setSecretRef(SecretRef secretRef) {
			this.secretRef = secretRef;
		}
		
	}
	
	public static class SecretRef extends Model {
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}

}
