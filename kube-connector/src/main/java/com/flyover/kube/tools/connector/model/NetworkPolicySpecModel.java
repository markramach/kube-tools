package com.flyover.kube.tools.connector.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class NetworkPolicySpecModel extends Model {

    private SelectorModel podSelector;
    private Set<String> policyTypes = new LinkedHashSet<>();
    private List<NetworkPolicyIngressRuleModel> ingress;
    private List<NetworkPolicyEgressRuleModel> egress;

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

	public List<NetworkPolicyEgressRuleModel> getEgress() {
		return egress;
	}

	public void setEgress(List<NetworkPolicyEgressRuleModel> egress) {
		this.egress = egress;
	}

	public Set<String> getPolicyTypes() {
		return policyTypes;
	}

	public void setPolicyTypes(Set<String> policyTypes) {
		this.policyTypes = policyTypes;
	}

}
