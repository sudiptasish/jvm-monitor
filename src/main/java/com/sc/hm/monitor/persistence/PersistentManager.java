package com.sc.hm.monitor.persistence;

import java.io.File;
import java.lang.reflect.Constructor;

import com.sc.hm.monitor.persistence.process.PersistentWorkerProcess;
import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.monitor.persistence.task.PersistentTask;
import com.sc.hm.monitor.persistence.worker.MonitorTaskWorker;
import com.sc.hm.monitor.persistence.worker.PersistentTaskWorker;
import com.sc.hm.monitor.util.Logger;

public class PersistentManager {

	private static MonitorTaskWorker monitorTaskWorker = new PersistentTaskWorker();
	
	public static void persistData (String dataType, String fileLocation) {
		try {
			if (dataType == null || "".equals(dataType)) {
				dataType = MonitorTask.TASK_ALL_PERSIST;
			}
			Logger.log("Started Persisting Data....");
			monitorTaskWorker.scheduleTask(new PersistentTask(dataType, fileLocation), 10);
			Logger.log("Data Persistence Completed....");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void persistData(File directory) {
		persistData(null, directory);
	}
	
	public static void startDataPersistenceProcess(File directory, String interval, String timeUnit) {
		new Thread(new PersistentWorkerProcess(directory, interval, timeUnit)).start();
	}
	
	public static void startDataPersistenceProcess(String task_class, File directory, String interval, String timeUnit) {
		new Thread(new PersistentWorkerProcess(task_class, directory, interval, timeUnit)).start();
	}
	
	public static void persistData (String dataType, File directory) {
		try {
			if (dataType == null || "".equals(dataType)) {
				dataType = MonitorTask.TASK_ALL_PERSIST;
			}
			Logger.log("Started Persisting Data....");
			monitorTaskWorker.scheduleTask(new PersistentTask(dataType, directory), 10);
			Logger.log("Data Persistence Completed....");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void persistData (String dataType, File directory, String task_class) {
		try {
			if (dataType == null || "".equals(dataType)) {
				dataType = MonitorTask.TASK_ALL_PERSIST;
			}
			Class<?> clazz = Class.forName(task_class);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class, File.class});
			MonitorTask taskObj = (MonitorTask)constructor.newInstance(new Object[] {dataType, directory});
			
			Logger.log("Started Persisting Data....");
			monitorTaskWorker.scheduleTask(taskObj, 10);
			Logger.log("Data Persistence Completed....");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void persistDataImmediate (String dataType, File directory, String task_class) {
		try {
			if (dataType == null || "".equals(dataType)) {
				dataType = MonitorTask.TASK_ALL_PERSIST;
			}
			Class<?> clazz = Class.forName(task_class);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class, File.class});
			MonitorTask taskObj = (MonitorTask)constructor.newInstance(new Object[] {dataType, directory});
			
			Logger.log("Started Persisting Data....");
			monitorTaskWorker.scheduleTaskImmediate(taskObj);
			Logger.log("Data Persistence Completed....");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
