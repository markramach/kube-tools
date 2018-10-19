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
public class IngressModel extends KubeModel {

	private IngressSpecModel spec = new IngressSpecModel();
	
	public IngressModel() {
		setApiVersion("extensions/v1beta1");
		setKind("Ingress");
	}	

	@Override
	public <T extends KubeModel> void merge(T model) {
		
		IngressModel s = (IngressModel)model;
		
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

	public IngressSpecModel getSpec() {
		return spec;
	}

	public void setSpec(IngressSpecModel spec) {
		this.spec = spec;
	}
	
}
