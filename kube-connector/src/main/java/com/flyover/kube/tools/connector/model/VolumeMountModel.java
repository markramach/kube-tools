/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author mramach
 *
 */
public class VolumeMountModel extends Model {

	private String name;
	private String mountPath;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String subPath;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMountPath() {
		return mountPath;
	}
	
	public void setMountPath(String mountPath) {
		this.mountPath = mountPath;
	}

	public String getSubPath() { return subPath; }

	public void setSubPath(String subPath) { this.subPath = subPath; }
	
}
