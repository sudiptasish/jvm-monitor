package com.sc.hm.monitor.shared.os;

import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

import com.sc.hm.monitor.shared.IMBeanObject;

public class OSMBeanSharedObject implements IMBeanObject {

	private String osName = "";
	private String osversion = "";
	private int availableProcessor = 0;
	private String architecture = "";
	private long totalPhysicalMemory = 0L;
	private long freePhysicalMemory = 0L;
	private long totalSwapSpace = 0L;
	private long freeSwapSpace = 0L;
	private long committedVirtualMemory = 0L;
	
	private long minCPUTime = Long.MAX_VALUE;
	private long maxCPUTime = 0L;
	private List<Long> timeList = null;
	private List<Long> processCPUTime = null;
	
	public OSMBeanSharedObject() {
		processCPUTime = new ArrayList<Long>();
		timeList = new ArrayList<Long>();
	}
	
	public void updateOSMBean(OperatingSystemMXBean osMXBean) throws Exception, UndeclaredThrowableException {
		setOsName(osMXBean.getName());
		setOsversion(osMXBean.getVersion());
		setAvailableProcessor(osMXBean.getAvailableProcessors());
		setArchitecture(osMXBean.getArch());
		setTimeList(System.currentTimeMillis());
		
		if (osMXBean instanceof com.sun.management.OperatingSystemMXBean) {
			com.sun.management.OperatingSystemMXBean sunOSMXBean = (com.sun.management.OperatingSystemMXBean)osMXBean;
			setProcessCPUTime(sunOSMXBean.getProcessCpuTime());
			setTotalPhysicalMemory(sunOSMXBean.getTotalPhysicalMemorySize());
			setFreePhysicalMemory(sunOSMXBean.getFreePhysicalMemorySize());
			setTotalSwapSpace(sunOSMXBean.getTotalSwapSpaceSize());
			setFreeSwapSpace(sunOSMXBean.getFreeSwapSpaceSize());
			setCommittedVirtualMemory(sunOSMXBean.getCommittedVirtualMemorySize());
		}
	}

	public long getStartTime() {
		return timeList.get(0);
	}

	public List<Long> getTimeList() {
		return timeList;
	}

	public void setTimeList(long currentTime) {
		this.timeList.add(currentTime);
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public int getAvailableProcessor() {
		return availableProcessor;
	}

	public void setAvailableProcessor(int availableProcessor) {
		this.availableProcessor = availableProcessor;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsversion() {
		return osversion;
	}

	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}

	public long getCommittedVirtualMemory() {
		return committedVirtualMemory;
	}

	public void setCommittedVirtualMemory(long committedVirtualMemory) {
		this.committedVirtualMemory = committedVirtualMemory;
	}

	public long getFreePhysicalMemory() {
		return freePhysicalMemory;
	}

	public void setFreePhysicalMemory(long freePhysicalMemory) {
		this.freePhysicalMemory = freePhysicalMemory;
	}

	public long getFreeSwapSpace() {
		return freeSwapSpace;
	}

	public void setFreeSwapSpace(long freeSwapSpace) {
		this.freeSwapSpace = freeSwapSpace;
	}

	public List<Long> getProcessCPUTime() {
		return processCPUTime;
	}

	public void setProcessCPUTime(long processCPUTime) {
		if (processCPUTime > maxCPUTime) {
			maxCPUTime = processCPUTime;
		}
		if (processCPUTime < minCPUTime) {
			minCPUTime = processCPUTime;
		}
		this.processCPUTime.add(processCPUTime);
	}

	public long getTotalPhysicalMemory() {
		return totalPhysicalMemory;
	}

	public void setTotalPhysicalMemory(long totalPhysicalMemory) {
		this.totalPhysicalMemory = totalPhysicalMemory;
	}

	public long getTotalSwapSpace() {
		return totalSwapSpace;
	}

	public void setTotalSwapSpace(long totalSwapSpace) {
		this.totalSwapSpace = totalSwapSpace;
	}

	public long getMaxCPUTime() {
		return maxCPUTime;
	}

	public void setMaxCPUTime(long maxCPUTime) {
		this.maxCPUTime = maxCPUTime;
	}

	public long getMinCPUTime() {
		return minCPUTime;
	}

	public void setMinCPUTime(long minCPUTime) {
		this.minCPUTime = minCPUTime;
	}
	
}
