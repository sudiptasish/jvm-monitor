package com.sc.hm.monitor.mbean.jmx.remote;

import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
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

import com.sc.hm.monitor.util.Logger;

public class RemoteMXBeanServer {
	
	private static RemoteMXBeanServer _INSTANCE_ = null;

	private MBeanServerConnection mbeanServerConnection = null;
	
	private RemoteMXBeanServer() {}
	
	public static RemoteMXBeanServer getInstance() {
		if (_INSTANCE_ == null) {
			synchronized (RemoteMXBeanServer.class) {
				if (_INSTANCE_ == null) {
					synchronized (RemoteMXBeanServer.class) {
						_INSTANCE_ = new RemoteMXBeanServer();
					}
				}
			}
		}
		return _INSTANCE_;
	}
	
	public void createRemoteMBeanServerConnection(RemoteMXBeanConnector mxBeanConnector) throws Exception {
		try {
			mbeanServerConnection = mxBeanConnector.getRemoteMXBeanConnector().getMBeanServerConnection();
			Logger.log("Created Remote MXBean Server....");
		}
		catch (IOException ie) {
			throw ie;
		}
	}
	
	public MemoryMXBean getRemoteMemoryMXBean(String name) {
		MemoryMXBean memoryBean = null;
		try {
			memoryBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, name, MemoryMXBean.class);		
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		return memoryBean;
	}
	
	public List<MemoryPoolMXBean> getRemoteMemoryPoolMXBeans(String domain) {
		List<MemoryPoolMXBean> memoryPoolBeans = new ArrayList<MemoryPoolMXBean>();
		try {
			Set<ObjectName> set = mbeanServerConnection.queryNames(new ObjectName(domain + ",*"), null);
			for (ObjectName objName : set) {
				MemoryPoolMXBean memoryPoolMXBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, objName.toString(), MemoryPoolMXBean.class);
				memoryPoolBeans.add(memoryPoolMXBean);
			}
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		catch (MalformedObjectNameException mone) {
			mone.printStackTrace();
		}
		return memoryPoolBeans;
	}
	
	public ThreadMXBean getRemoteThreadMXBean(String name) {
		ThreadMXBean threadBean = null;
		try {
			threadBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, name, ThreadMXBean.class);		
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		return threadBean;
	}
	
	public ClassLoadingMXBean getRemoteClassLoadingMXBean(String name) {
		ClassLoadingMXBean classLoadingBean = null;
		try {
			classLoadingBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, name, ClassLoadingMXBean.class);		
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		return classLoadingBean;
	}
	
	public CompilationMXBean getRemoteCompilationMXBean(String name) {
		CompilationMXBean compilationBean = null;
		try {
			compilationBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, name, CompilationMXBean.class);		
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		return compilationBean;
	}
	
	public OperatingSystemMXBean getRemoteOperatingSystemMXBean(String name) {
		OperatingSystemMXBean osBean = null;
		try {
			osBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, name, OperatingSystemMXBean.class);		
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		return osBean;
	}
	
	public OperatingSystemMXBean getRemoteSunOperatingSystemMXBean(String name) {
		com.sun.management.OperatingSystemMXBean osBean = null;
		try {
			osBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, name, com.sun.management.OperatingSystemMXBean.class);		
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		return osBean;
	}
	
	public RuntimeMXBean getRemoteRuntimeMXBean(String name) {
		RuntimeMXBean runtimeBean = null;
		try {
			runtimeBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, name, RuntimeMXBean.class);		
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		return runtimeBean;
	}
	
	public List<GarbageCollectorMXBean> getRemoteGarbageCollectorMXBeans(String domain) {
		List<GarbageCollectorMXBean> gbCollectorBeans = new ArrayList<GarbageCollectorMXBean>();
		try {
			Set<ObjectName> set = mbeanServerConnection.queryNames(new ObjectName(domain + ",*"), null);
			for (ObjectName objName : set) {
				GarbageCollectorMXBean gbCollectorBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, objName.toString(), GarbageCollectorMXBean.class);
				gbCollectorBeans.add(gbCollectorBean);
			}
		}
		catch (MalformedObjectNameException mone) {
			mone.printStackTrace();
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		return gbCollectorBeans;
	}
}
