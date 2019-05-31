package com.flyover.kube.tools.connector.model;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PersistentVolumeModel extends KubeModel {

	private PersistentVolumeSpecModel spec = new PersistentVolumeSpecModel();
	
    public PersistentVolumeModel() {
        setApiVersion("v1");
        setKind("PersistentVolume");
    }

    @Override
    public <T extends KubeModel> void merge(T model) {

        PersistentVolumeModel s = (PersistentVolumeModel) model;
        
        super.merge(model);
        setSpec(s.getSpec());

    }

    @Override
    public String checksum() {

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(new ObjectMapper().writeValueAsString(getSpec()).getBytes());

            return new String(Base64.getEncoder().encodeToString(digest));

        } catch (Exception e) {
            throw new RuntimeException("failed to create checksum", e);
        }

    }
    
    public PersistentVolumeSpecModel getSpec() {
		return spec;
	}

	public void setSpec(PersistentVolumeSpecModel spec) {
		this.spec = spec;
	}

	@JsonInclude(Include.NON_EMPTY)
	public static class PersistentVolumeSpecModel extends Model {
    	
		private Capacity capacity;
		private List<AccessMode> accessModes = new LinkedList<>(Arrays.asList(AccessMode.ReadWriteOnce));
		private PersistentVolumeReclaimPolicy persistentVolumeReclaimPolicy = PersistentVolumeReclaimPolicy.Delete;
		private String storageClassName;
		
		public Capacity getCapacity() {
			return capacity;
		}
		
		public void setCapacity(Capacity capacity) {
			this.capacity = capacity;
		}
		
		public List<AccessMode> getAccessModes() {
			return accessModes;
		}
		
		public void setAccessModes(List<AccessMode> accessModes) {
			this.accessModes = accessModes;
		}
		
		public PersistentVolumeReclaimPolicy getPersistentVolumeReclaimPolicy() {
			return persistentVolumeReclaimPolicy;
		}
		
		public void setPersistentVolumeReclaimPolicy(
				PersistentVolumeReclaimPolicy persistentVolumeReclaimPolicy) {
			this.persistentVolumeReclaimPolicy = persistentVolumeReclaimPolicy;
		}
		
		public String getStorageClassName() {
			return storageClassName;
		}
		
		public void setStorageClassName(String storageClassName) {
			this.storageClassName = storageClassName;
		}
		
    }
	
	public static class LocalPersistentVolumeSpecModel extends PersistentVolumeSpecModel {
		
		private Path local;
		private NodeAffinity nodeAffinity;

		public Path getLocal() {
			return local;
		}

		public void setLocal(Path local) {
			this.local = local;
		}

		public NodeAffinity getNodeAffinity() {
			return nodeAffinity;
		}

		public void setNodeAffinity(NodeAffinity nodeAffinity) {
			this.nodeAffinity = nodeAffinity;
		}
		
	}

	public static class Capacity extends Model {
		
		private String storage;

		public String getStorage() {
			return storage;
		}

		public void setStorage(String storage) {
			this.storage = storage;
		}
		
	}
	
	public static enum AccessMode {
		
		ReadWriteOnce
		
	}
	
	public static enum PersistentVolumeReclaimPolicy {
		
		Delete
		
	}
	
	public static class Path {
		
		private String path;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}
		
	}
	
	public static class NodeAffinity extends Model {
		
		private NodeAffinityRequired required;

		public NodeAffinityRequired getRequired() {
			return required;
		}

		public void setRequired(NodeAffinityRequired required) {
			this.required = required;
		}
		
	}
	
	public static class NodeAffinityRequired extends Model {
		
		private List<NodeSelectorTerm> nodeSelectorTerms = new LinkedList<>();

		public List<NodeSelectorTerm> getNodeSelectorTerms() {
			return nodeSelectorTerms;
		}

		public void setNodeSelectorTerms(List<NodeSelectorTerm> nodeSelectorTerms) {
			this.nodeSelectorTerms = nodeSelectorTerms;
		}

	}
	
	public static class NodeSelectorTerm extends Model {
		
		private List<Map<String, Object>>  matchExpressions = new LinkedList<>();

		public List<Map<String, Object>> getMatchExpressions() {
			return matchExpressions;
		}

		public void setMatchExpressions(List<Map<String, Object>> matchExpressions) {
			this.matchExpressions = matchExpressions;
		}

	}
	
}
