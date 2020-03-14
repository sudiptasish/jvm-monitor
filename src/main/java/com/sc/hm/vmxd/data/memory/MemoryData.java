package com.sc.hm.vmxd.data.memory;

import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class MemoryData {
	
	protected String name = "";
	protected String type = "";

	protected List<Date> dateList = new ArrayList<>(); 
	protected List<MemoryUsage> usageList = new ArrayList<>();
	
	private long used_min = Long.MAX_VALUE;
	private long used_max = 0L;
	
	private static int objectPendingFinalizationCount = 0;
	
	public MemoryData(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Date> getDateList() {
		return dateList;
	}

	public List<MemoryUsage> getUsageList() {
		return usageList;
	}
	
	public void setUsage(MemoryUsage usage) {
		usageList.add(usage);
		dateList.add(new Date());
		
		if (usage.getUsed() < used_min) {
			used_min = usage.getUsed();
		}
		if (usage.getUsed() > used_max) {
			used_max = usage.getUsed();
		}
	}
	
	public long getUsed_min() {
		return used_min;
	}

	public long getUsed_max() {
		return used_max;
	}

	public long getMax() {
		if (usageList.size() == 0) {
			return 0;
		}
		return usageList.get(usageList.size() - 1).getMax();
	}
	
	public long getCurrentUsed() {
		if (usageList.size() == 0) {
			return 0;
		}
		return usageList.get(usageList.size() - 1).getUsed();
	}
	
	public MemoryUsage getCurrentUsage() {
		if (usageList.size() == 0) {
			return new MemoryUsage(0, 0, 0, 0);
		}
		return usageList.get(usageList.size() - 1);
	}
	
	public int getUsageCount() {
		return usageList.size();
	}

	public int getObjectPendingFinalizationCount() {
		return objectPendingFinalizationCount;
	}

	public void setObjectPendingFinalizationCount(int objectPendingFinalizationCount) {
		MemoryData.objectPendingFinalizationCount = objectPendingFinalizationCount;
	}
}
