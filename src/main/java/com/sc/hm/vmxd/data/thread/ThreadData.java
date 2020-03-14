package com.sc.hm.vmxd.data.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class ThreadData {
	
	private long daemonThreadCount = 0L;
	private long peakThreadCount = 0L;
	private long totalStartedThreadCount = 0L;
	private long deadlockThreadCount = 0L;
	private long threadCount = 0L;

	private final List<Long> cpuTimeList = new ArrayList<>();
	private final List<Date> timeList = new ArrayList<>();
	
	private long cpu_max = 0L;
	private long cpu_min = Long.MAX_VALUE;
	
	public ThreadData() {}
	
	public void setCurrentThreadCpuTime(Number cpu_time) {
		long cpu_time_l = cpu_time.longValue();
		cpuTimeList.add(cpu_time_l);
		timeList.add(new Date());
		
		if (cpu_time_l < cpu_min) {
			cpu_min = cpu_time_l;
		}
		if (cpu_time_l > cpu_max) {
			cpu_max = cpu_time_l;
		}
	}

	public long getDeadlockThreadCount() {
		return deadlockThreadCount;
	}

	public void setDeadlockThreadCount(Number deadlockThreadCount) {
		this.deadlockThreadCount = deadlockThreadCount.longValue();
	}

	public long getDaemonThreadCount() {
		return daemonThreadCount;
	}

	public void setDaemonThreadCount(Number daemonThreadCount) {
		this.daemonThreadCount = daemonThreadCount.longValue();
	}

	public long getPeakThreadCount() {
		return peakThreadCount;
	}

	public void setPeakThreadCount(Number peakThreadCount) {
		this.peakThreadCount = peakThreadCount.longValue();
	}

	public long getTotalStartedThreadCount() {
		return totalStartedThreadCount;
	}

	public void setTotalStartedThreadCount(Number totalStartedThreadCount) {
		this.totalStartedThreadCount = totalStartedThreadCount.longValue();
	}

	public long getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(Number threadCount) {
		this.threadCount = threadCount.longValue();
	}

	public List<Long> getCpuTimeList() {
		return cpuTimeList;
	}

	public List<Date> getTimeList() {
		return timeList;
	}

	public long getCpu_max() {
		return cpu_max;
	}

	public long getCpu_min() {
		return cpu_min;
	}
}
