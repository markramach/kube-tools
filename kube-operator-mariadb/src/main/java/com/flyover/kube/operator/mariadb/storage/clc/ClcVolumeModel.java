/**
 * 
 */
package com.flyover.kube.operator.mariadb.storage.clc;

import java.security.MessageDigest;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flyover.kube.tools.connector.model.KubeModel;

/**
 * @author mramach
 *
 */
public class ClcVolumeModel extends KubeModel {

	private ClcVolumeSpecModel spec = new ClcVolumeSpecModel();
	
	public ClcVolumeModel() {
		setApiVersion("cd.ctl.io/v1");
		setKind("CephVolume");
	}
	
	@Override
	public <T extends KubeModel> void merge(T model) {
		
		ClcVolumeModel s = (ClcVolumeModel)model;
		
		super.merge(model);
		setSpec(s.getSpec());
		
	}
	
	@Override
	public String checksum() {
		
		try {
		
			ObjectMapper mapper = new ObjectMapper();
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(mapper.writeValueAsBytes(getSpec()));
			
			return new String(Base64.getEncoder().encodeToString(md.digest()));
			
		} catch (Exception e) {
			throw new RuntimeException("failed to create checksum", e);
		}
		
	}

	public ClcVolumeSpecModel getSpec() {
		return spec;
	}

	public void setSpec(ClcVolumeSpecModel spec) {
		this.spec = spec;
	}
	
}
