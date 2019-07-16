package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.EndpointModel;
import com.flyover.kube.tools.connector.model.EndpointSubsetModel;
import com.flyover.kube.tools.connector.model.EndpointSubsetModel.EndpointAddressModel;
import com.flyover.kube.tools.connector.model.EndpointSubsetModel.EndpointPortModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

import java.util.Optional;

public class Endpoint {

    private Kubernetes kube;
    private EndpointModel model = new EndpointModel();

    public Endpoint(Kubernetes kube) { this.kube = kube; }

    public KubeMetadataModel metadata() { return this.model.getMetadata(); }

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
    
    public Endpoint activeMerge() {
    	
        EndpointModel found = kube.find(this.model);

        if (found == null) {
            this.model = kube.create(this.model);
        } else {
            this.model = kube.activeUpdate(found, this.model);
        }

        return this;
        
    }

    public void delete() {
        kube.delete(this.model);
    }

    public Subset subset() {
        Optional<EndpointSubsetModel> subset =
                this.model.getSubsets().stream().findFirst();

        return new Subset(subset.orElseGet(() -> {
            EndpointSubsetModel subsetModel = new EndpointSubsetModel();
            this.model.getSubsets().add(subsetModel);

            return subsetModel;
        }));

    }

    public static class Subset {

        private EndpointSubsetModel model;

        public Subset(EndpointSubsetModel model)  {
            this.model = model;
        }

        public Subset addAddress(String ip) {
            EndpointAddressModel addressModel = new EndpointAddressModel();
            addressModel.setIp(ip);

            this.model.getAddresses().add(addressModel);

            return this;
        }

        public Subset addPort(String name, int port, String protocol) {
            EndpointPortModel portModel = new EndpointPortModel();
            portModel.setName(name);
            portModel.setPort(port);
            portModel.setProtocol(protocol);

            this.model.getPorts().add(portModel);

            return this;
        }

        public EndpointSubsetModel model() {
            return model;
        }
    }

}
