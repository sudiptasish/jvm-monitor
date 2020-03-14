package com.sc.hm.monitor.process.os;

import com.sc.hm.monitor.process.MBeanProcessNotifier;

public class OperatingSystemMBeanNotifier extends MBeanProcessNotifier {

	private String status = "";

	public OperatingSystemMBeanNotifier() {
		super();
	}
	
	public OperatingSystemMBeanNotifier(boolean flag) {
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
