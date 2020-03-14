package com.sc.hm.vmxd.data;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * A concrete factory class to create various repositories.
 * 
 * @author Sudiptasish Chanda
 */
public class MBeanDataRepositoryFactory extends AbstractMBeanDataRepositoryFactory {

	private final Map<String, DataRepository> repositoryMap = new HashMap<>();
	
	private MBeanDataRepositoryFactory() {
		initializeAllRepository();
	}
	
	public static MBeanDataRepositoryFactory getNewRepositoryFactory() {
		return new MBeanDataRepositoryFactory();
	}
	
	private void initializeAllRepository() {
		try {
			Class<?> clazz = Class.forName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			DataRepository repository = (DataRepository)constructor.newInstance(new Object[] {"MBEAN_REPOSITORY_MEMORY"});
			repositoryMap.put(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY_POOL);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			DataRepository repository = (DataRepository)constructor.newInstance(new Object[] {"MBEAN_REPOSITORY_MEMORY_POOL"});
			repositoryMap.put(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY_POOL, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_CLASS);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			DataRepository repository = (DataRepository)constructor.newInstance(new Object[] {"MBEAN_REPOSITORY_CLASS"});
			repositoryMap.put(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_CLASS, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_THREAD);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			DataRepository repository = (DataRepository)constructor.newInstance(new Object[] {"MBEAN_REPOSITORY_THREAD"});
			repositoryMap.put(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_THREAD, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			DataRepository repository = (DataRepository)constructor.newInstance(new Object[] {"MBEAN_REPOSITORY_GARBAGE_COLLECTOR"});
			repositoryMap.put(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_OPERATING_SYSTEM);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			DataRepository repository = (DataRepository)constructor.newInstance(new Object[] {"MBEAN_REPOSITORY_OPERATING_SYSTEM"});
			repositoryMap.put(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_OPERATING_SYSTEM, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_RUNTIME);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			DataRepository repository = (DataRepository)constructor.newInstance(new Object[] {"MBEAN_REPOSITORY_RUNTIME"});
			repositoryMap.put(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_RUNTIME, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_COMPILATION);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			DataRepository repository = (DataRepository)constructor.newInstance(new Object[] {"MBEAN_REPOSITORY_COMPILATION"});
			repositoryMap.put(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_COMPILATION, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @Override
	public DataRepository getRepositoryByName(String repository_name) {
		if (repositoryMap.containsKey(repository_name)) {
			return repositoryMap.get(repository_name);
		}
		return null;
	}

    @Override
	public void setNewRepository(String repository_name, DataRepository repository) {
		repositoryMap.put(repository_name, repository);
	}
}
