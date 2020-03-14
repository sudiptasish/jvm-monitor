package com.sc.hm.monitor.process.mpool;

import com.sc.hm.monitor.process.MBeanProcessNotifier;

public class MemoryPoolMBeanNotifier extends MBeanProcessNotifier {
	
	private String status = "";

	public MemoryPoolMBeanNotifier() {
		super();
		status = MBEAN_PROCESS_STATUS_NEW;
	}
	
	public MemoryPoolMBeanNotifier(boolean flag) {
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
