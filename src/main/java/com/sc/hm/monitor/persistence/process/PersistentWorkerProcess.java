package com.sc.hm.monitor.persistence.process;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Date;

import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.monitor.persistence.task.PersistentTask;
import com.sc.hm.monitor.persistence.worker.MonitorTaskWorker;
import com.sc.hm.monitor.persistence.worker.PersistentTaskWorker;
import com.sc.hm.monitor.util.Logger;

public class PersistentWorkerProcess implements Runnable {
	
	private MonitorTaskWorker monitorTaskWorker = new PersistentTaskWorker();
	private File directory = null;
	private String interval = "";
	private String timeUnit = "";
	private String task_class = "";
	
	private MonitorTask mTask = null;
	
	public PersistentWorkerProcess(File file, String timegap, String timeUnit) {
		this (null, file, timegap, timeUnit);
	}
	
	public PersistentWorkerProcess(String tc, File file, String timegap, String timeUnit) {
		directory = file;
		interval = timegap;
		task_class = tc;
		this.timeUnit = timeUnit;
		try {
			if (task_class != null && !"".equals(task_class)) {
				Constructor<?> constructor = Class.forName(task_class).getDeclaredConstructor(new Class[] {String.class, File.class, boolean.class});
				mTask = (MonitorTask)constructor.newInstance(new Object[] {MonitorTask.TASK_ALL_PERSIST, directory, true});
			}
			else {
				mTask = new PersistentTask(MonitorTask.TASK_ALL_PERSIST, directory, true);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long waitTime = 0L;
		if (timeUnit.equals("Seconds")) {
			waitTime = Long.parseLong(interval) * 1000;
		}
		else if (timeUnit.equals("Minutes")) {
			waitTime = Long.parseLong(interval) * 60 * 1000;
		}
		else if (timeUnit.equals("Hours")) {
			waitTime = Long.parseLong(interval) * 60 * 60 * 1000;
		}
		else if (timeUnit.equals("Days")) {
			waitTime = Long.parseLong(interval) * 24 * 60 * 60 * 1000;
		}
		while (true) {
			persistData(null, directory);
			try {
				Thread.sleep(waitTime);
			}
			catch (InterruptedException inte) {
				break;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}			
	}

	public void persistData (String dataType, File directory) {
		try {
			Logger.log("Started Persisting Data.... [Time: " + new Date() + "]");
			monitorTaskWorker.scheduleTask(mTask, 0);
			Logger.log("Data Persistence Completed.... [Time: " + new Date() + "]");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
