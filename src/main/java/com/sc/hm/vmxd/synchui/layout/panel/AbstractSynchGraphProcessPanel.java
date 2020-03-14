package com.sc.hm.vmxd.synchui.layout.panel;

import java.awt.LayoutManager;

import javax.swing.border.Border;

import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor;

/**
 * A Panel especially designed to display graph data.
 * A graph panel will have a processor thread associated to it, which will be
 * responsible for periodically fetching the utilization data from specific repository
 * and update the panel.
 * 
 * @author Sudiptasish Chanda
 */
public abstract class AbstractSynchGraphProcessPanel extends SynchBasicGraphPanel implements SynchComponentProcessActivity {
	
	protected static ApplicationConfiguration appConfig = ApplicationConfiguration.getInstance();

	private static final long serialVersionUID = 1L;
	private boolean isRun = false;
	private boolean forceStop = false;
	protected ComponentProcess graphProcess = null;
	
	private Thread processThread = null;

	public AbstractSynchGraphProcessPanel(int panel_width, int panel_height) {
		super(panel_width, panel_height);
	}
	
	public AbstractSynchGraphProcessPanel(int panel_width
	        , int panel_height
	        , Border panel_border
	        , LayoutManager layout) {
	    
		super(panel_width, panel_height, panel_border, layout);
	}
	
	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	
	public boolean isForceStop() {
		return forceStop;
	}

	public void setForceStop(boolean forceStop) {
		this.forceStop = forceStop;
	}

    @Override
	public void initializeComponentProcess(ComponentProcess graphProcess) {
		this.graphProcess = graphProcess;
	}
	
	protected void _startProcessInternal() {
		processThread = new Thread(graphProcess);
		processThread.start();
	}
	
	protected void _startProcessInternal(AbstractMBeanProcessExecutor executor) {
		executor.startExecuting(graphProcess);
	}
	
	protected void _stopProcessInternal() {
		try {
			processThread.join();
		}
		catch (InterruptedException inte) {
			inte.printStackTrace();
		}
	}
	
    /**
     * Entry point for starting the monitoring process.
     * @throws Exception 
     */
	public abstract void startSynchronizedMonitoring() throws Exception;
	
	//public abstract void startAsynchronizedMonitoring();

	public interface ComponentProcess extends Runnable {
		public void updateComponent() throws Exception;
		public void notifyOwner();
	}
}
