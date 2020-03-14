/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sc.hm.monitor.worker.pool;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.worker.process.WorkerThread;

/**
 *
 * @author schanda
 */
public class WorkerThreadPool {

    private String poolName = "";
    private WorkerThread[] workers = null;
    private boolean[] isWorking = null;
    
    public WorkerThreadPool() {
        this(10);
    }
    
    public WorkerThreadPool(int poolsize) {
        this ("DEFAULT_POOL", poolsize);
    }
    
    public WorkerThreadPool(String name, int poolsize) {
        poolName = name;
        initializePool(poolsize);
    }
    
    public String getPoolName() {
    	return poolName;
    }
    
    public void initializePool(int poolsize) {
        workers = new WorkerThread[poolsize];
        for (int i = 0; i < workers.length; i ++) {
            workers[i] = new WorkerThread();
        }
        isWorking = new boolean[poolsize];
        Logger.log("Initialized Worker Thread Pool [" + poolName + "]");
    }
    
    public WorkerThread getNextAvailableWorker() {
        for (int i = 0; i < isWorking.length; i ++) {
            if (!isWorking[i]) {
                isWorking[i] = true;
                return workers[i];
            }
        }
        return null;
    }
    
    public WorkerThread getAlwaysNextAvailableWorker() {
        for (int i = 0; i < isWorking.length; i ++) {
            if (!isWorking[i]) {
                isWorking[i] = true;
                return workers[i];
            }
        }
        WorkerThread newWorker = new WorkerThread();
        adjustWorkerThreadArray(newWorker);
        adjustWorkerStatusArray();
        
        return workers[workers.length - 1];
    }
    
    private void adjustWorkerThreadArray(WorkerThread newWorker) {
    	WorkerThread[] temp = new WorkerThread[workers.length + 1];
        System.arraycopy(workers, 0, temp, 0, workers.length);
        temp[workers.length] = newWorker;
        workers = temp;
    }
    
    private void adjustWorkerStatusArray() {
    	boolean[] temp = new boolean[isWorking.length + 1];
        System.arraycopy(isWorking, 0, temp, 0, isWorking.length);
        temp[isWorking.length] = true;
        isWorking = temp;
    }
}
