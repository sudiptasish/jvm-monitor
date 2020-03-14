package com.sc.hm.vmxd.process;

import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.ConnectException;
import java.text.MessageFormat;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.data.DataRepository;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.lock.ThirdPartySynchronizedLock;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;

public abstract class AbstractMBeanSynchProcess extends Observable implements Runnable {
	
    private String processName = "";
	protected String applicationId = "";
	
	protected ThirdPartySynchronizedLock thirdPartyLock = null;
	
	protected CountDownLatch doneInitCountDown = null;
	
	private long pauseTime = 4000L;
	private boolean runProcess = true;

	protected MXBeanServer mxbeanServer = null;
	
	protected DataRepository repository = null;
	
	private SynchProcessObserver observer = null;
	
	protected AbstractMBeanSynchProcess(String applicationId
	        , CountDownLatch doneInitCountDown
	        , SynchProcessObserver observer) {
	    
	    this.processName = getClass().getSimpleName();
    	this.applicationId = applicationId;
		this.doneInitCountDown = doneInitCountDown;
		this.observer = observer;
		initMXBeanServer();    		
        
		if (this.observer != null) {			
			observer.addActiveProcess(this);
			addObserver(observer);
		}
	}
	
	/**
     * @return the processName
     */
    public String getProcessName() {
        return processName;
    }

    /**
	 * Get a handle to the remote MBean server that this process will
	 * periodically connect to fetch certain metrics.
	 */
	private void initMXBeanServer() {
	    try {
	        this.mxbeanServer = MXBeanServerManager.getMXBeanServer(SynchApplicationConfiguration
                .getSynchInstance().getEnvironmentConfig().getApplicationId());
	    }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	/**
	 * Set the data repository for this process.
	 * The process periodically connects to th eremote MBean server and populates
	 * the metrics in this repository.
	 * 
	 * @param repository
	 */
	protected void setdataRepository(DataRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * Initialize the lock that will be used to syncup the repository.
	 * @param lock_name
	 */
	protected void initializeLock(String lock_name) {
		try {
			setThirdPartyLock(AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(lock_name));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the third party lock object.
	 * @param thirdPartyLock
	 */
	public void setThirdPartyLock(ThirdPartySynchronizedLock thirdPartyLock) {
		this.thirdPartyLock = thirdPartyLock;
	}
	
	/**
	 * Set the time as to how much this process will sleep in between two
	 * consecutive operation.
	 * @param time
	 */
	public synchronized void setPauseTime(long time) {
		pauseTime = time;
	}
	
	/**
	 * Set the flag to enable/disable the run.
	 * @param flag
	 */
	public synchronized void setRunProcess(boolean flag) {
		runProcess = flag;
	}

	/**
	 * Return the pause time of this process.
	 * @return long
	 */
	public long getPauseTime() {
		return pauseTime;
	}

	/**
	 * Check to see if this process is in running state.
	 * @return boolean     True, if running. False otherwise.
	 */
	public boolean isRunProcess() {
		return runProcess;
	}
	
	/**
	 * Perform post shutdown formalities.
	 * It may include cleaning up any resource that this process currently holds.
	 * @param processName
	 * @param e
	 */
	public synchronized void performShutdownFormality(String processName, Exception e) {
		Logger.log("Remote Connection Broken.... Exiting Process " + processName);
		if (this.observer != null) {
			setChanged();
			notifyObservers(e);
		}
	}
	
	public synchronized void run() {
	    String template = "Time [{0} Thread. Application: {1}] - {2} Millis";
	    
        try {
            try {
                thirdPartyLock.startAddItem();
                initialize();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {           
                thirdPartyLock.endAddItem();
                if (doneInitCountDown != null) {
                    doneInitCountDown.countDown();
                }
                Logger.log("[" + getProcessName() + "] Initialized Thread Repository.");
            }
            
            while (!Thread.currentThread().isInterrupted() && isRunProcess()) {
                long start_time = System.currentTimeMillis();
                try {
                    thirdPartyLock.startAddItem();
                    doWork();
                }
                catch (ConnectException ce) {
                    performShutdownFormality(getClass().getName(), ce);
                    break;
                }
                catch (UndeclaredThrowableException unte) {
                    performShutdownFormality(getClass().getName(), unte);
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    thirdPartyLock.endAddItem();                    
                }               
                long time_diff = System.currentTimeMillis() - start_time;
                Logger.log(MessageFormat.format(template, getProcessName(), applicationId, time_diff));
                
                try {
                    wait((getPauseTime() - time_diff) < 0 ? getPauseTime() : getPauseTime() - time_diff);
                }
                catch (InterruptedException inte) {
                    Logger.log("Thread " + getProcessName() + " interrupted. Exiting....");
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Does some initial work.
	 * @throws Exception
	 */
	public void initialize() throws Exception {}
	
	/**
	 * Entry point fot the actual work that this process would be performing.
	 * 
	 * @throws ConnectException
	 * @throws UndeclaredThrowableException
	 * @throws Exception
	 */
	public abstract void doWork() throws ConnectException, UndeclaredThrowableException, Exception;
}
