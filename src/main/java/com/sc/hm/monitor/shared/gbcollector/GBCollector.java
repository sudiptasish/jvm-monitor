package com.sc.hm.monitor.shared.gbcollector;

import java.util.ArrayList;
import java.util.List;

public class GBCollector {

	private String collectorName = "";
	private String[] memoryPools = null;
	private List<Long> collectionCount = null;
	private List<Long> collectionTime = null;
	private long totalCollectionCount = 0L;
	private long totalCollectionTime = 0L;
	private long minCollectionCount = Long.MAX_VALUE;
	private long maxCollectionCount = 0L;
	
	private List<Long> timeList = new ArrayList<Long>();
	
	public GBCollector() {
		collectionCount = new ArrayList<Long>();
		collectionTime = new ArrayList<Long>();
	}
	
	public GBCollector(String collectorName, String[] memoryPools, long collectionCount, long collectionTime) {
		this.collectionCount = new ArrayList<Long>();
		this.collectionTime = new ArrayList<Long>();
		this.collectorName = collectorName;
		this.memoryPools = memoryPools;
		this.collectionCount.add(collectionCount);
		this.collectionTime.add(collectionTime);
	}

	public List<Long> getCollectionCount() {
		return collectionCount;
	}
	public void setCollectionCount(long collectionCount) {
		long currentCollectionCount = collectionCount - totalCollectionCount;
		this.collectionCount.add(currentCollectionCount);
		setMaxCollectionCount(currentCollectionCount);
		setMinCollectionCount(currentCollectionCount);
		setTotalCollectionCount(collectionCount);
	}
	public List<Long> getCollectionTime() {
		return collectionTime;
	}
	public void setCollectionTime(long collectionTime) {		
		this.collectionTime.add(collectionTime - totalCollectionTime);
		setTotalCollectionTime(collectionTime);
	}
	public String getCollectorName() {
		return collectorName;
	}
	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}
	public String[] getMemoryPools() {
		return memoryPools;
	}
	public void setMemoryPools(String[] memoryPools) {
		this.memoryPools = memoryPools;
	}
	public long getTotalCollectionCount() {
		return totalCollectionCount;
	}
	public void setTotalCollectionCount(long totalCollectionCount) {
		this.totalCollectionCount = totalCollectionCount;
	}
	public long getTotalCollectionTime() {
		return totalCollectionTime;
	}
	public void setTotalCollectionTime(long totalCollectionTime) {
		this.totalCollectionTime = totalCollectionTime;
	}
	public long getMaxCollectionCount() {
		return maxCollectionCount;
	}
	public void setMaxCollectionCount(long maxCollectionCount) {
		if (maxCollectionCount > this.maxCollectionCount) {
			this.maxCollectionCount = maxCollectionCount;
		}
	}
	public long getMinCollectionCount() {
		return minCollectionCount;
	}
	public void setMinCollectionCount(long minCollectionCount) {
		if (minCollectionCount < this.minCollectionCount) {
			this.minCollectionCount = minCollectionCount;
		}
	}
	
	public void addTime(long current_time) {
		timeList.add(current_time);
	}
	
	public List<Long> getTimeList() {
		return timeList;
	}
	public String toString() {
		StringBuilder builder = new StringBuilder(500);
		builder.append("[Name: ").append(this.collectorName);		
		builder.append(". Collections: ").append(this.totalCollectionCount);
		builder.append(". Time: ").append(this.totalCollectionTime).append(" Millis]");
		
		return builder.toString();
	}
}
