package com.sc.hm.monitor.shared.classes;

import java.util.ArrayList;
import java.util.List;

public class ClassTypeInfo {

	private String typeName = "";
	private long minType = Long.MAX_VALUE;
	private long maxType = 0;
	
	private List<Long> countList = null;

	public ClassTypeInfo(String type) {
		typeName = type;
		countList = new ArrayList<Long>();
	}
	
	public void updateTypeInfo(long currentCount) {
		countList.add(currentCount);
		setMaxType(currentCount);
		setMinType(currentCount);
	}

	public List<Long> getCount() {
		return countList;
	}

	public void setCount(List<Long> count) {
		this.countList = count;
	}

	public long getMaxType() {
		return maxType;
	}

	public void setMaxType(long maxType) {
		if (maxType > this.maxType) {
			this.maxType = maxType;
		}
	}

	public long getMinType() {
		return minType;
	}

	public void setMinType(long minType) {
		if (minType < this.minType) {
			this.minType = minType;
		}
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
