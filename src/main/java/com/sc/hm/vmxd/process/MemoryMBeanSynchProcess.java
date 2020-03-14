package com.sc.hm.vmxd.process;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.ConnectException;
import java.util.concurrent.CountDownLatch;

import javax.management.AttributeList;
import javax.management.NotificationEmitter;
import javax.management.ObjectName;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.MemoryDataRepository;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;
import com.sc.hm.vmxd.service.SynchMemoryNotificationListener;

public class MemoryMBeanSynchProcess extends AbstractMBeanSynchProcess {
	
	private String[] memoryNames = {MemoryDataRepository.HEAP_MEMORY, MemoryDataRepository.NONHEAP_MEMORY};
	private String[] memoryAttributes = {"HeapMemoryUsage", "NonHeapMemoryUsage", "ObjectPendingFinalizationCount"};
	private ObjectName name = null;
	
	public MemoryMBeanSynchProcess(String applicationId, CountDownLatch countDownLatch) {	    
		this(applicationId, countDownLatch, null);
	}
	
	public MemoryMBeanSynchProcess(String applicationId
	        , CountDownLatch countDownLatch
	        , SynchProcessObserver observer) {
	    
		super(applicationId, countDownLatch, observer);
		
		try {
			setdataRepository(AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId)
			        .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY));
			initializeLock(AbstractLockRepository.MEMORY_LOCK);
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
		name = new ObjectName(ManagementFactory.MEMORY_MXBEAN_NAME);
        MemoryMXBean memory_mx_bean = mxbeanServer.getMemoryMXBean();
		
        EnvironmentConfigObject envConfig = SynchApplicationConfiguration.getSynchInstance().getEnvironmentConfig();
		if (envConfig != null && Boolean.valueOf(envConfig.getEnableMailing())) {
			NotificationEmitter notificationEmitter = (NotificationEmitter)memory_mx_bean;
			notificationEmitter.addNotificationListener(new SynchMemoryNotificationListener(), null, envConfig.getMailCause());
		}
		repository.initializeRepository(null);
	}
    
    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#doWork()
     */
    @Override
    public void doWork() throws ConnectException
                                , UndeclaredThrowableException
                                , Exception {
        
        AttributeList attributeList = mxbeanServer.getAttributeList(name, memoryAttributes);
        repository.addData("Memory", attributeList);
    }
}
