package com.sc.hm.vmxd.process;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.ThreadDataRepository;
import com.sc.hm.vmxd.data.thread.ThreadUsageData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class TopThreadMBeanSynchProcess extends AbstractMBeanSynchProcess {
    
    private boolean threadCpuTimeEnabled = false;
    
    private ObjectName name = null;
    
    private int iteration = 0;
    
    public TopThreadMBeanSynchProcess(String applicationId, CountDownLatch countDownLatch) {       
        this(applicationId, countDownLatch, null);
    }
    
    public TopThreadMBeanSynchProcess(String applicationId
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
            threadCpuTimeEnabled = true;
        }
    }

    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#doWork()
     */
    @Override
    public void doWork() throws ConnectException
                                , UndeclaredThrowableException
                                , Exception {
        
        // Start capturing individual thread cpu and user time.
        if (threadCpuTimeEnabled) {
            captureThreadCpuAndUserTime();
        }
        iteration ++;
    }
    
    /**
     * Capture the cpu and user time of individual vm thread.
     */
    private void captureThreadCpuAndUserTime() throws ConnectException
                                                    , UndeclaredThrowableException
                                                    , Exception {
        
        Logger.log(getClass().getSimpleName() + " started capturing individual thread cpu/user time");
        long startTime = System.currentTimeMillis();
        ThreadDataRepository tRepository = (ThreadDataRepository)repository;
        
        Object obj = mxbeanServer.getAttribute(new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME), "AllThreadIds");
        long[] ids = (long[])obj;
        
        if (iteration > 3) {
            List<Long> finalIds = new ArrayList<>(ids.length - 20);
            List<ThreadUsageData> list = tRepository.allUsages();
            
            // Discard the threads whose cpu usage is never calculated.
            for (int i = 0, length = list.size(); i < length; i ++) {
                ThreadUsageData usageData = list.get(i);
                if (usageData.getLastCpuTime() > 0) {
                    finalIds.add(usageData.getId());
                }
            }
            ids = new long[finalIds.size()];
            for (int k = 0; k < ids.length; k ++) {
                ids[k] = finalIds.get(k);
            }
        }
        
        // Refine the array to have only those thread which have the cpuUsage greater than 0.
        
        // Get the cpu time for ALL such thread(s).
        Object cpuData = mxbeanServer.invokeOperation(
                new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME)
                , "getThreadCpuTime"
                , new Object[] {ids}
                , new String[] {long[].class.getName()});
        
        if (cpuData != null) {
            long[] cpuTimes = (long[])cpuData;
            for (int i = 0; i < ids.length; i ++) {
                // If this is a new thread id, then fetch the thread name and initialize the 
                // usage mapping for this new thread.
                if (!tRepository.isExistUsageData(ids[i])) {
                    Object cData = mxbeanServer.invokeOperation(
                            new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME)
                            , "getThreadInfo"
                            , new Object[] {ids[i]}
                            , new String[] {long.class.getName()});
                    
                    if (cData != null) {
                        ThreadInfo threadInfo = ThreadInfo.from((CompositeData)cData);
                        tRepository.initUsage(threadInfo.getThreadId(), threadInfo.getThreadName());
                    }
                }
                tRepository.addCpuUsage(ids[i], cpuTimes[i]);
            }
        }
        
        Object userData = mxbeanServer.invokeOperation(
                new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME)
                , "getThreadUserTime"
                , new Object[] {ids}
                , new String[] {long[].class.getName()});
        
        if (userData != null) {
            long[] userTimes = (long[])userData;
            for (int i = 0; i < ids.length; i ++) {
                // If this is a new thread id, then fetch the thread name and initialize the 
                // usage mapping for this new thread.
                if (!tRepository.isExistUsageData(ids[i])) {
                    Object cData = mxbeanServer.invokeOperation(
                            new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME)
                            , "getThreadInfo"
                            , new Object[] {ids[i]}
                            , new String[] {long.class.getName()});
                    
                    if (cData != null) {
                        ThreadInfo threadInfo = ThreadInfo.from((CompositeData)cData);
                        tRepository.initUsage(threadInfo.getThreadId(), threadInfo.getThreadName());
                    }
                }
                tRepository.addUserUsage(ids[i], userTimes[i]);
            }
        }
        Logger.log(
                String.format("%s finished capturing thread cpu/user time. Elapsed Time (ms): %d"
                        , getClass().getSimpleName()
                        , (System.currentTimeMillis() - startTime)));
    }
    
    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#getPauseTime()
     */
    @Override
    public long getPauseTime() {
        return super.getPauseTime() * 2;
    }
}