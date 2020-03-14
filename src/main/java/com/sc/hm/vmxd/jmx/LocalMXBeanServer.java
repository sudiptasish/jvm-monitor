package com.sc.hm.vmxd.jmx;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

import javax.management.MBeanServer;

/**
 * Local MBean server.
 * 
 * @author Sudiptasish Chanda
 */
public class LocalMXBeanServer extends MXBeanServerAdapter  implements MXBeanServer {

	public LocalMXBeanServer(String name, MBeanServer server) {
		super(name, server);
	}
	
	public MemoryMXBean getMemoryMXBean() throws Exception {
		return ManagementFactory.getMemoryMXBean();
	}
	
	public RuntimeMXBean getRuntimeMXBean() throws Exception {
		return ManagementFactory.getRuntimeMXBean();
	}
	
	public OperatingSystemMXBean getOperatingSystemMXBean() throws Exception {
		return ManagementFactory.getOperatingSystemMXBean();
	}
	
	public CompilationMXBean getCompilationMXBean() throws Exception {
		return ManagementFactory.getCompilationMXBean();
	}
	
	public ThreadMXBean getThreadMXBean() throws Exception {
		return ManagementFactory.newPlatformMXBeanProxy(mbeanServer, ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
	}
	
	public List<MemoryPoolMXBean> getMemoryPoolMXBean() throws Exception {
		return ManagementFactory.getMemoryPoolMXBeans();
	}
	
	public List<GarbageCollectorMXBean> getGarbageCollectorMXBean() throws Exception {
		return ManagementFactory.getGarbageCollectorMXBeans();
	}
	
	public List<MemoryManagerMXBean> getMemoryManagerMXBean() throws Exception {
		return ManagementFactory.getMemoryManagerMXBeans();
	}

	public ClassLoadingMXBean getClassLoadingMXBean() throws Exception {
		return ManagementFactory.getClassLoadingMXBean();
	}
}
