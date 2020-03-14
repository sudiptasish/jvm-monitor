package com.sc.hm.vmxd.process.lock;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class LockRepository extends AbstractLockRepository {
	
	private Map<String, ThirdPartySynchronizedLock> lockMap = new HashMap<String, ThirdPartySynchronizedLock>();
	
	private LockRepository() {
		initializeLockInstance();
	}
	
	public static LockRepository getMBeanLockRepository() {
		return new LockRepository();
	}
	
	private void initializeLockInstance() {
		try {
			Class<?> clazz = Class.forName(AbstractLockRepository.MEMORY_LOCK);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			ThirdPartySynchronizedLock repository = (ThirdPartySynchronizedLock)constructor.newInstance(new Object[] {"MEMORY_LOCK"});
			lockMap.put(AbstractLockRepository.MEMORY_LOCK, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractLockRepository.MEMORY_POOL_LOCK);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			ThirdPartySynchronizedLock repository = (ThirdPartySynchronizedLock)constructor.newInstance(new Object[] {"MEMORY_POOL_LOCK"});
			lockMap.put(AbstractLockRepository.MEMORY_POOL_LOCK, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractLockRepository.GARBAGE_COLLECTOR_LOCK);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			ThirdPartySynchronizedLock repository = (ThirdPartySynchronizedLock)constructor.newInstance(new Object[] {"GARBAGE_COLLECTOR_LOCK"});
			lockMap.put(AbstractLockRepository.GARBAGE_COLLECTOR_LOCK, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractLockRepository.CLASS_LOADING_LOCK);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			ThirdPartySynchronizedLock repository = (ThirdPartySynchronizedLock)constructor.newInstance(new Object[] {"CLASS_LOADING_LOCK"});
			lockMap.put(AbstractLockRepository.CLASS_LOADING_LOCK, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractLockRepository.THREAD_LOCK);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			ThirdPartySynchronizedLock repository = (ThirdPartySynchronizedLock)constructor.newInstance(new Object[] {"THREAD_LOCK"});
			lockMap.put(AbstractLockRepository.THREAD_LOCK, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractLockRepository.OPERATING_SYSTEM_LOCK);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			ThirdPartySynchronizedLock repository = (ThirdPartySynchronizedLock)constructor.newInstance(new Object[] {"OPERATING_SYSTEM_LOCK"});
			lockMap.put(AbstractLockRepository.OPERATING_SYSTEM_LOCK, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractLockRepository.RUNTIME_LOCK);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			ThirdPartySynchronizedLock repository = (ThirdPartySynchronizedLock)constructor.newInstance(new Object[] {"RUNTIME_LOCK"});
			lockMap.put(AbstractLockRepository.RUNTIME_LOCK, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class<?> clazz = Class.forName(AbstractLockRepository.COMPILATION_LOCK);
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {String.class});
			ThirdPartySynchronizedLock repository = (ThirdPartySynchronizedLock)constructor.newInstance(new Object[] {"COMPILATION_LOCK"});
			lockMap.put(AbstractLockRepository.COMPILATION_LOCK, repository);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ThirdPartySynchronizedLock getSynchLockByName(String lock_name) {
		if (lockMap.containsKey(lock_name)) {
			return lockMap.get(lock_name);
		}
		return null;
	}

	public void setNewSynchronizedLock(String lock_name, ThirdPartySynchronizedLock synchLock) {
		if (!lockMap.containsKey(lock_name)) {
			lockMap.put(lock_name, synchLock);
		}
	}
}
