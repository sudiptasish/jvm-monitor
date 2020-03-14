package com.sc.hm.vmxd.process;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.ConnectException;
import java.util.concurrent.CountDownLatch;

import javax.management.AttributeList;
import javax.management.ObjectName;

import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;

public class OperatingSystemMBeanSynchProcess extends AbstractMBeanSynchProcess {
	
	private final String[] osAttributes = {"ProcessCpuLoad"
	        , "FreePhysicalMemorySize"
	        , "FreeSwapSpaceSize"
	        , "CommittedVirtualMemorySize"};
	
	private ObjectName name = null;
	
	public OperatingSystemMBeanSynchProcess(String applicationId, CountDownLatch countDownLatch) {	    
		this(applicationId, countDownLatch, null);
	}
	
	public OperatingSystemMBeanSynchProcess(String applicationId
	        , CountDownLatch countDownLatch
	        , SynchProcessObserver observer) {
	    
		super(applicationId, countDownLatch, observer);
		
		try {
			setdataRepository(AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId)
			        .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_OPERATING_SYSTEM));
			initializeLock(AbstractLockRepository.OPERATING_SYSTEM_LOCK);
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
		name = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);
			
		String[] attributes = new String[4];
		attributes[0] = "Name";
		attributes[1] = "Arch";
		attributes[2] = "Version";
		attributes[3] = "AvailableProcessors";
		
		OperatingSystemMXBean os_mx_bean = mxbeanServer.getOperatingSystemMXBean();
		//if (os_mx_bean instanceof com.sun.management.OperatingSystemMXBean) {
            String[] expanded = new String[attributes.length + 6];
            System.arraycopy(attributes, 0, expanded, 0, attributes.length);
            
            expanded[4] = "ProcessCpuLoad";
            expanded[5] = "TotalPhysicalMemorySize";
            expanded[6] = "FreePhysicalMemorySize";
            expanded[7] = "TotalSwapSpaceSize";
            expanded[8] = "FreeSwapSpaceSize";
            expanded[9] = "CommittedVirtualMemorySize";
            
            attributes = expanded;
		//}			
		AttributeList values = mxbeanServer.getAttributeList(name, attributes);
		
		repository.initializeRepository(values);
	}

    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.AbstractMBeanSynchProcess#doWork()
     */
    @Override
    public void doWork() throws ConnectException
                                , UndeclaredThrowableException
                                , Exception {
        
        AttributeList attributeList = mxbeanServer.getAttributeList(name, osAttributes);
        repository.addData("OperatingSystemMX", attributeList);
    }
}
