package com.sc.hm.monitor.config.manager;

import java.util.Enumeration;
import java.util.Vector;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.VMEnvironmentConfig;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.parser.cobject.CustomizedXMLObject;
import com.sc.hm.monitor.util.parser.cobject.ParserNode;
import com.sc.hm.monitor.util.parser.factory.AbstractXMLParserFactory;
import com.sc.hm.monitor.util.parser.factory.intf.ParserFactoryType;
import com.sc.hm.monitor.util.parser.factory.intf.XMLParser;

class VMConfigManager extends ConfigManager {
	
	private static final long serialVersionUID = 1L;
	private String[] configName = null;
	private VMEnvironmentConfig[] envConfig = null;
	
	public VMConfigManager() {
		super();
	}
	
	public void initializeEnvironment(String filePath) throws Exception {
		setEnvironmentType(this);
		setInitialConfigList(filePath);
		
		for (Enumeration<?> enm = enumerateInitalProperty(); enm.hasMoreElements();) {
			String propertyName = (String)enm.nextElement();
			Logger.log(propertyName + " :: " + getAttribute(propertyName));
		}
	}
	
	private void setInitialConfigList(String filePath) throws Exception {
		super.getInitialPropertyList(filePath);
	}
	
	public void spawnEnvironmentConfig() throws Exception {
		String VMConfig = getAttribute(getEnvironmentType());
		if (VMConfig != null && !"".equals(VMConfig)) {
			CustomizedXMLObject cXMLObject = initializeFromXML(VMConfig);
			retrievePrimeProperties(cXMLObject);
			_spawnInternal(cXMLObject);
		}
	}
	
	private void _spawnInternal(CustomizedXMLObject parentObject) throws Exception {
		Vector<CustomizedXMLObject> childList = parentObject.getChildrenByName(VMConstants.XML_APP_CONFIG.toString());
		configName = new String[childList.size()];
		envConfig = new VMEnvironmentConfig[childList.size()];
		
		int config_count = 0;
		for (Enumeration<CustomizedXMLObject> enm = childList.elements(); enm.hasMoreElements(); config_count ++) {
			CustomizedXMLObject appConfig = enm.nextElement();
			CustomizedXMLObject appId = appConfig.getChildrenByName(VMConstants.XML_APP_ID.getValue()).elementAt(0);
			configName[config_count] = appId.getNodeValue();
			envConfig[config_count] = new VMEnvironmentConfig(appId.getNodeValue());
			
			startIndividualConfiguration(appId.getNodeValue(), envConfig[config_count], appConfig);
		}
	}
	
	private CustomizedXMLObject initializeFromXML(String file) throws Exception {
		CustomizedXMLObject cXMLObject = new ParserNode(getEnvironmentType());
		ParserFactoryType parserFactory = AbstractXMLParserFactory.getXMLParserFactory().getParserFactory(VMConstants.PARSER_FACTORY_TYPE_DOM.toString());
		XMLParser xmlParser = parserFactory.newParser();
		xmlParser.parse(file, cXMLObject);
		Logger.log("Final XML Object: " + cXMLObject);
		
		return cXMLObject;
	}
	
	public VMEnvironmentConfig getEnvConfig(String config_name) {
		VMEnvironmentConfig env_onfig = null;
		for (byte i = 0; i < configName.length; i ++) {
			if (config_name.equals(configName[i])) {
				env_onfig = envConfig[i];
				break;
			}
		}
		return env_onfig;
	}
	
	public Vector<String> getAllApplicationIds() {
		Vector<String> v = new Vector<String>();
		for (byte i = 0; i < configName.length; i ++) {
			v.addElement(configName[i]);
		}
		return v;
	}
	
	public Vector<EnvironmentConfigObject> getAllApplications() {
		Vector<EnvironmentConfigObject> v = new Vector<EnvironmentConfigObject>();
		for (byte i = 0; i < configName.length; i ++) {
			v.addElement(envConfig[i].getEnvironmentObject());
		}
		return v;
	}
	
	public void flushEnvironmentConfig(String config_name) {
		for (byte i = 0; i < configName.length; i ++) {
			if (config_name.equals(configName[i])) {
				envConfig[i].resetEnvironmentProperty();
				break;
			}			
		}
	}
	
	public void flushAllEnvironmentConfig() {
		for (byte i = 0; i < envConfig.length; i ++) {
			envConfig[i].resetEnvironmentProperty();
		}
	}
	
	public void reloadEnvironment(EnvironmentConfigObject envConfigObject) throws Exception {
		for (byte i = 0; i < configName.length; i ++) {
			if (envConfigObject.getApplicationId().equals(configName[i])) {
				envConfig[i].setEnvironmentObject(envConfigObject);
				break;
			}			
		}
	}
	
	public void reloadEnvironment() throws Exception {
		for (byte config_count = 0; config_count < envConfig.length; config_count ++) {
			//initializeEnvironmentConfig(envConfig[config_count]);
		}
	}
	
	public String isExistConfiguration(String server, String port) {
		String existAppName = "";
		for (byte i = 0; i < envConfig.length; i ++) {
			String s = envConfig[i].getEnvironmentObject().getServerId();
			String p = envConfig[i].getEnvironmentObject().getPort();
			if (s.equalsIgnoreCase(server) && p.equalsIgnoreCase(port)) {
				existAppName = envConfig[i].getEnvironmentObject().getApplicationName();
				break;
			}
		}
		return existAppName;
	}
	
	public synchronized void createNewConfiguration(EnvironmentConfigObject envConfigObject) {
		String[] temp_configName = new String[configName.length + 1];
		VMEnvironmentConfig[] temp_envConfig = new VMEnvironmentConfig[envConfig.length + 1];
		
		System.arraycopy(configName, 0, temp_configName, 0, configName.length);
		temp_configName[configName.length] = envConfigObject.getApplicationId();
		configName = temp_configName;
		
		System.arraycopy(envConfig, 0, temp_envConfig, 0, envConfig.length);
		VMEnvironmentConfig vmConfig = new VMEnvironmentConfig(envConfigObject.getApplicationId());
		vmConfig.initializeCache();
		vmConfig.initialize(envConfigObject);
		temp_envConfig[envConfig.length] = vmConfig;
		envConfig = temp_envConfig;
	}
	
	public synchronized void deleteConfiguration(String config_name) {
		int i = 0;
		for (; i < configName.length; i ++) {
			if (config_name.equals(configName[i])) {
				deleteEnvironmentConfig(envConfig[i]);
				break;
			}			
		}
		String[] temp_configName = new String[configName.length - 1];
		System.arraycopy(configName, 0, temp_configName, 0, i);			// Copy first half Elements
		if (i <  configName.length - 1) {
			System.arraycopy(configName, (i + 1), temp_configName, i, configName.length - (i + 1));
		}
		
		VMEnvironmentConfig[] temp_envConfig = new VMEnvironmentConfig[envConfig.length - 1];
		System.arraycopy(envConfig, 0, temp_envConfig, 0, i);
		if (i <  configName.length - 1) {
			System.arraycopy(envConfig, (i + 1), temp_envConfig, i, envConfig.length - (i + 1));
		}
		configName = temp_configName;
		envConfig = temp_envConfig;
	}
	
	private void deleteEnvironmentConfig(VMEnvironmentConfig env_config) {
		env_config.resetEnvironmentProperty();
		env_config = null;
	}
}
