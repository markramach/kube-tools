package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.HostAliasModel;

public class HostAlias {

    private HostAliasModel model;

    public HostAlias (HostAliasModel model) { this.model = model; }

    public HostAliasModel model() { return this.model; }

}
