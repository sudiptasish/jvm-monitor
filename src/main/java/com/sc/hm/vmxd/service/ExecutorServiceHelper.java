package com.sc.hm.vmxd.service;

import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceHelper {
	
	private static final ExecutorServiceHelper executorServicehelper = new ExecutorServiceHelper();
	
	private Vector<ExecutorService> runningServicePool = new Vector<ExecutorService>(2);
	
	private ExecutorServiceHelper() {}
	
	public static ExecutorServiceHelper getExecutorServiceHelper() {
		return executorServicehelper;
	}

	public void addNewExecutorServicePool(ExecutorService executorService) {
		runningServicePool.addElement(executorService);
	}
	
	public Enumeration<ExecutorService> getRunningExecutorPools() {
		return runningServicePool.elements();
	}
	
	public ExecutorService createSingleExecutorPool() {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		addNewExecutorServicePool(executorService);
		return executorService;
	}
	
	public ExecutorService createCachedExecutorPool() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		addNewExecutorServicePool(executorService);
		return executorService;
	}
	
	public ExecutorService createFixedExecutorPool(int poolSize) {
		ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
		addNewExecutorServicePool(executorService);
		return executorService;
	}
	
	public void shutdownAllExecutorPools() {
		for (Enumeration<ExecutorService> enm = getRunningExecutorPools(); enm.hasMoreElements();) {
			shutdownAndAwait(enm.nextElement());
		}
	}
	
	private void shutdownAndAwait(ExecutorService executorService) {
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		}
		catch (InterruptedException inte) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}
