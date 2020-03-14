package com.sc.hm.monitor.ext.jmx;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.remote.JMXConnector;

public class MXBeanServerConnectionImpl implements MXBeanServerConnection {

	private JMXConnector jmxConnector = null;
	
	public MXBeanServerConnectionImpl (JMXConnector jmxConnector) {
		this.jmxConnector = jmxConnector;
	}
	
	public ManagedBeanServer getMXBeanServerConnection() throws Exception {
		ManagedBeanServer managedBeanServer = null;
		try {
			Class c = Class.forName("com.sc.hm.monitor.ext.jmx.ManagedBeanServerImpl");
			Constructor<ManagedBeanServer> constructor = c.getDeclaredConstructor(new Class[] {MBeanServerConnection.class});
			managedBeanServer = constructor.newInstance(new Object[] {jmxConnector.getMBeanServerConnection()});
		}
		catch (Exception e) {
			throw e;
		}
		return managedBeanServer;
	}
	
	public String getMXBeanServerConnectionId() throws IOException {
		return jmxConnector.getConnectionId();
	}
	
	public void closeMXBeanServerConnection() throws IOException {
		jmxConnector.close();
	}
	
	public void connectToMXBeanServer() throws IOException {
		jmxConnector.connect();
	}
	
	public void addMXBeanServerConnectionNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) {
		jmxConnector.addConnectionNotificationListener(listener, filter, handback);
	}
	
	public void removeMXBeanServerConnectionNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
		jmxConnector.removeConnectionNotificationListener(listener);
	}
	
	public void removeMXBeanServerConnectionNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws ListenerNotFoundException {
		jmxConnector.removeConnectionNotificationListener(listener, filter, handback);
	}
	
	public String toString() {
		return "[" + jmxConnector.toString() + "]";
	}
}
