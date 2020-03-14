package com.sc.hm.vmxd.process.lock;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractLockRepository {

	public static final String LOCK_TYPE_MBEAN = "LOCK_TYPE_MBEAN";
	
	public static final String MEMORY_LOCK = "com.sc.hm.vmxd.process.lock.ThirdPartyMemoryLock";
	public static final String MEMORY_POOL_LOCK = "com.sc.hm.vmxd.process.lock.ThirdPartyMemoryPoolLock";
	public static final String GARBAGE_COLLECTOR_LOCK = "com.sc.hm.vmxd.process.lock.ThirdPartyGarbageCollectorLock";
	public static final String CLASS_LOADING_LOCK = "com.sc.hm.vmxd.process.lock.ThirdPartyClassLoadingLock";
	public static final String THREAD_LOCK = "com.sc.hm.vmxd.process.lock.ThirdPartyThreadLock";
	public static final String OPERATING_SYSTEM_LOCK = "com.sc.hm.vmxd.process.lock.ThirdPartyOperatingSystemLock";
	public static final String COMPILATION_LOCK = "com.sc.hm.vmxd.process.lock.ThirdPartyCompilationLock";
	public static final String RUNTIME_LOCK = "com.sc.hm.vmxd.process.lock.ThirdPartyRuntimeLock";
	
	private static Map<String , AbstractLockRepository> lockRepositoryMap = new HashMap<String, AbstractLockRepository>();
	
	public AbstractLockRepository() {}
	
	public static AbstractLockRepository getLockRepository(String applicationId) throws Exception {
		if (!lockRepositoryMap.containsKey(applicationId)) {
			lockRepositoryMap.put(applicationId, LockRepository.getMBeanLockRepository());
		}
		return lockRepositoryMap.get(applicationId);
	}
	
	public abstract ThirdPartySynchronizedLock getSynchLockByName(String lock_name);
	
	public abstract void setNewSynchronizedLock(String lock_name, ThirdPartySynchronizedLock synchLock);
}
