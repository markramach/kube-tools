/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
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
		protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
			
			if (connection instanceof HttpsURLConnection) {
				prepareHttpsConnection((HttpsURLConnection) connection);
			}
			
			super.prepareConnection(connection, httpMethod);
			
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
