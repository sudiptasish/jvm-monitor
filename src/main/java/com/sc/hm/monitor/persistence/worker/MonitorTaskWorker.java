package com.sc.hm.monitor.persistence.worker;

import com.sc.hm.monitor.persistence.task.MonitorTask;

public abstract class MonitorTaskWorker {

	public MonitorTaskWorker() {}
	
	public abstract void scheduleTask(MonitorTask t, long milliseconds) throws Exception;
	
	public abstract void scheduleTaskImmediate(MonitorTask t) ;
}
