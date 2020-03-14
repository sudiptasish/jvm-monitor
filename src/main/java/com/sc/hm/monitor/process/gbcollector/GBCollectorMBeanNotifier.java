package com.sc.hm.monitor.process.gbcollector;

import com.sc.hm.monitor.process.MBeanProcessNotifier;

public class GBCollectorMBeanNotifier extends MBeanProcessNotifier {

	private String status = "";

	public GBCollectorMBeanNotifier() {
		super();
	}
	
	public GBCollectorMBeanNotifier(boolean flag) {
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