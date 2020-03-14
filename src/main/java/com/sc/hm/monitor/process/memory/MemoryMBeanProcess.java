package com.sc.hm.monitor.process.memory;

import java.lang.management.MemoryMXBean;
import java.lang.reflect.UndeclaredThrowableException;

import javax.management.NotificationEmitter;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.mbean.factory.ManagementBeanFactory;
import com.sc.hm.monitor.notif.listener.MemoryNotificationListener;
import com.sc.hm.monitor.process.AbstractMBeanProcess;
import com.sc.hm.monitor.process.MBeanWorkFlow;
import com.sc.hm.monitor.shared.IMBeanObject;
import com.sc.hm.monitor.shared.memory.MemoryMBeanSharedObject;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.ManagementUtil;
import com.sc.hm.monitor.worker.process.WorkerTask;

public class MemoryMBeanProcess extends AbstractMBeanProcess implements WorkerTask, MBeanWorkFlow {

	private String threadName = "MEMORY_MBEAN_PROCESS";
	
	private MemoryMXBean memory_mx_bean = null;
	
	public MemoryMBeanProcess(String mbeanType, ManagementBeanFactory mbeanFactory) {
		super(mbeanType, mbeanFactory);
		try {			
			initNotifier();
			initOther();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MemoryMBeanProcess(String name, String mbeanType, ManagementBeanFactory mbeanFactory) {
		super(mbeanType, mbeanFactory);
		try {			
			threadName = name;
			initNotifier();
			initOther();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initNotifier() {
		notifier = new MemoryMBeanNotifier(true);
	}
	
	public void initOther() throws Exception {
		memory_mx_bean = getMbeanFactory().getMemoryMXBean();
		EnvironmentConfigObject envConfig = appConfig.getEnvironmentConfig();
		if (Boolean.valueOf(envConfig.getEnableMailing())) {
			NotificationEmitter notificationEmitter = (NotificationEmitter)memory_mx_bean;
			notificationEmitter.addNotificationListener(new MemoryNotificationListener(), null, new String("MEMORY_THRESHOLD"));
		}
	}
	
	public void initMBeanInternal(String mbeanType) {
		try {
			IMBeanObject mbeanObject = mbeanSharedObject.getSharedMBean(mbeanType);
			storeMBean(mbeanObject);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void run() {
		Logger.log("Started MemoryMBeanProcess [Name: " + threadName + "]");
		try {
			for (boolean isFirstLoop = true; notifier.isRunMBeanProcess(); isFirstLoop = false) {
				long start_time = System.currentTimeMillis();
				populateSummary();			
				populateDetails();			
				
				if (isFirstLoop) {
					performStartupFormalities();
				}
				Logger.log("Time Memory Thread: " + (System.currentTimeMillis() - start_time));
				pause(ManagementUtil.DEFAULT_PROCESS_PAUSE_TIME);
			}
			performCleanExit(threadName);
		}
		catch (UndeclaredThrowableException unte) {
			performExitingFormalities(threadName);
		}
		catch (Exception e) {
			performExitingFormalities(threadName);
		}
	}
	
	public void populateSummary() throws Exception, UndeclaredThrowableException {
		MemoryMBeanSharedObject mMBean = repos.getMemory_mx_bean();
		synchronized (mMBean) {
			mMBean.addTime(System.currentTimeMillis());
			mMBean.addMemory(MemoryMBeanSharedObject.HEAP_MEMORY, memory_mx_bean.getHeapMemoryUsage());
			mMBean.addMemory(MemoryMBeanSharedObject.NONHEAP_MEMORY, memory_mx_bean.getNonHeapMemoryUsage());
			mMBean.setObjectPendingFinalization(memory_mx_bean.getObjectPendingFinalizationCount());
		}		
	}
	
	public void populateDetails() throws Exception {
		
	}
	
	public synchronized String getProcessStatus() {
		return notifier.getStatus();
	}
	
	public synchronized void setNotifier() {
		notifier.setRunMBeanProcess(true);
	}
	
	public synchronized void resetNotifier() {
		notifier.setRunMBeanProcess(false);
	}
	
	public void pause(int seconds) {
		try {
			this.wait(seconds * 1000);
		}
		catch (InterruptedException inte) {
			System.err.println(threadName + " Interrupted....");
			resetNotifier();
		}
	}
}
