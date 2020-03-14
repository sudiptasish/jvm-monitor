package com.sc.hm.vmxd.jmx;

import java.io.IOException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Remote mbean server.
 * 
 * @author Sudiptasish Chanda
 */
public class RemoteMXBeanServer extends MXBeanServerAdapter implements MXBeanServer {

	public RemoteMXBeanServer(String name, MBeanServerConnection server) {
		super(name, server);
	}
	
	public MemoryMXBean getMemoryMXBean() throws Exception {
		return ManagementFactory.newPlatformMXBeanProxy(mbeanServer
            , ManagementFactory.MEMORY_MXBEAN_NAME
            , MemoryMXBean.class);
	}
	
	public RuntimeMXBean getRuntimeMXBean() throws Exception {
		return ManagementFactory.newPlatformMXBeanProxy(mbeanServer
            , ManagementFactory.RUNTIME_MXBEAN_NAME,
            RuntimeMXBean.class);
	}
	
	public OperatingSystemMXBean getOperatingSystemMXBean() throws Exception {
		return ManagementFactory.newPlatformMXBeanProxy(mbeanServer
            , ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME
            , OperatingSystemMXBean.class);
	}
	
	public CompilationMXBean getCompilationMXBean() throws Exception {
		return ManagementFactory.newPlatformMXBeanProxy(mbeanServer
            , ManagementFactory.COMPILATION_MXBEAN_NAME
            , CompilationMXBean.class);
	}
	
	public ThreadMXBean getThreadMXBean() throws Exception {
		return ManagementFactory.newPlatformMXBeanProxy(mbeanServer
            , ManagementFactory.THREAD_MXBEAN_NAME
            , ThreadMXBean.class);
	}
	
	public List<MemoryPoolMXBean> getMemoryPoolMXBean() throws Exception {
		List<MemoryPoolMXBean> list = new ArrayList<MemoryPoolMXBean>();
		try {
			ObjectName objectName = new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + ",*");
			
			Set<?> set = mbeanServer.queryNames(objectName, null);
			for (Object obj : set) {
				ObjectName name = (ObjectName)obj;
				MemoryPoolMXBean memoryPoolMXBean = ManagementFactory.newPlatformMXBeanProxy(
                    mbeanServer
                    , name.toString()
                    , MemoryPoolMXBean.class);
				list.add(memoryPoolMXBean);
			}
		}
		catch (MalformedObjectNameException mlfone) {
			throw mlfone;
		}
		catch (IOException ie) {
			throw ie;
		}
		return list;
	}
	
	public List<GarbageCollectorMXBean> getGarbageCollectorMXBean() throws Exception {
		List<GarbageCollectorMXBean> list = new ArrayList<GarbageCollectorMXBean>();
		try {
			ObjectName objectName = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",*");
		
			Set<?> set = mbeanServer.queryNames(objectName, null);
			for (Object obj : set) {
				ObjectName name = (ObjectName)obj;
				GarbageCollectorMXBean gbCollectorMXBean = ManagementFactory.newPlatformMXBeanProxy(
                    mbeanServer
                    , name.toString()
                    , GarbageCollectorMXBean.class);
				list.add(gbCollectorMXBean);
			}
		}
		catch (MalformedObjectNameException mlfone) {
			throw mlfone;
		}
		catch (IOException ie) {
			throw ie;
		}
		return list;
	}
	
	public List<MemoryManagerMXBean> getMemoryManagerMXBean() throws Exception {
		List<MemoryManagerMXBean> list = new ArrayList<MemoryManagerMXBean>();
		try {
			ObjectName objectName = new ObjectName(ManagementFactory.MEMORY_MANAGER_MXBEAN_DOMAIN_TYPE + ",*");
			
			Set<?> set = mbeanServer.queryNames(objectName, null);
			for (Object obj : set) {
				ObjectName name = (ObjectName)obj;
				MemoryManagerMXBean gbCollectorMXBean = ManagementFactory.newPlatformMXBeanProxy(
                    mbeanServer
                    , name.toString()
                    , MemoryManagerMXBean.class);
				list.add(gbCollectorMXBean);
			}
		}
		catch (MalformedObjectNameException mlfone) {
			throw mlfone;
		}
		catch (IOException ie) {
			throw ie;
		}
		return list;
	}

	public ClassLoadingMXBean getClassLoadingMXBean() throws Exception {
		return ManagementFactory.newPlatformMXBeanProxy(mbeanServer
            , ManagementFactory.CLASS_LOADING_MXBEAN_NAME
            , ClassLoadingMXBean.class);
	}
}
