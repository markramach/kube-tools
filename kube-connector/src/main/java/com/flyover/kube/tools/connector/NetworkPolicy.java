package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.NetworkPolicyModel;
import com.flyover.kube.tools.connector.model.NetworkPolicySpecModel;

public class NetworkPolicy {

    private Kubernetes kube;
    private NetworkPolicyModel model;

    public NetworkPolicy(Kubernetes kube) {
        this(kube, new NetworkPolicyModel());
    }

    public NetworkPolicy(Kubernetes kube, NetworkPolicyModel model) {
        this.kube = kube;
        this.model = model;
    }

    public KubeMetadataModel metadata() {
        return this.model.getMetadata();
    }

    public NetworkPolicySpec spec() { return new NetworkPolicySpec(this.model.getSpec()); }


    public static class NetworkPolicySpec {
        private NetworkPolicySpecModel model;

        public NetworkPolicySpec(NetworkPolicySpecModel model) { this.model = model; }
    }

    public NetworkPolicy find() {
        this.model = kube.find(this.model);

        if (this.model == null) {
            return null;
        }
        return this;
    }

    public NetworkPolicy create() {
        this.model = kube.create(this.model);

        return this;
    }

    public void delete() {
        kube.delete(this.model);
    }

    public NetworkPolicy replace() {
        kube.delete(this.model);

        return create();
    }

}
