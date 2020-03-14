package com.sc.hm.vmxd.desktop.panel;

public interface MonitoringPanelProps {

	public MonitoringProcess getMonitoringProcess() throws Exception;
	
	static interface MonitoringProcess extends Runnable {
		public void setInitParam(Object obj);
		public void start() throws Exception;
	}
}
