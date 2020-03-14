package com.sc.hm.monitor.process.threads;

import com.sc.hm.monitor.process.MBeanProcessNotifier;

public class ThreadMBeanNotifier extends MBeanProcessNotifier {
	
	private String status = "";

	public ThreadMBeanNotifier() {
		super();
	}
	
	public ThreadMBeanNotifier(boolean flag) {
		super(flag);
		status = MBEAN_PROCESS_STATUS_NEW;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
