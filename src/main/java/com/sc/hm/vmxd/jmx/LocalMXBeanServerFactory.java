package com.sc.hm.vmxd.jmx;

import java.lang.management.ManagementFactory;

import com.sc.hm.monitor.util.Logger;

/**
 * MBean server factory to instantiate annd initialize a local mbeann server.
 * 
 * @author Sudiptasish Chanda
 */
public class LocalMXBeanServerFactory extends MXBeanServerFactory {
	
	private static LocalMXBeanServerFactory lSeverFactory = null;
	
	private boolean isExit = false;
	private MXBeanServer mxbeanServer = null;
	
	private LocalMXBeanServerFactory() {
		super();
	}
	
	public static MXBeanServerFactory getMXBeanServerFactory() {
		if (lSeverFactory == null) {
			synchronized (LocalMXBeanServerFactory.class) {
				if (lSeverFactory == null) {
					lSeverFactory = new LocalMXBeanServerFactory();
				}
			}
		}
		return lSeverFactory;
	}

	public synchronized void createMXBeanServer(String application) throws Exception {
		if (!isExit) {
			mxbeanServer = new LocalMXBeanServer(application, ManagementFactory.getPlatformMBeanServer());
			Logger.log("Created Local MX Bean Server....");
			isExit = true;
		}
	}
	
	public synchronized MXBeanServer getMXBeanServer(String application) throws Exception {
		if (mxbeanServer == null) {
			createMXBeanServer(application);
		}
		return mxbeanServer;
	}
}
