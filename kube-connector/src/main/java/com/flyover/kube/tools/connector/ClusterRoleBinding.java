package com.flyover.kube.tools.connector;


import com.flyover.kube.tools.connector.model.ClusterRoleBindingModel;
import com.flyover.kube.tools.connector.model.ClusterRoleBindingModel.RoleRefModel;
import com.flyover.kube.tools.connector.model.ClusterRoleBindingModel.SubjectModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

public class ClusterRoleBinding {

    private Kubernetes kube;

    private ClusterRoleBindingModel model = new ClusterRoleBindingModel();

    public ClusterRoleBinding(Kubernetes kube) { this.kube = kube; }

    public KubeMetadataModel metadata() { return this.model.getMetadata(); }

    public ClusterRoleBinding findOrCreate() {
        ClusterRoleBindingModel found = kube.find(this.model);

        this.model = found != null ? found : kube.create(this.model);

        return this;
    }

    public ClusterRoleBinding find() {
        this.model = kube.find(this.model);

        if (this.model == null) {
            return null;
        }

        return this;
    }

    public ClusterRoleBinding merge() {
        ClusterRoleBindingModel found = kube.find(this.model);

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

    public ClusterRoleBinding roleRef(ClusterRole clusterRole) {
        RoleRefModel roleRef = new RoleRefModel();
        roleRef.setApiGroup("rbac.authorization.k8s.io");
        roleRef.setKind("ClusterRole");
        roleRef.setName(clusterRole.metadata().getName());

        this.model.setRoleRef(roleRef);

        return this;
    }

    public ClusterRoleBinding subject(ServiceAccount sa) {
        SubjectModel subject = new SubjectModel();
        subject.setKind("ServiceAccount");
        subject.setNamespace(sa.metadata().getNamespace());
        subject.setName(sa.metadata().getName());

        this.model.getSubjects().add(subject);

        return this;
    }
}
