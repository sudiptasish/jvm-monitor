package com.sc.hm.vmxd.process;

import java.lang.management.ManagementFactory;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.ConnectException;
import java.util.concurrent.CountDownLatch;

import javax.management.AttributeList;
import javax.management.ObjectName;

import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;

public class RuntimeMBeanSynchProcess extends AbstractMBeanSynchProcess {
	
	private String[] rAttributes = {"Uptime"};
	private ObjectName name = null;
	private long pauseTime = 300 * 1000;
	
	public RuntimeMBeanSynchProcess(String applicationId, CountDownLatch countDownLatch) {	    
		this(applicationId, countDownLatch, null);
	}
	
	public RuntimeMBeanSynchProcess(String applicationId
	        , CountDownLatch countDownLatch
	        , SynchProcessObserver observer) {
		
	    super(applicationId, countDownLatch, observer);
		
	    try {
			setdataRepository(AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId)
			        .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_RUNTIME));
			initializeLock(AbstractLockRepository.RUNTIME_LOCK);
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
		name = new ObjectName(ManagementFactory.RUNTIME_MXBEAN_NAME);
			
		String[] initial = {"Name", "VmName", "VmVersion", "VmVendor", "SpecName"
		        , "SpecVersion", "SpecVendor", "ClassPath", "StartTime", "Uptime"};
		
		AttributeList attributeList = mxbeanServer.getAttributeList(name, initial);
        repository.initializeRepository(attributeList);
	}

    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#doWork()
     */
    @Override
    public void doWork() throws ConnectException
                                , UndeclaredThrowableException
                                , Exception {
        
        AttributeList attributeList = mxbeanServer.getAttributeList(name, rAttributes);
        repository.addData("RuntimeMX", attributeList);
    }

    public long getPauseTime() {
        return pauseTime;
    }
}
