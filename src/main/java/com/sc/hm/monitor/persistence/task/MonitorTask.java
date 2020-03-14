package com.sc.hm.monitor.persistence.task;

public abstract class MonitorTask {
	
	public static final String TASK_ALL_PERSIST = "TASK_ALL_PERSIST";
	public static final String TASK_MEMORY_PERSIST = "TASK_MEMORY_PERSIST";
	public static final String TASK_MEMORYPOOL_PERSIST = "TASK_MEMORYPOOL_PERSIST";
	public static final String TASK_CPUUSAGE_PERSIST = "TASK_CPUUSAGE_PERSIST";
	public static final String TASK_GBCOLLECTION_PERSIST = "TASK_GBCOLLECTION_PERSIST";
	public static final String TASK_CLASSES_PERSIST = "TASK_CLASSES_PERSIST";

	protected String taskName = "";

	public MonitorTask(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public abstract void performTask() throws Exception;
}
