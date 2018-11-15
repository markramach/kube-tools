package com.flyover.kube.tools.connector.model;

import com.flyover.kube.tools.connector.NetworkPolicy;

import java.util.List;

public class NetworkPolicySpecModel {

    private SelectorModel podSelector;
    private List<NetworkPolicyEgressRuleModel> egress;
    private List<NetworkPolicyIngressRuleModel> ingress;
    private List<String> policyTypes;

    public SelectorModel getPodSelector() {
        return podSelector;
    }

    public void setPodSelector(SelectorModel podSelector) {
        this.podSelector = podSelector;
    }

    public List<NetworkPolicyEgressRuleModel> getEgress() {
        return egress;
    }

    public void setEgress(List<NetworkPolicyEgressRuleModel> egress) {
        this.egress = egress;
    }

    public List<NetworkPolicyIngressRuleModel> getIngress() {
        return ingress;
    }

    public void setIngress(List<NetworkPolicyIngressRuleModel> ingress) {
        this.ingress = ingress;
    }

    public List<String> getPolicyTypes() {
        return policyTypes;
    }

    public void setPolicyTypes(List<String> policyTypes) {
        this.policyTypes = policyTypes;
    }

}
