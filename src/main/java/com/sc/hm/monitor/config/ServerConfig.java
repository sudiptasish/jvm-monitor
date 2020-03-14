package com.sc.hm.monitor.config;

public class ServerConfig {

	private String serverName = "";
	private String serverPort = "";
	private String protocol = "";
	private String url = "";
	private String name = "";
	private String password = "";
		
	public ServerConfig() {}
	
	public ServerConfig(String serverName, String serverPort, String protocol) {
		this(serverName, serverPort, protocol, "");
	}
	
	public ServerConfig(String serverName, String serverPort, String protocol, String url) {
		this.serverName = serverName;
		this.serverPort = serverPort;
		this.protocol = protocol;
		this.url = url;
	}
	
	public ServerConfig(String url) {
		this.url = url;
	}
	
	public String getServerPort() {
		return serverPort;
	}
	
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		return "[" + serverName + " " + serverPort + "]";
	}
}
