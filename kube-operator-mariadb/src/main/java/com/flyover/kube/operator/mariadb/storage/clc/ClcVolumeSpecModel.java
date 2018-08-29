/**
 * 
 */
package com.flyover.kube.operator.mariadb.storage.clc;

import com.flyover.kube.tools.connector.model.Model;

/**
 * @author mramach
 *
 */
public class ClcVolumeSpecModel extends Model {

	private int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
