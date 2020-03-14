package com.sc.hm.vmxd.data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;

import com.sc.hm.vmxd.data.gbcollector.GarbageCollectorData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Repository to hold the garbage collection info about the remote JVM.
 * It stores the collectionn details at memory pool level.
 * 
 * @author Sudiptasish Chanda
 */
public class GarbageCollectorDataRepository extends DataRepository {

	private final Map<String, GarbageCollectorData> gbCollectorMap = new HashMap<>();
	
	private final Map<String, Method> methods = new HashMap<>();

	public GarbageCollectorDataRepository(String name) {
		super(name);		
	}
	
    @Override
	public void initializeRepository(Object obj) throws Exception {
		Method method = null;
		AttributeList attributeList = (AttributeList)obj;
        Attribute nameAttr = (Attribute)attributeList.get(0);
        Attribute poolAttr = (Attribute)attributeList.get(1);
		
		GarbageCollectorData gbCollectorData = new GarbageCollectorData(nameAttr.getValue().toString());
		method = gbCollectorData.getClass().getMethod("setCollectionCount", long.class);
		methods.put("CollectionCount", method);
		
		method = gbCollectorData.getClass().getMethod("setCollectionTime", long.class);
		methods.put("CollectionTime", method);
		
		gbCollectorData.setMemoryPools((String[])poolAttr.getValue());
		
		gbCollectorMap.put(gbCollectorData.getName(), gbCollectorData);
		
		addData(gbCollectorData.getName(), attributeList);
	}
	
    @Override
	public void addData(String name, Object data) throws Exception {
	    if (data == null) {
	        return;
	    }
		AttributeList attributeList = (AttributeList)data;
		GarbageCollectorData gbCollectorData = gbCollectorMap.get(name);
			
		// First attribute is garbage collector name.
		// Second attribute is memory pool on which it is operating on.
		// These two attributes are fixed, and we don't want to populate them everytime.
		for (int i = 2; i < attributeList.size(); i ++) {
			Attribute attribute = (Attribute)attributeList.get(i);
			methods.get(attribute.getName()).invoke(gbCollectorData, (Long)attribute.getValue());
		}
	}
	
	public GarbageCollectorData getGarbageCollectorData(String gbCollectorName) {
		if (gbCollectorMap.containsKey(gbCollectorName)) {
			return gbCollectorMap.get(gbCollectorName);
		}
		return null;
	}
	
	public String[] getGarbageCollectorNames() {
		return (String[])gbCollectorMap.keySet().toArray(new String[gbCollectorMap.size()]);
	}
	
	public String[] getAssociatedMemoryPoolNames(String gbCollectorName) {
		if (gbCollectorMap.containsKey(gbCollectorName)) {
			String[] memoryPoolNames = gbCollectorMap.get(gbCollectorName).getMemoryPools();
			return memoryPoolNames;
		}
		return null;
	}

    /**
     * Return the list of garbage collector and their collection details for 
     * the given memory name.
     * If no garbage collector active in this memory/memory pool, then the returned
     * collectionn will be empty.
     * 
     * @param memoryName
     * @return GarbageCollectorData[]
     */
	public Collection<GarbageCollectorData> getGarbageCollectorByMemory(String memoryName) {
		List<GarbageCollectorData> list = new ArrayList<>();
        
		if (memoryName.equals(MemoryDataRepository.HEAP_MEMORY)
            || memoryName.equals(MemoryDataRepository.NONHEAP_MEMORY)) {
			return gbCollectorMap.values();
		}
		else {
			for (Map.Entry<String, GarbageCollectorData> me : gbCollectorMap.entrySet()) {
				GarbageCollectorData gbCollector = me.getValue();
				if (gbCollector.isAssociatedMemoryPool(memoryName)) {
					list.add(gbCollector);
				}
			}
		}
		return list;
	}
}
