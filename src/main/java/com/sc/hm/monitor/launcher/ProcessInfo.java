package com.sc.hm.monitor.launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sc.hm.monitor.net.data.NotificationObject;

public class ProcessInfo {
	
	public static final String PROCESS_STATUS_NEW = "NEW";
	public static final String PROCESS_STATUS_RUNNING = "RUNNING";
	public static final String PROCESS_STATUS_STOPPED = "STOPPED";
	public static final String PROCESS_STATUS_ERROR = "ERROR";
	public static final String PROCESS_STATUS_TERMINATED = "TERMINATED";

	private static Map<String, NotificationObject> processMap = new HashMap<String, NotificationObject>();
	
	private static Map<String, List<ProcessStatusListener>> listenerMap = new HashMap<String, List<ProcessStatusListener>>();
	
	public static synchronized void setProcessInfo(NotificationObject notifObject) {
		processMap.put(notifObject.getJobId(), notifObject);
	}
	
	public static synchronized NotificationObject getProcessNotification(String processId) {
		if (processMap.containsKey(processId)) {
			return processMap.get(processId);
		}
		return null;
	}
	
	public static synchronized void setProcessStatus(String processId, String status) {
		if (processMap.containsKey(processId)) {
			NotificationObject obj = processMap.get(processId);
			obj.setStatus(status);
			if (listenerMap.containsKey(processId)) {
				List<ProcessStatusListener> listenerList = listenerMap.get(processId);
				for (Iterator<ProcessStatusListener> itr = listenerList.iterator(); itr.hasNext();) {
					itr.next().onStatus(processId, status, obj.getException());
				}
			}
		}
	}
	
	public static synchronized void setProcessInfo(String processId, NotificationObject obj) {
		if (processMap.containsKey(processId)) {
			processMap.remove(processId);
			processMap.put(processId, obj);
			if (listenerMap.containsKey(processId)) {
				List<ProcessStatusListener> listenerList = listenerMap.get(processId);
				for (Iterator<ProcessStatusListener> itr = listenerList.iterator(); itr.hasNext();) {
					itr.next().onStatus(processId, obj.getStatus(), obj.getException());
				}
			}
		}
	}
	
	public static void registerListener(String appId, ProcessStatusListener listener) {
		if (listenerMap.containsKey(appId)) {
			List<ProcessStatusListener> listenerList = listenerMap.get(appId);
			listenerList.add(listener);
		}
		else {
			List<ProcessStatusListener> listenerList = new ArrayList<ProcessStatusListener>();
			listenerList.add(listener);
			listenerMap.put(appId, listenerList);
		}
	}
	
	public static interface ProcessStatusListener {
		public void onStatus(String processId, String status, String errorString);
	}
}
