/**
 * 
 */
package com.flyover.kube.tools.connector.model;

/**
 * @author mramach
 *
 */
public class DaemonsetSpecModel extends Model {
	
	private SelectorModel selector = new SelectorModel();
	private DaemonsetTemplateModel template = new DaemonsetTemplateModel();

	public DaemonsetTemplateModel getTemplate() {
		return template;
	}

	public void setTemplate(DaemonsetTemplateModel template) {
		this.template = template;
	}

	public SelectorModel getSelector() {
		return selector;
	}

	public void setSelector(SelectorModel selector) {
		this.selector = selector;
	}

}
