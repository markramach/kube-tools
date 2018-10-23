/**
 * 
 */
package com.flyover.docker.connector;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mramach
 *
 */
public class Dockerfile {
	
	private Path target;
	private List<String> commands = new LinkedList<>();

	public Dockerfile(Path target, String baseImage) {
		this.target = target;
		commands.add(String.format("FROM %s", baseImage));
	}
	
	public Dockerfile run(String cmd) {
		commands.add(String.format("RUN %s", cmd));
		return this;
	}
	
	public Dockerfile add(String source, String dest) {
		commands.add(String.format("ADD %s %s", source, dest));
		return this;
	}
	
	public Dockerfile volume(String volume) {
		commands.add(String.format("VOLUME %s", volume));
		return this;
	}
	
	public Dockerfile env(String name, String value) {
		commands.add(String.format("ENV %s %s", name, value));
		return this;
	}
	
	public Dockerfile entrypoint(String cmd) {
		commands.add(String.format("ENTRYPOINT %s", cmd));
		return this;
	}

	public byte[] getBytes() {

		return commands.stream()
			.collect(Collectors.joining("\n"))
				.getBytes();
		
	}

	public Path getTarget() {
		return target;
	}

}
