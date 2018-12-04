package com.flyover.kube.tools.connector.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigMapModel extends KubeModel {

    private Map<String, String> data = new LinkedHashMap<>();

    public ConfigMapModel() {
        setApiVersion("v1");
        setKind("ConfigMap");
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
