package com.sc.hm.monitor.shared.mpool;

import java.lang.management.MemoryPoolMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sc.hm.monitor.shared.IMBeanObject;

public class MemoryPoolMBeanSharedObject implements IMBeanObject {

	private static final long serialVersionUID = 1L;

	private final Map<String, MemoryPool> memoryPoolMap = new HashMap<String, MemoryPool>();
	
	private final Map<String, Boolean> poolConfigMap = new HashMap<String, Boolean>();
	
	protected List<Long> timeList = new ArrayList<Long>();
	
	private boolean populate = true;
	
	private final Lock aLock = new ReentrantLock();
	private final Condition conditionObj = aLock.newCondition();	

	public MemoryPoolMBeanSharedObject() {}
	
	public void addMemoryPool(MemoryPoolMXBean mpoolMXBean) throws Exception, UndeclaredThrowableException {
		String memoryPoolName = mpoolMXBean.getName();
		if (!memoryPoolMap.containsKey(memoryPoolName)) {
			MemoryPool memoryPool = new MemoryPool();
			memoryPool.setMemoryName(memoryPoolName);
			memoryPool.setMemoryType(mpoolMXBean.getType());
			memoryPool.addUsage(mpoolMXBean.getUsage());
			memoryPool.setPoolManager(mpoolMXBean.getMemoryManagerNames());
			memoryPool.setUsageThresholdSupported(mpoolMXBean.isUsageThresholdSupported());
			memoryPool.setCollectionUsageThresholdSupported(mpoolMXBean.isCollectionUsageThresholdSupported());
			if (memoryPool.isUsageThresholdSupported()) {
				memoryPool.setUsageThresholdCount(mpoolMXBean.getUsageThresholdCount());
				memoryPool.setUsageThreshold(mpoolMXBean.getUsageThreshold());
			}
			if (memoryPool.isCollectionUsageThresholdSupported()) {
				memoryPool.setCollectionUsageThresholdCount(mpoolMXBean.getCollectionUsageThresholdCount());
				memoryPool.setCollectionUsageThreshold(mpoolMXBean.getCollectionUsageThreshold());
			}
			memoryPoolMap.put(memoryPoolName, memoryPool);
			poolConfigMap.put(memoryPoolName, false);
		}
		else {
			MemoryPool memoryPool = memoryPoolMap.get(memoryPoolName);
			memoryPool.addUsage(mpoolMXBean.getUsage());
			if (memoryPool.getUsageThreshold() > 0) {
				memoryPool.setUsageThresholdCount(mpoolMXBean.getUsageThresholdCount());
			}
			if (memoryPool.getCollectionUsageThreshold() > 0) {
				memoryPool.setCollectionUsageThresholdCount(mpoolMXBean.getCollectionUsageThresholdCount());
			}
			if (poolConfigMap.get(memoryPoolName)) {
				memoryPool.setMemoryPool(mpoolMXBean);
				poolConfigMap.put(memoryPoolName, false);
			}
			memoryPoolMap.put(memoryPoolName, memoryPool);
		}
	}

	public String[] getMemoryPoolNames() {
		Vector<String> poolNames = new Vector<String>(memoryPoolMap.size());
		for (Iterator<String> itr = memoryPoolMap.keySet().iterator(); itr.hasNext();) {
			poolNames.addElement(itr.next());
		}
		return (String[])poolNames.toArray(new String[poolNames.size()]);
	}

	public String getFirstMemoryPoolName() {
		String poolName = "";
		for (Iterator<String> itr = memoryPoolMap.keySet().iterator(); itr.hasNext();) {
			poolName = itr.next();
			break;
		}
		return poolName;
	}

	public synchronized boolean isChangedPoolConfig(String memoryPoolName) {
		return poolConfigMap.get(memoryPoolName);
	}

	public synchronized void setChangedPoolConfig(String memoryPoolName) {
		poolConfigMap.put(memoryPoolName, true);
	}

	public int getMemoryPoolCount() {
		return memoryPoolMap.size();
	}

	public Map<String, MemoryPool> getMemoryPoolMap() {
		return memoryPoolMap;
	}
	
	public void populateMemoryInfo(List<MemoryPoolMXBean> mpoolMXBeans) throws Exception, UndeclaredThrowableException {
		aLock.lock();		
		try {
			if (!populate) {
				try {
					conditionObj.await();
				}
				catch (InterruptedException inte) {
					inte.printStackTrace();
				}
			}
			for (MemoryPoolMXBean mpoolMXBean : mpoolMXBeans) {
				addMemoryPool(mpoolMXBean);
			}
			populate = false;
			conditionObj.signalAll();
		}
		finally {
			aLock.unlock();
		}
	}

	public MemoryPool useMemoryInfo(String mpoolName) throws Exception {
		aLock.lock();
		try {
			if (populate) {
				try {
					conditionObj.await();
				}
				catch (InterruptedException inte) {
					inte.printStackTrace();
				}
			}
			return getMemoryPool(mpoolName);
		}
		finally {
			aLock.unlock();
		}
	}
	
	public void completedMemoryInfo(String mpoolName) throws Exception {
		aLock.lock();
		try {
			populate = true;
			conditionObj.signalAll();
		}
		finally {
			aLock.unlock();
		}
	}

	public MemoryPool getMemoryPool(String mpoolName) {
		return memoryPoolMap.get(mpoolName);
	}

	public void removeMemoryPool(String mpool) {
		if (memoryPoolMap.containsKey(mpool)) {
			memoryPoolMap.remove(mpool);
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
