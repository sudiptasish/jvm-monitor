package com.sc.hm.monitor.shared.threads;

import java.lang.management.ThreadMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sc.hm.monitor.shared.IMBeanObject;
import com.sc.hm.monitor.util.Logger;

public class ThreadMBeanSharedObject implements IMBeanObject {

	private long liveThreads = 0L;
	private long daemonThreads = 0L;
	private long peakThreads = 0L;
	private long startedThreads = 0L;
	private long deadlockedThreads = 0L;
	
	private boolean firstTime = true;
	
	private Map<Long, VMThreadInfo> threadInfoMap = new HashMap<Long, VMThreadInfo>();
	
	public ThreadMBeanSharedObject() {}
		
	public void updateThreadMBean(ThreadMXBean threadMXBean) throws Exception, UndeclaredThrowableException {
		setLiveThreads(threadMXBean.getThreadCount());
		setDaemonThreads(threadMXBean.getDaemonThreadCount());
		setPeakThreads(threadMXBean.getPeakThreadCount());
		setStartedThreads(threadMXBean.getTotalStartedThreadCount());
		if (threadMXBean.findMonitorDeadlockedThreads() != null) {
			setDeadlockedThreads(threadMXBean.findMonitorDeadlockedThreads().length);
		}
		if (firstTime) {
			if (threadMXBean.isThreadCpuTimeSupported()) {
				threadMXBean.setThreadCpuTimeEnabled(true);
			}
			else {
				Logger.log("Thread CPU Time Not Supported");
			}
			firstTime = false;
		}
		
		long[] ids = threadMXBean.getAllThreadIds();
		for (int i = 0; i < ids.length; i ++) {
			if (threadInfoMap.containsKey(ids[i])) {
				VMThreadInfo threadInfo = threadInfoMap.get(ids[i]);
				threadInfo.updateThreadInformation(threadMXBean.getThreadInfo(ids[i], Integer.MAX_VALUE), threadMXBean.getThreadCpuTime(ids[i]));
				threadInfoMap.put(ids[i], threadInfo);
			}
			else {
				VMThreadInfo threadInfo = new VMThreadInfo();
				threadInfo.updateThreadInformation(threadMXBean.getThreadInfo(ids[i], Integer.MAX_VALUE), threadMXBean.getThreadCpuTime(ids[i]));
				threadInfoMap.put(ids[i], threadInfo);
			}
		}
	}

	public long getDaemonThreads() {
		return daemonThreads;
	}
	
	public void setDaemonThreads(long daemonThreads) {
		this.daemonThreads = daemonThreads;
	}
	
	public long getDeadlockedThreads() {
		return deadlockedThreads;
	}
	
	public void setDeadlockedThreads(long deadlockedThreads) {
		this.deadlockedThreads = deadlockedThreads;
	}
	
	public long getLiveThreads() {
		return liveThreads;
	}
	
	public void setLiveThreads(long liveThreads) {
		this.liveThreads = liveThreads;
	}
	
	public long getPeakThreads() {
		return peakThreads;
	}
	
	public void setPeakThreads(long peakThreads) {
		this.peakThreads = peakThreads;
	}
	
	public long getStartedThreads() {
		return startedThreads;
	}
	
	public void setStartedThreads(long startedThreads) {
		this.startedThreads = startedThreads;
	}
	
	public Long[] getAllThreadIds() {
		Set<Long> set = threadInfoMap.keySet();
		return (Long[])set.toArray(new Long[set.size()]);
	}
	
	public VMThreadInfo getThreadInfo(long threadId) {
		if (threadInfoMap.containsKey(threadId)) {
			return threadInfoMap.get(threadId);
		}
		return null;
	}
	
	public String toString() {
		return "[" + this.liveThreads + " " + this.daemonThreads + " " + this.peakThreads + " " + this.startedThreads + " " + this.deadlockedThreads + "]";
	}
}
