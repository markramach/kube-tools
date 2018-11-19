package com.flyover.kube.tools.connector.model;

import java.util.List;
import java.util.Map;

public class NetworkPolicyEgressRuleModel extends Model {

    private Map<String, String> allowAll;

    private List<NetworkPolicyPeerModel> to;

    public Map<String, String> getAllowAll() { return allowAll; }

    public void setAllowAll(Map<String, String> allowAll) { this.allowAll = allowAll; }

    public List<NetworkPolicyPeerModel> getTo() {
        return to;
    }

    public void setTo(List<NetworkPolicyPeerModel> to) {
        this.to = to;
    }
}
