package com.flyover.kube.tools.connector.model;

import java.util.HashMap;
import java.util.Map;

public class ComputeResourceModel {

    private Map<String, String> limits = new HashMap<>();
    private Map<String, String> requests = new HashMap<>();

    public Map<String, String> getLimits() {
        return limits;
    }

    public void setLimits(Map<String, String> limits) {
        this.limits = limits;
    }

    public Map<String, String> getRequests() {
        return requests;
    }

    public void setRequests(Map<String, String> requests) {
        this.requests = requests;
    }


}
