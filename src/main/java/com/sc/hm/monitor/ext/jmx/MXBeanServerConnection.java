package com.sc.hm.monitor.ext.jmx;

import java.io.IOException;

import javax.management.ListenerNotFoundException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

public interface MXBeanServerConnection {

	public String CONNECTION_PROTOCOL = "rmi";
	public String CONNECTOR_URL = "/jndi/{0}://{1}:{2}/jmxrmi";
	
	public ManagedBeanServer getMXBeanServerConnection() throws Exception;
	
	public String getMXBeanServerConnectionId() throws IOException;
	
	public void closeMXBeanServerConnection() throws IOException;
	
	public void connectToMXBeanServer() throws IOException;
	
	public void addMXBeanServerConnectionNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback);
	
	public void removeMXBeanServerConnectionNotificationListener(NotificationListener listener) throws ListenerNotFoundException;
	
	public void removeMXBeanServerConnectionNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws ListenerNotFoundException;
}
