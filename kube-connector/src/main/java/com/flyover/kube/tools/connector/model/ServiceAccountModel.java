/**
 * 
 */
package com.flyover.kube.tools.connector.model;


/**
 * @author mramach
 *
 */
public class ServiceAccountModel extends KubeModel {
	
	public ServiceAccountModel() {
		setApiVersion("v1");
		setKind("ServiceAccount");
	}	
	
}
