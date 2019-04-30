/**
 * 
 */
package com.flyover.kube.tools.connector.model;

/**
 * @author mramach
 *
 */
public class HttpGetProbeModel extends ProbeModel {

	private HttpGetModel httpGet = new HttpGetModel();

	public HttpGetModel getHttpGet() {
		return httpGet;
	}

	public void setHttpGet(HttpGetModel httpGet) {
		this.httpGet = httpGet;
	}

	public static class HttpGetModel extends Model {
		
		private String host;
		private String path;
		private int port;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}
		
	}
	
}
