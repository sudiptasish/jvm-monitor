package com.sc.hm.monitor.mbean.jmx.local;


public class LocalMBeanHandler {
	
	private LocalMBeanConnector mBeanConnector = null;

	public LocalMBeanHandler() {
		initializeLocalConnector();
	}
	
	private void initializeLocalConnector() {
		mBeanConnector = new LocalMBeanConnector();
	}
}
