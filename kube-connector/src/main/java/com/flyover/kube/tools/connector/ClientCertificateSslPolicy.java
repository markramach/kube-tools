/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author mramach
 *
 */
public class ClientCertificateSslPolicy implements SslPolicy {

	private X509Certificate certificate;
	private PrivateKey privateKey;
	
	public ClientCertificateSslPolicy(X509Certificate certificate, PrivateKey privateKey) {
		
		this.certificate = certificate;
		this.privateKey = privateKey;
		
	}

	@Override
	public void configure(RestTemplate restTemplate) {
		restTemplate.setRequestFactory(new SkipCertVerificationClientHttpRequestFactory());
	}
	
	private class SkipCertVerificationClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
		
		@Override
		protected HttpURLConnection openConnection(URL url, Proxy proxy) throws IOException {
			
			URLConnection urlConnection = (proxy != null ? url.openConnection(proxy) : url.openConnection());
			
			if (!HttpURLConnection.class.isInstance(urlConnection)) {
				throw new IllegalStateException("HttpURLConnection required for [" + url + "] but got: " + urlConnection);
			}
			
			return (HttpURLConnection) urlConnection;
			
		}

		@Override
		protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
			
			if (connection instanceof HttpsURLConnection) {
				prepareHttpsConnection((HttpsURLConnection) connection);
			}
			
			try {
				
				super.prepareConnection(connection, httpMethod);
				
			} catch (ProtocolException e) {

				/*
				 * This is really dumb. The native HttpUrlConnection in the jvm
				 * doesn't support the PATCH method. To get aroudn this, rather
				 * than rip out all the connection prep, I've udpated the field
				 * via reflection. 
				 */
				
				if("PATCH".equalsIgnoreCase(httpMethod)) {

					try {
						
						// set the method field to PATCH
						Field field = ReflectionUtils.findField(connection.getClass(), "method");
						field.setAccessible(true);
						field.set(connection, httpMethod);
						
						field = ReflectionUtils.findField(connection.getClass(), "delegate");
						
						/*
						 * If this is an SSL connection the connection is a wrapper and
						 * we need to update the delegate connection in the object.
						 */
						
						if(field != null) {
						
							field.setAccessible(true);
							// get the delegate target
							Object delegate = field.get(connection);
							// set the method field to PATCH
							field = ReflectionUtils.findField(delegate.getClass(), "method");
							field.setAccessible(true);
							field.set(delegate, httpMethod);
							
						}
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}
				
			}
			
		}

		private void prepareHttpsConnection(HttpsURLConnection connection) {
			
			connection.setHostnameVerifier(new SkipHostnameVerifier());
			
			try { connection.setSSLSocketFactory(createSslSocketFactory()); } catch (Exception ex) {}
			
		}

		private SSLSocketFactory createSslSocketFactory() throws Exception {
			
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(
					new KeyManager[] { new ClientCertificateX509KeyManager() }, 
					new TrustManager[] { new SkipX509TrustManager() }, 
					new SecureRandom());
			
			return context.getSocketFactory();
			
		}
		
	}
	
	private static class SkipHostnameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String s, SSLSession sslSession) {
			return true;
		}

	}
	
	private class ClientCertificateX509KeyManager implements X509KeyManager {
		
		@Override
		public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
			return certificate.getSubjectX500Principal().getName();
		}

		@Override
		public X509Certificate[] getCertificateChain(String alias) {
			return new X509Certificate[] { certificate };
		}
		
		@Override
		public PrivateKey getPrivateKey(String alias) {
			return privateKey;
		}
		
		// no additional implementation needed at this point
		
		@Override
		public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
			return null;
		}

		@Override
		public String[] getClientAliases(String keyType, Principal[] issuers) {
			return null;
		}

		@Override
		public String[] getServerAliases(String keyType, Principal[] issuers) {
			return null;
		}
		
	}
	
	private static class SkipX509TrustManager implements X509TrustManager {

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {}

	}

}
