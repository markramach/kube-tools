/**
 * 
 */
package com.flyover.kube.tools.connector;

import okhttp3.OkHttpClient.Builder;

import org.springframework.web.client.RestTemplate;

/**
 * @author mramach
 *
 */
public interface SslPolicy {
	
	void configure(RestTemplate restTemplate);

	void configure(Builder client);

}
