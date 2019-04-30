/**
 * 
 */
package com.flyover.kube.tools.connector.model;

/**
 * @author mramach
 *
 */
public class DaemonsetSpecModel extends Model {
	
	private DaemonsetTemplateModel template = new DaemonsetTemplateModel();

	public DaemonsetTemplateModel getTemplate() {
		return template;
	}

	public void setTemplate(DaemonsetTemplateModel template) {
		this.template = template;
	}

}
