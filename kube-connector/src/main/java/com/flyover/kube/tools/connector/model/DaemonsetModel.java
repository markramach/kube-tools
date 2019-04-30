/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.security.MessageDigest;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author mramach
 *
 */
public class DaemonsetModel extends KubeModel {

	private DaemonsetSpecModel spec = new DaemonsetSpecModel();
	
	public DaemonsetModel() {
		setApiVersion("extensions/v1beta1");
		setKind("DaemonSet");
	}

	@Override
	public <T extends KubeModel> void merge(T model) {
		
		DaemonsetModel d = (DaemonsetModel)model;
		
		super.merge(model);
		setSpec(d.getSpec());
		
	}

	@Override
	public String checksum() {
		
		try {
		
			String data = new ObjectMapper().writeValueAsString(spec);
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			return new String(Base64.getEncoder().encodeToString(md.digest(data.getBytes())));
			
		} catch (Exception e) {
			throw new RuntimeException("failed to create checksum", e);
		}
		
	}

	public DaemonsetSpecModel getSpec() {
		return spec;
	}

	public void setSpec(DaemonsetSpecModel spec) {
		this.spec = spec;
	}
	
}


