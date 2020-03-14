package com.sc.hm.vmxd.data.gbcollector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class GarbageCollectorData {

	private String name = "";
	private String[] memoryPools = null;
	
	private final List<Date> dateList = new ArrayList<>(); 
	private final List<Long> collectionList = new ArrayList<>();
	
	private long totalCollectionCount = 0L;
	private long currentCollectionTime = 0L;
	private long totalCollectionTime = 0L;
	private long minCollectionCount = Long.MAX_VALUE;
	private long maxCollectionCount = 0L;
	
	public GarbageCollectorData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getMemoryPools() {
		return memoryPools;
	}

	public void setMemoryPools(String[] memoryPools) {
		this.memoryPools = memoryPools;
	}
	
	public void setCollectionCount(long collection) {
		if (collection > totalCollectionCount) {
			long currentCollectionCount = collection - totalCollectionCount;
			collectionList.add(currentCollectionCount);
			dateList.add(new Date());
			totalCollectionCount = collection;
			
			if (currentCollectionCount < minCollectionCount) {
				minCollectionCount = currentCollectionCount;
			}
			if (currentCollectionCount > maxCollectionCount) {
				maxCollectionCount = currentCollectionCount;
			}
		}
	}

	public long getTotalCollectionCount() {
		return totalCollectionCount;
	}

	public long getTotalCollectionTime() {
		return totalCollectionTime;
	}

	public void setCollectionTime(long collectionTime) {
		if (collectionTime > this.totalCollectionTime) {
			this.currentCollectionTime = collectionTime - this.totalCollectionTime;		
			this.totalCollectionTime = collectionTime;
		}
	}

	public long getCurrentCollectionTime() {
		return currentCollectionTime;
	}

	public long getMinCollectionCount() {
		return minCollectionCount;
	}

	public long getMaxCollectionCount() {
		return maxCollectionCount;
	}

	public List<Date> getDateList() {
		return dateList;
	}

	public List<Long> getCollectionList() {
		return collectionList;
	}
	
	public boolean isAssociatedMemoryPool(String mpoolName) {
		for (String memoryPool : memoryPools) {
			if (memoryPool.equalsIgnoreCase(mpoolName)) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder(500);
		builder.append("[Name: ").append(this.name);		
		builder.append(". Collections: ").append(this.totalCollectionCount);
		builder.append(". Time: ").append(this.totalCollectionTime).append(" Millis]");
		
		return builder.toString();
	}
	
	public String toCustomString() {
		StringBuilder builder = new StringBuilder(500);
		builder.append("Name: ").append(this.name);		
		builder.append("\nCollections: ").append(this.totalCollectionCount);
		builder.append("\nTime: ").append(this.totalCollectionTime).append(" Millis]");
		
		return builder.toString();
	}
}
