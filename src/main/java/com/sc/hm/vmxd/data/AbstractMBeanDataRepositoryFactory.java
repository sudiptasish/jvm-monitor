package com.sc.hm.vmxd.data;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMBeanDataRepositoryFactory {
	
	public static final String REPOSITORY_TYPE_MBEAN = "REPOSITORY_TYPE_MBEAN";
	
	public static final String MBEAN_REPOSITORY_MEMORY = "com.sc.hm.vmxd.data.MemoryDataRepository";
	public static final String MBEAN_REPOSITORY_MEMORY_POOL = "com.sc.hm.vmxd.data.MemoryPoolDataRepository";
	public static final String MBEAN_REPOSITORY_CLASS = "com.sc.hm.vmxd.data.ClassDataRepository";
	public static final String MBEAN_REPOSITORY_THREAD = "com.sc.hm.vmxd.data.ThreadDataRepository";
	public static final String MBEAN_REPOSITORY_GARBAGE_COLLECTOR = "com.sc.hm.vmxd.data.GarbageCollectorDataRepository";
	public static final String MBEAN_REPOSITORY_OPERATING_SYSTEM = "com.sc.hm.vmxd.data.OperatingSystemDataRepository";
	public static final String MBEAN_REPOSITORY_COMPILATION = "com.sc.hm.vmxd.data.CompilationDataRepository";
	public static final String MBEAN_REPOSITORY_RUNTIME = "com.sc.hm.vmxd.data.RuntimeDataRepository";
	
	private static Map<String, AbstractMBeanDataRepositoryFactory> repositoryFactoryMap = new HashMap<String, AbstractMBeanDataRepositoryFactory>();
	
	public AbstractMBeanDataRepositoryFactory() {}
	
	public static AbstractMBeanDataRepositoryFactory getDataRepositoryFactory(String applicationId) throws Exception {
		if (!repositoryFactoryMap.containsKey(applicationId)) {
			repositoryFactoryMap.put(applicationId, MBeanDataRepositoryFactory.getNewRepositoryFactory());
		}
		return repositoryFactoryMap.get(applicationId);
	}
	
	public abstract DataRepository getRepositoryByName(String repository_name);
	
	public abstract void setNewRepository(String repository_name, DataRepository repository);
}
