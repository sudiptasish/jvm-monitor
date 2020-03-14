package com.sc.hm.monitor.launcher;

import java.lang.management.MemoryPoolMXBean;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.config.ServerConfig;
import com.sc.hm.monitor.launcher.observer.MBeanProcessObserver;
import com.sc.hm.monitor.mbean.factory.ManagementBeanFactory;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.ManagementUtil;
import com.sc.hm.monitor.worker.pool.WorkerThreadManager;
import com.sc.hm.monitor.worker.process.WorkerTask;
import com.sc.hm.monitor.worker.process.WorkerThread;

public class MBeanProcessLauncher {
	
	private static ApplicationConfiguration appConfig = ApplicationConfiguration.getInstance();
    
    private static WorkerThreadManager threadManager = WorkerThreadManager.getManager();
    
    private static ManagementBeanFactory mbeanFactory = null;
    
    private static MBeanProcessObserver observer = null;
    
    private static final Map<String, String> processMap = new HashMap<String, String>(8);
    
    private static Object lock = new Object();

    static {
        init();
        configureManager();
        initializeObserver();
    }
    
    private static void init() {
        processMap.put(ManagementUtil.CLASS_MBEAN_PROCESS, "com.sc.hm.monitor.process.classes.ClassMBeanProcess");
        processMap.put(ManagementUtil.COMPILE_MBEAN_PROCESS, "com.sc.hm.monitor.process.compile.CompilationMBeanProcess");
        processMap.put(ManagementUtil.GBCOLLECTOR_MBEAN_PROCESS, "com.sc.hm.monitor.process.gbcollector.GBCollectorMBeanProcess");
        processMap.put(ManagementUtil.MEMORY_MBEAN_PROCESS, "com.sc.hm.monitor.process.memory.MemoryMBeanProcess");
        processMap.put(ManagementUtil.MEMORYPOOL_MBEAN_PROCESS, "com.sc.hm.monitor.process.mpool.MemoryPoolMBeanProcess");
        processMap.put(ManagementUtil.OS_MBEAN_PROCESS, "com.sc.hm.monitor.process.os.OperatingSystemMBeanProcess");
        processMap.put(ManagementUtil.RUNTIME_MBEAN_PROCESS, "com.sc.hm.monitor.process.runtime.RuntimeMBeanProcess");
        processMap.put(ManagementUtil.THREAD_MBEAN_PROCESS, "com.sc.hm.monitor.process.threads.ThreadMBeanProcess");
    }
    
    private static void configureManager() {
    	threadManager.createThreadPool(ManagementUtil.DEFAULT_WORKER_THREAD_POOL_NAME, 20);
    }
    
    private static void initializeObserver() {
    	observer = new MBeanProcessObserver(lock);
    }
    
    public static void initMBeanFactory(String vmType, ServerConfig serverConfig) throws Exception {
    	if (vmType.equalsIgnoreCase(ManagementUtil.JVM_TYPE_LOCAL)) {
    		mbeanFactory = ManagementBeanFactory.getManagementBeanFactory(ManagementBeanFactory.FACTORY_TYPE_LOCAL);
    		mbeanFactory.initializeMBeanFactory(null);
    	}
    	else if (vmType.equalsIgnoreCase(ManagementUtil.JVM_TYPE_REMOTE)) {
    		mbeanFactory = ManagementBeanFactory.getManagementBeanFactory(ManagementBeanFactory.FACTORY_TYPE_REOMTE);
    		mbeanFactory.initializeMBeanFactory(new Object[] {serverConfig.getServerName(), serverConfig.getServerPort(), serverConfig.getUrl()});
    	}
    }
    
    public static void destroyMBeanConnection() {
    	mbeanFactory.disconnectMXBeanServer();
    }
    
    public static ManagementBeanFactory getManagementBeanFactory() {
    	return mbeanFactory;
    }
    
    public static void setProgressLevelParameter(JProgressBar bar, JLabel label) {
    	observer.setProgressLevelParameter(bar, label);
	}
    
    public static void launchAllProcess(String vmType, ServerConfig serverConfig) throws Exception {
    	initMBeanFactory(vmType, serverConfig);
    	Vector<WorkerTask> vector = new Vector<WorkerTask>(processMap.size());
        try {
            int i = 0;            
            for (Iterator<Map.Entry<String, String>> itr = processMap.entrySet().iterator(); itr.hasNext(); i ++) {
                Map.Entry<String, String> me = itr.next();
                String mbeanType = me.getKey();
                String processId = me.getValue();
                
                Constructor<?> constructor = Class.forName(processId)
                        .getDeclaredConstructor(new Class[] {String.class, ManagementBeanFactory.class});
                Object obj = constructor.newInstance(new Object[] {mbeanType, mbeanFactory});
                observer.setStatusForProcess(obj);
                ((Observable)obj).addObserver(observer);
                vector.addElement((WorkerTask)obj);
            }
            _launchProcessInternal(vector);
            waitTillStatusRunning();
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
    }

	private static void _launchProcessInternal(Vector<WorkerTask> vector) {		
		for (WorkerTask task : vector) {
			 WorkerThread worker = threadManager.getNextAvailableWorkerFromPool(ManagementUtil.DEFAULT_WORKER_THREAD_POOL_NAME);
			 worker.setName(ManagementUtil.MGMT_WORKER_THREAD + "-" + task.getClass().getName());
			 appConfig.putThreadReference(worker);
             worker.executeTask(task);
             try {
				Thread.sleep(100);
             }
             catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
    
    public static void launchProcess(String processId, String mbeanType) {
        try {
            Constructor constructor = Class.forName(processMap.get(processId)).getDeclaredConstructor(new Class[] {String.class, ManagementBeanFactory.class});
            Object obj = constructor.newInstance(new Object[] {mbeanType, mbeanFactory});
            WorkerThread worker = threadManager.getNextAvailableWorkerFromPool(ManagementUtil.DEFAULT_WORKER_THREAD_POOL_NAME);
            worker.executeTask((WorkerTask)obj);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void setThreshold(String memory, Long thresholdValue) {
    	List<MemoryPoolMXBean> mpoolMXBeans = mbeanFactory.getMemoryPoolMXBeans();
		for (MemoryPoolMXBean mpoolMXBean : mpoolMXBeans) {
			if (mpoolMXBean.getName().equalsIgnoreCase(memory)) {
				synchronized (mpoolMXBean) {
					mpoolMXBean.setUsageThreshold(thresholdValue.longValue());
				}
				break;
			}
		}
    }
    
    public static void waitTillStatusRunning() {
    	System.out.print("Waiting for All Process State to Become RUNNING....");
    	try {
    		synchronized (lock) {
    			if (!observer.isComplete()) {
	    			lock.wait();
	    		}
    		}
    	}
    	catch (InterruptedException inte) {
    		inte.printStackTrace();
    	}
    	Logger.log("All Process Up!!!!");
    }
    
    public static void stopAllProcess() {
        
    }
    
    public static void stopProcess(String processId) {
        
    }
}
