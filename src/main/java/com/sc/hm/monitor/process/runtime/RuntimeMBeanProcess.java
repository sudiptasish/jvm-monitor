package com.sc.hm.monitor.process.runtime;

import java.lang.management.RuntimeMXBean;
import java.lang.reflect.UndeclaredThrowableException;

import com.sc.hm.monitor.mbean.factory.ManagementBeanFactory;
import com.sc.hm.monitor.process.AbstractMBeanProcess;
import com.sc.hm.monitor.process.MBeanWorkFlow;
import com.sc.hm.monitor.shared.IMBeanObject;
import com.sc.hm.monitor.shared.runtime.RuntimeMBeanSharedObject;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.ManagementUtil;
import com.sc.hm.monitor.worker.process.WorkerTask;

public class RuntimeMBeanProcess extends AbstractMBeanProcess implements WorkerTask, MBeanWorkFlow {
	
	private String threadName = "RUNTIME_MBEAN_PROCESS";
	
	private RuntimeMXBean runtime_mx_bean = null;
	
	public RuntimeMBeanProcess(String mbeanType, ManagementBeanFactory mbeanFactory) {
		super(mbeanType, mbeanFactory);
		try {			
			initNotifier();
			initOther();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public RuntimeMBeanProcess(String name, String mbeanType, ManagementBeanFactory mbeanFactory) {
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
		notifier = new RuntimeMBeanNotifier(true);
	}
	
	public void initOther() throws Exception {
		runtime_mx_bean = getMbeanFactory().getRuntimeMXBean();
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
		Logger.log("Started RuntimeMBeanProcess [Name: " + threadName + "]");
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
		RuntimeMBeanSharedObject rMBean = repos.getRuntime_mx_bean();
		synchronized (rMBean) {
			rMBean.setBootClasspath(runtime_mx_bean.getBootClassPath());
			rMBean.setClasspath(runtime_mx_bean.getClassPath());
			rMBean.setInputArguments(runtime_mx_bean.getInputArguments());
			rMBean.setJvmName(runtime_mx_bean.getName());
			rMBean.setLibrarypath(runtime_mx_bean.getLibraryPath());
			rMBean.setSpecName(runtime_mx_bean.getSpecName());
			rMBean.setSpecVendor(runtime_mx_bean.getSpecVendor());
			rMBean.setSpecVersion(runtime_mx_bean.getSpecVersion());
			rMBean.setStartTime(runtime_mx_bean.getStartTime());
			rMBean.setSystemProperties(runtime_mx_bean.getSystemProperties());
			rMBean.setUpTime(runtime_mx_bean.getUptime());
			rMBean.setVmName(runtime_mx_bean.getVmName());
			rMBean.setVmVendor(runtime_mx_bean.getVmVendor());
			rMBean.setVmVersion(runtime_mx_bean.getVmVersion());
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
