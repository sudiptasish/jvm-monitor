package com.sc.hm.monitor.shared.mpool;

import java.lang.management.MemoryPoolMXBean;

import com.sc.hm.monitor.shared.memory.Memory;

public class MemoryPool extends Memory {

	private boolean usageThresholdSupported = false;
	private boolean collectionUsageThresholdSupported = false;
	private long usageThreshold = -9L;
	private long usageThresholdCount = -9L;
	private long collectionUsageThreshold = -9L;
	private long collectionUsageThresholdCount = -9L;
	
	private String[] poolManager = null;

	public MemoryPool() {
		super();
	}

	public void setMemoryPool(MemoryPoolMXBean mPool) {
		if (usageThresholdSupported) {
			setUsageThreshold(mPool.getUsageThreshold());
		}
		if (collectionUsageThresholdSupported) {
			setCollectionUsageThreshold(mPool.getCollectionUsageThreshold());
		}		
	}

	public boolean isCollectionUsageThresholdSupported() {
		return collectionUsageThresholdSupported;
	}

	public void setCollectionUsageThresholdSupported(boolean collectionUsageThresholdSupported) {
		this.collectionUsageThresholdSupported = collectionUsageThresholdSupported;
	}

	public boolean isUsageThresholdSupported() {
		return usageThresholdSupported;
	}

	public void setUsageThresholdSupported(boolean usageThresholdSupported) {
		this.usageThresholdSupported = usageThresholdSupported;
	}

	public String[] getPoolManager() {
		return poolManager;
	}

	public void setPoolManager(String[] poolManager) {
		this.poolManager = poolManager;
	}

	public long getCollectionUsageThreshold() {
		return collectionUsageThreshold;
	}

	public void setCollectionUsageThreshold(long collectionUsageThreshold) {
		this.collectionUsageThreshold = collectionUsageThreshold;
	}

	public long getCollectionUsageThresholdCount() {
		return collectionUsageThresholdCount;
	}

	public void setCollectionUsageThresholdCount(long collectionUsageThresholdCount) {
		this.collectionUsageThresholdCount = collectionUsageThresholdCount;
	}

	public long getUsageThreshold() {
		return usageThreshold;
	}

	public void setUsageThreshold(long usageThreshold) {
		this.usageThreshold = usageThreshold;
	}

	public long getUsageThresholdCount() {
		return usageThresholdCount;
	}

	public void setUsageThresholdCount(long usageThresholdCount) {
		this.usageThresholdCount = usageThresholdCount;
	}
}
