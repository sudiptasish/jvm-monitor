package com.sc.hm.monitor.shared.compile;

import com.sc.hm.monitor.shared.IMBeanObject;

public class CompilationMBeanSharedObject implements IMBeanObject {

	private String compilerName = "";
	private long compilationTime = 0L;
	private boolean isCompMonitoringSupported = false;
	
	public CompilationMBeanSharedObject() {}
	
	public CompilationMBeanSharedObject(String compilerName, long compilationTime, boolean isCompMonitoringSupported) {
		this.compilerName = compilerName;
		this.compilationTime = compilationTime;
		this.isCompMonitoringSupported = isCompMonitoringSupported;
	}

	public long getCompilationTime() {
		return compilationTime;
	}
	public void setCompilationTime(long compilationTime) {
		this.compilationTime = compilationTime;
	}
	public String getCompilerName() {
		return compilerName;
	}
	public void setCompilerName(String compilerName) {
		this.compilerName = compilerName;
	}
	public boolean isCompMonitoringSupported() {
		return isCompMonitoringSupported;
	}
	public void setCompMonitoringSupported(boolean isCompMonitoringSupported) {
		this.isCompMonitoringSupported = isCompMonitoringSupported;
	}
	
	public String toString() {
		return "[" + this.compilerName + " " + this.compilationTime + " " + this.isCompMonitoringSupported + "]";
	}
}
