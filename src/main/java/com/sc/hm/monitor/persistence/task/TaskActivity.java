package com.sc.hm.monitor.persistence.task;

public interface TaskActivity {

	public void persistMemoryData() throws Exception;
	
	public void persistMemoryPoolData() throws Exception;
	
	public void persistCPUUsageData() throws Exception;
	
	public void persistGarbageCollectorData() throws Exception;
	
	public void persistClassLoadingData() throws Exception;
}
