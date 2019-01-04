/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mramach
 *
 */
public class SecretModel extends KubeModel {
	
	private Map<String, String> data = new LinkedHashMap<>();
	private String type;
	
	public SecretModel() {
		setApiVersion("v1");
		setKind("Secret");
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String checksum() {
		
		try {
		
			Map<String, String> annotations = new LinkedHashMap<>(getMetadata().getAnnotations());
			// This value mutates as the spec mutates and should be ignored.
			annotations.remove("com.flyover.checksum");
			
			ObjectMapper mapper = new ObjectMapper();
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(mapper.writeValueAsBytes(annotations));
			md.update(mapper.writeValueAsBytes(getData()));
			md.update(mapper.writeValueAsBytes(getKind()));
			
			return new String(Base64.getEncoder().encodeToString(md.digest()));
			
		} catch (Exception e) {
			throw new RuntimeException("failed to create checksum", e);
		}
		
	}
	
}
