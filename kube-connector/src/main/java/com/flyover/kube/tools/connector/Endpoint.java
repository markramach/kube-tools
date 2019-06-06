package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.EndpointModel;
import com.flyover.kube.tools.connector.model.EndpointSubsetModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

public class Endpoint {

    private Kubernetes kube;
    private EndpointModel model = new EndpointModel();

    public Endpoint(Kubernetes kube) { this.kube = kube; }

    public KubeMetadataModel metadata() { return this.model.getMetadata(); }




    public static class EndpointSubset {

        private EndpointSubsetModel model;

        public EndpointSubset(EndpointSubsetModel model) { this.model = model; }
    }

}
