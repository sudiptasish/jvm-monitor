package com.sc.hm.monitor.mbean.factory;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

import com.sc.hm.monitor.mbean.jmx.remote.RemoteMXBeanHandler;
import com.sc.hm.monitor.mbean.jmx.remote.RemoteMXBeanServer;
import com.sc.hm.monitor.util.Logger;

public class RemoteManagementBeanFactory extends ManagementBeanFactory {
	
	private static RemoteManagementBeanFactory remoteManagementBeanFactory = null;
	
	private RemoteMXBeanHandler remoteMXBeanHandler = null;
	private RemoteMXBeanServer remoteMXBeanServer = null;

	private RemoteManagementBeanFactory() {
		super();
		getRemoteMXBeanServerSingleton();
		initializeMXBeanHandler();
	}
	
	public static synchronized RemoteManagementBeanFactory getRemoteManagementBeanFactory() {
		if (remoteManagementBeanFactory == null) {
			remoteManagementBeanFactory = new RemoteManagementBeanFactory();
		}
		return remoteManagementBeanFactory;
	}
	
	private void getRemoteMXBeanServerSingleton() {
		remoteMXBeanServer = RemoteMXBeanServer.getInstance();
	}
	
	private void initializeMXBeanHandler() {
		remoteMXBeanHandler = new RemoteMXBeanHandler();
	}
	
	public void initializeMBeanFactory(Object[] init_params) throws Exception {
		String remoteHost = (String)init_params[0];
		String port = (String)init_params[1];
		String url = (String)init_params[2];
		initRemoteMXBeanServer(remoteHost, port, url);
	}
	
	private void initRemoteMXBeanServer(String host, String port, String url) throws Exception {
		if (!remoteMXBeanHandler.isConnected()) {
			remoteMXBeanHandler.setHost(host);
			remoteMXBeanHandler.setPort(port);
			remoteMXBeanHandler.configureURL(url);
			remoteMXBeanHandler.connectToRemoteMXBean();
			remoteMXBeanHandler.createRemoteMXBeanConnector(remoteMXBeanServer);
			Logger.log("Remote MBean Server Initialized....");
		}
	}
	
	public void disconnectMXBeanServer() {
		remoteMXBeanHandler.removeServerConnection();
	}
	
	public boolean checkRemoteConnection(String host, String port) throws Exception {
		initRemoteMXBeanServer(host, port, "");
		return true;
	}
	
	public MemoryMXBean getMemoryMXBean() {
		return remoteMXBeanServer.getRemoteMemoryMXBean(MEMORY_MXBEAN);
	}
	
	public List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
		return remoteMXBeanServer.getRemoteMemoryPoolMXBeans(MEMORY_POOL_MXBEAN_DOMAIN);
	}
	
	public ThreadMXBean getThreadMXBean() {
		return remoteMXBeanServer.getRemoteThreadMXBean(THREAD_MXBEAN);
	}
	
	public ClassLoadingMXBean getClassLoadingMXBean() {
		return remoteMXBeanServer.getRemoteClassLoadingMXBean(CLASS_LOADING_MX_BEAN);
	}
	
	public CompilationMXBean getCompilationMXBean() {
		return remoteMXBeanServer.getRemoteCompilationMXBean(COMPILATION_MXBEAN);
	}
	
	public OperatingSystemMXBean getOperatingSystemMXBean() {
		return remoteMXBeanServer.getRemoteSunOperatingSystemMXBean(OPERATING_SYSTEM_MXBEAN);
	}
	
	public RuntimeMXBean getRuntimeMXBean() {
		return remoteMXBeanServer.getRemoteRuntimeMXBean(RUNTIME_MXBEAN);
	}
	
	public List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
		return remoteMXBeanServer.getRemoteGarbageCollectorMXBeans(GARBAGE_COLLECTOR_MXBEAN_DOMAIN);
	}
}
