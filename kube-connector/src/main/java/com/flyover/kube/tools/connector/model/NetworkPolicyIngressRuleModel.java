package com.flyover.kube.tools.connector.model;

import java.util.List;

public class NetworkPolicyIngressRuleModel {

    private List<NetworkPolicyPeerModel> from;

    public List<NetworkPolicyPeerModel> getFrom() {
        return from;
    }

    public void setFrom(List<NetworkPolicyPeerModel> from) {
        this.from = from;
    }
}
