package com.flyover.kube.tools.connector.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class ClusterRoleModel extends KubeModel {

    List<RuleModel> rules = new LinkedList<>();

    public ClusterRoleModel() {
        setApiVersion("rbac.authorization.k8s.io/v1");
        setKind("ClusterRole");
    }

    @Override
    public <T extends KubeModel> void merge(T model) {

        ClusterRoleModel clusterRoleModel = (ClusterRoleModel) model;

        super.merge(model);
        setRules(clusterRoleModel.getRules());

    }

    @Override
    public String checksum() {

        try {

            String data = new ObjectMapper().writeValueAsString(rules);
            MessageDigest md = MessageDigest.getInstance("MD5");

            return new String(Base64.getEncoder().encodeToString(md.digest(data.getBytes())));

        } catch (Exception e) {
            throw new RuntimeException("failed to create checksum", e);
        }

    }

    public List<RuleModel> getRules() {
        return rules;
    }

    public void setRules(List<RuleModel> rules) {
        this.rules = rules;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class RuleModel extends Model {

        private List<String> apiGroups = new LinkedList<>();
        private List<String> resourceNames = new LinkedList<>();
        private List<String> resources = new LinkedList<>();
        private List<String> verbs = new LinkedList<>();

        public List<String> getApiGroups() {
            return apiGroups;
        }

        public void setApiGroups(List<String> apiGroups) {
            this.apiGroups = apiGroups;
        }

        public List<String> getResources() {
            return resources;
        }

        public void setResources(List<String> resources) {
            this.resources = resources;
        }

        public List<String> getVerbs() {
            return verbs;
        }

        public void setVerbs(List<String> verbs) {
            this.verbs = verbs;
        }

        public List<String> getResourceNames() {
            return resourceNames;
        }

        public void setResourceNames(List<String> resourceNames) {
            this.resourceNames = resourceNames;
        }

    }
}
