/**
 * 
 */
package com.flyover.kube.tools.connector.model;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mramach
 *
 */
public class CustomResourceDefinitionModel extends KubeModel {

	private CustomResourceDefinitionSpecModel spec = new CustomResourceDefinitionSpecModel();
	
	public CustomResourceDefinitionModel() {
		setApiVersion("apiextensions.k8s.io/v1beta1");
		setKind("CustomResourceDefinition");
	}	

	@Override
	public <T extends KubeModel> void merge(T model) {
		
		CustomResourceDefinitionModel s = (CustomResourceDefinitionModel)model;
		
		super.merge(model);
		setSpec(s.getSpec());
		
	}
	
	@Override
	public String checksum() {
		
		try {
		
			ObjectMapper mapper = new ObjectMapper();
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(mapper.writeValueAsBytes(getSpec()));
			
			return new String(Base64.getEncoder().encodeToString(md.digest()));
			
		} catch (Exception e) {
			throw new RuntimeException("failed to create checksum", e);
		}
		
	}

	public CustomResourceDefinitionSpecModel getSpec() {
		return spec;
	}

	public void setSpec(CustomResourceDefinitionSpecModel spec) {
		this.spec = spec;
	}
	
	public static class CustomResourceDefinitionSpecModel extends Model {
		
		private String scope = "Namespaced";
		private String group;
		private String version;
		private NamesModel names = new NamesModel();
		
		public String getScope() {
			return scope;
		}
		
		public void setScope(String scope) {
			this.scope = scope;
		}

		public String getGroup() {
			return group;
		}

		public void setGroup(String group) {
			this.group = group;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public NamesModel getNames() {
			return names;
		}

		public void setNames(NamesModel names) {
			this.names = names;
		}
		
	}
	
	public static class NamesModel extends Model {
		
		private String kind;
		private String plural;
		private String singular;
		private List<String> shortNames = new LinkedList<>();
		
		public String getKind() {
			return kind;
		}
		
		public void setKind(String kind) {
			this.kind = kind;
		}

		public String getPlural() {
			return plural;
		}

		public void setPlural(String plural) {
			this.plural = plural;
		}

		public String getSingular() {
			return singular;
		}

		public void setSingular(String singular) {
			this.singular = singular;
		}

		public List<String> getShortNames() {
			return shortNames;
		}

		public void setShortNames(List<String> shortNames) {
			this.shortNames = shortNames;
		}
		
	}
	
}
