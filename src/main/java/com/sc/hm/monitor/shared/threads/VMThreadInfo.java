package com.sc.hm.monitor.shared.threads;

import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.List;

import com.sc.hm.monitor.util.Logger;

public class VMThreadInfo {

	private long threadId = 0L;
	private String threadName = "";
	private String threadState = "";
	private String threadStackTrace = "";
	private String lockName = "";
	private long lockOwnerId = 0L;
	private String lockOwnerName = "";
	private long blockedCount = 0L;
	private long blockedTime = 0L;
	private long waitedCount = 0L;
	private long waitedTime = 0L;
	private List<Long> threadCPUTimes = null;
	
	public VMThreadInfo() {
		threadCPUTimes = new ArrayList<Long>();
	}
	
	public VMThreadInfo(long threadId, String threadName, String threadState,
			String threadStackTrace, String lockName, long lockOwnerId,
			String lockOwnerName, long blockedCount) {
		
		this.threadId = threadId;
		this.threadName = threadName;
		this.threadState = threadState;
		this.threadStackTrace = threadStackTrace;
		this.lockName = lockName;
		this.lockOwnerId = lockOwnerId;
		this.lockOwnerName = lockOwnerName;
		this.blockedCount = blockedCount;		
		threadCPUTimes = new ArrayList<Long>();
	}
	
	public void updateThreadInformation(ThreadInfo threadInfo, long threadCPUTime) {
		if (threadInfo != null) {
			setThreadId(threadInfo.getThreadId());
			setThreadName(threadInfo.getThreadName());
			setThreadState(threadInfo.getThreadState().toString() + (threadInfo.isInNative() ? ". Running In Native" : "."));
			setThreadStackTrace(threadInfo.getStackTrace());
			setLockName(threadInfo.getLockName());
			setLockOwnerId(threadInfo.getLockOwnerId());
			setLockOwnerName(threadInfo.getLockOwnerName());
			setBlockedCount(threadInfo.getBlockedCount());
			setBlockedTime(threadInfo.getBlockedTime());
			setWaitedCount(threadInfo.getWaitedCount());
			setWaitedTime(threadInfo.getWaitedTime());
			setThreadCPUTime(threadCPUTime);
		}
		else {
			Logger.log("ThreadInfo NULL !!!!!!!!!!!!!!!!!!!");
		}
	}

	public long getThreadId() {
		return threadId;
	}
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public String getThreadState() {
		return threadState;
	}
	public void setThreadState(String threadState) {
		this.threadState = threadState;
	}
	public String getThreadStackTrace() {
		return threadStackTrace;
	}
	public void setThreadStackTrace(StackTraceElement[] threadStackTrace) {
		String temp = "";
		for (int i = 0; i < threadStackTrace.length; i ++) {
			temp += threadStackTrace[i].toString() + "\n";
		}
		this.threadStackTrace = temp;
	}
	public String getLockName() {
		return lockName;
	}
	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
	public long getLockOwnerId() {
		return lockOwnerId;
	}
	public void setLockOwnerId(long lockOwnerId) {
		this.lockOwnerId = lockOwnerId;
	}
	public String getLockOwnerName() {
		return lockOwnerName;
	}
	public void setLockOwnerName(String lockOwnerName) {
		this.lockOwnerName = lockOwnerName;
	}
	public long getBlockedCount() {
		return blockedCount;
	}
	public void setBlockedCount(long blockedCount) {
		this.blockedCount = blockedCount;
	}
	public List<Long> getThreadCPUTime() {
		return threadCPUTimes;
	}
	public void setThreadCPUTime(long threadCPUTime) {
		this.threadCPUTimes.add(threadCPUTime);
	}
	public long getBlockedTime() {
		return blockedTime;
	}
	public void setBlockedTime(long blockedTime) {
		this.blockedTime = blockedTime;
	}
	public long getWaitedCount() {
		return waitedCount;
	}
	public void setWaitedCount(long waitedCount) {
		this.waitedCount = waitedCount;
	}
	public long getWaitedTime() {
		return waitedTime;
	}
	public void setWaitedTime(long waitedTime) {
		this.waitedTime = waitedTime;
	}

	public String toString() {
		long cpuTime = threadCPUTimes.size() > 0 ? threadCPUTimes.get(threadCPUTimes.size() - 1) : 0L;
		
		StringBuilder builder = new StringBuilder(1000);
		builder.append("Name: ").append(threadName).append("      [Thread Id: ").append(threadId).append("]").append("\n");
		builder.append("Current State: ").append(threadState).append("   [");
		if (threadState.equals(Thread.State.BLOCKED.toString())) {
			builder.append("Waiting For Lock ").append("'" + lockName + "'").append(" Owned By Thread ").append(lockOwnerName).append("]\n");
		}
		builder.append("Blocked Count: ").append(blockedCount).append("     [Blocked Time: ").append(blockedTime).append("]").append("\n");
		builder.append("Waited Count  : ").append(waitedCount).append("     [Waiting Time: ").append(waitedTime).append("]").append("\n");
		builder.append("CPU Usage    : ").append(cpuTime).append(" Nano Seconds").append("\n");
		builder.append("\n\n");
		builder.append("Stack Trace:").append("\n");
		builder.append(threadStackTrace.toString());
		
		return builder.toString();
	}
}
