package com.sc.hm.monitor.config.manager;

import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.InputStream;
import java.util.Vector;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;

public class VMConfigurationUtil {
	
    public static final String DEFAULT_CONFIG = "config/monitor-config.xml";
	private static ConfigManager configManager = null;
	
	static {
		configManager = new ConfigManager(new VMConfigManager());
	}
	
	public static void loadConfigurations() {
		try {
			String configFilePath = System.getProperty("monitor.config.path");
			if (configFilePath == null) {
			    configFilePath = DEFAULT_CONFIG;
			}
			File file = new File(configFilePath);
			if (!file.exists()) {				
				InputStream iStream = ClassLoader.getSystemClassLoader().getResourceAsStream(configFilePath);
				if (iStream.available() == 0) {
					throw new Exception("Configuration File Not Found... Exiting from the System!!!!");
				}
				else {
					if (iStream != null) {
						iStream.close();
					}
				}
			}
			configManager.initilizeVMEnvironment(configFilePath);
		}
		catch (Exception e) {
			System.err.println("Configuration File 'monitor-config.xml' Not Found in the Classpath.");
			System.exit(-1);
		}
	}
	
	public static synchronized void refreshConfigurations() {
		try {
			configManager.refreshVMEnvironment();
		}
		catch (Exception e) {
			System.err.println("Error Refreshing Environment" + e);
		}
	}
	
	public static synchronized void refreshConfigurations(EnvironmentConfigObject envConfigObject) {
		try {
			configManager.refreshVMEnvironment(envConfigObject);
		}
		catch (Exception e) {
			System.err.println("Error Refreshing Environment" + e);
		}
	}
	
	public static String getPrimeProperty(String propertyName) {
		return configManager.getPrimeProperty(propertyName);
	}
	
	public static int getPrimePropertyAsInt(String propertyName) {
		return parseInt(configManager.getPrimeProperty(propertyName));
	}
	
	public static EnvironmentConfigObject getEnvConfigProperty(String configName) {
		return configManager.getEnvironmentProperty(configName);
	}
	
	public static Vector<String> getAllApplicationIds() {
		return configManager.getAllApplicationIds();
	}
	
	public static void setEnvConfigProperty(String configName, EnvironmentConfigObject envConfigObject) {
		configManager.setEnvironmentProperty(configName, envConfigObject);
	}
	
	public static EnvironmentConfigObject getDefaultEnvConfigProperty() {
		return configManager.getEnvironmentProperty(VMConstants.DEFAULT_CONFIG.getValue());
	}
	
	public static int generateNextApplicationId() {
		int applicationId = -1;
		Vector<String> appIds = configManager.getAllApplicationIds();
		for (String appId : appIds) {
			if (parseInt(appId) > applicationId) {
				applicationId = parseInt(appId);
			}
		}
		return (applicationId + 1);
	}
	
	public static String isExistConfiguration(String server, String port) {
		return configManager.isExistConfiguration(server, port);
	}
	
	public static Vector<EnvironmentConfigObject> getAllApplications() {
		return configManager.getAllApplications();
	}
	
	public static void createNewConfiguration(EnvironmentConfigObject envConfigObject) {
		configManager.createNewConfiguration(envConfigObject);
	}
	
	public static void deleteConfiguration(String configName) {
		configManager.deleteConfiguration(configName);
	}
	
	public static String getPrimePropertiesConverted() {
		return configManager.getPrimePropertiesConverted();
	}
}
