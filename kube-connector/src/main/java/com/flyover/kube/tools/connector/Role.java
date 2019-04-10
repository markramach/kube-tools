/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.Arrays;
import java.util.Collections;
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
	
	public Role find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public Role merge() {
		
		RoleModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model);
		} else {
			this.model = kube.update(found, this.model);
		}
		
		return this;
		
	}
	
	public void delete() {
		
		kube.delete(this.model);
		
	}
	
	public Role rule(List<String> apiGroups, List<String> resources, List<String> verbs) {
		return rule(apiGroups, Collections.emptyList(), resources, verbs);
	}
	
	public Role rule(List<String> apiGroups, List<String> resourceNames, List<String> resources, List<String> verbs) {
		
		RuleModel rule = new RuleModel();
		rule.setApiGroups(apiGroups);
		rule.setResourceNames(resourceNames);
		rule.setResources(resources);
		rule.setVerbs(verbs);
		
		this.model.getRules().add(rule);
		
		return this;
		
	}
	
	public static List<String> l(String...values) {
		return Arrays.asList(values);
	}

}
