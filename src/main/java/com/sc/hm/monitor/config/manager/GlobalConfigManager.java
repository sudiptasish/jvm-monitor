package com.sc.hm.monitor.config.manager;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.VMEnvironmentConfig;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.util.parser.cobject.CustomizedXMLObject;

public abstract class GlobalConfigManager {
	
	private String environmentType = "";
	private final Properties initial = new Properties();

	public GlobalConfigManager() {}
	
	public void setEnvironmentType(GlobalConfigManager configManager) {
		if (configManager instanceof VMConfigManager) {
			setEnvironmentType(VMConstants.XML_MONITOR_APP.getValue());
		}
	}
	
	public void setEnvironmentType(String envType) {
		environmentType = envType;
	}
	
	protected String getEnvironmentType() {
		return environmentType;
	}
	
	protected Enumeration<?> enumerateInitalProperty() {
		return initial.keys();
	}
	
	protected String getAttribute(String attributeName) {
		return initial.getProperty(attributeName);
	}
	
	public void getInitialPropertyList(String filePath) throws Exception {
		initial.setProperty(environmentType, filePath);
	}
	
	protected void retrievePrimeProperties(CustomizedXMLObject transportConfig) throws Exception {
		Vector<CustomizedXMLObject> child = null;
		String appId = "";
		
		child = transportConfig.getChildrenByName(VMConstants.XML_DEBUG_CONFIG.toString());
		if (child.size() != 0) {
			child = transportConfig.getChildrenByName(VMConstants.XML_ENABLE_PROCESS_DEBUG.toString());
			if (child.size() == 0) {
				throw new Exception("Missing 'enable-process-debug' tag in Configuration File [monitor-config.xml]");
			}
			appId = child.elementAt(0).getNodeValue();
			initial.setProperty(VMConstants.XML_ENABLE_PROCESS_DEBUG.toString(), appId);
			
			child = transportConfig.getChildrenByName(VMConstants.XML_DEBUG_PORT.toString());
			if (child.size() == 0) {
				throw new Exception("Missing 'debug-port' tag in Configuration File [monitor-config.xml]");
			}
			appId = child.elementAt(0).getNodeValue();
			initial.setProperty(VMConstants.XML_DEBUG_PORT.toString(), appId);
            
            child = transportConfig.getChildrenByName(VMConstants.XML_CMD_LAUNCHER.toString());
            if (child.size() == 0) {
                throw new Exception("Missing 'launcher' tag in Configuration File [monitor-config.xml]");
            }
            appId = child.elementAt(0).getNodeValue();
            initial.setProperty(VMConstants.XML_CMD_LAUNCHER.toString(), appId);
            
            child = transportConfig.getChildrenByName(VMConstants.XML_COMMAND_OPT.toString());
            if (child.size() == 0) {
                throw new Exception("Missing 'cmd-opt' tag in Configuration File [monitor-config.xml]");
            }
            appId = child.elementAt(0).getNodeValue();
            initial.setProperty(VMConstants.XML_COMMAND_OPT.toString(), appId);
		}		
		
		child = transportConfig.getChildrenByName(VMConstants.XML_TRANSPORT_CONFIG.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'transport-config' tag in Configuration File [monitor-config.xml]");
		}
		child = transportConfig.getChildrenByName(VMConstants.XML_TRANSPORT_HOST.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'transport-host' tag in Configuration File [monitor-config.xml]");
		}
		appId = child.elementAt(0).getNodeValue();
		initial.setProperty(VMConstants.XML_TRANSPORT_HOST.toString(), appId);
		
		child = transportConfig.getChildrenByName(VMConstants.XML_TRANSPORT_PORT.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'transport-port' tag in Configuration File [monitor-config.xml]");
		}
		appId = child.elementAt(0).getNodeValue();
		initial.setProperty(VMConstants.XML_TRANSPORT_PORT.toString(), appId);
		
		child = transportConfig.getChildrenByName(VMConstants.XML_SMTP_HOST.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'smtp-host' tag in Configuration File [monitor-config.xml]");
		}
		appId = child.elementAt(0).getNodeValue();
		initial.setProperty(VMConstants.XML_SMTP_HOST.toString(), appId);		
	}
	
	public String getPrimePropertiesConvertedInternal() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("\n    ").append("<").append(VMConstants.XML_DEBUG_CONFIG.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_ENABLE_PROCESS_DEBUG.toString()).append(">").append(getAttribute(VMConstants.XML_ENABLE_PROCESS_DEBUG.toString())).append("</").append(VMConstants.XML_ENABLE_PROCESS_DEBUG.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_DEBUG_PORT.toString()).append(">").append(getAttribute(VMConstants.XML_DEBUG_PORT.toString())).append("</").append(VMConstants.XML_DEBUG_PORT.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_CMD_LAUNCHER.toString()).append(">").append(getAttribute(VMConstants.XML_CMD_LAUNCHER.toString())).append("</").append(VMConstants.XML_CMD_LAUNCHER.toString()).append(">");
        sBuilder.append("\n        ").append("<").append(VMConstants.XML_COMMAND_OPT.toString()).append(">").append(getAttribute(VMConstants.XML_COMMAND_OPT.toString())).append("</").append(VMConstants.XML_COMMAND_OPT.toString()).append(">");
        sBuilder.append("\n    ").append("</").append(VMConstants.XML_DEBUG_CONFIG.toString()).append(">");
		sBuilder.append("\n    ").append("<").append(VMConstants.XML_TRANSPORT_CONFIG.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_TRANSPORT_HOST.toString()).append(">").append(getAttribute(VMConstants.XML_TRANSPORT_HOST.toString())).append("</").append(VMConstants.XML_TRANSPORT_HOST.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_TRANSPORT_PORT.toString()).append(">").append(getAttribute(VMConstants.XML_TRANSPORT_PORT.toString())).append("</").append(VMConstants.XML_TRANSPORT_PORT.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_SMTP_HOST.toString()).append(">").append(getAttribute(VMConstants.XML_SMTP_HOST.toString())).append("</").append(VMConstants.XML_SMTP_HOST.toString()).append(">");
		sBuilder.append("\n    ").append("</").append(VMConstants.XML_TRANSPORT_CONFIG.toString()).append(">");
		
		return sBuilder.toString();
	}
	
	public abstract Vector<String> getAllApplicationIds();
	
	public abstract Vector<EnvironmentConfigObject> getAllApplications();
	
	public abstract void createNewConfiguration(EnvironmentConfigObject envConfigObject);
	
	public abstract void deleteConfiguration(String configName);
	
	public abstract void initializeEnvironment(String filePath) throws Exception;
	
	public abstract void spawnEnvironmentConfig() throws Exception;
	
	public abstract VMEnvironmentConfig getEnvConfig(String config_name);
	
	public abstract void reloadEnvironment() throws Exception;
	
	public abstract void reloadEnvironment(EnvironmentConfigObject envConfigObject) throws Exception;
	
	public abstract void flushEnvironmentConfig(String config_name);
	
	public abstract void flushAllEnvironmentConfig();
	
	public abstract String isExistConfiguration(String server, String port);
}
