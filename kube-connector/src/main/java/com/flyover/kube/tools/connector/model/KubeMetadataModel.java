/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



/**
 * @author mramach
 *
 */
public class KubeMetadataModel extends Model {
	
	private String name;
	private String namespace;
	@JsonInclude(Include.NON_NULL)
	private Integer generation;
	private Map<String, String> labels = new LinkedHashMap<>();
	private Map<String, String> annotations = new LinkedHashMap<>();
	@JsonInclude(Include.NON_EMPTY)
	private List<OwnerReferenceModel> ownerReferences = new LinkedList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Integer getGeneration() {
		return generation;
	}

	public void setGeneration(Integer generation) {
		this.generation = generation;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public Map<String, String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Map<String, String> annotations) {
		this.annotations = annotations;
	}
	
//	  ownerReferences:
//		  - apiVersion: apps/v1
//		    blockOwnerDeletion: true
//		    controller: true
//		    kind: DaemonSet
//		    name: weave-net
//		    uid: 03dbcd14-ab04-11e8-bbe8-000c29e72f0d
	
	public List<OwnerReferenceModel> getOwnerReferences() {
		return ownerReferences;
	}

	public void setOwnerReferences(List<OwnerReferenceModel> ownerReferences) {
		this.ownerReferences = ownerReferences;
	}

	public static class OwnerReferenceModel extends Model {
		
		private String kind;
		private String name;
		
		public String getKind() {
			return kind;
		}
		
		public void setKind(String kind) {
			this.kind = kind;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}

}
