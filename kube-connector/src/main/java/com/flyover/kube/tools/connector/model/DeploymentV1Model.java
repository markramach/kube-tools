/**
 * 
 */
package com.flyover.kube.tools.connector.model;

/**
 * @author mramach
 *
 */
public class DeploymentV1Model extends DeploymentModel {
	
	public DeploymentV1Model() {
		setApiVersion("apps/v1");
		setKind("Deployment");
	}

}
