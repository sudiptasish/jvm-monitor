package com.sc.hm.monitor.process.compile;

import java.lang.management.CompilationMXBean;
import java.lang.reflect.UndeclaredThrowableException;

import com.sc.hm.monitor.mbean.factory.ManagementBeanFactory;
import com.sc.hm.monitor.process.AbstractMBeanProcess;
import com.sc.hm.monitor.process.MBeanWorkFlow;
import com.sc.hm.monitor.shared.IMBeanObject;
import com.sc.hm.monitor.shared.compile.CompilationMBeanSharedObject;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.ManagementUtil;
import com.sc.hm.monitor.worker.process.WorkerTask;

public class CompilationMBeanProcess extends AbstractMBeanProcess implements WorkerTask, MBeanWorkFlow {
	
	private String threadName = "COMPILE_MBEAN_PROCESS";
	
	private CompilationMXBean compile_mx_bean = null;
	
	public CompilationMBeanProcess(String mbeanType, ManagementBeanFactory mbeanFactory) {
        super(mbeanType, mbeanFactory);
        try {			
            initNotifier();
            initOther();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public CompilationMBeanProcess(String name, String mbeanType, ManagementBeanFactory mbeanFactory) {
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
		notifier = new CompilationMBeanNotifier(true);
	}
	
	public void initOther() throws Exception {
		compile_mx_bean = getMbeanFactory().getCompilationMXBean();		
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
		Logger.log("Started CompilationMBeanProcess [Name: " + threadName + "]");
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
		CompilationMBeanSharedObject cMBean = repos.getCompiler_mx_bean();
		synchronized (cMBean) {
			cMBean.setCompilerName(compile_mx_bean.getName());
			cMBean.setCompilationTime(compile_mx_bean.getTotalCompilationTime());
			cMBean.setCompMonitoringSupported(compile_mx_bean.isCompilationTimeMonitoringSupported());
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
