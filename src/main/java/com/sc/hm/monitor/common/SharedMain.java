package com.sc.hm.monitor.common;

import com.sc.hm.monitor.config.ServerConfig;

public class SharedMain {

	private boolean proceed = false;
	public Object lock = new Object();
	
	private boolean thisVM = true;
	private ServerConfig serverConfig = null;

	public boolean isProceed() {
		return proceed;
	}

	public void setProceed(boolean proceed) {
		this.proceed = proceed;
	}

	public void acquireLockAndWaitForNotification() {
		synchronized (lock) {
			try {
				lock.wait();
			}
			catch (InterruptedException inte) {}
		}
	}
	
	public void notifyLockOwner() {
		synchronized (lock) {
			lock.notify();
		}
	}

	public boolean isThisVM() {
		return thisVM;
	}

	public void setThisVM(boolean thisVM) {
		this.thisVM = thisVM;
	}

	public ServerConfig getServerConfig() {
		return serverConfig;
	}

	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}
}
