/**
 * 
 */
package com.flyover.kube.tools.connector.storage.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.flyover.kube.tools.connector.model.VolumeModel;

/**
 * @author mramach
 *
 */
public class EmptyDirVolumeModel extends VolumeModel {

	private Map<String, Object> emptyDir = new LinkedHashMap<>();

	public Map<String, Object> getEmptyDir() {
		return emptyDir;
	}

	public void setEmptyDir(Map<String, Object> emptyDir) {
		this.emptyDir = emptyDir;
	}

}
