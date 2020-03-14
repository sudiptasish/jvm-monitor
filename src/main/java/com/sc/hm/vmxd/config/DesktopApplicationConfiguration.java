package com.sc.hm.vmxd.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;

public class DesktopApplicationConfiguration {

	private static DesktopApplicationConfiguration desktop_appConfig = null;
	
	private Map<String, EnvironmentConfigObject> envConfigMap = new HashMap<String, EnvironmentConfigObject>();
	
	private DesktopApplicationConfiguration() {
	}
	
	public static DesktopApplicationConfiguration getDesktopApplicationInstance() {
		if (desktop_appConfig == null) {
			synchronized (DesktopApplicationConfiguration.class) {
				if (desktop_appConfig == null) {
					synchronized (DesktopApplicationConfiguration.class) {
						desktop_appConfig = new DesktopApplicationConfiguration();
					}
				}
			}
		}
		return desktop_appConfig;
	}
	
	public void setEnvironmentConfig(String applicationId, EnvironmentConfigObject object) {
		envConfigMap.put(applicationId, object);
	}
	
	public EnvironmentConfigObject getEnvironmentConfig(String applicationId) {
		if (envConfigMap.containsKey(applicationId)) {
			return envConfigMap.get(applicationId);
		}
		return null;
	}
	
	public String[] getAllApplicationIds() {
		Set<String> set = envConfigMap.keySet();
		return (String[])set.toArray(new String[set.size()]);
	}
}
