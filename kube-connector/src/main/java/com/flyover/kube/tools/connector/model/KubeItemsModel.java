/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author mramach
 *
 */
public class KubeItemsModel<T> extends Model {

	private String kind;
	private String apiVersion;
	private KubeMetadataModel metadata;
	private List<T> items = new LinkedList<T>();

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public KubeMetadataModel getMetadata() {
		return metadata;
	}

	public void setMetadata(KubeMetadataModel metadata) {
		this.metadata = metadata;
	}
	
	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

}
