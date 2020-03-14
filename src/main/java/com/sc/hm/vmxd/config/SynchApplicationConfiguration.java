package com.sc.hm.vmxd.config;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;


public class SynchApplicationConfiguration {

	private static SynchApplicationConfiguration synch_appConfig = null;
	
	private EnvironmentConfigObject envConfigObject = null;
	
	private SynchProcessObserver currentObserver = null;
	
	private final Lock mainLock = new ReentrantLock();
	private final Condition exitCondition = mainLock.newCondition();
	
	private SynchApplicationConfiguration() {
	}
	
	/**
	 * Static accessor method to return the singleton app config instance.
	 * @return SynchApplicationConfiguration
	 */
	public static SynchApplicationConfiguration getSynchInstance() {
		if (synch_appConfig == null) {
			synchronized (SynchApplicationConfiguration.class) {
				if (synch_appConfig == null) {
					synchronized (SynchApplicationConfiguration.class) {
						synch_appConfig = new SynchApplicationConfiguration();
					}
				}
			}
		}
		return synch_appConfig;
	}
	
	/**
	 * Set the environment configuration object for this current monitoring job.
	 * This object will have the essential information, such as connection
	 * credentials, remote host, port and other information.
	 * 
	 * @param object
	 * @throws Exception
	 */
	public void setEnvironmentConfig(EnvironmentConfigObject object) throws Exception {
		envConfigObject = object;
	}
	
	/**
	 * Return the current environment configuration object.
	 * @return EnvironmentConfigObject
	 */
	public EnvironmentConfigObject getEnvironmentConfig() {
		return envConfigObject;
	}
	
	/**
	 * Get the current observer instance.
	 * @return SynchProcessObserver
	 */
	public SynchProcessObserver getCurrentObserver() {
		return currentObserver;
	}

	/**
	 * Set the current observer instance.
	 * @param currentObserver
	 */
	public void setCurrentObserver(SynchProcessObserver currentObserver) {
		this.currentObserver = currentObserver;
	}

	/**
	 * Main thread will call this API and will be blocked forever.
	 * Unless and until the notifyMainThreadForException is invoked during
	 * shutdown, at which time the main thread will be unblocked and the
	 * program will exit.
	 * 
	 * @throws Exception
	 */
	public void waitUntilExceptionOrEnd() throws Exception {
		try {
			mainLock.lock();
			exitCondition.await();
		}
		finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * API to bring back the main thread in action.
	 * This will be called whenever a shutdown (of monitoring) has been initiated
	 * by user.
	 * 
	 * @throws Exception
	 */
	public void notifyMainThreadForException() throws Exception {
		try {
			mainLock.lock();
			exitCondition.signal();
		}
		finally {
			mainLock.unlock();
		}
	}
}
