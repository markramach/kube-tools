/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.NodeModel;

/**
 * @author mramach
 *
 */
public class KubernetesNode {
	
	private Kubernetes kube;
	private NodeModel model;

	public KubernetesNode(Kubernetes kube) {
		this.kube = kube;
		this.model = new NodeModel();
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public KubernetesNode find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public void delete() {
		
		kube.delete(model);
		
	}

	public KubernetesNode cordon() {
		
		this.model.getSpec().any().put("unschedulable", true);
		
		Map<Object, Object> patch = new LinkedHashMap<Object, Object>();
		patch.put("spec", Collections.singletonMap("unschedulable", true));
		
		this.model = kube.patch(this.model, patch);
		
		return this;
		
	}
	
	public KubernetesNode uncordon() {
		
		Map<Object, Object> patch = new LinkedHashMap<Object, Object>();
		patch.put("spec", Collections.singletonMap("unschedulable", null));
		
		this.model = kube.patch(this.model, patch);
		
		return this;
		
	}

}
