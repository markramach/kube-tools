/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.net.URI;

import org.springframework.web.util.UriComponentsBuilder;

import com.flyover.kube.tools.connector.model.EvictionModel;
import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.KubeModel;
import com.flyover.kube.tools.connector.model.PodModel;

/**
 * @author mramach
 *
 */
public class Pod {

	private Kubernetes kube;
	private PodModel model = new PodModel();
	
	public Pod(Kubernetes kube) {
		this.kube = kube;
	}
	
	public Pod(Kubernetes kube, PodModel model) {
		this.kube = kube;
		this.model = model;
	}
	
	public KubeMetadataModel metadata() {
		return this.model.getMetadata();
	}

	public String podIP() {
		return this.model.getStatus().getPodIP();
	}
	
	public Pod find() {
		
		this.model = kube.find(this.model);
		
		if(this.model == null) {
			return null;
		}
		
		return this;
		
	}
	
	public String exec(String...command) {
		
		return kube.exec(model, command);
		
	}
	
	public String execContainer(String container, String...command) {
		
		return kube.exec(container, model, command);
		
	}
	
	public Pod evict() {
		
		KubeModel ev = new EvictionModel();
		ev.getMetadata().setNamespace(metadata().getNamespace());
		ev.getMetadata().setName(metadata().getName());
		
		URI uri = UriComponentsBuilder
    			.fromHttpUrl(kube.getConfig().getEndpoint())
    			.path("api/v1/namespaces/")
    			.path(metadata().getNamespace())
    			.path("/pods/")
    			.path(metadata().getName())
    			.path("/eviction")
    			.build()
    				.toUri();
			
		// this is a special case
		kube.create(uri, ev, new Callback<KubeModel>() { });
		
		return find();
		
	}
	
}
