package com.sc.hm.monitor.ext.jmx;

import java.lang.reflect.Constructor;
import java.util.Properties;

import javax.management.remote.JMXConnector;

import com.sc.hm.monitor.config.ServerConfig;

public class ManagedBeanServerManager {

	public static MXBeanServerConnection getConnection(String host, String port) throws Exception {
		if ("".equals(host) || "".equals(port)) {
			throw new Exception("Empty Host/Port Parameter");
		}
		return _internalConnect(new ServerConfig(host, port, MXBeanServerConnection.CONNECTION_PROTOCOL));
	}
	
	public static MXBeanServerConnection getConnection(String jmxURL) throws Exception {
		if ("".equals(jmxURL)) {
			throw new Exception("Empty JMX URL");
		}
		return _internalConnect(new ServerConfig(jmxURL));
	}
	
	public static MXBeanServerConnection getConnection(Properties props) throws Exception {
		String host = props.getProperty("host", "localhost");
		String port = props.getProperty("port");
		if (port == null || "".equals(port)) {
			throw new Exception ("No Port is Specified.");
		}		
		return _internalConnect(new ServerConfig(host, port, MXBeanServerConnection.CONNECTION_PROTOCOL));
	}
	
	private static MXBeanServerConnection _internalConnect(ServerConfig serverConfig) throws Exception {
		MXBeanServerConnection mxBeanServerConnection = null;
		try {
			JMXConnector jmxConnector = JMXConnectorManager.getJMXConnector(serverConfig, MXBeanServerConnection.CONNECTOR_URL);
			Class c = Class.forName("com.sc.hm.monitor.ext.jmx.MXBeanServerConnectionImpl");
			Constructor<MXBeanServerConnection> constructor = c.getDeclaredConstructor(new Class[] {JMXConnector.class});
			mxBeanServerConnection = constructor.newInstance(new Object[] {jmxConnector});			
		}
		catch (Exception e) {
			throw e;
		}		
		return mxBeanServerConnection;
	}
}
