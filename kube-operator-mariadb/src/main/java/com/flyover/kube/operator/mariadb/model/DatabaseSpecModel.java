/**
 * 
 */
package com.flyover.kube.operator.mariadb.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.flyover.kube.tools.connector.model.Model;

/**
 * @author mramach
 *
 */
public class DatabaseSpecModel extends Model {

	private static final int DEFAULT_REPLICAS = 3;

	private String clusterId = UUID.randomUUID().toString();
	private boolean bootstrap = true;
	private int replicas = DEFAULT_REPLICAS;
	private Map<String, String> nodeSelector = new LinkedHashMap<>();

	public int getReplicas() {
		return replicas;
	}

	public void setReplicas(int replicas) {
		this.replicas = replicas;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public boolean isBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(boolean bootstrap) {
		this.bootstrap = bootstrap;
	}

	public Map<String, String> getNodeSelector() {
		return nodeSelector;
	}

	public void setNodeSelector(Map<String, String> nodeSelector) {
		this.nodeSelector = nodeSelector;
	}
	
}
