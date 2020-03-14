package com.sc.hm.vmxd.jmx.manager;

import java.util.HashMap;
import java.util.Map;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.MXBeanServerFactory;

/**
 * MBean server manager to hold the server factory.
 * 
 * @author Sudiptasish Chanda
 */
public class MXBeanServerManager {
	
	private static final Map<String, MXBeanServerFactory> mxbeanServerFactoryMap = new HashMap<>();

	public static void initializeMXBeanServer(String applicationId, String serverType) throws Exception {
		if (!mxbeanServerFactoryMap.containsKey(applicationId)) {
			MXBeanServerFactory mxbeanServerFactory = MXBeanServerFactory.getMXBeanServerFactory(serverType);
			mxbeanServerFactory.createMXBeanServer(applicationId);
			mxbeanServerFactoryMap.put(applicationId, mxbeanServerFactory);
			Logger.log("Created MXBean Server for Application [" + applicationId + "]");
		}
	}
	
	public static MXBeanServer getMXBeanServer(String applicationId) throws Exception {
		if (mxbeanServerFactoryMap.containsKey(applicationId)) {
			return mxbeanServerFactoryMap.get(applicationId).getMXBeanServer(applicationId);
		}
		return null;
	}
}
