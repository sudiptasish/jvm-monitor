package com.sc.hm.monitor.util;

public final class ManagementUtil {

	public static final int DEFAULT_SUMMARY_REFRESH_TIME = 4;
	public static final int DEFAULT_MEMORY_REFRESH_TIME = 4;
	public static final int DEFAULT_MEMORY_POOL_REFRESH_TIME = 4;
	public static final int DEFAULT_THREAD_REFRESH_TIME = 4;
	public static final int DEFAULT_CLASSES_REFRESH_TIME = 4;
	
	public static final int DEFAULT_PROCESS_PAUSE_TIME = 4;
        
    public static final String CLASS_MBEAN_PROCESS = "CLASS_MBEAN_PROCESS";
    public static final String COMPILE_MBEAN_PROCESS = "COMPILE_MBEAN_PROCESS";
    public static final String GBCOLLECTOR_MBEAN_PROCESS = "GBCOLLECTOR_MBEAN_PROCESS";
    public static final String MEMORY_MBEAN_PROCESS = "MEMORY_MBEAN_PROCESS";
    public static final String MEMORYPOOL_MBEAN_PROCESS = "MEMORYPOOL_MBEAN_PROCESS";
    public static final String OS_MBEAN_PROCESS = "OS_MBEAN_PROCESS";
    public static final String RUNTIME_MBEAN_PROCESS = "RUNTIME_MBEAN_PROCESS";
	public static final String THREAD_MBEAN_PROCESS = "THREAD_MBEAN_PROCESS";
	
	public static final String DEFAULT_WORKER_THREAD_POOL_NAME = "JVM_WORKER_THREAD_POOL";
	
	public static final String JVM_TYPE_LOCAL = "JVM_TYPE_LOCAL";
	public static final String JVM_TYPE_REMOTE = "JVM_TYPE_REMOTE";
	
	public static final String HOST = "HOST";
	public static final String PORT = "PORT";
	public static final String URL = "URL";
	
	public static final String CURRENT_DIR = System.getProperty("user.dir");
	
	public static final String MGMT_WORKER_THREAD = "MGMT-Worker";
}
