package com.sc.hm.vmxd.jmx.config;

import java.util.HashMap;
import java.util.Map;

import com.sc.hm.vmxd.jmx.connector.ServerConnectorConfig;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class JMXConfigurationSingleton {

	private static JMXConfigurationSingleton jmxConfiguration = null;
	
	private final Map<String, ServerConnectorConfig> map = new HashMap<>();
	
	private JMXConfigurationSingleton() {}
	
	public static JMXConfigurationSingleton getInstance() {
		if (jmxConfiguration == null) {
			synchronized (JMXConfigurationSingleton.class) {
				if (jmxConfiguration == null) {
					jmxConfiguration = new JMXConfigurationSingleton();
				}
			}
		}
		return jmxConfiguration;
	}
	
	public void setJMXConfiguration(String application, ServerConnectorConfig connectorConfig) {
		map.put(application, connectorConfig);
	}
	
	public ServerConnectorConfig getJMXConfiguration(String application) {
		if (map.containsKey(application)) {
			return map.get(application);
		}
		return null;
	}
}
