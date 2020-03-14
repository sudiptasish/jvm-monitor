package com.sc.hm.monitor.common;



public class VMConstants {
	
	private String value = "";

	public static final VMConstants PARSER_FACTORY_TYPE_DOM = new VMConstants("PARSER_FACTORY_TYPE_DOM");
	public static final VMConstants PARSER_FACTORY_TYPE_SAX = new VMConstants("PARSER_FACTORY_TYPE_SAX");
	
	public static final VMConstants CONFIG_TYPE_PROPERTIES = new VMConstants("properties");
	public static final VMConstants CONFIG_TYPE_XML = new VMConstants("xml");
	
	public static final VMConstants DEFAULT_CONFIG = new VMConstants("1001");
	
	public static final VMConstants XML_MONITOR_APP = new VMConstants("monitor-app");	
	
	public static final VMConstants XML_DEBUG_CONFIG = new VMConstants("debug-config");
	public static final VMConstants XML_ENABLE_PROCESS_DEBUG = new VMConstants("enable-process-debug");
	public static final VMConstants XML_DEBUG_PORT = new VMConstants("debug-port");
    public static final VMConstants XML_CMD_LAUNCHER = new VMConstants("launcher");
	public static final VMConstants XML_COMMAND_OPT = new VMConstants("cmd-opt");
	
	public static final VMConstants XML_TRANSPORT_CONFIG = new VMConstants("transport-config");
	public static final VMConstants XML_TRANSPORT_HOST = new VMConstants("transport-host");
	public static final VMConstants XML_TRANSPORT_PORT = new VMConstants("transport-port");
	public static final VMConstants XML_SMTP_HOST = new VMConstants("smtp-host");
	
	public static final VMConstants XML_APP_CONFIG = new VMConstants("app-config");
	public static final VMConstants XML_APP_ID = new VMConstants("app-id");
	public static final VMConstants XML_APP_NAME = new VMConstants("app-name");
	public static final VMConstants XML_APP_CREDENTIALS = new VMConstants("app-credentials");
	public static final VMConstants XML_SERVER_NAME = new VMConstants("server-name");
	public static final VMConstants XML_SERVER_PORT = new VMConstants("server-port");
	public static final VMConstants XML_USER_CREDENTIALS = new VMConstants("user-credentials");
	public static final VMConstants XML_USER_NAME = new VMConstants("user-name");
	public static final VMConstants XML_USER_PASSWORD = new VMConstants("user-password");
    public static final VMConstants XML_MBEAN_SERVER = new VMConstants("mbean-server");
    public static final VMConstants XML_USER_ROLE = new VMConstants("user-role");
    public static final VMConstants XML_ROLE_PASSWORD = new VMConstants("role-password");
	public static final VMConstants XML_CONFIG_DETAILS = new VMConstants("config-details");	
	public static final VMConstants XML_ENABLE_SSL = new VMConstants("enable-ssl");
	public static final VMConstants XML_ENABLE_LOGGING = new VMConstants("enable-logging");
	public static final VMConstants XML_ENABLE_MAILING = new VMConstants("enable-mailing");
	public static final VMConstants XML_MAILING_CONFIG = new VMConstants("mail-config");
	public static final VMConstants XML_MAILING_CAUSE = new VMConstants("mail-cause");
	public static final VMConstants XML_MAILING_FROM = new VMConstants("mail-from");
	public static final VMConstants XML_MAILING_TO = new VMConstants("mail-to");
	public static final VMConstants XML_MAILING_CC = new VMConstants("mail-cc");
	public static final VMConstants XML_ENABLE_AUTO_P = new VMConstants("enable-auto-persistence");
	public static final VMConstants XML_ENABLE_AUTO_C = new VMConstants("enable-auto-connect");
	public static final VMConstants XML_PERSISTENCE = new VMConstants("persistence");
	public static final VMConstants XML_PERSISTENCE_FILE = new VMConstants("persistence-file-path");
	public static final VMConstants XML_PERSISTENCE_INTERVAL = new VMConstants("persistence-interval");
	public static final VMConstants XML_INTERVAL_TYPE = new VMConstants("interval-type");
	public static final VMConstants XML_ENABLE_INDPNDNT_START = new VMConstants("enable-independent-startup");
	public static final VMConstants XML_VM_ARGS = new VMConstants("vm-args");
	public static final VMConstants XML_VM_MIN_PARAM = new VMConstants("min-param");
	public static final VMConstants XML_VM_MAX_PARAM = new VMConstants("max-param");
	
	public static final VMConstants XML_VM_MONITOR_AREA = new VMConstants("monitor-area");
	public static final VMConstants XML_VM_MONITOR_MEMORY = new VMConstants("monitor-memory");
	public static final VMConstants XML_VM_MONITOR_MEMORYPOOL = new VMConstants("monitor-memorypool");
	public static final VMConstants XML_VM_MONITOR_THREAD = new VMConstants("monitor-thread");
	public static final VMConstants XML_VM_MONITOR_GC = new VMConstants("monitor-gc");
	public static final VMConstants XML_VM_MONITOR_CLASS = new VMConstants("monitor-class");
	public static final VMConstants XML_VM_MONITOR_CPU = new VMConstants("monitor-cpu");
	public static final VMConstants XML_VM_MONITOR_MBEAN = new VMConstants("monitor-mbean");
	
	public static final VMConstants MONITOR_PROCESS_MAIN = new VMConstants("com.sc.hm.monitor.main.JVMMonitorMain");
	//public static final VMConstants MONITOR_PROCESS = new VMConstants("com.sc.hm.monitor.main.VMMonitorMain");
	public static final VMConstants MONITOR_PROCESS = new VMConstants("com.sc.hm.vmxd.main.VMXDMain");
	
	public static final VMConstants MAIL_CAUSE_MEM_THRESHOLD_EXCEED = new VMConstants("MEMORY_THRESHOLD_EXCEEDED");
	public static final VMConstants MAIL_CAUSE_THREAD_CPU_EXCEED = new VMConstants("THREAD_CPU_EXCEEDED");
	
	public static final VMConstants VM_TRANSPORT = new VMConstants("VM_TRANSPORT");
	public static final VMConstants VM_TRANSPORT_SERVER_SOCKET = new VMConstants("VM_TRANSPORT_SERVER_SOCKET");
	public static final VMConstants VM_TRANSPORT_CLIENT_SOCKET = new VMConstants("VM_TRANSPORT_CLIENT_SOCKET");
	public static final VMConstants NOTIFICATION_SERVER_SHUTDOWN = new VMConstants("NOTIFICATION_SHUTDOWN");
	
	public static final VMConstants NOTIFIER_THREAD_NAME = new VMConstants("NTF-Thread");
	
	
	public VMConstants(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String toString() {
		return this.value;
	}
}
