package com.flyover.kube.tools.connector.model;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigMapModel extends KubeModel {

    private Map<String, String> data = new LinkedHashMap<>();

    public ConfigMapModel() {
        setApiVersion("v1");
        setKind("ConfigMap");
    }
    
	@Override
	public <T extends KubeModel> void merge(T model) {
		
		ConfigMapModel s = (ConfigMapModel)model;
		
		super.merge(model);
		setData(s.getData());
		
	}
	
	@Override
	public String checksum() {
		
		try {
		
			ObjectMapper mapper = new ObjectMapper();
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(mapper.writeValueAsBytes(getData()));
			
			return new String(Base64.getEncoder().encodeToString(md.digest()));
			
		} catch (Exception e) {
			throw new RuntimeException("failed to create checksum", e);
		}
		
	}

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
