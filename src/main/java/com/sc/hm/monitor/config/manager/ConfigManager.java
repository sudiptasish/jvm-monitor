package com.sc.hm.monitor.config.manager;

import java.util.Vector;

import com.sc.hm.monitor.config.VMEnvironmentConfig;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.util.parser.cobject.CustomizedXMLObject;

class ConfigManager extends GlobalConfigManager {
	
	private GlobalConfigManager configManager = null;
	
	public ConfigManager() {}
	
	public ConfigManager(GlobalConfigManager manager) {
		configManager = manager;
	}
	
	public void initilizeVMEnvironment(String filePath) throws Exception {
		initializeEnvironment(filePath);
		spawnEnvironmentConfig();
	}
	
	public void initializeEnvironment(String filePath) throws Exception {
		configManager.initializeEnvironment(filePath);
	}
	
	public void spawnEnvironmentConfig() throws Exception {
		configManager.spawnEnvironmentConfig();
	}

	public void startIndividualConfiguration(String configName, VMEnvironmentConfig envConfig, CustomizedXMLObject appConfig) throws Exception {
		envConfig.initializeCache();
		envConfig.setEnvornmentFile(configName);
		
		initializeEnvironmentConfig(envConfig, appConfig);
	}
	
	public void initializeEnvironmentConfig(VMEnvironmentConfig envConfig, CustomizedXMLObject appConfig) throws Exception {
		envConfig.initialize(appConfig);
	}
	
	public EnvironmentConfigObject getEnvironmentProperty(String envConfig) {
		return getEnvConfig(envConfig).getEnvironmentCache().getEnvConfiguration();
	}
	
	public void setEnvironmentProperty(String envConfig, EnvironmentConfigObject envConfigObject) {
		getEnvConfig(envConfig).getEnvironmentCache().setEnvConfiguration(envConfigObject);
	}
	
	public String getPrimeProperty(String propertyName) {
		return configManager.getAttribute(propertyName);
	}
	
	public Vector<String> getAllApplicationIds() {
		return configManager.getAllApplicationIds();
	}
	
	public Vector<EnvironmentConfigObject> getAllApplications() {
		return configManager.getAllApplications();
	}
	
	public void createNewConfiguration(EnvironmentConfigObject envConfigObject) {
		configManager.createNewConfiguration(envConfigObject);
	}
	
	public void deleteConfiguration(String configName) {
		configManager.deleteConfiguration(configName);
	}
	
	public String getPrimePropertiesConverted() {
		return configManager.getPrimePropertiesConvertedInternal();
	}
	
	public String isExistConfiguration(String server, String port) {
		return configManager.isExistConfiguration(server, port);
	}
	
	public VMEnvironmentConfig getEnvConfig(String config_name) {
		return configManager.getEnvConfig(config_name);
	}

	public void refreshVMEnvironment() throws Exception {
		flushAllEnvironmentConfig();
		reloadEnvironment();
	}

	public void refreshVMEnvironment(EnvironmentConfigObject envConfigObject) throws Exception {
		flushEnvironmentConfig(envConfigObject.getApplicationId());
		reloadEnvironment(envConfigObject);
	}
	
	public void reloadEnvironment(EnvironmentConfigObject envConfigObject) throws Exception {
		configManager.reloadEnvironment(envConfigObject);
	}
	
	public void reloadEnvironment() throws Exception {
		configManager.reloadEnvironment();
	}
	
	public void flushEnvironmentConfig(String config_name) {
		configManager.flushEnvironmentConfig(config_name);
	}
	
	public void flushAllEnvironmentConfig() {
		configManager.flushAllEnvironmentConfig();
	}
}
