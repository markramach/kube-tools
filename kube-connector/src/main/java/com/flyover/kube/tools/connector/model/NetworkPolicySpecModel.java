package com.flyover.kube.tools.connector.model;

import java.util.List;

public class NetworkPolicySpecModel extends Model {

    private SelectorModel podSelector;
    private List<NetworkPolicyIngressRuleModel> ingress;

    public SelectorModel getPodSelector() {
        return podSelector;
    }

    public void setPodSelector(SelectorModel podSelector) {
        this.podSelector = podSelector;
    }

    public List<NetworkPolicyIngressRuleModel> getIngress() {
        return ingress;
    }

    public void setIngress(List<NetworkPolicyIngressRuleModel> ingress) {
        this.ingress = ingress;
    }

}
