package com.sc.hm.monitor.shared.memory;

import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.sc.hm.monitor.shared.IMBeanObject;

public class MemoryMBeanSharedObject implements IMBeanObject {
	
	public static final String HEAP_MEMORY = "HEAP_MEMORY";
	public static final String NONHEAP_MEMORY = "NONHEAP_MEMORY";

	private long objectPendingFinalization = 0L;
	
	private final Map<String, Memory> memoryMap = new HashMap<String, Memory>();
	
	private List<Long> timeList = new ArrayList<Long>();

	public MemoryMBeanSharedObject() {}
	
	public void addMemory(String name, MemoryUsage usage) throws Exception, UndeclaredThrowableException {
		if (!memoryMap.containsKey(name)) {
			Memory memory = new Memory();
			memory.addUsage(usage);
			memory.setMemoryName(name);
			memory.setMemoryType(name.equals(HEAP_MEMORY) ? MemoryType.HEAP : MemoryType.NON_HEAP);
			memoryMap.put(name, memory);
		}
		else {
			Memory memory = memoryMap.get(name);
			memory.addUsage(usage);
			memoryMap.put(name, memory);
		}
	}

	public String[] getMemoryNames() {
		Vector<String> memoryNames = new Vector<String>(memoryMap.size());
		for (Iterator<String> itr = memoryMap.keySet().iterator(); itr.hasNext();) {
			memoryNames.addElement(itr.next());
		}
		return (String[])memoryNames.toArray(new String[memoryNames.size()]);
	}

	public long getObjectPendingFinalization() {
		return objectPendingFinalization;
	}

	public void setObjectPendingFinalization(long objectPendingFinalization) {
		this.objectPendingFinalization = objectPendingFinalization;
	}

	public int getMemoryPoolCount() {
		return memoryMap.size();
	}

	public Map<String, Memory> getMemoryPoolMap() {
		return memoryMap;
	}

	public Memory getMemory(String mName) {
		return memoryMap.get(mName);
	}

	public void removeMemoryPool(String mName) {
		if (memoryMap.containsKey(mName)) {
			memoryMap.remove(mName);
		}
	}

	public long getStartTime() {
		return timeList.get(0);
	}

	public void addTime(long currentTime) {
		timeList.add(currentTime);
	}
	
	public List<Long> getTimeList() {
		return timeList;
	}
}
