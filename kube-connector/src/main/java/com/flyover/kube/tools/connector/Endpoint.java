package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.EndpointModel;
import com.flyover.kube.tools.connector.model.EndpointSubsetModel;
import com.flyover.kube.tools.connector.model.EndpointSubsetModel.EndpointAddressModel;
import com.flyover.kube.tools.connector.model.EndpointSubsetModel.EndpointPortModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

import java.util.Arrays;
import java.util.List;

public class Endpoint {

    private Kubernetes kube;
    private EndpointModel model = new EndpointModel();

    public Endpoint(Kubernetes kube) { this.kube = kube; }

    public KubeMetadataModel metadata() { return this.model.getMetadata(); }

    public Endpoint findOrCreate() {
        EndpointModel found = kube.find(this.model);

        this.model = found != null ? found : kube.create(this.model);

        return this;
    }

    public Endpoint find() {
        this.model = kube.find(this.model);

        if (this.model == null) {
            return null;
        }

        return this;
    }

    public Endpoint merge() {
        EndpointModel found = kube.find(this.model);

        if (found == null) {
            this.model = kube.create(this.model);
        } else {
            this.model = kube.update(found, this.model);
        }

        return this;
    }

    public void delete() {
        kube.delete(this.model);
    }

    public Endpoint subset(List<EndpointAddressModel> addresses, List<EndpointPortModel> ports) {
        EndpointModel subset = new EndpointModel();

        EndpointSubsetModel model = new EndpointSubsetModel();
        model.setAddresses(addresses);
        model.setPorts(ports);

        subset.setSubsets(Arrays.asList(model));

        this.model.getSubsets().add(model);

        return this;
    }

}
