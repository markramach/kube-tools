package com.flyover.kube.tools.connector.model;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PersistentVolumeClaimModel extends KubeModel {

	private PersistentVolumeClaimSpecModel spec = new PersistentVolumeClaimSpecModel();
	
    public PersistentVolumeClaimModel() {
        setApiVersion("v1");
        setKind("PersistentVolumeClaim");
    }

    @Override
    public <T extends KubeModel> void merge(T model) {

        PersistentVolumeClaimModel s = (PersistentVolumeClaimModel) model;
        
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
    
    public PersistentVolumeClaimSpecModel getSpec() {
		return spec;
	}

	public void setSpec(PersistentVolumeClaimSpecModel spec) {
		this.spec = spec;
	}

	@JsonInclude(Include.NON_EMPTY)
	public static class PersistentVolumeClaimSpecModel extends Model {
    	
		private List<AccessMode> accessModes = new LinkedList<>(Arrays.asList(AccessMode.ReadWriteOnce));
		private VolumeMode volumeMode = VolumeMode.Filesystem;
		private Requests resources;
		private String storageClassName;
		
		public List<AccessMode> getAccessModes() {
			return accessModes;
		}
		
		public void setAccessModes(List<AccessMode> accessModes) {
			this.accessModes = accessModes;
		}
		
		public VolumeMode getVolumeMode() {
			return volumeMode;
		}
		
		public void setVolumeMode(VolumeMode volumeMode) {
			this.volumeMode = volumeMode;
		}
		
		public Requests getResources() {
			return resources;
		}
		
		public void setResources(Requests resources) {
			this.resources = resources;
		}
		
		public String getStorageClassName() {
			return storageClassName;
		}
		
		public void setStorageClassName(String storageClassName) {
			this.storageClassName = storageClassName;
		}
		
    }
	
	public static class Requests extends Model {
		
		private Storage requests;

		public Storage getRequests() {
			return requests;
		}

		public void setRequests(Storage requests) {
			this.requests = requests;
		}
		
	}

	public static class Storage extends Model {
		
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
	
	public static enum VolumeMode {
		
		Filesystem
		
	}
	
}
