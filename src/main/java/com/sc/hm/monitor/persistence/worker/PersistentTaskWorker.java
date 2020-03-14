package com.sc.hm.monitor.persistence.worker;

import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.monitor.util.Logger;

public class PersistentTaskWorker extends MonitorTaskWorker {
	
	private ChildTask[] ct = new ChildTask[5];
	private boolean[] working = new boolean[5];
	
	public PersistentTaskWorker() {
		super();
		for (int i = 0; i < ct.length; i ++) {
			ct[i] = new ChildTask();
		}
	}
	
	public void scheduleTaskImmediate(MonitorTask t) {
		startProcessingImmediate(t);
	}

	public void scheduleTask(MonitorTask t, long milliseconds) throws Exception {		
		startProcessing(t, milliseconds);
	}
	
	public void startProcessing(MonitorTask t, long milliseconds) throws Exception {
		ChildTask ctask = null;
		for (int i = 0; i < working.length; i ++) {
			if (!working[i]) {
				working[i] = true;
				ctask = ct[i];
				break;
			}
		}
		if (ctask != null) {
			synchronized (ctask) {
				ctask.setTask(t);
				try {
					Thread.sleep(milliseconds);
				}
				catch (InterruptedException inte) {}
			}
			new Thread(ctask).start();
		}
		else {
			throw new Exception ("No Available ChildTask Worker to Perform The Task");
		}
	}
	
	public void startProcessingImmediate(MonitorTask t) {
		ChildTask ctask = null;
		for (int i = 0; i < working.length; i ++) {
			if (!working[i]) {
				working[i] = true;
				ctask = ct[i];
				break;
			}
		}
		if (ctask == null) {
			ctask = new ChildTask();
		}
		ctask.setTask(t);
		Thread thread = new Thread(ctask); 
		thread.start();
		try {
			thread.join();
		}
		catch (Exception e) {}
		Logger.log("Completed Data Persistence Job....");
	}
	
	public synchronized final void resetStatus(ChildTask ctask) {
		for (int i = 0; i < working.length; i ++) {
			if (ctask == ct[i]) {
				working[i] = false;
				break;
			}
		}
	}
	
	private class ChildTask implements Runnable {
		private MonitorTask t = null;
		
		public ChildTask() {}
		
		public MonitorTask getTask() {
			return t;
		}

		public void setTask(MonitorTask t) {
			this.t = t;
		}

		public void run() {
			perform();
			resetStatus(this);
		}
		
		public void perform() {
			try {
				t.performTask();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
