/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mramach
 *
 */
public class ContainerModel extends Model {
	
	private String name;
	private String image;
	private String imagePullPolicy;
	private List<PortModel> ports = new LinkedList<>();
	private List<EnvModel> env = new LinkedList<>();
	private List<VolumeMountModel> volumeMounts = new LinkedList<>();
	private List<String> command = new LinkedList<>();
	private Collection<String> args = new HashSet<>();
	private ProbeModel readinessProbe;
	private ComputeResourceModel resources;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<PortModel> getPorts() {
		return ports;
	}

	public void setPorts(List<PortModel> ports) {
		this.ports = ports;
	}

	public List<EnvModel> getEnv() {
		return env;
	}

	public void setEnv(List<EnvModel> env) {
		this.env = env;
	}

	public String getImagePullPolicy() {
		return imagePullPolicy;
	}

	public void setImagePullPolicy(String imagePullPolicy) {
		this.imagePullPolicy = imagePullPolicy;
	}

	public List<String> getCommand() {
		return command;
	}

	public void setCommand(List<String> command) {
		this.command = command;
	}

	public Collection<String> getArgs() {
		return args;
	}

	public void setArgs(Collection<String> args) {
		this.args = new HashSet<>(args);
	}

	public List<VolumeMountModel> getVolumeMounts() {
		return volumeMounts;
	}

	public void setVolumeMounts(List<VolumeMountModel> volumeMounts) {
		this.volumeMounts = volumeMounts;
	}

	public ProbeModel getReadinessProbe() {
		return readinessProbe;
	}

	public void setReadinessProbe(ProbeModel readinessProbe) {
		this.readinessProbe = readinessProbe;
	}

	public ComputeResourceModel getResources() { return resources; }

	public void setResources(ComputeResourceModel resources) { this.resources = resources; }

}
