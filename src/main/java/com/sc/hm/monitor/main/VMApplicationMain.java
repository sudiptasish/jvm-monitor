package com.sc.hm.monitor.main;

import com.sc.hm.monitor.config.manager.VMConfigurationUtil;
import com.sc.hm.monitor.main.frame.DashboardFrame;
import com.sc.hm.monitor.net.transport.TransportHandler;

public class VMApplicationMain {
	
	public static void main (String[] args) {
		VMConfigurationUtil.loadConfigurations();
		
		DashboardFrame dFrame = new DashboardFrame();
		initializeTransport();
		dFrame.setVisible(true);
	}
	
	private static void initializeTransport() {
		TransportHandler.createListenerSocket();
	}
}
