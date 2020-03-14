package com.sc.hm.vmxd.process.executor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;

public class MBeanCustomProcessExecutor extends AbstractMBeanProcessExecutor {

	public MBeanCustomProcessExecutor(String name) {
		super(name);
	}

	public int getExecutorProcessCount() {
		return 0;
	}

	public void startExecuting(MXBeanServer mxbeanServer, String application) {
		
	}

	public void startCustomExecuting(MXBeanServer mxbeanServer, String application, String process) {
		
	}

	public void startExecuting(MXBeanServer mxbeanServer, String application, CountDownLatch latch) {
		
	}

	public void startExecuting(Runnable r) {
		
	}

	public Future<Object> submitFutureTask(MXBeanServer mxbeanServer,
			String application, String process) throws Exception {
		return null;
	}

	public void startExecuting(MXBeanServer mxbeanServer, String application,
			CountDownLatch latch, SynchProcessObserver observer) {
		
	}
}
