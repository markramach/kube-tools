package com.flyover.kube.tools.connector.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NetworkPolicyEgressRuleModel extends Model {

    private List<Map<String, Object>> to;
    private List<Port> ports;

    public List<Map<String, Object>> getTo() {
    	
    	if(to == null) {
    		to = new LinkedList<>();
    	}
    	
        return to;
        
    }

    public void setTo(List<Map<String, Object>> to) {
        this.to = to;
    }
    
    public List<Port> getPorts() {
		return ports;
	}

	public void setPorts(List<Port> ports) {
		this.ports = ports;
	}

	public static class Port extends Model {
    	
    	private String protocol;
    	private int port;
    	
		public String getProtocol() {
			return protocol;
		}
		
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
		
		public int getPort() {
			return port;
		}
		
		public void setPort(int port) {
			this.port = port;
		}
    	
    }
}
