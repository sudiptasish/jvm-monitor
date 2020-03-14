package com.sc.hm.monitor.mbean.factory;

import static java.lang.management.ManagementFactory.*;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

public abstract class ManagementBeanFactory {

	public static final String FACTORY_TYPE_LOCAL = "FACTORY_TYPE_LOCAL";
	public static final String FACTORY_TYPE_REOMTE = "FACTORY_TYPE_REOMTE";

	protected final String CLASS_LOADING_MX_BEAN = CLASS_LOADING_MXBEAN_NAME;
	protected final String COMPILATION_MXBEAN = COMPILATION_MXBEAN_NAME;
	protected final String MEMORY_MXBEAN = MEMORY_MXBEAN_NAME;
	protected final String OPERATING_SYSTEM_MXBEAN = OPERATING_SYSTEM_MXBEAN_NAME;
	protected final String RUNTIME_MXBEAN = RUNTIME_MXBEAN_NAME;
	protected final String THREAD_MXBEAN = THREAD_MXBEAN_NAME;
	protected final String MEMORY_POOL_MXBEAN_DOMAIN = MEMORY_POOL_MXBEAN_DOMAIN_TYPE;
	protected final String MEMORY_MANAGER_MXBEAN_DOMAIN = MEMORY_MANAGER_MXBEAN_DOMAIN_TYPE;
	protected final String GARBAGE_COLLECTOR_MXBEAN_DOMAIN = GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE;
	
	public ManagementBeanFactory() {}
	
	public static ManagementBeanFactory getManagementBeanFactory(String factoryType) {
		if (factoryType.equalsIgnoreCase(FACTORY_TYPE_LOCAL)) {
			return LocalManagementBeanFactory.getLocalManagementBeanFactory();
		}
		else if (factoryType.equalsIgnoreCase(FACTORY_TYPE_REOMTE)) {
			return RemoteManagementBeanFactory.getRemoteManagementBeanFactory();
		}
		else {
			return null;
		}
	}
	
	public abstract void initializeMBeanFactory(Object[] init_params) throws Exception;
	
	public abstract MemoryMXBean getMemoryMXBean();
	
	public abstract List<MemoryPoolMXBean> getMemoryPoolMXBeans();
	
	public abstract ThreadMXBean getThreadMXBean();
	
	public abstract ClassLoadingMXBean getClassLoadingMXBean();
	
	public abstract CompilationMXBean getCompilationMXBean();
	
	public abstract OperatingSystemMXBean getOperatingSystemMXBean();
	
	public abstract RuntimeMXBean getRuntimeMXBean();
	
	public abstract List<GarbageCollectorMXBean> getGarbageCollectorMXBeans();
	
	public abstract void disconnectMXBeanServer();
}
