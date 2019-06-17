package com.flyover.kube.tools.connector.model;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClusterRoleModel extends KubeModel {

    private List<RuleModel> rules = new LinkedList<>();
    private AggregationRuleModel aggregationRule;

    public ClusterRoleModel() {
        setApiVersion("rbac.authorization.k8s.io/v1");
        setKind("ClusterRole");
    }

    @Override
    public <T extends KubeModel> void merge(T model) {

        ClusterRoleModel clusterRoleModel = (ClusterRoleModel) model;

        super.merge(model);
        setRules(clusterRoleModel.getRules());
        setAggregationRule(clusterRoleModel.getAggregationRule());

    }

    @Override
    public String checksum() {

        try {
        	
        	Map<String, String> annotations = new LinkedHashMap<>(getMetadata().getAnnotations());
			// This value mutates as the spec mutates and should be ignored.
			annotations.remove("com.flyover.checksum");
			
			ObjectMapper mapper = new ObjectMapper();
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(mapper.writeValueAsBytes(annotations));
			md.update(mapper.writeValueAsBytes(rules));
			md.update(mapper.writeValueAsBytes(aggregationRule));
			
			return new String(Base64.getEncoder().encodeToString(md.digest()));

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

    public AggregationRuleModel getAggregationRule() {
		return aggregationRule;
	}

	public void setAggregationRule(AggregationRuleModel aggregationRule) {
		this.aggregationRule = aggregationRule;
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
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class AggregationRuleModel extends Model {
    	
    	private List<ClusterRoleSelectorModel> clusterRoleSelectors = new LinkedList<>();

		public List<ClusterRoleSelectorModel> getClusterRoleSelectors() {
			return clusterRoleSelectors;
		}

		public void setClusterRoleSelectors(List<ClusterRoleSelectorModel> clusterRoleSelectors) {
			this.clusterRoleSelectors = clusterRoleSelectors;
		}
    	
    }
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ClusterRoleSelectorModel extends Model {
    	
    	Map<String, Object> matchLabels = new LinkedHashMap<>();

		public Map<String, Object> getMatchLabels() {
			return matchLabels;
		}

		public void setMatchLabels(Map<String, Object> matchLabels) {
			this.matchLabels = matchLabels;
		}
    	
    }
    
}
