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

public class ClassMBeanSynchProcess extends AbstractMBeanSynchProcess {
	
	private String[] classTypeNames = {"Total Class", "Loaded Class", "Unloaded Class"};
	private String[] classAttributes = {"TotalLoadedClassCount", "LoadedClassCount", "UnloadedClassCount"};
	private ObjectName name = null;
	
	public ClassMBeanSynchProcess(String applicationId, CountDownLatch countDownLatch) {
		this(applicationId, countDownLatch, null);
	}
	
	public ClassMBeanSynchProcess(String applicationId
	        , CountDownLatch countDownLatch
	        , SynchProcessObserver observer) {
	    
		super(applicationId, countDownLatch, observer);
		
		try {
			setdataRepository(AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId)
			        .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_CLASS));
			initializeLock(AbstractLockRepository.CLASS_LOADING_LOCK);
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
		name = new ObjectName(ManagementFactory.CLASS_LOADING_MXBEAN_NAME);
		repository.initializeRepository(null);
	}

    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#doWork()
     */
    @Override
    public void doWork() throws ConnectException
                                , UndeclaredThrowableException
                                , Exception {
        
        AttributeList attributeList = mxbeanServer.getAttributeList(name, classAttributes);
        repository.addData("ClassLoading", attributeList);
    }
}
