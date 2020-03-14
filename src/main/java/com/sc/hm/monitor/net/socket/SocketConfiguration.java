package com.sc.hm.monitor.net.socket;

public class SocketConfiguration {

	private String host = "";
	private int port = 0;
	
	public SocketConfiguration() {}

	public SocketConfiguration(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
