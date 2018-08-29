/**
 * 
 */
package com.flyover.kube.tools.connector.model;

/**
 * @author mramach
 *
 */
public class ProbeModel extends Model {

	private int initialDelaySeconds;
	private int periodSeconds;
	private int failureThreshold = 3;
	
	public int getInitialDelaySeconds() {
		return initialDelaySeconds;
	}
	
	public void setInitialDelaySeconds(int initialDelaySeconds) {
		this.initialDelaySeconds = initialDelaySeconds;
	}

	public int getPeriodSeconds() {
		return periodSeconds;
	}

	public void setPeriodSeconds(int periodSeconds) {
		this.periodSeconds = periodSeconds;
	}

	public int getFailureThreshold() {
		return failureThreshold;
	}

	public void setFailureThreshold(int failureThreshold) {
		this.failureThreshold = failureThreshold;
	}
	
}
