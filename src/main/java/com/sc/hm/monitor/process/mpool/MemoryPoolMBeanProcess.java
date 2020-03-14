package com.sc.hm.monitor.process.mpool;

import java.lang.management.MemoryPoolMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import com.sc.hm.monitor.mbean.factory.ManagementBeanFactory;
import com.sc.hm.monitor.process.AbstractMBeanProcess;
import com.sc.hm.monitor.process.MBeanWorkFlow;
import com.sc.hm.monitor.shared.IMBeanObject;
import com.sc.hm.monitor.shared.mpool.MemoryPoolMBeanSharedObject;
import com.sc.hm.monitor.util.GraphImageProperties;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.ManagementUtil;
import com.sc.hm.monitor.worker.process.WorkerTask;

public class MemoryPoolMBeanProcess extends AbstractMBeanProcess implements WorkerTask, MBeanWorkFlow {

	private String threadName = "MEMORYPOOL_MBEAN_PROCESS";
	
	private List<MemoryPoolMXBean> memorypool_mx_beans = null;
	
	public MemoryPoolMBeanProcess(String mbeanType, ManagementBeanFactory mbeanFactory) {
        super(mbeanType, mbeanFactory);
        try {			
            initNotifier();
            initOther();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public MemoryPoolMBeanProcess(String name, String mbeanType, ManagementBeanFactory mbeanFactory) {
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
		notifier = new MemoryPoolMBeanNotifier(true);
	}
	
	public void initOther() throws Exception {
		memorypool_mx_beans = getMbeanFactory().getMemoryPoolMXBeans();
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
		Logger.log("Started MemoryPoolMBeanProcess [Name: " + threadName + "]");
		try {
			for (boolean isFirstLoop = true; notifier.isRunMBeanProcess(); isFirstLoop = false) {
				populateSummary();
				populateDetails();
				
				if (isFirstLoop) {
					performStartupFormalities();
				}								
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
	
	public void populateSummary() throws Exception {
		
	}
	
	public void populateDetails() throws Exception, UndeclaredThrowableException {
		MemoryPoolMBeanSharedObject memoryPoolShared = repos.getMpool_mx_bean();
		
		if (appConfig.getGraphType().equals(GraphImageProperties.GRAPH_TYPE_SYNCH)) {
			memoryPoolShared.addTime(System.currentTimeMillis());
			memoryPoolShared.populateMemoryInfo(memorypool_mx_beans);
		}
		else {
			long start_time = System.currentTimeMillis();
			synchronized (memoryPoolShared) {
				memoryPoolShared.addTime(System.currentTimeMillis());
				for (MemoryPoolMXBean mpoolMXBean : memorypool_mx_beans) {
					memoryPoolShared.addMemoryPool(mpoolMXBean);
				}
			}
			Logger.log("Time MemoryPool Thread: " + (System.currentTimeMillis() - start_time));
			pause(ManagementUtil.DEFAULT_PROCESS_PAUSE_TIME);
		}
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
