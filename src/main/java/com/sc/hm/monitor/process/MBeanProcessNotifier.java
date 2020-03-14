package com.sc.hm.monitor.process;

public abstract class MBeanProcessNotifier {
	
	public static final String MBEAN_PROCESS_STATUS_NEW = "N";
	public static final String MBEAN_PROCESS_STATUS_STARTED = "S";
	public static final String MBEAN_PROCESS_STATUS_RUNNING = "R";
	public static final String MBEAN_PROCESS_STATUS_STOPPED = "T";
	public static final String MBEAN_PROCESS_STATUS_ERROR = "E";

	private boolean isRunMBeanProcess = true;
	
	public MBeanProcessNotifier() {}
	
	public MBeanProcessNotifier(boolean flag) {
		isRunMBeanProcess = flag;
	}

	public synchronized boolean isRunMBeanProcess() {
		return isRunMBeanProcess;
	}

	public synchronized void setRunMBeanProcess(boolean isRunMBeanProcess) {
		this.isRunMBeanProcess = isRunMBeanProcess;
	}
	
	public abstract String getStatus();
	
	public abstract void setStatus(String status);
}
