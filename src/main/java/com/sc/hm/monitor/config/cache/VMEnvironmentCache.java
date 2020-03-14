package com.sc.hm.monitor.config.cache;



public class VMEnvironmentCache {

	private Object envObject = null;
	
	public VMEnvironmentCache() {
		initializeCache();
	}
	
	public void initializeCache() {}
	
	public void setEnvConfiguration(EnvironmentConfigObject value) {
		this.envObject= value;
	}
	
	public EnvironmentConfigObject getEnvConfiguration() {
		return (EnvironmentConfigObject)this.envObject;
	}
	
	public void flushEnvConfiguration() {
		this.envObject = null;
	}
}
