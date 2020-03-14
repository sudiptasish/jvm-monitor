package com.sc.hm.monitor.shared.runtime;

import java.util.List;
import java.util.Map;

import com.sc.hm.monitor.shared.IMBeanObject;

public class RuntimeMBeanSharedObject implements IMBeanObject {

	private String jvmName = "";
	private String specName = "";
	private String specVendor = "";
	private String specVersion = "";
	private String bootClasspath = "";
	private String classpath = "";
	private List<String> inputArguments = null;
	private String librarypath = "";
	private long startTime = 0L;
	private long upTime = 0L;
	private String vmName = "";
	private String vmVendor = "";
	private String vmVersion = "";
	private Map<String, String> systemProperties = null;
	
	public RuntimeMBeanSharedObject() {}

	public RuntimeMBeanSharedObject(String jvmName, String specName, String specVendor, String specVersion, String bootClasspath, String classpath, List<String> inputArguments, String librarypath, long startTime, long upTime, String vmName, String vmVendor, String vmVersion, Map<String, String> systemProperties) {
		this.jvmName = jvmName;
		this.specName = specName;
		this.specVendor = specVendor;
		this.specVersion = specVersion;
		this.bootClasspath = bootClasspath;
		this.classpath = classpath;
		this.inputArguments = inputArguments;
		this.librarypath = librarypath;
		this.startTime = startTime;
		this.upTime = upTime;
		this.vmName = vmName;
		this.vmVendor = vmVendor;
		this.vmVersion = vmVersion;
		this.systemProperties = systemProperties;
	}

	public String getBootClasspath() {
		return bootClasspath;
	}

	public void setBootClasspath(String bootClasspath) {
		this.bootClasspath = bootClasspath;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public List<String> getInputArguments() {
		return inputArguments;
	}

	public void setInputArguments(List<String> inputArguments) {
		this.inputArguments = inputArguments;
	}

	public String getJvmName() {
		return jvmName;
	}

	public void setJvmName(String jvmName) {
		this.jvmName = jvmName;
	}

	public String getLibrarypath() {
		return librarypath;
	}

	public void setLibrarypath(String librarypath) {
		this.librarypath = librarypath;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getSpecVendor() {
		return specVendor;
	}

	public void setSpecVendor(String specVendor) {
		this.specVendor = specVendor;
	}

	public String getSpecVersion() {
		return specVersion;
	}

	public void setSpecVersion(String specVersion) {
		this.specVersion = specVersion;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getUpTime() {
		return upTime;
	}

	public void setUpTime(long upTime) {
		this.upTime = upTime;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getVmVendor() {
		return vmVendor;
	}

	public void setVmVendor(String vmVendor) {
		this.vmVendor = vmVendor;
	}

	public String getVmVersion() {
		return vmVersion;
	}

	public void setVmVersion(String vmVersion) {
		this.vmVersion = vmVersion;
	}

	public Map<String, String> getSystemProperties() {
		return systemProperties;
	}

	public void setSystemProperties(Map<String, String> systemProperties) {
		this.systemProperties = systemProperties;
	}
	
	public String toString() {
		return "[" + this.systemProperties + "]";
	}
}
