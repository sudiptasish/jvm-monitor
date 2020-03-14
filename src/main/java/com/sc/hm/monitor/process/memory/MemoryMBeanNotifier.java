package com.sc.hm.monitor.process.memory;

import com.sc.hm.monitor.process.MBeanProcessNotifier;

public class MemoryMBeanNotifier extends MBeanProcessNotifier {

	private String status = "";

	public MemoryMBeanNotifier() {
		super();
	}
	
	public MemoryMBeanNotifier(boolean flag) {
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
