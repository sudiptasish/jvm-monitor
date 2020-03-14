/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sc.hm.monitor.worker.process;

/**
 *
 * @author schanda
 */
public class WorkerThread extends Thread {
    
    private WorkerTask wTask = null;

    public WorkerThread() {
        super();
    }
    
    public void executeTask(WorkerTask wTask) {
        this.wTask = wTask;
        start();
    }
    
    public void run() {
        wTask.run();
    }
}
