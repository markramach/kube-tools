package com.flyover.kube.tools.connector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.flyover.kube.tools.connector.model.ClusterRoleModel;
import com.flyover.kube.tools.connector.model.ClusterRoleModel.AggregationRuleModel;
import com.flyover.kube.tools.connector.model.ClusterRoleModel.ClusterRoleSelectorModel;
import com.flyover.kube.tools.connector.model.ClusterRoleModel.RuleModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

public class ClusterRole {

    private Kubernetes kube;
    private ClusterRoleModel model = new ClusterRoleModel();

    public ClusterRole(Kubernetes kube) { this.kube = kube; }

    public KubeMetadataModel metadata() { return this.model.getMetadata(); }

    public ClusterRole findOrCreate() {
        ClusterRoleModel found = kube.find(this.model);

        this.model = found != null ? found : kube.create(this.model);

        return this;
    }

    public ClusterRole find() {
        this.model = kube.find(this.model);

        if (this.model == null) {
            return null;
        }

        return this;
    }

    public ClusterRole merge() {
        ClusterRoleModel found = kube.find(this.model);

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

    public ClusterRole rule(List<String> apiGroups, List<String> resources, List<String> verbs) {
        return rule(apiGroups, Collections.emptyList(), resources, verbs);
    }

    public ClusterRole rule(List<String> apiGroups, List<String> resourceNames, List<String> resources, List<String> verbs) {

        RuleModel rule = new RuleModel();
        rule.setApiGroups(apiGroups);
        rule.setResourceNames(resourceNames);
        rule.setResources(resources);
        rule.setVerbs(verbs);

        this.model.getRules().add(rule);

        return this;
    }
    
    public ClusterRole nonResourceRule(List<String> nonResourceURLs, List<String> verbs) {

        RuleModel rule = new RuleModel();
        rule.setNonResourceURLs(nonResourceURLs);
        rule.setVerbs(verbs);

        this.model.getRules().add(rule);

        return this;
    }
    
    public AggregationRule aggregationRule() {
    	
    	if(this.model.getAggregationRule() == null) {
    		this.model.setAggregationRule(new AggregationRuleModel());
    	}
    	
    	return new AggregationRule(this.model.getAggregationRule());
    	
    }

    public static List<String> l(String...values) {
        return Arrays.asList(values);
    }
    
    public static class AggregationRule {
    	
    	private AggregationRuleModel model;

		public AggregationRule(AggregationRuleModel model) {
			this.model = model;
		}

		public AggregationRule clusterRoleSelector(Map<String, Object> matchLabels) {
			
			ClusterRoleSelectorModel selectorModel = new ClusterRoleSelectorModel();
			selectorModel.setMatchLabels(matchLabels);
			
			model.getClusterRoleSelectors().add(selectorModel);			
			
			return this;
		}
    	
    }
}
