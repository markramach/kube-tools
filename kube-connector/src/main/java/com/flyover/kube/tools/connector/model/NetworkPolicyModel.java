package com.flyover.kube.tools.connector.model;

import java.security.MessageDigest;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;


public class NetworkPolicyModel extends KubeModel {

    private NetworkPolicySpecModel spec = new NetworkPolicySpecModel();

    public NetworkPolicyModel() {
        setApiVersion("networking.k8s.io/v1");
        setKind("NetworkPolicy");
    }
    
	@Override
	public <T extends KubeModel> void merge(T model) {
		
		NetworkPolicyModel d = (NetworkPolicyModel)model;
		
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

    public NetworkPolicySpecModel getSpec() { return spec; }

    public void setSpec(NetworkPolicySpecModel spec) { this.spec = spec; }
}
