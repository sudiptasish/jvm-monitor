package com.sc.hm.monitor.event.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import com.sc.hm.monitor.event.ConfigUpdater;
import com.sc.hm.monitor.util.Logger;

public class DashboardWindowEventListener implements WindowListener {

	public DashboardWindowEventListener() {}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	    Logger.log("Window Closed. Event: " + e);
	}

	public void windowClosing(WindowEvent we) {
		try {
			ConfigUpdater.updateConfig();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			System.exit(0);
		}
	}

	public void windowDeactivated(WindowEvent e) {
        Logger.log("Window Deactivated. Event: " + e);
	}

	public void windowDeiconified(WindowEvent e) {
        Logger.log("Window Deiconified. Event: " + e);
	}

	public void windowIconified(WindowEvent e) {
        Logger.log("Window Iconified. Event: " + e);
	}

	public void windowOpened(WindowEvent e) {
        Logger.log("Window Opened. Event: " + e);
	}
}
