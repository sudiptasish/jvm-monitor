package com.sc.hm.vmxd.process;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.ConnectException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.management.AttributeList;
import javax.management.ObjectName;

import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;

public class MemoryPoolMBeanSynchProcess extends AbstractMBeanSynchProcess {
	
	private String[] poolNames = null;
	private String[][] poolAttributes = null;
	private ObjectName[] names = null;
	
	public MemoryPoolMBeanSynchProcess(String applicationId, CountDownLatch countDownLatch) {
		this(applicationId, countDownLatch, null);
	}
	
	public MemoryPoolMBeanSynchProcess(String applicationId
	        , CountDownLatch countDownLatch
	        , SynchProcessObserver observer) {
	    
		super(applicationId, countDownLatch, observer);
		
		try {
			setdataRepository(AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId)
			        .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY_POOL));
			initializeLock(AbstractLockRepository.MEMORY_POOL_LOCK);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#initialize()
     */
    @Override
    public void initialize() throws Exception {			
		List<MemoryPoolMXBean> mpoolList = mxbeanServer.getMemoryPoolMXBean();
		poolNames = new String[mpoolList.size()];
		poolAttributes = new String[mpoolList.size()][];
		names = new ObjectName[mpoolList.size()];
		
		Vector<String> vector = new Vector<String>();
		AttributeList attributeList = null;
		
		for (int i = 0; i < poolNames.length; i ++) {
			MemoryPoolMXBean mpool_mx_bean = mpoolList.get(i);
			poolNames[i] = mpool_mx_bean.getName();
			names[i] = new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + ",name=" + poolNames[i]);
			
			vector.addElement("Name");
			vector.addElement("Type");
            vector.addElement("Usage");
			
			Object val = mxbeanServer.getAttribute(names[i], "UsageThresholdSupported");				
			if (Boolean.parseBoolean(val.toString())) {
			    vector.addElement("UsageThreshold");
				vector.addElement("UsageThresholdCount");
			}
			val = mxbeanServer.getAttribute(names[i], "CollectionUsageThresholdSupported");
			if (Boolean.parseBoolean(val.toString())) {
			    vector.addElement("CollectionUsageThreshold");
				vector.addElement("CollectionUsageThresholdCount");
			}
			poolAttributes[i] = (String[])vector.toArray(new String[vector.size()]);
			vector.clear();
			
			attributeList = mxbeanServer.getAttributeList(names[i], poolAttributes[i]);
			repository.initializeRepository(attributeList);
		}
		vector = null;
		mpoolList = null;
	}
    
    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#doWork()
     */
    @Override
    public void doWork() throws ConnectException
                                , UndeclaredThrowableException
                                , Exception {
        
        for (int i = 0; i < poolNames.length; i ++) {
            try {
                AttributeList attributeList = mxbeanServer.getAttributeList(names[i], poolAttributes[i]);
                repository.addData(poolNames[i], attributeList);
            }
            catch (NullPointerException e) {
                System.err.println(String.format("%s => Pool: %s. Attributes: %s"
                        , getClass().getSimpleName()
                        , names[i]
                        , Arrays.toString(poolAttributes)));
                
                e.printStackTrace();
            }
        }
    }
}
