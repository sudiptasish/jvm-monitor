package com.sc.hm.monitor.process.runtime;

import com.sc.hm.monitor.process.MBeanProcessNotifier;

public class RuntimeMBeanNotifier extends MBeanProcessNotifier {

	private String status = "";

	public RuntimeMBeanNotifier() {
		super();
	}
	
	public RuntimeMBeanNotifier(boolean flag) {
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
