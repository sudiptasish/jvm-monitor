package com.sc.hm.monitor.shared.memory;

import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

public class Memory {

	private String memoryName = "";
	private MemoryType memoryType = null;
	
	protected long init = 0L;
	protected long max = 0L;
	protected long committed = 0L;
	protected long used = 0L;
	
	protected long used_max = 0L;
	protected long used_min = Long.MAX_VALUE;
	
	protected List<Long> poolUsage = null;
	protected List<Long> poolCommitted = null;
	
	protected long committed_max = 0L;
	protected long committed_min = Long.MAX_VALUE;
	
	public Memory() {
		poolUsage = new ArrayList<Long>();
		poolCommitted = new ArrayList<Long>();
	}

	public void addUsage(MemoryUsage usage) throws Exception, UndeclaredThrowableException {
		init = usage.getInit();
		max = usage.getMax();
		committed = usage.getCommitted();
		used = usage.getUsed();

		poolUsage.add(used);
		poolCommitted.add(committed);
		
		setUsed_max();
		setUsed_min();
		
		setCommitted_max();
		setCommitted_min();
	}
	
	public String getMemoryName() {
		return memoryName;
	}

	public void setMemoryName(String memoryName) {
		this.memoryName = memoryName;
	}

	public long getCommitted() {
		return committed;
	}

	public void setCommitted(long committed) {
		this.committed = committed;
	}

	public long getInit() {
		return init;
	}

	public void setInit(long init) {
		this.init = init;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public MemoryType getMemoryType() {
		return memoryType;
	}

	public void setMemoryType(MemoryType memoryType) {
		this.memoryType = memoryType;
	}

	public List<Long> getPoolUsage() {
		return poolUsage;
	}

	public List<Long> getPoolCommitted() {
		return poolCommitted;
	}

	public void setPoolUsage(List<Long> poolUsage) {
		this.poolUsage = poolUsage;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public long getUsed_max() {
		return used_max;
	}

	public void setUsed_max() {
		if (used > used_max) {
			used_max = used;
		}
	}

	public void setUsed_min() {
		if (used < used_min) {
			used_min = used;
		}
	}

	public long getUsed_min() {
		return used_min;
	}

	public long getCommitted_max() {
		return committed_max;
	}

	public void setCommitted_max() {
		if (committed > committed_max) {
			committed_max = committed;
		}
	}

	public void setCommitted_min() {
		if (committed < committed_min) {
			committed_min = committed;
		}
	}

	public long getCommitted_min() {
		return committed_min;
	}
}
