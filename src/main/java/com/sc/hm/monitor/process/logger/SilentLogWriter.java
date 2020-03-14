package com.sc.hm.monitor.process.logger;

public class SilentLogWriter extends AbstractLogWriter {
	
	private boolean silentLogging = false;
	private String filePath = "";
	
	public SilentLogWriter() {}

	public void enableSilentLogging() {
		silentLogging = true;
	}
	
	public void disableSilentLogging() {
		silentLogging = false;
	}

	public void monitorLogging() {
		
	}
	
	private class PersistenceLogger {
		
	}
}
