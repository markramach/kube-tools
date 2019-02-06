package com.flyover.kube.tools.connector.model;


public class NetworkPolicyModel extends KubeModel {

    private NetworkPolicySpecModel spec = new NetworkPolicySpecModel();

    public NetworkPolicyModel() {
        setApiVersion("networking.k8s.io/v1");
        setKind("NetworkPolicy");
    }

    public NetworkPolicySpecModel getSpec() { return spec; }

    public void setSpec(NetworkPolicySpecModel spec) { this.spec = spec; }
}
