package com.flyover.kube.tools.connector.storage.model;

import com.flyover.kube.tools.connector.model.VolumeModel;

import java.util.List;

public class ConfigMapVolumeModel extends VolumeModel {
    private Integer defaultMode = 420;
    private List<KeyToPathModel> items = null;

    public Integer getDefaultMode() {
        return defaultMode;
    }

    public void setDefaultMode(Integer defaultMode) {
        this.defaultMode = defaultMode;
    }

    public List<KeyToPathModel> getItems() {
        return items;
    }

    public void setItems(List<KeyToPathModel> items) {
        this.items = items;
    }

    public static class KeyToPathModel{
        private String key = null;
        private String path = null;

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
