/**
 * 
 */
package com.flyover.kube.operator.mariadb.companion.model;

import java.security.MessageDigest;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flyover.kube.tools.connector.model.KubeModel;

/**
 * @author mramach
 *
 */
public class DatabaseUserModel extends KubeModel {
	
	private DatabaseUserSpecModel spec = new DatabaseUserSpecModel();
	
	public DatabaseUserModel() {
		setApiVersion("mariadb.operator.flyover.com/v1");
		setKind("DatabaseUser");
	}
	
	@Override
	public <T extends KubeModel> void merge(T model) {
		
		DatabaseUserModel d = (DatabaseUserModel)model;
		
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

	public DatabaseUserSpecModel getSpec() {
		return spec;
	}

	public void setSpec(DatabaseUserSpecModel spec) {
		this.spec = spec;
	}

}
