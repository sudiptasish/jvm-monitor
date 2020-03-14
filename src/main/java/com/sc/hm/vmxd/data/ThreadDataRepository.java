package com.sc.hm.vmxd.data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;

import com.sc.hm.vmxd.data.thread.ThreadData;
import com.sc.hm.vmxd.data.thread.ThreadUsageData;

/**
 * Repository to store the thread details of remote JVM.
 * 
 * @author Sudiptasish Chanda
 */
public class ThreadDataRepository extends DataRepository {
	
	private ThreadData threadData = null;
	private Map<Long, ThreadUsageData> usageMapping = new HashMap<>();

	private Map<String, Method> methods = new HashMap<>();

	public ThreadDataRepository(String name) {
		super(name);
	}

    @Override
	public void initializeRepository(Object data) throws Exception {
		Method method = null;
		threadData = new ThreadData();
        
        method = threadData.getClass().getMethod("setCurrentThreadCpuTime", Number.class);
        methods.put("CurrentThreadCpuTime", method);
        
        method = threadData.getClass().getMethod("setDaemonThreadCount", Number.class);
        methods.put("DaemonThreadCount", method);
        
        method = threadData.getClass().getMethod("setPeakThreadCount", Number.class);
        methods.put("PeakThreadCount", method);
        
        method = threadData.getClass().getMethod("setTotalStartedThreadCount", Number.class);
        methods.put("TotalStartedThreadCount", method);
        
        method = threadData.getClass().getMethod("setThreadCount", Number.class);
        methods.put("ThreadCount", method);
        
		addData("ThreadMX", data);
		
		setInitializedRepository(true);
	}

    @Override
	public void addData(String name, Object data) throws Exception {
	    if (data == null) {
	        return;
	    }
		AttributeList attributeList = (AttributeList)data;
		
		for (int i = 1; i < attributeList.size(); i ++) {
			Attribute attribute = (Attribute)attributeList.get(i);
			methods.get(attribute.getName()).invoke(threadData, (Number)attribute.getValue());
		}
	}
	
	/**
	 * Return the generic thread data
	 * @return ThreadData
	 */
	public ThreadData getThreadData() {
		return threadData;
	}
	
	/**
	 * Check to see if the thread as identified by this id has any
	 * usage mapping or not.
	 * @param id
	 * @return boolean
	 */
	public boolean isExistUsageData(Long id) {
	    return usageMapping.containsKey(id);
	}
	
	/**
	 * Initialize the thread usage map.
	 * @param id
	 * @param name
	 */
	public void initUsage(Long id, String name) {
	    ThreadUsageData usageData = new ThreadUsageData();
	    usageData.setId(id);
	    usageData.setName(name);
	    
	    usageMapping.put(id, usageData);
	}
	
	/**
	 * Add the CPU Usage info for this thread as identified by this id.
	 * @param id
	 * @param cpuTime
	 */
	public void addCpuUsage(Long id, Long cpuTime) {
	    ThreadUsageData usageData = usageMapping.get(id);
	    if (usageData != null && cpuTime != 0) {
	        usageData.addCpuTime(cpuTime);
	    }
	}
    
    /**
     * Add the User Usage info for this thread as identified by this id.
     * @param id
     * @param userTime
     */
    public void addUserUsage(Long id, Long userTime) {
        ThreadUsageData usageData = usageMapping.get(id);
        if (usageData != null && userTime != 0) {
            usageData.addUserTime(userTime);
        }
    }
	
    /**
     * Return the thread usage mapping.
     * @return Map
     */
	public Map<Long, ThreadUsageData> getThreadUsageData() {
	    return usageMapping;
	}
	
	/**
	 * Return the collection of all usages.
	 * @return List
	 */
	public List<ThreadUsageData> allUsages() {
	    return new ArrayList<>(usageMapping.values());
	}
}
