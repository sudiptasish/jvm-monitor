package com.sc.hm.monitor.event.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.net.data.NotificationObject;

public class MonitorWindowEventListener implements WindowListener {
	
	private static ApplicationConfiguration appConfig = null;
	
	static {
		appConfig = ApplicationConfiguration.getInstance();
	}
	
	public MonitorWindowEventListener() {}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
		
	}

	public void windowClosing(WindowEvent we) {
		try {
			NotificationObject notifObject = ProcessInfo.getProcessNotification(appConfig.getEnvironmentConfig().getApplicationId());
			notifObject.setStatus(ProcessInfo.PROCESS_STATUS_STOPPED);
			Thread notifThread = appConfig.getThreadReference(VMConstants.NOTIFIER_THREAD_NAME.getValue() + "-" + notifObject.getJobId());
			if (notifThread != null) {
				notifThread.interrupt();
			}
			for (Iterator<String> itr = appConfig.getActiveThreadProcess(); itr.hasNext();) {
				Thread t = appConfig.getThreadReference(itr.next());
				t.interrupt();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

}
