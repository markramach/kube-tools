/**
 * 
 */
package com.flyover.kube.tools.connector.model;




/**
 * @author mramach
 *
 */
public class NodeModel extends KubeModel {

	private NodeSpecModel spec;
	private NodeStatusModel status;
	
	public NodeModel() {
		setApiVersion("v1");
		setKind("Node");
	}

	public NodeSpecModel getSpec() {
		return spec;
	}

	public void setSpec(NodeSpecModel spec) {
		this.spec = spec;
	}

	public NodeStatusModel getStatus() {
		return status;
	}

	public void setStatus(NodeStatusModel status) {
		this.status = status;
	}

}


