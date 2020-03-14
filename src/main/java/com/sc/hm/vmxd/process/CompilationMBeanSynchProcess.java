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

public class CompilationMBeanSynchProcess extends AbstractMBeanSynchProcess {
	
	private String[] cAttributes = {"Name", "TotalCompilationTime"};
	private ObjectName name = null;
	private long pauseTime = 3600 * 1000;
	
	public CompilationMBeanSynchProcess(String applicationId, CountDownLatch countDownLatch) {
		this(applicationId, countDownLatch, null);
	}
	
	public CompilationMBeanSynchProcess(String applicationId
	        , CountDownLatch countDownLatch
	        , SynchProcessObserver observer) {
	    
		super(applicationId, countDownLatch, observer);
		
		try {
			setdataRepository(AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId)
			        .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_COMPILATION));
			initializeLock(AbstractLockRepository.COMPILATION_LOCK);
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
        name = new ObjectName(ManagementFactory.COMPILATION_MXBEAN_NAME);
        repository.initializeRepository(null);
    }

    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#doWork()
     */
    @Override
    public void doWork() throws ConnectException
                                , UndeclaredThrowableException
                                , Exception {
        
        AttributeList attributeList = mxbeanServer.getAttributeList(name, cAttributes);
        repository.addData("CompilationMX", attributeList);
    }

    public long getPauseTime() {
        return pauseTime;
    }
}
