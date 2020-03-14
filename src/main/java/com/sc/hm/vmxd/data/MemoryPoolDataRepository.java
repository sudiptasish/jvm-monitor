package com.sc.hm.vmxd.data;

import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.openmbean.CompositeData;

import com.sc.hm.vmxd.data.memory.MemoryPoolData;

/**
 * Repository for holding the memory utilization data.
 * The JMX thread periodically connects to the remote VM to collect the memory
 * pool utilization data only too store it in this repository.
 * 
 * @author Sudiptasish Chanda
 */
public class MemoryPoolDataRepository extends DataRepository {
	
	private Map<String, MemoryPoolData> memoryPoolMap = new HashMap<>();
	
	private Map<String, Method> mpoolMethods = new HashMap<>();

	public MemoryPoolDataRepository(String name) {
		super(name);
	}
	
    @Override
	public void initializeRepository(Object obj) throws Exception {
		Method method = null;
		
		AttributeList attributeList = (AttributeList)obj;
        Attribute nameAttr = (Attribute)attributeList.get(0);
        Attribute typeAttr = (Attribute)attributeList.get(1);
        
		MemoryPoolData mPoolData = new MemoryPoolData(nameAttr.getValue().toString(), typeAttr.getValue().toString());
		
		method = mPoolData.getClass().getMethod("setUsage", MemoryUsage.class);
		mpoolMethods.put("Usage", method);
		
		method = mPoolData.getClass().getDeclaredMethod("setUsageThreshold", long.class);
        mpoolMethods.put("UsageThreshold", method);
        
        method = mPoolData.getClass().getDeclaredMethod("setUsageThresholdCount", long.class);
        mpoolMethods.put("UsageThresholdCount", method);
        
        method = mPoolData.getClass().getDeclaredMethod("setCollectionUsageThreshold", long.class);
        mpoolMethods.put("CollectionUsageThreshold", method);
        
        method = mPoolData.getClass().getDeclaredMethod("setCollectionUsageThresholdCount", long.class);
        mpoolMethods.put("CollectionUsageThresholdCount", method);
        
        memoryPoolMap.put(mPoolData.getName(), mPoolData);
        
        addData(mPoolData.getName(), obj);
	}
	
	public void addData(String name, Object data) throws Exception {
	    if (data == null) {
	        return;
	    }
		AttributeList attributeList = (AttributeList)data;
		MemoryPoolData mpoolData = memoryPoolMap.get(name);
		// First attribute is name.
		// Second attribute is type.
		// Third attribute is memory usage.
		addMemoryData(mpoolData, (Attribute)attributeList.get(2));
			
		for (int i = 3; i < attributeList.size(); i ++) {
			Attribute attribute = (Attribute)attributeList.get(i);
			mpoolMethods.get(attribute.getName()).invoke(mpoolData, (Long)attribute.getValue());
		}
	}
	
	private void addMemoryData(MemoryPoolData mpoolData, Attribute attribute) {
		CompositeData cData = (CompositeData)attribute.getValue();
		MemoryUsage usage = MemoryUsage.from(cData);
		try {
			mpoolMethods.get(attribute.getName()).invoke(mpoolData, usage);
		}
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public MemoryPoolData getMemoryPoolData(String memoryPoolName) {
		if (memoryPoolMap.containsKey(memoryPoolName)) {
			return memoryPoolMap.get(memoryPoolName);
		}
		return null;
	}
	
	public String[] getMemoryPoolNames() {
		return (String[])memoryPoolMap.keySet().toArray(new String[memoryPoolMap.size()]);
	}
}
