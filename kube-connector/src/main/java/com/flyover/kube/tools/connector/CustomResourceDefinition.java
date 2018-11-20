/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.util.Arrays;

import com.flyover.kube.tools.connector.model.CustomResourceDefinitionModel;
import com.flyover.kube.tools.connector.model.CustomResourceDefinitionModel.CustomResourceDefinitionSpecModel;
import com.flyover.kube.tools.connector.model.CustomResourceDefinitionModel.NamesModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;

/**
 * @author mramach
 *
 */
public class CustomResourceDefinition {

	private Kubernetes kube;
	private CustomResourceDefinitionModel model = new CustomResourceDefinitionModel();
	
	public CustomResourceDefinition(Kubernetes kube) {
		this.kube = kube;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}
	
	public CustomResourceDefinitionSpec spec() {
		return new CustomResourceDefinitionSpec(this.model.getSpec());
	}
	
	public CustomResourceDefinition merge() {
		
		CustomResourceDefinitionModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model);
		} else {
			this.model = kube.update(found, this.model);
		}
		
		return this;
		
	}
	
	public CustomResourceDefinition find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public void delete() {
		
		kube.delete(this.model);
		
	}
	
	public static class CustomResourceDefinitionSpec {
		
		private CustomResourceDefinitionSpecModel model;

		public CustomResourceDefinitionSpec(CustomResourceDefinitionSpecModel model) {
			this.model = model;
		}
		
		public CustomResourceDefinitionSpec namespaced() {
			this.model.setScope("Namespaced");
			return this;
		}
		
		public CustomResourceDefinitionSpec cluster() {
			this.model.setScope("Cluster");
			return this;
		}
		
		public CustomResourceDefinitionSpec group(String group) {
			this.model.setGroup(group);
			return this;
		}
		
		public CustomResourceDefinitionSpec version(String version) {
			this.model.setVersion(version);
			return this;
		}
		
		public Names names() {
			return new Names(this.model.getNames());
		}
		
	}
	
	public static class Names {
		
		private NamesModel model;

		public Names(NamesModel model) {
			this.model = model;
		}
		
		public Names kind(String kind) {
			this.model.setKind(kind);
			return this;
		}
		
		public Names plural(String plural) {
			this.model.setPlural(plural);
			return this;
		}
		
		public Names singular(String singular) {
			this.model.setSingular(singular);
			return this;
		}
		
		public Names shortNames(String...shortNames) {
			this.model.setShortNames(Arrays.asList(shortNames));
			return this;
		}
		
	}

}
