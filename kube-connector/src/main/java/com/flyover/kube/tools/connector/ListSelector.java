/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mramach
 *
 */
public class ListSelector {
	
	private Map<String, String> labelSelectors = new LinkedHashMap<>();
	private Map<String, String> fieldSelectors = new LinkedHashMap<>();
	
	public Map<String, String> getLabelSelectors() {
		return labelSelectors;
	}
	
	public void setLabelSelectors(Map<String, String> labelSelectors) {
		this.labelSelectors = labelSelectors;
	}
	
	public Map<String, String> getFieldSelectors() {
		return fieldSelectors;
	}
	
	public void setFieldSelectors(Map<String, String> fieldSelectors) {
		this.fieldSelectors = fieldSelectors;
	}

}
