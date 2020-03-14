package com.sc.hm.monitor.ext.jmx;

public abstract class AbstractMXBeanServerHandler {

	public AbstractMXBeanServerHandler() {}
	
	public abstract void initializeMBeanServer(String host, int port);
	
	public abstract void initializeMBeanServer(String jmxURL);
	
	public abstract ManagedBeanServer getManagedMBeanServer();
}
