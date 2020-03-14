package com.sc.hm.monitor.worker.pool;

import java.util.HashMap;
import java.util.Map;

import com.sc.hm.monitor.worker.process.WorkerThread;

public class WorkerThreadManager {

	private Map<String, WorkerThreadPool> threadPoolMap = null;
	
	private static WorkerThreadManager manager = null;
	
	private WorkerThreadManager() {
		threadPoolMap = new HashMap<String, WorkerThreadPool>();
	}
	
	public static WorkerThreadManager getManager() {
		if (manager == null) {
			synchronized (WorkerThreadManager.class) {
				if (manager == null) {
					synchronized (WorkerThreadManager.class) {
						manager = new WorkerThreadManager();
					}
				}
			}
		}
		return manager;
	}
	
	public void createThreadPool() {
		WorkerThreadPool threadPool = new WorkerThreadPool();
		threadPoolMap.put(threadPool.getPoolName(), threadPool);
	}
	
	public void createThreadPool(int poolSize) {
		WorkerThreadPool threadPool = new WorkerThreadPool(poolSize);
		threadPoolMap.put(threadPool.getPoolName(), threadPool);
	}
	
	public void createThreadPool(String poolName, int poolSize) {
		WorkerThreadPool threadPool = new WorkerThreadPool(poolName, poolSize);
		threadPoolMap.put(threadPool.getPoolName(), threadPool);
	}
	
	public WorkerThread getNextAvailableWorkerFromPool(String name) {
		if (threadPoolMap.containsKey(name)) {
			WorkerThreadPool threadPool = threadPoolMap.get(name);
			return threadPool.getNextAvailableWorker();
		}
		return null;
	}
	
	public WorkerThread getAlwaysNextAvailableWorkerFromPool(String name) {
		if (threadPoolMap.containsKey(name)) {
			WorkerThreadPool threadPool = threadPoolMap.get(name);
			return threadPool.getAlwaysNextAvailableWorker();
		}
		return null;
	}
}
