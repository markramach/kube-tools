/**
 * 
 */
package com.flyover.kube.operator.mariadb.model;

import java.security.MessageDigest;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flyover.kube.tools.connector.model.KubeModel;

/**
 * @author mramach
 *
 */
public class DatabaseModel extends KubeModel {
	
	private DatabaseSpecModel spec = new DatabaseSpecModel();
	
	public DatabaseModel() {
		setApiVersion("mariadb.operator.flyover.com/v1");
		setKind("Database");
	}
	
	@Override
	public <T extends KubeModel> void merge(T model) {
		
		DatabaseModel d = (DatabaseModel)model;
		
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

	public DatabaseSpecModel getSpec() {
		return spec;
	}

	public void setSpec(DatabaseSpecModel spec) {
		this.spec = spec;
	}

}
