package com.sc.hm.monitor.launcher.observer;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.process.AbstractMBeanProcess;
import com.sc.hm.monitor.process.MBeanProcessNotifier;

public class MBeanProcessObserver implements Observer {
	
	private Object lock = null;
	private final Map<AbstractMBeanProcess, String> statusMap = new HashMap<AbstractMBeanProcess, String>(8);
	private boolean complete = false;
	
	private transient JProgressBar progressBar = null;
	private transient JLabel progressLabel = null;
	
	public MBeanProcessObserver(Object aLock) {
		lock = aLock;
	}
	
	public synchronized void setStatusForProcess(Object obj) {
		AbstractMBeanProcess process = (AbstractMBeanProcess)obj;
		statusMap.put(process, process.getProcessStatus());
	}

	public synchronized boolean isComplete() {
		return complete;		
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	public void setProgressLevelParameter(JProgressBar bar, JLabel label) {
		progressBar = bar;
		progressLabel = label;
	}

	public synchronized void update(Observable observable, Object obj) {
		if (MBeanProcessNotifier.MBEAN_PROCESS_STATUS_RUNNING.equals((String)obj)) {
			if (statusMap.containsKey(observable)) {
				progressBar.setValue(progressBar.getValue() + 1);
				progressLabel.setText("Loading Process " + observable.getClass() + "....");
				statusMap.put((AbstractMBeanProcess)observable, (String)obj);
			}
			if (!statusMap.containsValue(MBeanProcessNotifier.MBEAN_PROCESS_STATUS_NEW)) {
				synchronized (lock) {
					complete = true;
					progressLabel.setText("Completed Loading....");
					progressLabel = null;
					progressBar = null;
					lock.notify();
				}
			}
		}
		else if (MBeanProcessNotifier.MBEAN_PROCESS_STATUS_ERROR.equals((String)obj) || MBeanProcessNotifier.MBEAN_PROCESS_STATUS_STOPPED.equals((String)obj)) {
			statusMap.put((AbstractMBeanProcess)observable, (String)obj);
			if (!statusMap.containsValue(MBeanProcessNotifier.MBEAN_PROCESS_STATUS_RUNNING)) {
				try {
					ApplicationConfiguration.getInstance().notifyMainLockOwner(this);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
