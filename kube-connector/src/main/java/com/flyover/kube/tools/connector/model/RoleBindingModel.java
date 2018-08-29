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
public class RoleBindingModel extends KubeModel {
	
	private RoleRefModel roleRef = new RoleRefModel();
	private List<SubjectModel> subjects = new LinkedList<>();
	
	public RoleBindingModel() {
		setApiVersion("rbac.authorization.k8s.io/v1");
		setKind("RoleBinding");
	}	
	
	public RoleRefModel getRoleRef() {
		return roleRef;
	}

	public void setRoleRef(RoleRefModel roleRef) {
		this.roleRef = roleRef;
	}

	public List<SubjectModel> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectModel> subjects) {
		this.subjects = subjects;
	}

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
