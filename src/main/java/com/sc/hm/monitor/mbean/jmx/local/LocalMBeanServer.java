package com.sc.hm.monitor.mbean.jmx.local;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

public class LocalMBeanServer {

	private static LocalMBeanServer _INSTANCE_ = null;

	private LocalMBeanServer() {}
	
	public static LocalMBeanServer getInstance() {
		if (_INSTANCE_ == null) {
			synchronized (LocalMBeanServer.class) {
				if (_INSTANCE_ == null) {
					synchronized (LocalMBeanServer.class) {
						_INSTANCE_ = new LocalMBeanServer();
					}
				}
			}
		}
		return _INSTANCE_;
	}
	
	public MemoryMXBean getLocalMemoryMXBean() {
		return ManagementFactory.getMemoryMXBean();
	}
	
	public List<MemoryPoolMXBean> getLocalMemoryPoolMXBeans() {
		return ManagementFactory.getMemoryPoolMXBeans();
	}
	
	public ThreadMXBean getLocalThreadMXBean() {
		return ManagementFactory.getThreadMXBean();
	}
	
	public ClassLoadingMXBean getLocalClassLoadingMXBean() {
		return ManagementFactory.getClassLoadingMXBean();
	}
	
	public CompilationMXBean getLocalCompilationMXBean() {
		return ManagementFactory.getCompilationMXBean();
	}
	
	public OperatingSystemMXBean getLocalOperatingSystemMXBean() {
		return ManagementFactory.getOperatingSystemMXBean();
	}
	
	public RuntimeMXBean getLocalRuntimeMXBean() {
		return ManagementFactory.getRuntimeMXBean();
	}
	
	public List<GarbageCollectorMXBean> getLocalGarbageCollectorMXBeans() {
		return ManagementFactory.getGarbageCollectorMXBeans();
	}
}
