package com.sc.hm.monitor.persistence.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.MemoryUsage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.ClassDataRepository;
import com.sc.hm.vmxd.data.GarbageCollectorDataRepository;
import com.sc.hm.vmxd.data.MemoryDataRepository;
import com.sc.hm.vmxd.data.MemoryPoolDataRepository;
import com.sc.hm.vmxd.data.OperatingSystemDataRepository;
import com.sc.hm.vmxd.data.classes.ClassData;
import com.sc.hm.vmxd.data.gbcollector.GarbageCollectorData;
import com.sc.hm.vmxd.data.memory.MemoryData;
import com.sc.hm.vmxd.data.memory.MemoryPoolData;
import com.sc.hm.vmxd.data.os.OperatingSystemData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.lock.ThirdPartySynchronizedLock;

public class SynchPersistentTask extends MonitorTask implements TaskActivity {
	
	private String persistentDirectory = "";
	private File directory = null;
	private boolean flushData = false;
	
	private String applicationId = "";

	private MemoryDataRepository mDataRepository = null;
	private MemoryPoolDataRepository mpoolDataRepository = null;
	private GarbageCollectorDataRepository gDataRepository = null;
	private OperatingSystemDataRepository osDataRepository = null;
	private ClassDataRepository cDataRepository = null;
	
	private ThirdPartySynchronizedLock memoryLock = null;
	private ThirdPartySynchronizedLock mpoolLock = null;
	private ThirdPartySynchronizedLock gcLock = null;
	private ThirdPartySynchronizedLock osLock = null;
	private ThirdPartySynchronizedLock classLock = null;
	
	public SynchPersistentTask(String name, String persistentDirectory) {
		super(name);
		this.persistentDirectory = persistentDirectory.replaceAll("\\", "/");
	}
	
	public SynchPersistentTask(String name, File persistentDirectory) {
		this(name, persistentDirectory, false);
	}
	
	public SynchPersistentTask(String name, File persistentDirectory, boolean flushData) {
		super(name);
		this.directory = persistentDirectory;
		this.flushData = flushData;
		initOther();
	}
	
	private void initOther() {
		applicationId = SynchApplicationConfiguration.getSynchInstance().getEnvironmentConfig().getApplicationId();
		try {
			memoryLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.MEMORY_LOCK);
			mpoolLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.MEMORY_POOL_LOCK);
			gcLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.GARBAGE_COLLECTOR_LOCK);
			osLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.OPERATING_SYSTEM_LOCK);
			classLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.CLASS_LOADING_LOCK);
			
			AbstractMBeanDataRepositoryFactory mbeanRepositoryFactory = AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId);
			
			mDataRepository = (MemoryDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY);
			mpoolDataRepository = (MemoryPoolDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY_POOL);
			gDataRepository = (GarbageCollectorDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR);
			osDataRepository = (OperatingSystemDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_OPERATING_SYSTEM);
			cDataRepository = (ClassDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_CLASS);			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getPersistentDirectory() {
		return persistentDirectory;
	}

	public void setPersistentDirectory(String persistentDirectory) {
		this.persistentDirectory = persistentDirectory;
	}

	public void performTask() throws Exception {
		if (getTaskName().equals(TASK_MEMORY_PERSIST)) {
			persistMemoryData();
		}
		else if (getTaskName().equals(TASK_MEMORYPOOL_PERSIST)) {
			persistMemoryPoolData();
		}
		else if (getTaskName().equals(TASK_CPUUSAGE_PERSIST)) {
			persistCPUUsageData();
		}
		else if (getTaskName().equals(TASK_GBCOLLECTION_PERSIST)) {
			persistGarbageCollectorData();
		}
		else if (getTaskName().equals(TASK_CLASSES_PERSIST)) {
			persistClassLoadingData();
		}
		else if (getTaskName().equals(TASK_ALL_PERSIST)) {
			persistMemoryData();
			persistMemoryPoolData();
			persistCPUUsageData();
			persistGarbageCollectorData();
			persistClassLoadingData();			
		}
	}
	
	public void persistMemoryData() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			memoryLock.startGetItem();
			String[] memoryNames = mDataRepository.getMemoryNames();
			for (String name : memoryNames) {
				File file = new File(directory, "/Data_Memory_" + name + ".csv");
				MemoryData memoryData = mDataRepository.getMemoryData(name);
				List<MemoryUsage> poolUsage = memoryData.getUsageList();
				List<Date> timeUsage = memoryData.getDateList();
				
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
				pw.println("Timestamp,Init,Used,Committed,Max");
				int size = poolUsage.size();
				
				for (int i = 0; i < size; i ++) {
					MemoryUsage memoryUsage = poolUsage.get(i);
					pw.println(new StringBuilder().append(df.format(timeUsage.get(i))).append(",").append(memoryUsage.getInit()).append(",").append(memoryUsage.getUsed()).append(",").append(memoryUsage.getCommitted()).append(",").append(memoryUsage.getMax()));
				}
				pw.close();
				if (flushData) {
					clearData(poolUsage, timeUsage);
					Logger.log(size + " Record(s) Flushed for Memory.");
				}
			}
		}
		finally {
			memoryLock.endGetItem();
		}
	}
	
	public void persistMemoryPoolData() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			mpoolLock.startGetItem();
			String[] memoryPoolNames = mpoolDataRepository.getMemoryPoolNames();
			for (String name : memoryPoolNames) {
				File file = new File(directory, "/Data_MemoryPool_" + name + ".csv");
				MemoryPoolData memoryPoolData = mpoolDataRepository.getMemoryPoolData(name);
				List<MemoryUsage> poolUsage = memoryPoolData.getUsageList();
				List<Date> timeUsage = memoryPoolData.getDateList();
				
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
				pw.println("Timestamp,Init,Used,Committed,Max");
				int size = poolUsage.size();
				
				for (int i = 0; i < size; i ++) {
					MemoryUsage memoryUsage = poolUsage.get(i);
					pw.println(new StringBuilder().append(df.format(timeUsage.get(i))).append(",").append(memoryUsage.getInit()).append(",").append(memoryUsage.getUsed()).append(",").append(memoryUsage.getCommitted()).append(",").append(memoryUsage.getMax()));
				}
				pw.close();
				if (flushData) {
					clearData(poolUsage, timeUsage);
					Logger.log(size + " Record(s) Flushed for Memory Pool.");
				}
			}
		}
		finally {
			mpoolLock.endGetItem();
		}
	}
	
	public void persistCPUUsageData() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			osLock.startGetItem();
			OperatingSystemData osData = osDataRepository.getOsData();
			File file = new File(directory, "/CPU_Usage.csv");
			
			List<Double> cpuUsage = osData.getCpuUsage();
			List<Date> timeUsage = osData.getTimeList();
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
			pw.println("Timestamp,CPU_Usage");
			int size = cpuUsage.size();
			
			for (int i = 0; i < size; i ++) {
				pw.println(new StringBuilder().append(df.format(timeUsage.get(i))).append(",").append(cpuUsage.get(i)));
			}
			pw.close();
			if (flushData) {
				clearData(cpuUsage, timeUsage);
				Logger.log(size + " Record(s) Flushed for CPU Usage.");
			}
		}
		finally {
			osLock.endGetItem();
		}
	}
	
	public void persistGarbageCollectorData() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			gcLock.startGetItem();
			String[] gcNames = gDataRepository.getGarbageCollectorNames();
			for (String name : gcNames) {
				File file = new File(directory, "/GarbageCollector_" + name + ".csv");
				GarbageCollectorData gcData = gDataRepository.getGarbageCollectorData(name);
				List<Long> collectionUsage = gcData.getCollectionList();
				List<Date> timeUsage = gcData.getDateList();
				
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
				pw.println("Timestamp,Collection Count");
				int size = collectionUsage.size();
				
				for (int i = 0; i < size; i ++) {
					pw.println(new StringBuilder().append(df.format(timeUsage.get(i))).append(",").append(collectionUsage.get(i)));
				}
				pw.close();
				if (flushData) {
					clearData(collectionUsage, timeUsage);
					Logger.log(size + " Record(s) Flushed for Garbage Collector.");
				}
			}
		}
		finally {
			gcLock.endGetItem();
		}
	}
	
	public void persistClassLoadingData() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			classLock.startGetItem();
			ClassData classData = cDataRepository.getClassData();
			File file = new File(directory, "/ClassLoading.csv");
			
			List<Long> totalLoadedClass = classData.getTotalLoadedClassCount();
			List<Long> loadedClass = classData.getLoadedClassCount();
			List<Long> unloadedClass = classData.getUnloadedClassCount();
			List<Date> timeUsage = classData.getTimeList();
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
			pw.println("Timestamp,Total Class,Loaded Class,Unloaded Class");
			int size = timeUsage.size();
			
			for (int i = 0; i < size; i ++) {
				pw.println(new StringBuilder().append(df.format(timeUsage.get(i))).append(",").append(totalLoadedClass.get(i)).append(",").append(loadedClass.get(i)).append(",").append(unloadedClass.get(i)));
			}
			pw.close();
			if (flushData) {
				clearData(totalLoadedClass, loadedClass, unloadedClass, timeUsage);
				Logger.log(size + " Record(s) Flushed for Class Loading.");
			}
		}
		finally {
			classLock.endGetItem();
		}
	}
	
	private void clearData(List<?>... lists) {
		for (List<?> list : lists) {
			list.clear();
		}
	}
}