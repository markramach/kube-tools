/**
 * 
 */
package com.flyover.kube.tools.connector.model;

/**
 * @author mramach
 *
 */
public class DaemonsetV1Model extends DaemonsetModel {

	public DaemonsetV1Model() {
		setApiVersion("apps/v1");
		setKind("DaemonSet");
	}
	
}
