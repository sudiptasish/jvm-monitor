package com.sc.hm.monitor.ext.jmx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.ObjectName;

public class MXBeanServerHandler extends AbstractMXBeanServerHandler {
	
	private ManagedBeanServer mxBeanServer = null;
	
	public MXBeanServerHandler() {
		super();
	}

	public void initializeMBeanServer(String host, int port) {
		try {
			MXBeanServerConnection mxBeanServerConnection = ManagedBeanServerManager.getConnection(host, String.valueOf(port));
			mxBeanServer = mxBeanServerConnection.getMXBeanServerConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initializeMBeanServer(String jmxURL) {
		try {
			MXBeanServerConnection mxBeanServerConnection = ManagedBeanServerManager.getConnection(jmxURL);
			mxBeanServer = mxBeanServerConnection.getMXBeanServerConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ManagedBeanServer getManagedMBeanServer() {
		return mxBeanServer;
	}
	
	public Map<String, List<String>> groupNames(Set<ObjectName> names) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (ObjectName name : names) {
			String domainType = name.getKeyProperty("type");
			if (!map.containsKey(domainType)) {
				List<String> list = new ArrayList<String>();
				if (name.getKeyProperty("name") != null) {
					list.add(name.getKeyProperty("name"));
					map.put(domainType, list);
				}
				else {
					map.put(domainType, list);
				}
			}
			else {
				List<String> list = map.get(domainType);
				if (name.getKeyProperty("name") != null) {
					list.add(name.getKeyProperty("name"));
					map.put(domainType, list);
				}
			}
		}		
		return map;
	}
}
