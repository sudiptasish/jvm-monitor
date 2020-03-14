package com.sc.hm.vmxd.synchui.layout.panel;

/**
 * Interface that defines a component activity.
 * All the GUI has a processor thread attached to it, which is responsible for
 * updating the graph data and other dynamic info.
 * 
 * @author Sudiptasish Chanda
 */
public interface SynchComponentProcessActivity {
	
	public void initializeComponentProcess(AbstractSynchGraphProcessPanel.ComponentProcess graphProcess);

    /**
     * Start the ui processor thread.
     */
	public void startProcess();

    /**
     * Stop the ui processor thread.
     */
	public void stopProcess();

    /**
     * Check if the ui processor thread is running.
     * @return boolean
     */
	public boolean isRun();
}
