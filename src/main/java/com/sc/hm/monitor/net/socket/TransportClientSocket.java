package com.sc.hm.monitor.net.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TransportClientSocket implements ITransportSocketType {

	private InetAddress host = null;
	private int port = -1;
	
	private Socket client = null;
	
	public TransportClientSocket() {
		initializeDefaultConfiguration();
	}
	
	public TransportClientSocket(int port) {
		initializeDefaultConfiguration(port);
	}
	
	public TransportClientSocket(String host, int port) {
		initializeDefaultConfiguration(host, port);
	}
	
	private void initializeDefaultConfiguration() {
		initializeDefaultConfiguration(-1);
	}
	
	public OutputStream getTransportOutputStream() throws IOException {
		return client.getOutputStream();
	}
	
	public InputStream getTransportInputStream() throws IOException {
		return client.getInputStream();
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
	
	private void initializeDefaultConfiguration(String host, int port) {
		try {
			this.host = InetAddress.getByName(host);
			this.port = (port == -1) ? 11979 : port;
		}
		catch(UnknownHostException une) {
			System.err.println("Unknown Host" + une);
		}
	}
	
	public void initializeSocket() {
		try {
			this.client = new Socket(host, port);			
		}
		catch (IOException ie) {
			System.err.println("Error!!! Initializng Client Socket" + ie);
		}
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
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
	
	public void closeTransportClient() {
		if (this.client != null) {
			try {
				this.client.close();
			}
			catch (IOException e) {
				System.err.println("Error!!! While Closing Transport Client" + e);
			}
			this.client = null;
		}
	}
	
	public String toString() {
		return new StringBuilder().append("[").append(this.host).append(" ").append(this.port).append(" ").append(this.client).append("]").toString();
	}
}
