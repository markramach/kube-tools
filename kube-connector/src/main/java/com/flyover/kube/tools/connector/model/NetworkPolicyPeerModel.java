package com.flyover.kube.tools.connector.model;

public class NetworkPolicyPeerModel extends Model {

    private SelectorModel namespaceSelector;
    private SelectorModel podSelector;
    private IpBlock ipBlock;

    public SelectorModel getNamespaceSelector() {
        return namespaceSelector;
    }

    public void setNamespaceSelector(SelectorModel namespaceSelector) {
        this.namespaceSelector = namespaceSelector;
    }

    public SelectorModel getPodSelector() {
        return podSelector;
    }

    public void setPodSelector(SelectorModel podSelector) {
        this.podSelector = podSelector;
    }

    public IpBlock getIpBlock() { return ipBlock; }

    public void setIpBlock(IpBlock ipBlock) { this.ipBlock = ipBlock; }

}
