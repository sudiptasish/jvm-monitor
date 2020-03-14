package com.sc.hm.vmxd.data;

import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.openmbean.CompositeData;

import com.sc.hm.vmxd.data.memory.MemoryData;

/**
 * Repository for holding the memory utilization data.
 * The JMX thread periodically connects to the remote VM to collect the memory
 * utilization data only too store it in this repository.
 * 
 * @author Sudiptasish Chanda
 */
public class MemoryDataRepository extends DataRepository {
	
	public static final String HEAP_MEMORY = "Heap Memory";
	public static final String NONHEAP_MEMORY = "NonHeap Memory";

	private Map<String, MemoryData> memoryMap = new HashMap<>();
	
	private Map<String, Method> methods = new HashMap<>();

	public MemoryDataRepository(String name) {
		super(name);
	}

    @Override
	public void initializeRepository(Object obj) throws Exception {
		Method method = null;
		
		MemoryData memoryData = new MemoryData(HEAP_MEMORY, "HEAP");
		memoryMap.put(HEAP_MEMORY, memoryData);
		
		memoryData = new MemoryData(NONHEAP_MEMORY, "NON_HEAP");
		memoryMap.put(NONHEAP_MEMORY, memoryData);
		
		method = memoryData.getClass().getMethod("setUsage", MemoryUsage.class);
		methods.put("HeapMemoryUsage", method);
		
		method = memoryData.getClass().getMethod("setUsage", MemoryUsage.class);
		methods.put("NonHeapMemoryUsage", method);
		
		method = memoryData.getClass().getMethod("setObjectPendingFinalizationCount", int.class);
		methods.put("ObjectPendingFinalizationCount", method);
		
		addData("Memory", obj);
		setInitializedRepository(true);
	}

    @Override
	public void addData(String name, Object data) throws Exception {
	    if (data == null) {
	        return;
	    }
		AttributeList attributeList = (AttributeList)data;
		MemoryData memoryData = memoryMap.get(NONHEAP_MEMORY);
		addMemoryData(memoryData, (Attribute)attributeList.get(1));
		
		memoryData = memoryMap.get(HEAP_MEMORY);
		addMemoryData(memoryData, (Attribute)attributeList.get(0));
		
		for (int i = 2; i < attributeList.size(); i ++) {
			Attribute attribute = (Attribute)attributeList.get(i);
			methods.get(attribute.getName()).invoke(memoryData, (Integer)attribute.getValue());
		}
	}
	
	private void addMemoryData(MemoryData mpoolData, Attribute attribute) {
		CompositeData cData = (CompositeData)attribute.getValue();
		MemoryUsage usage = MemoryUsage.from(cData);
		try {
			methods.get(attribute.getName()).invoke(mpoolData, usage);
		}
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public long getObjectPendingFinalizationCount() {
		return memoryMap.get(HEAP_MEMORY).getObjectPendingFinalizationCount();
	}
	
	public MemoryData getMemoryData(String memoryPoolName) {
		if (memoryMap.containsKey(memoryPoolName)) {
			return memoryMap.get(memoryPoolName);
		}
		return null;
	}
	
	public String[] getMemoryNames() {
		return new String[] {HEAP_MEMORY, NONHEAP_MEMORY};
	}
}
