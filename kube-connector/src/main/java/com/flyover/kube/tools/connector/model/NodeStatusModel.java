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
public class NodeStatusModel extends Model {

	private List<NodeConditionModel> conditions = new LinkedList<>();

	public List<NodeConditionModel> getConditions() {
		return conditions;
	}

	public void setConditions(List<NodeConditionModel> conditions) {
		this.conditions = conditions;
	}
	
}
