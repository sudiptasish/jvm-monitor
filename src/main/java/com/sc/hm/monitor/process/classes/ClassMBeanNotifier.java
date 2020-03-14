package com.sc.hm.monitor.process.classes;

import com.sc.hm.monitor.process.MBeanProcessNotifier;

public class ClassMBeanNotifier extends MBeanProcessNotifier {

	private String status = "";

	public ClassMBeanNotifier() {
		super();
	}
	
	public ClassMBeanNotifier(boolean flag) {
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
