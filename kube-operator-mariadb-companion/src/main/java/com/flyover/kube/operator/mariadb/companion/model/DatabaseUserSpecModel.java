/**
 * 
 */
package com.flyover.kube.operator.mariadb.companion.model;

import java.util.LinkedList;
import java.util.List;

import com.flyover.kube.tools.connector.model.Model;

/**
 * @author mramach
 *
 */
public class DatabaseUserSpecModel extends Model {

	private String clusterId;
	private UserModel user;

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	
	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public static class UserModel extends Model {
		
		private String name;
		private List<GrantsModel> grants = new LinkedList<>();

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<GrantsModel> getGrants() {
			return grants;
		}

		public void setGrants(List<GrantsModel> grants) {
			this.grants = grants;
		}
		
	}
	
	public static class GrantsModel {
		
		private String schema;
		private List<String> privileges = new LinkedList<>();

		public String getSchema() {
			return schema;
		}

		public void setSchema(String schema) {
			this.schema = schema;
		}

		public List<String> getPrivileges() {
			return privileges;
		}

		public void setPrivileges(List<String> privileges) {
			this.privileges = privileges;
		}
		
	}

}
