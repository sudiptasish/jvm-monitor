package com.sc.hm.vmxd.data.memory;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class MemoryPoolData extends MemoryData {
	
	private boolean usageThresholdSupported = false;
	private boolean collectionUsageThresholdSupported = false;
	
	private long usageThreshold = -9L;
	private long usageThresholdCount = -9L;
	private long collectionUsageThreshold = -9L;
	private long collectionUsageThresholdCount = -9L;

	public MemoryPoolData(String name, String type) {
		super(name, type);
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

	public boolean isUsageThresholdSupported() {
		return usageThresholdSupported;
	}

	public void setUsageThresholdSupported(boolean usageThresholdSupported) {
		this.usageThresholdSupported = usageThresholdSupported;
	}

	public boolean isCollectionUsageThresholdSupported() {
		return collectionUsageThresholdSupported;
	}

	public void setCollectionUsageThresholdSupported(boolean collectionUsageThresholdSupported) {
		this.collectionUsageThresholdSupported = collectionUsageThresholdSupported;
	}
}
