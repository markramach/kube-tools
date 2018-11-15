package com.flyover.kube.tools.connector.model;

import java.util.List;

public class NetworkPolicyEgressRuleModel {

    private List<NetworkPolicyPeerModel> to;

    public List<NetworkPolicyPeerModel> getTo() {
        return to;
    }

    public void setTo(List<NetworkPolicyPeerModel> to) {
        this.to = to;
    }
}
