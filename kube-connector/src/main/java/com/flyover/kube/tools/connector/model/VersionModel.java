/**
 * 
 */
package com.flyover.kube.tools.connector.model;


/**
 * @author mramach
 *
 */
public class VersionModel extends Model {

	private String gitVersion;
	private String major;
	private String minor;

	public String getGitVersion() {
		return gitVersion;
	}

	public void setGitVersion(String gitVersion) {
		this.gitVersion = gitVersion;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMinor() {
		return minor;
	}

	public void setMinor(String minor) {
		this.minor = minor;
	}
	
}
