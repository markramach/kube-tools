/**
 * 
 */
package com.flyover.kube.operator.mariadb.storage.clc;

import java.util.List;

import com.flyover.kube.tools.connector.Kubernetes;
import com.flyover.kube.tools.connector.Volume;
import com.flyover.kube.tools.connector.storage.StorageProvider;
import com.flyover.kube.tools.connector.storage.model.RbdVolumeModel;

/**
 * @author mramach
 *
 */
public class ClcStorageProvider implements StorageProvider {
	
	private List<String> monitors;
	private String pool;
	private String location;
	private int size;
	
	public ClcStorageProvider(List<String> monitors, String pool, String location, int size) {
		this.monitors = monitors;
		this.pool = pool;
		this.location = location;
		this.size = size;
	}

	@Override
	public Volume build(Kubernetes kube, String namespace, String name, String alias) {
		
		ClcVolume v = clcVolume(kube, namespace, name).find();
		
		if(v == null) {
			
			v = clcVolume(kube, namespace, name);
			v.metadata().getLabels().put("location", location);
			v.model().getSpec().setSize(size);
			
		}
		
		v = v.merge();
		
		RbdVolumeModel model = new RbdVolumeModel();
		model.setName(alias);
		model.getRbd().setMonitors(monitors);
		model.getRbd().setUser(name);
		model.getRbd().setImage(name);
		model.getRbd().setPool(pool);
		model.getRbd().getSecretRef().setName(name);
		
		return new Volume(model);
		
	}
	
	private ClcVolume clcVolume(Kubernetes kube, String namespace, String name) {
		
		ClcVolume v = new ClcVolume(kube);
		v.metadata().setNamespace(namespace);
		v.metadata().setName(name);
		
		return v;
		
	}

}
