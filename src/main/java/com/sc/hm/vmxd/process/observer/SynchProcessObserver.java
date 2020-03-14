package com.sc.hm.vmxd.process.observer;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.net.data.NotificationObject;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.process.AbstractMBeanSynchProcess;

public class SynchProcessObserver implements Observer {
	
	private String applicationId = "";
	private Map<String, AbstractMBeanSynchProcess> monitorProcessMap = new HashMap<>();

	public SynchProcessObserver(String applicationId) {
		this.applicationId = applicationId;
	}
	
	/**
	 * Add the current active background process to this observer.
	 * @param process
	 */
	public void addActiveProcess(AbstractMBeanSynchProcess process) {
		monitorProcessMap.put(process.getClass().getName(), process);
	}
	
	/**
	 * Return the background process as identified by this process name.
	 * @param name
	 * @return AbstractMBeanSynchProcess
	 */
	public AbstractMBeanSynchProcess getActiveProcess(String name) {
		return monitorProcessMap.get(name);
	}
	
	/**
	 * Set the run flag to false for all the currently running background processes.
	 */
	public void markAllForStop() {
	    for (Map.Entry<String, AbstractMBeanSynchProcess> me : monitorProcessMap.entrySet()) {
	        me.getValue().setRunProcess(false);
	    }
	}

	public synchronized void update(Observable o, Object arg) {
	    AbstractMBeanSynchProcess mbeanProcess = monitorProcessMap.remove(o.getClass().getName());
	    mbeanProcess.setRunProcess(false);
	    
		if (monitorProcessMap.size() == 0) {
			try {
				NotificationObject notifObject = ProcessInfo.getProcessNotification(applicationId);
				notifObject.setException((Exception)arg);
				notifObject.setNotification(ProcessInfo.PROCESS_STATUS_ERROR);
				notifObject.setStatus(ProcessInfo.PROCESS_STATUS_ERROR);
				SynchApplicationConfiguration.getSynchInstance().notifyMainThreadForException();
				Logger.log("Notified Main Thread....");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
