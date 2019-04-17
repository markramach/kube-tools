package com.flyover.kube.tools.connector;

import java.util.Map;

import com.flyover.kube.tools.connector.model.ConfigMapModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

public class ConfigMap {
    private Kubernetes kube;
    private ConfigMapModel model = new ConfigMapModel();

    public KubeMetadataModel metadata() {
        return this.model.getMetadata();
    }

    public ConfigMap(Kubernetes kube) {
        this.kube = kube;
    }

    public Map<String, String> data() {
        return this.model.getData();
    }

    public ConfigMap data(String key, String value) {
        this.model.getData().put(key, value);
        return this;
    }

    public String data(String key) {
        return this.model.getData().get(key);
    }

    public ConfigMap find() {

        this.model = kube.find(this.model);

        if(this.model == null) {
            return null;
        }

        return this;
    }

    public ConfigMap create() {

        this.model = kube.create(this.model);

        return this;
    }
    
	public ConfigMap merge() {
		
		ConfigMapModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model);
		} else {
			this.model = kube.update(found, this.model);
		}
		
		return this;
		
	}

    public ConfigMap replace() {

        kube.delete(this.model);

        return create();
    }
    
	public void delete() {
	    kube.delete(this.model);
    }
	
}
