/**
 * 
 */
package com.flyover.kube.tools.connector.model;


/**
 * @author mramach
 *
 */
public class EvictionModel extends KubeModel {
	
	public EvictionModel() {
		setApiVersion("policy/v1beta1");
		setKind("Eviction");
	}
	
}
