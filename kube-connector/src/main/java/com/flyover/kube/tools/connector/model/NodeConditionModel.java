/**
 * 
 */
package com.flyover.kube.tools.connector.model;


/**
 * @author mramach
 *
 */
public class NodeConditionModel extends Model {

	private String type;
	private String status;
	private String reason;
	private String message;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
