package com.sc.hm.monitor.persistence.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.shared.classes.ClassMBeanSharedObject;
import com.sc.hm.monitor.shared.gbcollector.GBCollector;
import com.sc.hm.monitor.shared.gbcollector.GBCollectorMBeanSharedObject;
import com.sc.hm.monitor.shared.memory.Memory;
import com.sc.hm.monitor.shared.memory.MemoryMBeanSharedObject;
import com.sc.hm.monitor.shared.mpool.MemoryPool;
import com.sc.hm.monitor.shared.mpool.MemoryPoolMBeanSharedObject;
import com.sc.hm.monitor.shared.os.OSMBeanSharedObject;
import com.sc.hm.monitor.util.Logger;

public class PersistentTask extends MonitorTask implements TaskActivity {
	
	private String persistentDirectory = "";
	private File directory = null;
	private boolean flushData = false;

	public PersistentTask(String name, String persistentDirectory) {
		super(name);
		this.persistentDirectory = persistentDirectory.replaceAll("\\", "/");
	}
	
	public PersistentTask(String name, File persistentDirectory) {
		this(name, persistentDirectory, false);
	}
	
	public PersistentTask(String name, File persistentDirectory, boolean flushData) {
		super(name);
		this.directory = persistentDirectory;
		this.flushData = flushData;
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
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MemoryMBeanSharedObject m_mxBean = MBeanSharedObjectRepository.getInstance().getMemory_mx_bean();
		
		synchronized (m_mxBean) {
			String[] memoryNames = m_mxBean.getMemoryNames();
			for (String name : memoryNames) {
				File file = new File(directory, "/Data_Memory_" + name + ".csv");
				Memory memory = m_mxBean.getMemory(name);
				long init = memory.getInit();
				long max = memory.getMax();
				List<Long> usage = memory.getPoolUsage();
				List<Long> timeList = m_mxBean.getTimeList();
				List<Long> committed = memory.getPoolCommitted();
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
				pw.println("Timestamp,Init,Used,Committed,Max");
				int size = usage.size();
				int i = 0;
				for (; i < size; i ++) {
					dt.setTime(timeList.get(i));
					pw.println(new StringBuilder().append(df.format(dt)).append(",").append(init).append(",").append(usage.get(i)).append(",").append(committed.get(i)).append(",").append(max));
				}
				pw.close();
				if (flushData) {
					clearData(usage, timeList, committed);
					Logger.log(i + " Record(s) Flushed for Memory.");
				}
			}
		}
	}
	
	public void persistMemoryPoolData() throws Exception {
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MemoryPoolMBeanSharedObject mp_mxBean = MBeanSharedObjectRepository.getInstance().getMpool_mx_bean();
		
		synchronized (mp_mxBean) {
			String[] memoryNames = mp_mxBean.getMemoryPoolNames();
			for (String name : memoryNames) {
				File file = new File(directory, "/Data_MemoryPool_" + name + ".csv");
				MemoryPool memoryPool = mp_mxBean.getMemoryPool(name);
				long init = memoryPool.getInit();
				long max = memoryPool.getMax();
				List<Long> timeList = mp_mxBean.getTimeList();
				List<Long> usage = memoryPool.getPoolUsage();
				List<Long> committed = memoryPool.getPoolCommitted();
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
				pw.println("Timestamp,Init,Used,Committed,Max");
				int size = usage.size();
				int i = 0;
				for (; i < size; i ++) {
					dt.setTime(timeList.get(i));
					pw.println(new StringBuilder().append(df.format(dt)).append(",").append(init).append(",").append(usage.get(i)).append(",").append(committed.get(i)).append(",").append(max));
				}
				pw.close();
				if (flushData) {
					clearData(timeList, usage, committed);
					Logger.log(i + " Record(s) Flushed for Memory Pool.");
				}
			}
		}
	}
	
	public void persistCPUUsageData() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OSMBeanSharedObject os_mxBean = MBeanSharedObjectRepository.getInstance().getOs_mx_bean();
		
		synchronized (os_mxBean) {
			List<Long> usage = os_mxBean.getProcessCPUTime();
			List<Long> time = os_mxBean.getTimeList();
			File file = new File(directory, "/CPU_Usage.csv");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
			pw.println("Time,CPU_Usage");
			Date dt = new Date();
			int size = usage.size();
			int i = 0;
			for (; i < size; i ++) {
				dt.setTime(time.get(i));
				pw.println(new StringBuilder().append(df.format(dt)).append(",").append(usage.get(i)));
			}
			pw.close();
			if (flushData) {
				clearData(usage, time);
				Logger.log(i + " Record(s) Flushed for CPU Usage.");
			}
		}
	}
	
	public void persistGarbageCollectorData() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GBCollectorMBeanSharedObject gb_mxBean = MBeanSharedObjectRepository.getInstance().getGbcollector_mx_bean();
		
		synchronized (gb_mxBean) {
			String[] gbCollectorNames = gb_mxBean.getGarbageCollectorNames();
			for (String name : gbCollectorNames) {
				File file = new File(directory, "/GarbageCollector_" + name + ".csv");
				GBCollector collector = gb_mxBean.getGarbageCollectorByName(name);
				List<Long> timeList = collector.getTimeList();
				List<Long> collectionCount = collector.getCollectionCount();
				List<Long> elapsedTime = collector.getCollectionTime();
				int size = collectionCount.size();
				
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
				pw.println("Collection Time,Collection Count,Elapsed Time");
				Date dt = new Date();
				int i = 0;
				for (; i < size; i ++) {
					dt.setTime(timeList.get(i));
					pw.println(new StringBuilder().append(df.format(dt)).append(",").append(collectionCount.get(i)).append(",").append(elapsedTime.get(i)));
				}
				pw.close();
				if (flushData) {
					clearData(timeList, collectionCount, elapsedTime);
					Logger.log(i + " Record(s) Flushed for Garbage Collector.");
				}
			}
		}
	}
	
	public void persistClassLoadingData() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ClassMBeanSharedObject c_mxBean = MBeanSharedObjectRepository.getInstance().getClass_mx_bean();
		
		synchronized (c_mxBean) {
			File file = new File(directory, "/ClassLoading.csv");
			List<Long> timeList = c_mxBean.getTimeList();
			List<Long> tc_countList = c_mxBean.getTargetClassType(ClassMBeanSharedObject.CLASS_TYPE_TOTAL).getCount();
			List<Long> lc_countList = c_mxBean.getTargetClassType(ClassMBeanSharedObject.CLASS_TYPE_LOADED).getCount();
			List<Long> unc_countList = c_mxBean.getTargetClassType(ClassMBeanSharedObject.CLASS_TYPE_UNLOADED).getCount();
			int size = tc_countList.size();
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, flushData)));
			pw.println("Timestamp,Total Class,Loaded Class,Unloaded Class");
			Date dt = new Date();
			int i = 0;
			for (; i < size; i ++) {
				dt.setTime(timeList.get(i));
				pw.println(new StringBuilder().append(df.format(dt)).append(",").append(tc_countList.get(i)).append(",").append(lc_countList.get(i)).append(",").append(unc_countList.get(i)));
			}
			pw.close();
			if (flushData) {
				clearData(timeList, tc_countList, lc_countList, unc_countList);
				Logger.log(i + " Record(s) Flushed for Classloader.");
			}
		}
	}
	
	private void clearData(List<?>... lists) {
		for (List<?> list : lists) {
			list.clear();
		}
	}
}
