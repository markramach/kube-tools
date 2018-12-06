/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.SecretModel;

/**
 * @author mramach
 *
 */
public class Secret {

	private Kubernetes kube;
	private SecretModel model = new SecretModel();
	
	public Secret(Kubernetes kube) {
		this.kube = kube;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public Map<String, String> data() {
		return this.model.getData();
	}
	
	public Secret data(String key, String value) {
		this.model.getData().put(key, Base64.getEncoder().encodeToString(value.getBytes())); 
		return this;
	}

	public Secret data(Map<String, String> secrets ) {
		for(String key: secrets.keySet()) {
			this.model.getData().put(key, Base64.getEncoder().encodeToString(secrets.get(key).getBytes()));
		}
		return this;
	}
	
	public String data(String key) {
		return new String(Base64.getDecoder().decode(this.model.getData().get(key))); 
	}

	public Secret dockerconfigjson(String registry, String username, String password) {
		
		Map<String, Object> reg = Collections.singletonMap("auth", 
			Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes()));
		
		Map<String, Object> auths = Collections.singletonMap(registry, reg);
		
		Map<String, Object> data = Collections.singletonMap("auths", auths);
		
		try {
			
			String dockerconfigjson = new ObjectMapper().writeValueAsString(data);
			
			return type("kubernetes.io/dockerconfigjson")
				.data(".dockerconfigjson", dockerconfigjson);
			
		} catch (Exception e) {
			throw new RuntimeException("failed while building dockerconfigjson", e);
		}
		
	}
	
	public String type() {
		return this.model.getType();
	}
	
	public Secret type(String type) {
		this.model.setType(type);
		return this;
	}

	public Secret find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public Secret create() {
		
		this.model = kube.create(this.model);
		
		return this;
		
	}
	
	public Secret replace() {
		
		kube.delete(this.model);
		
		return create();
		
	}

	public void delete() {
	    kube.delete(this.model);
    }

}
