package com.sc.hm.monitor.process.compile;

import com.sc.hm.monitor.process.MBeanProcessNotifier;

public class CompilationMBeanNotifier extends MBeanProcessNotifier {

	private String status = "";

	public CompilationMBeanNotifier() {
		super();
	}
	
	public CompilationMBeanNotifier(boolean flag) {
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
