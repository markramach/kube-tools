/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author mramach
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class DeploymentSpecModel extends Model {
	
	private int replicas;
	private SelectorModel selector = new SelectorModel();
	private StrategyModel strategy;
	private DeploymentTemplateModel template = new DeploymentTemplateModel();

	public DeploymentTemplateModel getTemplate() {
		return template;
	}

	public void setTemplate(DeploymentTemplateModel template) {
		this.template = template;
	}

	public SelectorModel getSelector() {
		return selector;
	}

	public void setSelector(SelectorModel selector) {
		this.selector = selector;
	}

	public int getReplicas() {
		return replicas;
	}

	public void setReplicas(int replicas) {
		this.replicas = replicas;
	}

	public StrategyModel getStrategy() {
		return strategy;
	}

	public void setStrategy(StrategyModel strategy) {
		this.strategy = strategy;
	}

}
