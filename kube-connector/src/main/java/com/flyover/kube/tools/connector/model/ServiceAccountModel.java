/**
 * 
 */
package com.flyover.kube.tools.connector.model;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mramach
 *
 */
public class ServiceAccountModel extends KubeModel {
	
	public ServiceAccountModel() {
		setApiVersion("v1");
		setKind("ServiceAccount");
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

			return new String(Base64.getEncoder().encodeToString(md.digest()));

		} catch (Exception e) {
			throw new RuntimeException("failed to create checksum", e);
		}

	}
}
