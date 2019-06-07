package com.flyover.kube.tools.connector.model;

import java.util.LinkedList;
import java.util.List;

public class EndpointSubsetModel extends Model {

    private List<EndpointAddressModel> addresses = new LinkedList<>();
    private List<EndpointPortModel> ports = new LinkedList<>();

    public List<EndpointAddressModel> getAddresses() { return addresses; }

    public void setAddresses(List<EndpointAddressModel> addresses) { this.addresses = addresses; }

    public List<EndpointPortModel> getPorts() { return ports; }

    public void setPorts(List<EndpointPortModel> ports) { this.ports = ports; }

    public static class EndpointAddressModel extends Model {

        private String ip;

        public String getIp() { return ip; }

        public void setIp(String ip) { this.ip = ip; }
    }

    public static class EndpointPortModel extends Model {
        private String name;
        private int port;
        private String protocol = "TCP";

        public String getName() { return name; }

        public void setName(String name) { this.name = name; }

        public int getPort() { return port; }

        public void setPort(int port) { this.port = port; }

        public String getProtocol() { return protocol; }

        public void setProtocol(String protocol) { this.protocol = protocol; }
    }
}
