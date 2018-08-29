/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.List;

import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.RoleModel;
import com.flyover.kube.tools.connector.model.RoleModel.RuleModel;

/**
 * @author mramach
 *
 */
public class Role {

	private Kubernetes kube;
	private RoleModel model = new RoleModel();
	
	public Role(Kubernetes kube) {
		this.kube = kube;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public Role findOrCreate() {
		
		RoleModel found = kube.find(this.model);
		
		this.model = found != null ? found : kube.create(this.model); 
		
		return this;
		
	}
	
	public Role rule(List<String> apiGroups, List<String> resources, List<String> verbs) {
		
		RuleModel rule = new RuleModel();
		rule.setApiGroups(apiGroups);
		rule.setResources(resources);
		rule.setVerbs(verbs);
		
		this.model.getRules().add(rule);
		
		return this;
		
	}

}
