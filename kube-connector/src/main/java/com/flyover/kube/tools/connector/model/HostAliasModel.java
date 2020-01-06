package com.flyover.kube.tools.connector.model;

import java.util.LinkedList;
import java.util.List;

public class HostAliasModel extends Model {

    private String ip;
    private List<String> hostnames = new LinkedList<>();

    public String getIp() { return ip; }

    public void setIp(String ip) { this.ip = ip; }

    public List<String> getHostnames() { return hostnames; }

    public void setHostnames(List<String> hostnames) { this.hostnames = hostnames; }

}