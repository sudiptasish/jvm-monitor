package com.sc.hm.monitor.mbean.jmx.remote;

import java.net.MalformedURLException;

import javax.management.remote.JMXServiceURL;

import com.sc.hm.monitor.util.Logger;

public class RemoteMXBeanHandler {
	
	private boolean connected = false;
	
	private String host = "";
	private String port = "";
	
	private String _URL_ = "";
	
	private RemoteMXBeanConnector mxBeanConnector = null;
	
	public RemoteMXBeanHandler() {
		initializeRemoteConnector();
	}

	public RemoteMXBeanHandler(String host, String port) {
		this.host = host;
		this.port = port;	
		initializeRemoteConnector();
	}
	
	private void initializeRemoteConnector() {
		mxBeanConnector = new RemoteMXBeanConnector();
	}
	
	public void configureURL(String jmxURL) {
		if (host != null && !"".equals(host) && port != null && !"".equals(port)) {
			_URL_ = "/jndi/rmi://" + host + ":" + port + "/jmxrmi";
		}
		else {
			_URL_ = jmxURL;
		}
	}
	
	public String get_URL_() {
		return _URL_;
	}

	public void set_URL_(String _url_) {
		_URL_ = _url_;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public RemoteMXBeanConnector getMxBeanConnector() {
		return mxBeanConnector;
	}

	public void connectToRemoteMXBean() throws Exception {
		JMXServiceURL jmxURL = null;
		try {
			if (host != null && !"".equals(host) && port != null && !"".equals(port)) {
				System.out.print("Trying to Connect [Host: " + host + ". Port: " + port + "] ....");
				jmxURL = new JMXServiceURL("rmi", "", 0, _URL_);
			}
			else {
				Logger.log("Connecting using URL [" + _URL_ + "]");
				jmxURL = new JMXServiceURL(_URL_);
			}			
			mxBeanConnector.configureConnector(jmxURL);			
		}
		catch (MalformedURLException murle) {
			throw murle;
		}
	}
	
	public void createRemoteMXBeanConnector(RemoteMXBeanServer remoteMXBeanServer) throws Exception {
		remoteMXBeanServer.createRemoteMBeanServerConnection(getMxBeanConnector());
		setConnected(true);
	}
	
	public void removeServerConnection() {
		setConnected(false);
		mxBeanConnector.disconnect();
	}
}
