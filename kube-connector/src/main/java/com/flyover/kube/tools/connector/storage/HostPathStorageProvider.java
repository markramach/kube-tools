/**
 * 
 */
package com.flyover.kube.tools.connector.storage;

import java.util.UUID;

import com.flyover.kube.tools.connector.Kubernetes;
import com.flyover.kube.tools.connector.Volume;
import com.flyover.kube.tools.connector.storage.model.HostPathVolumeModel;
import com.flyover.kube.tools.connector.storage.model.HostPathVolumeModel.HostPathModel;

/**
 * @author mramach
 *
 */
public class HostPathStorageProvider implements StorageProvider {
	
	private String root = "/tmp";
	
	/* (non-Javadoc)
	 * @see com.flyover.kube.tools.connector.storage.StorageProvider#build(java.lang.String)
	 */
	@Override
	public Volume build(Kubernetes kube, String namespace, String name, String alias) {
		
		HostPathModel hostPath = new HostPathModel();
		hostPath.setPath(String.format("%s/%s", root, UUID.randomUUID().toString()));
		
		HostPathVolumeModel model = new HostPathVolumeModel();
		model.setName(alias);
		model.setHostPath(hostPath);
		
		return new Volume(model);
		
	}

	public void setRoot(String root) {
		this.root = root;
	}

}
