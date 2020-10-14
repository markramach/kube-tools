/**
 * 
 */
package com.flyover.kube.tools.connector;

import okhttp3.Request.Builder;

import org.springframework.web.client.RestTemplate;

/**
 * @author mramach
 *
 */
public interface KubernetesAuthenticator {
	
	void configure(RestTemplate restTemplate);

	default void configure(Builder builder){
		
		// do nothing
		
	}
	
}
