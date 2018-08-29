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
public class PodModel extends KubeModel {

	private PodSpecModel spec = new PodSpecModel();
	private PodStatusModel status = new PodStatusModel();
	
	public PodModel() {
		setApiVersion("v1");
		setKind("Pod");
	}

	@Override
	public <T extends KubeModel> void merge(T model) {
		
		PodModel d = (PodModel)model;
		
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

	public PodSpecModel getSpec() {
		return spec;
	}

	public void setSpec(PodSpecModel spec) {
		this.spec = spec;
	}

	public PodStatusModel getStatus() {
		return status;
	}

	public void setStatus(PodStatusModel status) {
		this.status = status;
	}
	
}


