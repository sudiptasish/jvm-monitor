package com.sc.hm.monitor.process.gbcollector;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import com.sc.hm.monitor.mbean.factory.ManagementBeanFactory;
import com.sc.hm.monitor.process.AbstractMBeanProcess;
import com.sc.hm.monitor.process.MBeanWorkFlow;
import com.sc.hm.monitor.shared.IMBeanObject;
import com.sc.hm.monitor.shared.gbcollector.GBCollectorMBeanSharedObject;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.ManagementUtil;
import com.sc.hm.monitor.worker.process.WorkerTask;

public class GBCollectorMBeanProcess extends AbstractMBeanProcess implements WorkerTask, MBeanWorkFlow {

	private String threadName = "GARBAGE_COLLECTOR_MBEAN_PROCESS";
	
	private List<GarbageCollectorMXBean> gbcollector_mx_bean = null;
	
	public GBCollectorMBeanProcess(String mbeanType, ManagementBeanFactory mbeanFactory) {
		super(mbeanType, mbeanFactory);
		try {			
			initNotifier();
			initOther();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public GBCollectorMBeanProcess(String name, String mbeanType, ManagementBeanFactory mbeanFactory) {
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
		notifier = new GBCollectorMBeanNotifier(true);
	}
	
	public void initOther() throws Exception {
		gbcollector_mx_bean = getMbeanFactory().getGarbageCollectorMXBeans();		
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
		Logger.log("Started GBCollectorMBeanProcess [Name: " + threadName + "]");
		try {
			for (boolean isFirstLoop = true; notifier.isRunMBeanProcess(); isFirstLoop = false) {
				populateSummary();			
				populateDetails();			
				
				if (isFirstLoop) {
					performStartupFormalities();
				}
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
		GBCollectorMBeanSharedObject gcMBean = repos.getGbcollector_mx_bean();
		synchronized (gcMBean) {			
			for (GarbageCollectorMXBean gcMXBean : gbcollector_mx_bean) {
				gcMBean.updateGarbageCollector(gcMXBean);				
			}
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
