package com.flyover.kube.tools.connector.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class EndpointModel extends KubeModel {

    private List<EndpointSubsetModel> subsets = new LinkedList<>();

    public EndpointModel() {
        setApiVersion("v1");
        setKind("Endpoints");
    }

    @Override
    public <T extends KubeModel> void merge(T model) {

        EndpointModel e = (EndpointModel) model;

        super.merge(model);
        setSubsets(e.getSubsets());
    }

    @Override
    public String checksum() {

        try {
            String data = new ObjectMapper().writeValueAsString(subsets);
            MessageDigest md = MessageDigest.getInstance("MD5");

            return new String(Base64.getEncoder().encodeToString(md.digest(data.getBytes())));

        } catch (Exception e) {
            throw new RuntimeException("failed to create checksum", e);
        }

    }

    public List<EndpointSubsetModel> getSubsets() {
        return subsets;
    }

    public void setSubsets(List<EndpointSubsetModel> subsets) {
        this.subsets = subsets;
    }
}
