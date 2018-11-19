package com.flyover.kube.tools.connector.model;

public class IpBlock extends Model {

    private String cidr;
    private String except;

    public String getCidr() { return cidr; }

    public void setCidr(String cidr) { this.cidr = cidr; }

    public String getExcept() { return except; }

    public void setExcept(String except) { this.except = except; }
}
