package com.flyover.kube.tools.connector.storage.model;

import java.util.LinkedList;
import java.util.List;

import com.flyover.kube.tools.connector.model.Model;
import com.flyover.kube.tools.connector.model.VolumeModel;

public class ConfigMapVolumeModel extends VolumeModel {
	
	private ConfigMapModel configMap = new ConfigMapModel();
	
	public ConfigMapModel getConfigMap() {
		return configMap;
	}

	public void setConfigMap(ConfigMapModel configMap) {
		this.configMap = configMap;
	}

	public static class ConfigMapModel extends Model {
		
		private String name;
		private List<ConfigMapItemModel> items = new LinkedList<>();
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public List<ConfigMapItemModel> getItems() {
			return items;
		}
		
		public void setItems(List<ConfigMapItemModel> items) {
			this.items = items;
		}
		
	}

    public static class ConfigMapItemModel extends Model {
    	
        private String key;
        private String path;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
        
    }
}
