package com.sc.hm.vmxd.main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.net.data.NotificationObject;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.dialog.ShutdownFrame;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;
import com.sc.hm.vmxd.service.ExecutorServiceHelper;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class SynchMonitorWindowEventListener implements WindowListener {
	
	private static SynchApplicationConfiguration sync_appConfig = null;
	
	private JFrame frame = null;
	private ShutdownFrame shutdownFrame = new ShutdownFrame();
	
	static {
		sync_appConfig = SynchApplicationConfiguration.getSynchInstance();
	}
	
	public SynchMonitorWindowEventListener(JFrame f) {
		frame = f;
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent we) {
		
	}

	public void windowClosing(WindowEvent we) {
		clearFrame(frame);
		shutdownFrame.setVisible(true);
		
		new Thread(new Runnable() {
			public void run() {
				performShutdown();
			}
		}).start();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	private void performShutdown() {
		try {
			NotificationObject notifObject = ProcessInfo.getProcessNotification(
			        sync_appConfig.getEnvironmentConfig().getApplicationId());
			notifObject.setStatus(ProcessInfo.PROCESS_STATUS_STOPPED);
			
			SynchProcessObserver observer = SynchApplicationConfiguration.getSynchInstance().getCurrentObserver();
			if (observer != null) {
			    observer.markAllForStop();
			}
			ExecutorServiceHelper.getExecutorServiceHelper().shutdownAllExecutorPools();
			Logger.log("Executor Service Shutdown...");
			SynchApplicationConfiguration.getSynchInstance().notifyMainThreadForException();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void clearFrame(JFrame f) {
		f.getContentPane().removeAll();
		f.setVisible(false);
		f.dispose();
		f = null;
	}
}
