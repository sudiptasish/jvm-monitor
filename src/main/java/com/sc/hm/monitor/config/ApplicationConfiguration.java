package com.sc.hm.monitor.config;

import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.net.VMThreadGroup;
import com.sc.hm.monitor.util.GraphImageProperties;
import com.sc.hm.monitor.util.Logger;

public class ApplicationConfiguration {

	private static ApplicationConfiguration appConfig = null;
	
	private final Lock notificationLock = new ReentrantLock();
	private final Condition notificationCondition = notificationLock.newCondition();
	
	private final Lock mainLock = new ReentrantLock();
	private final Condition mainCondition = mainLock.newCondition();
	
	private Properties props = null;
	
	private String LOOK_AND_FEEL = "";
	
	private String GRAPH_TYPE = GraphImageProperties.GRAPH_TYPE_ASYNCH;
	
	private EnvironmentConfigObject envConfigObject = null;
	
	private VMThreadGroup vmThreadGroup = null;
	
	private ApplicationConfiguration() {
		props = new Properties();
		vmThreadGroup = VMThreadGroup.getThreadGroup();
	}
	
	public static ApplicationConfiguration getInstance() {
		if (appConfig == null) {
			synchronized (ApplicationConfiguration.class) {
				if (appConfig == null) {
					synchronized (ApplicationConfiguration.class) {
						appConfig = new ApplicationConfiguration();
					}
				}
			}
		}
		return appConfig;
	}
	
	public void putProperty(String key, String value) {
		props.setProperty(key, value);
	}
	
	public String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}
	
	public String getProperty(String key) {
		return props.getProperty(key);
	}
	
	public void setEnvironmentConfig(EnvironmentConfigObject object, Object caller) throws Exception {
		envConfigObject = object;
		notifyEnvNotificationLockOwner(caller);
	}
	
	public EnvironmentConfigObject getEnvironmentConfig() {
		return envConfigObject;
	}
	
	public void putThreadReference(Thread t) {
		vmThreadGroup.putThreadReference(t.getName(), t);
	}
	
	public void removeThreadReference(Thread t) {
		vmThreadGroup.removeThreadReference(t.getName());
	}
	
	public void removeThreadReference(String name) {
		vmThreadGroup.removeThreadReference(name);
	}
	
	public Thread getThreadReference(String name) {
		Thread t = vmThreadGroup.getThread(name);
		return t;
	}
	
	public Iterator<String> getActiveThreadProcess() {
		return vmThreadGroup.getAllThreads();
	}
	
	public void acquiredEnvNotificationLock(Object obj) throws Exception {
		try {
			notificationLock.lock();
			Logger.log("Object " + obj.getClass() + " Has Acquired the Env Notification Lock... Going to Sleep Mode");
			try {
				notificationCondition.await();
			}
			catch (InterruptedException inte) {}
			Logger.log("Object " + obj.getClass() + " Has Released the Env Notification Lock... Awaking....");
		}
		finally {
			notificationLock.unlock();
		}
	}
	
	public void notifyEnvNotificationLockOwner(Object obj) throws Exception {
		try {
			notificationLock.lock();
			Logger.log("Object " + obj.getClass() + " Has Acquired the Env Notification Lock... Notifying Blocked Thread");
			notificationCondition.signal();
		}
		finally {
			notificationLock.unlock();
		}
	}
	
	public void acquiredMainLock(Object obj) throws Exception {
		try {
			mainLock.lock();
			Logger.log("Object " + obj.getClass() + " Has Acquired the Main Lock... Going to Sleep Mode");
			try {
				mainCondition.await();
			}
			catch (InterruptedException inte) {}
			Logger.log("Object " + obj.getClass() + " Has Released the Main Lock... Awaking....");
		}
		finally {
			mainLock.unlock();
		}
	}
	
	public void notifyMainLockOwner(Object obj) throws Exception {
		try {
			mainLock.lock();
			Logger.log("Object " + obj.getClass() + " Has Acquired the Main Lock... Notifying Blocked Thread");
			mainCondition.signal();
		}
		finally {
			mainLock.unlock();
		}
	}
	
	public void setLookAndFeel(String class_name) {
		LOOK_AND_FEEL = class_name;
	}
	
	public String getLookAndFeel() {
		return LOOK_AND_FEEL;
	}
	
	public void setGraphType(String graph_type) {
		GRAPH_TYPE = graph_type;
	}
	
	public String getGraphType() {
		return GRAPH_TYPE;
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
