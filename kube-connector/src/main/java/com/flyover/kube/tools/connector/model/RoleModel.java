/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.util.LinkedList;
import java.util.List;


/**
 * @author mramach
 *
 */
public class RoleModel extends KubeModel {
	
	private List<RuleModel> rules = new LinkedList<>();
	
	public RoleModel() {
		setApiVersion("rbac.authorization.k8s.io/v1");
		setKind("Role");
	}	
	
	public List<RuleModel> getRules() {
		return rules;
	}

	public void setRules(List<RuleModel> rules) {
		this.rules = rules;
	}

	public static class RuleModel extends Model {
		
		private List<String> apiGroups = new LinkedList<>();
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
		
	}
	
}
