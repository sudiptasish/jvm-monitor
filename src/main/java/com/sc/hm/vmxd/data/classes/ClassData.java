package com.sc.hm.vmxd.data.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class ClassData {

	private String name = "";
	private final List<Long> totalLoadedClassCount = new ArrayList<>();
	private final List<Long> unloadedClassCount = new ArrayList<>();
	private final List<Long> loadedClassCount = new ArrayList<>();
	private final List<Date> timeList = new ArrayList<>();
	
	private long min_count = Long.MAX_VALUE;
	private long max_count = 0L;
	
	public ClassData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setTotalLoadedClassCount(Long count) {
		totalLoadedClassCount.add(count);
		if (count < min_count) {
			min_count = count;
		}
		if (count > max_count) {
			max_count = count;
		}
	}
	
	public void setUnloadedClassCount(Long count) {
		unloadedClassCount.add(count);
		if (count < min_count) {
			min_count = count;
		}
		if (count > max_count) {
			max_count = count;
		}
	}
	
	public void setLoadedClassCount(Integer count) {
		loadedClassCount.add(new Long(count));
		timeList.add(new Date());
		if (count < min_count) {
			min_count = count;
		}
		if (count > max_count) {
			max_count = count;
		}
	}

	public long getMin_count() {
		return min_count;
	}

	public long getMax_count() {
		return max_count;
	}

	public List<Long> getTotalLoadedClassCount() {
		return totalLoadedClassCount;
	}

	public List<Long> getUnloadedClassCount() {
		return unloadedClassCount;
	}

	public List<Long> getLoadedClassCount() {
		return loadedClassCount;
	}

	public List<Date> getTimeList() {
		return timeList;
	}
	
	public long currentLoadedClass() {
		if (loadedClassCount.size() == 0) {
			return 0;
		}
		return loadedClassCount.get(loadedClassCount.size() - 1);
	}
	
	public long totalLoadedClass() {
		if (totalLoadedClassCount.size() == 0) {
			return 0;
		}
		return totalLoadedClassCount.get(totalLoadedClassCount.size() - 1);
	}
	
	public long currentUnLoadedClass() {
		if (unloadedClassCount.size() == 0) {
			return 0;
		}
		return unloadedClassCount.get(unloadedClassCount.size() - 1);
	}
}
