package com.sc.hm.vmxd.data.os;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class OperatingSystemData {

	private String name = "";
	private List<Double> cpuUsage = new ArrayList<>();
	private List<Date> timeList = new ArrayList<>();
	
	private long totalPhysicalMemorySize = 0L;
	private long freePhysicalMemorySize = 0L;
	private long totalSwapSpaceSize = 0L;
	private long freeSwapSpaceSize = 0L;
	
	private long committedVirtualMemorySize = 0L;
	private String version = "";	
	private int availableProcessor = 0;
	private String arch = "";
	
	private double min_cpu = Double.MAX_VALUE;
	private double max_cpu = 0.0D;
	
	public OperatingSystemData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setProcessCpuTime(Double cpuTime) {
		cpuUsage.add(cpuTime);
		if (cpuTime > max_cpu) {
			max_cpu = cpuTime;
		}
		if (cpuTime < min_cpu) {
			min_cpu = cpuTime;
		}
		timeList.add(new Date());
	}
    
    public void setProcessCpuLoad(Double cpuTime) {
        if (cpuTime < 0) {
            return;
        }
        Double cpuLoad = Math.round(cpuTime * 10000.0) / 100.0;
        cpuUsage.add(cpuLoad);
        if (cpuLoad > max_cpu) {
            max_cpu = cpuLoad;
        }
        if (cpuLoad < min_cpu) {
            min_cpu = cpuLoad;
        }
        timeList.add(new Date());
    }

	public long getTotalPhysicalMemorySize() {
		return totalPhysicalMemorySize;
	}

	public void setTotalPhysicalMemorySize(Long totalPhysicalMemorySize) {
		this.totalPhysicalMemorySize = totalPhysicalMemorySize;
	}

	public long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}

	public void setFreePhysicalMemorySize(Long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}

	public long getTotalSwapSpaceSize() {
		return totalSwapSpaceSize;
	}

	public void setTotalSwapSpaceSize(Long totalSwapSpaceSize) {
		this.totalSwapSpaceSize = totalSwapSpaceSize;
	}

	public long getFreeSwapSpaceSize() {
		return freeSwapSpaceSize;
	}

	public void setFreeSwapSpaceSize(Long freeSwapSpaceSize) {
		this.freeSwapSpaceSize = freeSwapSpaceSize;
	}

	public long getCommittedVirtualMemorySize() {
		return committedVirtualMemorySize;
	}

	public void setCommittedVirtualMemorySize(Long committedVirtualMemorySize) {
		this.committedVirtualMemorySize = committedVirtualMemorySize;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getAvailableProcessor() {
		return availableProcessor;
	}

	public void setAvailableProcessors(Integer availableProcessor) {
		this.availableProcessor = availableProcessor;
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public double getMin_cpu() {
		return min_cpu;
	}

	public void setMin_cpu(double min_cpu) {
		this.min_cpu = min_cpu;
	}

	public double getMax_cpu() {
		return max_cpu;
	}

	public void setMax_cpu(double max_cpu) {
		this.max_cpu = max_cpu;
	}

	public List<Double> getCpuUsage() {
		return cpuUsage;
	}

	public List<Date> getTimeList() {
		return timeList;
	}
	
	public double getCurrentCpuTime() {
		if (cpuUsage.size() == 0) {
			return 0;
		}
		return cpuUsage.get(cpuUsage.size() - 1);
	}
}
