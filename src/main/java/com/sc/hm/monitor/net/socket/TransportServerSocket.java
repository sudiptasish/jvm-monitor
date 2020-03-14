package com.sc.hm.monitor.net.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TransportServerSocket implements ITransportSocketType {

	private InetAddress host = null;
	private int port = -1;
	
	private ServerSocket server = null;
	
	public TransportServerSocket() {
		initializeDefaultConfiguration();
	}
	
	public TransportServerSocket(int port) {
		initializeDefaultConfiguration(port);
	}
	
	private void initializeDefaultConfiguration() {
		initializeDefaultConfiguration(-1);
	}
	
	private void initializeDefaultConfiguration(int port) {
		try {
			this.host = InetAddress.getLocalHost();
			this.port = (port == -1) ? 11979 : port;
		}
		catch(UnknownHostException une) {
			System.err.println("Unknown Host" + une);
		}
	}
	
	public OutputStream getTransportOutputStream() throws IOException {
		return null;
	}
	
	public InputStream getTransportInputStream() throws IOException {
		return null;
	}
	
	public void initializeSocket() {
		try {
			this.server = new ServerSocket(port);
		}
		catch (IOException ie) {
			System.err.println("Error!!! Initializng Server Socket" + ie);
		}
	}
	
	public Socket acceptConnection() throws Exception {
		return server.accept();
	}

	public InetAddress getHost() {
		return host;
	}

	public void setHost(InetAddress host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}
	
	public void closeTransportClient() {
		if (this.server != null) {
			try {
				this.server.close();
			}
			catch (IOException e) {
				System.err.println("Error!!! While Closing Transport Server" + e);
			}
			this.server = null;
		}
	}
	
	public String toString() {
		return "[" + this.host + " " + this.port + " " + this.server + "]";
	}
}
