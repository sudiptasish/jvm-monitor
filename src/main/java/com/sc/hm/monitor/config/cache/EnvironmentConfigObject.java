package com.sc.hm.monitor.config.cache;

import java.io.Serializable;

import com.sc.hm.monitor.common.VMConstants;

public class EnvironmentConfigObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String smtp_host = "";
	
	private String applicationId = "";
	private String applicationName = "";
	
	private String serverId = "";
	private String port = "";
	private String userName = "";
	private String password = "";
	private String role = "";
	private String rolePassword = "";
	private String protocol = "";
    private String jndiRoot = "";
    private String mserver = "";
    private String providerPackage = "";
    private String waitTime = "";
	
	private String enableSSL = "";
	private String enableLogging = "";
	private String enableMailing = "";
	private String mailCause = "";
	private String mailFrom = "";
	private String mailTo = "";
	private String mailCc = "";
	private String enableConnect = "";
	private String enablePersistence = "";
	private String persistFilePath = "";
	private String persistInterval = "";
	private String intervalType = "";
	private String independentProcess = "";
	private String vmMemoryMin = "";
	private String vmMemoryMax = "";
	
	private String monitorMemory = "true";
	private String monitorMemorypool = "true";
	private String monitorThread = "true";
	private String monitorGc = "true";
	private String monitorClass = "true";
	private String monitorCpu = "true";
	private String monitorMBean = "true";
	
	public EnvironmentConfigObject() {}

	public EnvironmentConfigObject(String applicationId,
			String applicationName, String serverId, String port,
			String userName, String password, String enableSSL,
			String enableLogging, String enableMailing, String mailCause,
			String mailFrom, String mailTo, String mailCc,
			String enableConnect, String enablePersistence,
			String persistFilePath, String persistInterval, String intervalType,
			String independentProcess, String vmMemoryMin, String vmMemoryMax) {
		
		this.applicationId = applicationId;
		this.applicationName = applicationName;
		this.serverId = serverId;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.enableSSL = enableSSL;
		this.enableLogging = enableLogging;
		this.enableMailing = enableMailing;
		this.mailCause = mailCause;
		this.mailFrom = mailFrom;
		this.mailTo = mailTo;
		this.mailCc = mailCc;
		this.enableConnect = enableConnect;
		this.enablePersistence = enablePersistence;
		this.persistFilePath = persistFilePath;
		this.persistInterval = persistInterval;
		this.intervalType = intervalType;
		this.independentProcess = independentProcess;
		this.vmMemoryMin = vmMemoryMin;
		this.vmMemoryMax = vmMemoryMax;
	}

	public String getSmtp_host() {
		return smtp_host;
	}

	public void setSmtp_host(String smtp_host) {
		this.smtp_host = smtp_host;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the jndiRoot
     */
    public String getJndiRoot() {
        return jndiRoot;
    }

    /**
     * @param jndiRoot the jndiRoot to set
     */
    public void setJndiRoot(String jndiRoot) {
        this.jndiRoot = jndiRoot;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the rolePassword
     */
    public String getRolePassword() {
        return rolePassword;
    }

    /**
     * @param rolePassword the rolePassword to set
     */
    public void setRolePassword(String rolePassword) {
        this.rolePassword = rolePassword;
    }

    /**
     * @return the mserver
     */
    public String getMserver() {
        return mserver;
    }

    /**
     * @param mserver the mserver to set
     */
    public void setMserver(String mserver) {
        this.mserver = mserver;
    }

    /**
     * @return the providerPackage
     */
    public String getProviderPackage() {
        return providerPackage;
    }

    /**
     * @param providerPackage the providerPackage to set
     */
    public void setProviderPackage(String providerPackage) {
        this.providerPackage = providerPackage;
    }

    /**
     * @return the waitTime
     */
    public String getWaitTime() {
        return waitTime;
    }

    /**
     * @param waitTime the waitTime to set
     */
    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getEnableSSL() {
		return enableSSL;
	}

	public void setEnableSSL(String enableSSL) {
		this.enableSSL = enableSSL;
	}

	public String getEnableLogging() {
		return enableLogging;
	}

	public void setEnableLogging(String enableLogging) {
		this.enableLogging = enableLogging;
	}

	public String getEnableMailing() {
		return enableMailing;
	}

	public void setEnableMailing(String enableMailing) {
		this.enableMailing = enableMailing;
	}

	public String getMailCause() {
		return mailCause;
	}

	public void setMailCause(String mailCause) {
		this.mailCause = mailCause;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailCc() {
		return mailCc;
	}

	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}

	public String getEnableConnect() {
		return enableConnect;
	}

	public void setEnableConnect(String enableConnect) {
		this.enableConnect = enableConnect;
	}

	public String getEnablePersistence() {
		return enablePersistence;
	}

	public void setEnablePersistence(String enablePersistence) {
		this.enablePersistence = enablePersistence;
	}

	public String getPersistFilePath() {
		return persistFilePath;
	}

	public void setPersistFilePath(String persistFilePath) {
		this.persistFilePath = persistFilePath;
	}

	public String getPersistInterval() {
		return persistInterval;
	}

	public void setPersistInterval(String persistInterval) {
		this.persistInterval = persistInterval;
	}

	public String getIntervalType() {
		return intervalType;
	}

	public void setIntervalType(String intervalType) {
		this.intervalType = intervalType;
	}

	public String getIndependentProcess() {
		return independentProcess;
	}

	public void setIndependentProcess(String independentProcess) {
		this.independentProcess = independentProcess;
	}

	public String getVmMemoryMin() {
		return vmMemoryMin;
	}

	public void setVmMemoryMin(String vmMemoryMin) {
		this.vmMemoryMin = vmMemoryMin;
	}

	public String getVmMemoryMax() {
		return vmMemoryMax;
	}

	public void setVmMemoryMax(String vmMemoryMax) {
		this.vmMemoryMax = vmMemoryMax;
	}
	
	public String getMonitorMemory() {
		return monitorMemory;
	}

	public void setMonitorMemory(String monitorMemory) {
		this.monitorMemory = monitorMemory;
	}

	public String getMonitorMemorypool() {
		return monitorMemorypool;
	}

	public void setMonitorMemorypool(String monitorMemorypool) {
		this.monitorMemorypool = monitorMemorypool;
	}

	public String getMonitorThread() {
		return monitorThread;
	}

	public void setMonitorThread(String monitorThread) {
		this.monitorThread = monitorThread;
	}

	public String getMonitorGc() {
		return monitorGc;
	}

	public void setMonitorGc(String monitorGc) {
		this.monitorGc = monitorGc;
	}

	public String getMonitorClass() {
		return monitorClass;
	}

	public void setMonitorClass(String monitorClass) {
		this.monitorClass = monitorClass;
	}

	public String getMonitorCpu() {
		return monitorCpu;
	}

	public void setMonitorCpu(String monitorCpu) {
		this.monitorCpu = monitorCpu;
	}

	public String getMonitorMBean() {
		return monitorMBean;
	}

	public void setMonitorMBean(String monitorMBean) {
		this.monitorMBean = monitorMBean;
	}

	public String toXML() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("\n    ").append("<").append(VMConstants.XML_APP_CONFIG.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_APP_ID.toString()).append(">").append(applicationId).append("</").append(VMConstants.XML_APP_ID.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_APP_NAME.toString()).append(">").append(applicationName).append("</").append(VMConstants.XML_APP_NAME.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_VM_MONITOR_AREA.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_VM_MONITOR_MEMORY.toString()).append(">").append(monitorMemory).append("</").append(VMConstants.XML_VM_MONITOR_MEMORY.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_VM_MONITOR_MEMORYPOOL.toString()).append(">").append(monitorMemorypool).append("</").append(VMConstants.XML_VM_MONITOR_MEMORYPOOL.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_VM_MONITOR_THREAD.toString()).append(">").append(monitorThread).append("</").append(VMConstants.XML_VM_MONITOR_THREAD.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_VM_MONITOR_GC.toString()).append(">").append(monitorGc).append("</").append(VMConstants.XML_VM_MONITOR_GC.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_VM_MONITOR_CLASS.toString()).append(">").append(monitorClass).append("</").append(VMConstants.XML_VM_MONITOR_CLASS.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_VM_MONITOR_CPU.toString()).append(">").append(monitorCpu).append("</").append(VMConstants.XML_VM_MONITOR_CPU.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_VM_MONITOR_MBEAN.toString()).append(">").append(monitorMBean).append("</").append(VMConstants.XML_VM_MONITOR_MBEAN.toString()).append(">");
		sBuilder.append("\n        ").append("</").append(VMConstants.XML_VM_MONITOR_AREA.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_APP_CREDENTIALS.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_SERVER_NAME.toString()).append(">").append(serverId).append("</").append(VMConstants.XML_SERVER_NAME.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_SERVER_PORT.toString()).append(">").append(port).append("</").append(VMConstants.XML_SERVER_PORT.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_USER_CREDENTIALS.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_USER_NAME.toString()).append(">").append(userName).append("</").append(VMConstants.XML_USER_NAME.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_USER_PASSWORD.toString()).append(">").append(password).append("</").append(VMConstants.XML_USER_PASSWORD.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_MBEAN_SERVER.toString()).append(">").append(mserver).append("</").append(VMConstants.XML_MBEAN_SERVER.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_USER_ROLE.toString()).append(">").append(role).append("</").append(VMConstants.XML_USER_ROLE.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_ROLE_PASSWORD.toString()).append(">").append(rolePassword).append("</").append(VMConstants.XML_ROLE_PASSWORD.toString()).append(">");
        sBuilder.append("\n            ").append("</").append(VMConstants.XML_USER_CREDENTIALS.toString()).append(">");
		sBuilder.append("\n        ").append("</").append(VMConstants.XML_APP_CREDENTIALS.toString()).append(">");
		sBuilder.append("\n        ").append("<").append(VMConstants.XML_CONFIG_DETAILS.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_ENABLE_SSL.toString()).append(">").append(enableSSL).append("</").append(VMConstants.XML_ENABLE_SSL.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_ENABLE_LOGGING.toString()).append(">").append(enableLogging).append("</").append(VMConstants.XML_ENABLE_LOGGING.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_ENABLE_MAILING.toString()).append(">").append(enableMailing).append("</").append(VMConstants.XML_ENABLE_MAILING.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_MAILING_CONFIG.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_MAILING_CAUSE.toString()).append(">").append(mailCause).append("</").append(VMConstants.XML_MAILING_CAUSE.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_MAILING_FROM.toString()).append(">").append(mailFrom).append("</").append(VMConstants.XML_MAILING_FROM.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_MAILING_TO.toString()).append(">").append(mailTo).append("</").append(VMConstants.XML_MAILING_TO.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_MAILING_CC.toString()).append(">").append(mailCc).append("</").append(VMConstants.XML_MAILING_CC.toString()).append(">");
		sBuilder.append("\n            ").append("</").append(VMConstants.XML_MAILING_CONFIG.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_ENABLE_AUTO_P.toString()).append(">").append(enablePersistence).append("</").append(VMConstants.XML_ENABLE_AUTO_P.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_PERSISTENCE.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_PERSISTENCE_FILE.toString()).append(">").append(persistFilePath).append("</").append(VMConstants.XML_PERSISTENCE_FILE.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_PERSISTENCE_INTERVAL.toString()).append(">").append(persistInterval).append("</").append(VMConstants.XML_PERSISTENCE_INTERVAL.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_INTERVAL_TYPE.toString()).append(">").append(intervalType).append("</").append(VMConstants.XML_INTERVAL_TYPE.toString()).append(">");
		sBuilder.append("\n            ").append("</").append(VMConstants.XML_PERSISTENCE.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_ENABLE_AUTO_C.toString()).append(">").append(enableConnect).append("</").append(VMConstants.XML_ENABLE_AUTO_C.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_ENABLE_INDPNDNT_START.toString()).append(">").append(independentProcess).append("</").append(VMConstants.XML_ENABLE_INDPNDNT_START.toString()).append(">");
		sBuilder.append("\n            ").append("<").append(VMConstants.XML_VM_ARGS.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_VM_MIN_PARAM.toString()).append(">").append(vmMemoryMin).append("</").append(VMConstants.XML_VM_MIN_PARAM.toString()).append(">");
		sBuilder.append("\n                ").append("<").append(VMConstants.XML_VM_MAX_PARAM.toString()).append(">").append(vmMemoryMax).append("</").append(VMConstants.XML_VM_MAX_PARAM.toString()).append(">");
		sBuilder.append("\n            ").append("</").append(VMConstants.XML_VM_ARGS.toString()).append(">");
		sBuilder.append("\n        ").append("</").append(VMConstants.XML_CONFIG_DETAILS.toString()).append(">");
		sBuilder.append("\n    ").append("</").append(VMConstants.XML_APP_CONFIG.toString()).append(">");
		
		return sBuilder.toString();
	}
}
