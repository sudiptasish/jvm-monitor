package com.sc.hm.monitor.shared.gbcollector;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.sc.hm.monitor.shared.IMBeanObject;

public class GBCollectorMBeanSharedObject implements IMBeanObject {
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, GBCollector> gbMap = new HashMap<String, GBCollector>();
	
	public GBCollectorMBeanSharedObject() {}
	
	public void updateGarbageCollector(GarbageCollectorMXBean gbMXBean) throws Exception, UndeclaredThrowableException {
		String name = gbMXBean.getName();
		if (!gbMap.containsKey(name)) {
			GBCollector gbCollectorInfo = new GBCollector(name, gbMXBean.getMemoryPoolNames(), gbMXBean.getCollectionCount(), gbMXBean.getCollectionTime());
			gbCollectorInfo.setMemoryPools(gbMXBean.getMemoryPoolNames());
			gbCollectorInfo.addTime(System.currentTimeMillis());
			gbMap.put(name, gbCollectorInfo);
		}
		else {
			GBCollector gbCollectorInfo = gbMap.get(name);
			if (gbMXBean.getCollectionCount() > gbCollectorInfo.getTotalCollectionCount()) {
				gbCollectorInfo.setCollectionCount(gbMXBean.getCollectionCount());
				gbCollectorInfo.setCollectionTime(gbMXBean.getCollectionTime());
				gbCollectorInfo.addTime(System.currentTimeMillis());
				gbMap.put(name, gbCollectorInfo);
			}
		}
	}
	
	public GBCollector[] getGarbageCollectorByMemory(String mName) {
		Vector<GBCollector> vector = new Vector<GBCollector>();
		synchronized (this) {
			for (Iterator<GBCollector> itr = gbMap.values().iterator(); itr.hasNext();) {
				GBCollector gbCollector = itr.next();
				String[] memoryPoolNames = gbCollector.getMemoryPools();
				for (byte i = 0; i < memoryPoolNames.length; i ++) {
					if (mName.equalsIgnoreCase(memoryPoolNames[i])) {
						vector.addElement(gbCollector);
					}
				}
			}
		}
		return (GBCollector[])vector.toArray(new GBCollector[vector.size()]);
	}
	
	public GBCollector[] getAllGarbageCollectors() {
		return (GBCollector[])gbMap.values().toArray(new GBCollector[gbMap.values().size()]);
	}
	
	public GBCollector getGarbageCollectorByName(String name) {
		GBCollector[] gbCollectors = getAllGarbageCollectors();
		for (byte i = 0; i < gbCollectors.length; i ++) {
			if (name.equalsIgnoreCase(gbCollectors[i].getCollectorName())) {
				return gbCollectors[i];
			}
		}
		return null;
	}
	
	public String[] getGarbageCollectorNames() {		
		GBCollector[] gbCollectors = getAllGarbageCollectors();
		String names[] = new String[gbCollectors.length];
		for (byte i = 0; i < gbCollectors.length; i ++) {
			names[i] = gbCollectors[i].getCollectorName();
		}
		return names;
	}
}
