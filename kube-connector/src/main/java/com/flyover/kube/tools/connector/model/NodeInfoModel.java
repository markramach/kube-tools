/**
 * 
 */
package com.flyover.kube.tools.connector.model;


/**
 * @author mramach
 *
 */
public class NodeInfoModel extends Model {

	private String kubeletVersion;
	private String kubeProxyVersion;
	
	public String getKubeletVersion() {
		return kubeletVersion;
	}
	
	public void setKubeletVersion(String kubeletVersion) {
		this.kubeletVersion = kubeletVersion;
	}
	
	public String getKubeProxyVersion() {
		return kubeProxyVersion;
	}

	public void setKubeProxyVersion(String kubeProxyVersion) {
		this.kubeProxyVersion = kubeProxyVersion;
	}
	
}
