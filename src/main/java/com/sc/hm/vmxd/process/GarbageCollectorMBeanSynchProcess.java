package com.sc.hm.vmxd.process;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
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

public class GarbageCollectorMBeanSynchProcess extends AbstractMBeanSynchProcess {
	
	private String[] gbCollectorNames = null;
	private String[][] gbCollectorAttributes = null;
	private ObjectName[] names = null;
	
	public GarbageCollectorMBeanSynchProcess(String applicationId, CountDownLatch countDownLatch) {
		this(applicationId, countDownLatch, null);
	}
	
	public GarbageCollectorMBeanSynchProcess(String applicationId
	        , CountDownLatch countDownLatch
	        , SynchProcessObserver observer) {
	    
		super(applicationId, countDownLatch, observer);
		
		try {
			setdataRepository(AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId)
			        .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR));
			initializeLock(AbstractLockRepository.GARBAGE_COLLECTOR_LOCK);
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
        List<GarbageCollectorMXBean> gbCollectorList = mxbeanServer.getGarbageCollectorMXBean();
		gbCollectorNames = new String[gbCollectorList.size()];
		gbCollectorAttributes = new String[gbCollectorList.size()][];
		names = new ObjectName[gbCollectorList.size()];
		
		Vector<String> vector = new Vector<String>();
		AttributeList attributeList = null;
		
		for (int i = 0; i < gbCollectorNames.length; i ++) {
			GarbageCollectorMXBean gbcollector_mx_bean = gbCollectorList.get(i);
			
			gbCollectorNames[i] = gbcollector_mx_bean.getName();
			names[i] = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",name=" + gbCollectorNames[i]);
				
			vector.addElement("Name");
			vector.addElement("MemoryPoolNames");
            vector.addElement("CollectionCount");
			vector.addElement("CollectionTime");				
			
			gbCollectorAttributes[i] = (String[])vector.toArray(new String[vector.size()]);
			vector.clear();
			
			attributeList = mxbeanServer.getAttributeList(names[i], gbCollectorAttributes[i]);
            repository.initializeRepository(attributeList);
		}
		vector.clear();
		vector = null;
		gbCollectorList = null;
	}
    
    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#doWork()
     */
    @Override
    public void doWork() throws ConnectException
                                , UndeclaredThrowableException
                                , Exception {
        
        for (int i = 0; i < gbCollectorNames.length; i ++) {
            try {
                AttributeList attributeList = mxbeanServer.getAttributeList(names[i], gbCollectorAttributes[i]);
                repository.addData(gbCollectorNames[i], attributeList);
            }
            catch (NullPointerException e) {
                System.err.println(String.format("%s => GC: %s. Attributes: %s"
                        , getClass().getSimpleName()
                        , names[i]
                        , Arrays.toString(gbCollectorAttributes)));
                
                e.printStackTrace();
            }
        }
    }
}
