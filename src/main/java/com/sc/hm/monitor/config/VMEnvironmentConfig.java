package com.sc.hm.monitor.config;

import java.util.Vector;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.config.cache.VMEnvironmentCache;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.parser.cobject.CustomizedXMLObject;

public class VMEnvironmentConfig {

	private String config_name = "";
	private String env_file = "";
	
	private VMEnvironmentCache envCache = null;
	
	public VMEnvironmentConfig(String config_name) {
		this.config_name = config_name;		
	}
	
	public String getEnvironmentConfigName() {
		return config_name;
	}
	
	public VMEnvironmentCache getEnvironmentCache() {
		return envCache;
	}
	
	public void initializeCache() {
		envCache = new VMEnvironmentCache();
	}
	
	public void setEnvornmentFile(String file) {
		env_file = file;
	}
	
	public String getEnvornmentFile() {
		return env_file;
	}
	
	public void initialize(CustomizedXMLObject appConfig) throws Exception {
		init_internal(appConfig);
	}
	
	private void init_internal(CustomizedXMLObject appConfig) throws Exception {
		Vector<CustomizedXMLObject> child = null;
		String appId = "";
		String appName = "";
		
		EnvironmentConfigObject envConfigObject = new EnvironmentConfigObject();
		
		child = appConfig.getChildrenByName(VMConstants.XML_APP_ID.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'app-id' tag in Configuration File [monitor-config.xml]");
		}
		appId = child.elementAt(0).getNodeValue();
		//setEnvironmentProperty(VMConstants.XML_APP_ID.toString(), appId);
		envConfigObject.setApplicationId(appId);
		
		child = appConfig.getChildrenByName(VMConstants.XML_APP_NAME.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'app-name' tag in Configuration File [monitor-config.xml]");
		}
		appName = child.elementAt(0).getNodeValue();
		//setEnvironmentProperty(VMConstants.XML_APP_NAME.toString(), appName);
		envConfigObject.setApplicationName(appName);
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MONITOR_AREA.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'monitor-area' tag in Configuration File [monitor-config.xml]");
		}
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MONITOR_MEMORY.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'monitor-memory' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_SERVER_NAME.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMonitorMemory(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MONITOR_MEMORYPOOL.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'monitor-memorypool' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_SERVER_NAME.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMonitorMemorypool(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MONITOR_THREAD.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'monitor-thread' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_SERVER_NAME.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMonitorThread(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MONITOR_GC.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'monitor-gc' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_SERVER_NAME.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMonitorGc(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MONITOR_CLASS.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'monitor-class' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_SERVER_NAME.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMonitorClass(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MONITOR_CPU.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'monitor-cpu' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_SERVER_NAME.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMonitorCpu(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MONITOR_MBEAN.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'monitor-mbean' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_SERVER_NAME.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMonitorMBean(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_APP_CREDENTIALS.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'app-credentials' tag in Configuration File [monitor-config.xml]");
		}
		child = appConfig.getChildrenByName(VMConstants.XML_SERVER_NAME.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'server-name' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_SERVER_NAME.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setServerId(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_SERVER_PORT.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'server-port' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_SERVER_PORT.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setPort(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_USER_CREDENTIALS.toString());
        if (child.size() == 0) {
            throw new Exception("Missing 'user-credentials' tag in Configuration File [monitor-config.xml]");
        }
        child = appConfig.getChildrenByName(VMConstants.XML_USER_NAME.toString());
        if (child.size() == 0) {
            throw new Exception("Missing 'user-name' tag in Configuration File [monitor-config.xml]");
        }
        envConfigObject.setUserName(child.elementAt(0).getNodeValue());
        
        child = appConfig.getChildrenByName(VMConstants.XML_USER_PASSWORD.toString());
        if (child.size() == 0) {
            throw new Exception("Missing 'user-password' tag in Configuration File [monitor-config.xml]");
        }
        envConfigObject.setPassword(child.elementAt(0).getNodeValue());
        
        child = appConfig.getChildrenByName(VMConstants.XML_USER_ROLE.toString());
        if (child.size() == 0) {
            throw new Exception("Missing 'user-role' tag in Configuration File [monitor-config.xml]");
        }
        envConfigObject.setRole(child.elementAt(0).getNodeValue());
        
        child = appConfig.getChildrenByName(VMConstants.XML_ROLE_PASSWORD.toString());
        if (child.size() == 0) {
            throw new Exception("Missing 'role-password' tag in Configuration File [monitor-config.xml]");
        }
        envConfigObject.setRolePassword(child.elementAt(0).getNodeValue());
        
        child = appConfig.getChildrenByName(VMConstants.XML_MBEAN_SERVER.toString());
        if (child.size() == 0) {
            throw new Exception("Missing 'mbean-server' tag in Configuration File [monitor-config.xml]");
        }
        envConfigObject.setMserver(child.elementAt(0).getNodeValue());
        
        child = appConfig.getChildrenByName(VMConstants.XML_CONFIG_DETAILS.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'config-details' tag in Configuration File [monitor-config.xml]");
		}
		child = appConfig.getChildrenByName(VMConstants.XML_ENABLE_SSL.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'enable-ssl' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_ENABLE_SSL.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setEnableSSL(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_ENABLE_LOGGING.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'enable-logging' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_ENABLE_LOGGING.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setEnableLogging(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_ENABLE_MAILING.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'enable-mailing' tag in Configuration File [monitor-config.xml");
		}
		//setEnvironmentProperty(VMConstants.XML_ENABLE_MAILING.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setEnableMailing(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_MAILING_CONFIG.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'mail-config' tag in Configuration File [monitor-config.xml]");
		}
		
		child = appConfig.getChildrenByName(VMConstants.XML_MAILING_CAUSE.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'mail-cause' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_MAILING_CAUSE.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMailCause(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_MAILING_FROM.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'mail-from' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_MAILING_FROM.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMailFrom(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_MAILING_TO.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'mail-to' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_MAILING_TO.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMailTo(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_MAILING_CC.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'mail-cc' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_MAILING_CC.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setMailCc(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_ENABLE_AUTO_P.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'enable-auto-persistence' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_ENABLE_AUTO_P.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setEnablePersistence(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_ENABLE_AUTO_C.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'enable-auto-connect' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_ENABLE_AUTO_C.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setEnableConnect(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_PERSISTENCE.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'persistence' tag in Configuration File [monitor-config.xml]");
		}
		
		child = appConfig.getChildrenByName(VMConstants.XML_PERSISTENCE_FILE.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'persistence-file-path' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_PERSISTENCE_FILE.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setPersistFilePath(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_PERSISTENCE_INTERVAL.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'persistence-interval' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_PERSISTENCE_INTERVAL.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setPersistInterval(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_ENABLE_INDPNDNT_START.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'enable-independent-startup' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_ENABLE_INDPNDNT_START.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setIndependentProcess(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_ARGS.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'vm-args' tag in Configuration File [monitor-config.xml]");
		}
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MIN_PARAM.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'min-param' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_VM_MIN_PARAM.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setVmMemoryMin(child.elementAt(0).getNodeValue());
		
		child = appConfig.getChildrenByName(VMConstants.XML_VM_MAX_PARAM.toString());
		if (child.size() == 0) {
			throw new Exception("Missing 'max-param' tag in Configuration File [monitor-config.xml]");
		}
		//setEnvironmentProperty(VMConstants.XML_VM_MAX_PARAM.toString(), child.elementAt(0).getNodeValue());
		envConfigObject.setVmMemoryMax(child.elementAt(0).getNodeValue());
		setEnvironmentObject(envConfigObject);
		
		Logger.log("Loaded Configuration for Application [Id: " + appId + ". Name: " + appName + ".]");
	}
	
	public void initialize(EnvironmentConfigObject envConfigObject) {
		setEnvironmentObject(envConfigObject);
	}
	
	public void setEnvironmentObject(EnvironmentConfigObject envConfigObject) {
		envCache.setEnvConfiguration(envConfigObject);
	}
	
	public EnvironmentConfigObject getEnvironmentObject() {
		return envCache.getEnvConfiguration();
	}
	
	public void resetEnvironmentProperty() {
		envCache.flushEnvConfiguration();
	}
}
