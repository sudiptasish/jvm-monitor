package com.sc.hm.vmxd.process;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.ConnectException;
import java.util.concurrent.CountDownLatch;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.ThreadDataRepository;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;

public class ThreadMBeanSynchProcess extends AbstractMBeanSynchProcess {
	
	private String[] threadAttributes = {"CurrentThreadCpuTime"
	        , "DaemonThreadCount"
	        , "PeakThreadCount"
	        , "TotalStartedThreadCount"
	        , "DaemonThreadCount"};
	
	private ObjectName name = null;
	
	public ThreadMBeanSynchProcess(String applicationId, CountDownLatch countDownLatch) {	    
		this(applicationId, countDownLatch, null);
	}
	
	public ThreadMBeanSynchProcess(String applicationId
	        , CountDownLatch countDownLatch
	        , SynchProcessObserver observer) {
	    
		super(applicationId, countDownLatch, observer);
		
		try {
			setdataRepository(AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId)
			        .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_THREAD));
			initializeLock(AbstractLockRepository.THREAD_LOCK);
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
    	name = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
		
		Object obj = mxbeanServer.getAttribute(name, "ThreadCpuTimeSupported");
		if (Boolean.parseBoolean(obj.toString())) {
		    mxbeanServer.setAttribute(name, new Attribute("ThreadCpuTimeEnabled", Boolean.TRUE));
		}
		obj = mxbeanServer.getAttribute(new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME), "AllThreadIds");
        long[] ids = (long[])obj;
        
        Object cData = mxbeanServer.invokeOperation(
                new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME)
                , "getThreadInfo"
                , new Object[] {ids, Integer.MAX_VALUE}
                , new String[] {long[].class.getName(), int.class.getName()});
        
        if (cData != null) {
            ThreadDataRepository tRepository = (ThreadDataRepository)repository;
            CompositeData[] datas = (CompositeData[])cData;
            for (int i = 0; i < datas.length; i ++) {
                if (datas[i] != null) {
                    ThreadInfo threadInfo = ThreadInfo.from(datas[i]);
                    tRepository.initUsage(threadInfo.getThreadId(), threadInfo.getThreadName());
                }
            }
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
        
        AttributeList attributeList = mxbeanServer.getAttributeList(name, threadAttributes);
        repository.addData("ThreadMX", attributeList);
    }
}