package com.sc.hm.monitor.process;

import java.util.Observable;

import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.mbean.factory.ManagementBeanFactory;
import com.sc.hm.monitor.shared.IMBeanObject;
import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.shared.factory.MBeanSharedObjectFactory;
import com.sc.hm.monitor.util.Logger;

public abstract class AbstractMBeanProcess extends Observable {
	
	protected String mbeanType = "";
	
	protected MBeanProcessNotifier notifier = null;
	
	private ManagementBeanFactory mbeanFactory = null;
	
	protected static MBeanSharedObjectRepository repos = null;
	protected static MBeanSharedObjectFactory mbeanSharedObject = null;
	
	protected static ApplicationConfiguration appConfig = ApplicationConfiguration.getInstance();
	
	static {
		initOther();		
	}
	
	public AbstractMBeanProcess(String mbeanType, ManagementBeanFactory mbeanFactory) {
		this.mbeanType = mbeanType;
		this.mbeanFactory = mbeanFactory;
		initMBeanObject();
	}
	
	private static void initOther() {
		repos = MBeanSharedObjectRepository.getInstance();
		mbeanSharedObject = MBeanSharedObjectFactory.getMBeanFactory();
	}
	
	protected void initMBeanObject() {
		initMBeanInternal(this.mbeanType);
	}
	
	protected synchronized void storeMBean(IMBeanObject mbean) {
		repos.storeMBean(mbean);
	}
	
	protected ManagementBeanFactory getMbeanFactory() {
		return mbeanFactory;
	}

	protected void setMbeanFactory(ManagementBeanFactory mbeanFactory) {
		this.mbeanFactory = mbeanFactory;
	}
	
	protected void performStartupFormalities() {
		notifier.setStatus(MBeanProcessNotifier.MBEAN_PROCESS_STATUS_RUNNING);
		notifyObserver();
	}
	
	protected synchronized void performExitingFormalities(String processId) {
		Logger.log("Connection Broken.... Exiting Process [Name: " + processId + "]");
		notifier.setStatus(MBeanProcessNotifier.MBEAN_PROCESS_STATUS_ERROR);
		notifyObserver();
	}
	
	protected synchronized void performCleanExit(String processId) {
		Logger.log("Clean Exit for Process [Name: " + processId + "]");
		notifier.setStatus(MBeanProcessNotifier.MBEAN_PROCESS_STATUS_STOPPED);
		notifyObserver();
	}
	
	private void notifyObserver() {
		setChanged();
		notifyObservers(notifier.getStatus());
	}
	
	public abstract String getProcessStatus();

	protected abstract void initNotifier();
	
	protected abstract void initMBeanInternal(String mbeanType);
}
