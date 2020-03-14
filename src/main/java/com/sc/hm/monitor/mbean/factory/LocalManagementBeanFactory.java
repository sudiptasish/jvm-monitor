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

import com.sc.hm.monitor.mbean.jmx.local.LocalMBeanServer;

public class LocalManagementBeanFactory extends ManagementBeanFactory {
	
	private static LocalManagementBeanFactory localManagementBeanFactory = null;
	
	private LocalMBeanServer localMBeanServer = null;

	private LocalManagementBeanFactory() {
		super();
		getLocalMXBeanServerSingleton();
	}
	
	public static synchronized LocalManagementBeanFactory getLocalManagementBeanFactory() {
		if (localManagementBeanFactory == null) {
			localManagementBeanFactory = new LocalManagementBeanFactory();
		}
		return localManagementBeanFactory;
	}
	
	private void getLocalMXBeanServerSingleton() {
		localMBeanServer = LocalMBeanServer.getInstance();
	}
	
	public void initializeMBeanFactory(Object[] init_params) throws Exception {		
	}
	
	public MemoryMXBean getMemoryMXBean() {
		return localMBeanServer.getLocalMemoryMXBean();
	}
	
	public List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
		return localMBeanServer.getLocalMemoryPoolMXBeans();
	}
	
	public ThreadMXBean getThreadMXBean() {
		return localMBeanServer.getLocalThreadMXBean();
	}
	
	public ClassLoadingMXBean getClassLoadingMXBean() {
		return localMBeanServer.getLocalClassLoadingMXBean();
	}
	
	public CompilationMXBean getCompilationMXBean() {
		return localMBeanServer.getLocalCompilationMXBean();
	}
	
	public OperatingSystemMXBean getOperatingSystemMXBean() {
		return localMBeanServer.getLocalOperatingSystemMXBean();
	}
	
	public RuntimeMXBean getRuntimeMXBean() {
		return localMBeanServer.getLocalRuntimeMXBean();
	}
	
	public List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
		return localMBeanServer.getLocalGarbageCollectorMXBeans();
	}
	
	public void disconnectMXBeanServer() {}
}
