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
public class HostPathVolumeModel extends VolumeModel {

	private HostPathModel hostPath;
	
	public HostPathModel getHostPath() {
		return hostPath;
	}

	public void setHostPath(HostPathModel hostPath) {
		this.hostPath = hostPath;
	}
	
	public static class HostPathModel extends Model {
		
		private String path;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}
		
	}

}
