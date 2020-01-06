package com.flyover.kube.tools.connector.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedList;
import java.util.List;

public class HostAliasModel extends Model {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ip;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> hostnames = new LinkedList<>();

    public String getIp() { return ip; }

    public void setIp(String ip) { this.ip = ip; }

    public List<String> getHostnames() { return hostnames; }

    public void setHostnames(List<String> hostnames) { this.hostnames = hostnames; }

}