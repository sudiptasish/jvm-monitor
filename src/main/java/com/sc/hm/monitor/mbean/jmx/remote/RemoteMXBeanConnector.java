package com.sc.hm.monitor.mbean.jmx.remote;

import java.io.IOException;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sc.hm.monitor.util.Logger;

public class RemoteMXBeanConnector {

	private JMXConnector jmxConnector = null;
	
	public RemoteMXBeanConnector() {}
	
	public void configureConnector (JMXServiceURL serviceURL) throws Exception {
		try {
			jmxConnector = JMXConnectorFactory.connect(serviceURL);
			Logger.log("Connected. [Id: " + getConnectionId() + "]");
		}
		catch (IOException ie) {
			throw ie;
		}
	}
	
	public JMXConnector getRemoteMXBeanConnector() {
		return jmxConnector;
	}
	
	public String getConnectionId() throws IOException {
		return jmxConnector.getConnectionId();
	}
	
	public void disconnect() {
		if (jmxConnector != null) {
			try {
				jmxConnector.close();
			}
			catch (Exception e) {
				System.err.println("Remote Server Stopped. Connection already Destroyed....\n" + e.getMessage());
			}
		}
	}
}
