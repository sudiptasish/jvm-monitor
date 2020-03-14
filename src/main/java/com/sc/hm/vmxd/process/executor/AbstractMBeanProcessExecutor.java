package com.sc.hm.vmxd.process.executor;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;
import com.sc.hm.vmxd.service.ExecutorServiceHelper;

/**
 * 
 * @author Sudiptasish Chanda
 */
public abstract class AbstractMBeanProcessExecutor {
	
	public static final String MEMORY_MBEAN_PROCESS = "com.sc.hm.vmxd.process.MemoryMBeanSynchProcess";
	public static final String MPOOL_MBEAN_PROCESS = "com.sc.hm.vmxd.process.MemoryPoolMBeanSynchProcess";
	public static final String THREAD_MBEAN_PROCESS = "com.sc.hm.vmxd.process.ThreadMBeanSynchProcess";
    public static final String TOP_THREAD_MBEAN_PROCESS = "com.sc.hm.vmxd.process.TopThreadMBeanSynchProcess";
	public static final String GB_MBEAN_PROCESS = "com.sc.hm.vmxd.process.GarbageCollectorMBeanSynchProcess";
	public static final String CLASS_MBEAN_PROCESS = "com.sc.hm.vmxd.process.ClassMBeanSynchProcess";
	public static final String OS_MBEAN_PROCESS = "com.sc.hm.vmxd.process.OperatingSystemMBeanSynchProcess";
	public static final String RUNTIME_MBEAN_PROCESS = "com.sc.hm.vmxd.process.RuntimeMBeanSynchProcess";
	public static final String COMPILATION_MBEAN_PROCESS = "com.sc.hm.vmxd.process.CompilationMBeanSynchProcess";
	
	protected String[] executor_names = {"com.sc.hm.vmxd.process.ClassMBeanSynchProcess"
										, "com.sc.hm.vmxd.process.GarbageCollectorMBeanSynchProcess"
										, "com.sc.hm.vmxd.process.MemoryMBeanSynchProcess"
										, "com.sc.hm.vmxd.process.MemoryPoolMBeanSynchProcess"
										, "com.sc.hm.vmxd.process.OperatingSystemMBeanSynchProcess"
										, "com.sc.hm.vmxd.process.ThreadMBeanSynchProcess"
                                        , "com.sc.hm.vmxd.process.TopThreadMBeanSynchProcess"
										, "com.sc.hm.vmxd.process.CompilationMBeanSynchProcess"
										, "com.sc.hm.vmxd.process.RuntimeMBeanSynchProcess"
	};
	
	protected Runnable[] executor_processes = new Runnable[executor_names.length];
	
	private String executorName = "";
	
	private Executor executorService = null;
	
	public AbstractMBeanProcessExecutor(String name) {
		executorName = name;
	}
	
	/**
	 * Initialize the underlying executor.
	 * It will create a cached thread pool with no upper limit.
	 */
	protected void initializeExecutor() {
		executorService = ExecutorServiceHelper.getExecutorServiceHelper().createCachedExecutorPool();
	}
	
	/**
	 * Initialize the underlying executor service with fixed number of workers.
	 * @param pool_size
	 */
	protected void initializeExecutor(int pool_size) {
		executorService = ExecutorServiceHelper.getExecutorServiceHelper().createFixedExecutorPool(pool_size);
	}
	
	/**
	 * Return the unique executor name.
	 * @return String
	 */
	public String getExecutorName() {
		return executorName;
	}
	
	protected void _executeDirect(Runnable r) {
		executorService.execute(r);
	}
    
	/**
	 * This is the API which is used today to start the background MBean server process.
	 * 
	 * @param index        Index of the process name in the fixed array.
	 * @param process      Name of the process.
	 * @param application  Application Id
	 * @param latch        Count down latch.
	 * @param observer     Process Observer instance. 
	 */
    protected void _executeInternal(int index
            , String process
            , String application
            , CountDownLatch latch
            , SynchProcessObserver observer) {
        
        try {
            Runnable mbeanSynchProcess = createProcess(process, application, latch, observer);
            executor_processes[index] = mbeanSynchProcess;
            executorService.execute(mbeanSynchProcess);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    /**
     * This API will be called from the desktop application viewer, where we
     * show status of multiple processes that belong to multiple JVM.
     * 
     * @param process       Name of the process.
     * @param application   Application Id
     * @param mxbeanServer  Remote mbean server
     */
	protected void _executeInternal(String process, String application, MXBeanServer mxbeanServer) {
		try {
		    Runnable mbeanSynchProcess = createProcess(process, application, mxbeanServer);  
			executorService.execute(mbeanSynchProcess);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Future<Object> _executeAsynchTask(String process
	        , String application
	        , MXBeanServer mxbeanServer) throws Exception {
	    
		try {
			Class<?> clazz = Class.forName(process);
			Constructor<?> constructor = clazz.getDeclaredConstructor(MXBeanServer.class, String.class);
			Callable<Object> mbeanAsynchProcess = (Callable<Object>)constructor.newInstance(mxbeanServer, application);
			
			return ((ExecutorService)executorService).submit(mbeanAsynchProcess);
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Create and return a new executable process.
	 * 
	 * @param process      Name of the process
	 * @param params       Arguments.
	 * @return Runnable
	 */
	private Runnable createProcess(String process, Object... params) throws Exception {
	    Class<?>[] paramTypes = new Class[params.length];
	    for (int i = 0; i < params.length; i ++) {
	        paramTypes[i] = params[i].getClass();
	    }
	    Class<?> clazz = Class.forName(process);
        Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
        Runnable mbeanSynchProcess = (Runnable)constructor.newInstance(params);
        
        return mbeanSynchProcess;
	}
	
	/**
	 * Shutdown the underlying executor service.
	 * Once the executor is stopped, no more task will be accepted.
	 */
	public void stopExecutorPool() {
		((ExecutorService)executorService).shutdown();
	}
	
	/**
	 * Force shutdown the executor service.
	 */
	public void stopExecutorPoolImmediate() {
		((ExecutorService)executorService).shutdownNow();
	}
	
	/**
	 * Return the total number of process count.
	 * @return int
	 */
	public abstract int getExecutorProcessCount();
	
	/**
	 * This is the entry point to start all the server (background) processes.
	 * The MXBeanServer parameter is not needed, because the inderlying processes
	 * will fetch the remote MBean server proxy from the server store with the
	 * help of the applicationId.
	 * 
	 * @param mxbeanServer      MBean Server
     * @param application       Unique Application Id
     * @param latch             Count down latch
	 * @param observer
	 */
	public abstract void startExecuting(MXBeanServer mxbeanServer
	        , String application
	        , CountDownLatch latch
	        , SynchProcessObserver observer);

	/**
	 * Directly execute the runnable object.
	 * @param r
	 */
    public abstract void startExecuting(Runnable r);

    /**
     * Start the specific process as identified by process parameter.
     * 
     * @param mxbeanServer      MBean Server
     * @param application       Unique Application Id
     * @param process           Executor process to be started.
     */
	public abstract void startCustomExecuting(MXBeanServer mxbeanServer
	        , String application
	        , String process);
	
	/**
	 * The behavior of this method is same as the above, except the fact
	 * that, instead of executing a runnable object, it wil submit a Callable.
	 * Caller has to invoke the Future.get() API to know the status of the process.
	 * 
	 * @param mxbeanServer      MBean Server
     * @param application       Unique Application Id
     * @param process           Executor process to be started.
     * 
     * @return Future           Future object.
	 * @throws Exception
	 */
	public abstract Future<Object> submitFutureTask(MXBeanServer mxbeanServer
	        , String application
	        , String process) throws Exception;
}
