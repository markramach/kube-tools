/**
 * 
 */
package com.flyover.kube.tools.connector.model;

/**
 * @author mramach
 *
 */
public class TcpProbeModel extends ProbeModel {

	private TcpSocketModel tcpSocket = new TcpSocketModel();
	
	public TcpSocketModel getTcpSocket() {
		return tcpSocket;
	}

	public void setTcpSocket(TcpSocketModel tcpSocket) {
		this.tcpSocket = tcpSocket;
	}

	public static class TcpSocketModel extends Model {
		
		private int port;

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}
		
	}
	
}
