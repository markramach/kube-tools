package com.flyover.kube.tools.connector.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class ClusterRoleBindingModel extends KubeModel {

    private RoleRefModel roleRef = new RoleRefModel();
    private List<SubjectModel> subjects = new LinkedList<>();

    public ClusterRoleBindingModel() {
        setApiVersion("rbac.authorization.k8s.io/v1");
        setKind("ClusterRoleBinding");
    }

    @Override
    public <T extends KubeModel> void merge(T model) {

        ClusterRoleBindingModel r = (ClusterRoleBindingModel) model;

        super.merge(model);
        setRoleRef(r.getRoleRef());
        setSubjects(r.getSubjects());

    }

    @Override
    public String checksum() {

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(new ObjectMapper().writeValueAsString(getRoleRef()).getBytes());
            byte[] digest = md.digest(new ObjectMapper().writeValueAsString(getSubjects()).getBytes());

            return new String(Base64.getEncoder().encodeToString(digest));

        } catch (Exception e) {
            throw new RuntimeException("failed to create checksum", e);
        }

    }

    public RoleRefModel getRoleRef() { return roleRef; }

    public void setRoleRef(RoleRefModel roleRef) { this.roleRef = roleRef; }

    public List<SubjectModel> getSubjects() { return subjects; }

    public void setSubjects(List<SubjectModel> subjects) { this.subjects = subjects; }

    public static class SubjectModel extends Model {

        private String kind;
        private String name;
        private String namespace;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

    }

    public static class RoleRefModel extends Model {

        private String apiGroup;
        private String kind;
        private String name;

        public String getApiGroup() {
            return apiGroup;
        }

        public void setApiGroup(String apiGroup) {
            this.apiGroup = apiGroup;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
