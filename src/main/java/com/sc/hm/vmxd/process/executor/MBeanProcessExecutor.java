package com.sc.hm.vmxd.process.executor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;

class MBeanProcessExecutor extends AbstractMBeanProcessExecutor {
	
	public MBeanProcessExecutor(String name) {
		super(name);
		initializeExecutor();
	}

	public MBeanProcessExecutor(String name, int executor_count) {
		super(name);
		initializeExecutor(executor_count);
	}

	/* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor#getExecutorProcessCount()
     */
    @Override
    public int getExecutorProcessCount() {
		return executor_names.length;
	}

    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor#startExecuting()
     */
    @Override
    public void startExecuting(MXBeanServer mxbeanServer
            , String application
            , CountDownLatch latch
            , SynchProcessObserver observer) {
        
        for (int i = 0; i < executor_names.length; i ++) {
            _executeInternal(i, executor_names[i], application, latch, observer);
            Logger.log(String.format("Started Process %s [Application: %s]. Latch: %s"
                    , executor_names[i]
                    , application
                    , latch));
        }
    }
	
    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor#startExecuting(java.lang.Runnable)
     */
    @Override
    public void startExecuting(Runnable r) {
		_executeDirect(r);
	}

    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor#startCustomExecuting(com.sc.hm.vmxd.jmx.MXBeanServer, java.lang.String, java.lang.String)
     */
    @Override
    public void startCustomExecuting(MXBeanServer mxbeanServer
            , String application
            , String process) {
        
		_executeInternal(process, application, mxbeanServer);
		Logger.log(String.format("Started Process %s [Application: %s]", process, application));
	}
	
    /* (non-Javadoc)
     * @see com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor#submitFutureTask(com.sc.hm.vmxd.jmx.MXBeanServer, java.lang.String, java.lang.String)
     */
    @Override
    public Future<Object> submitFutureTask(MXBeanServer mxbeanServer
	        , String application
	        , String process) throws Exception {
	    
		return _executeAsynchTask(process, application, mxbeanServer);
	}
}
