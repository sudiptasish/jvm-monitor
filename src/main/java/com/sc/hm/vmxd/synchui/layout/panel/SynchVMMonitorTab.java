package com.sc.hm.vmxd.synchui.layout.panel;

import javax.swing.JPanel;

import com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.common.SynchUISelectionPanel;

public abstract class SynchVMMonitorTab extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED sharedObj = null;
	
	protected String application = "";
	
	protected SynchUISelectionPanel panel_1 = null;
	protected SynchMonitoringGraphPanel panel_2 = null;

	public SynchVMMonitorTab(String applicationId) {
		super();
		application = applicationId;
	}
	
	public abstract void initSharedObj();
	
	public abstract void initPanel() throws Exception;
	
	public abstract void addComponent();
	
	public void startTabMonitorProcess() {
		panel_2.startProcess();
	}
	
	public void startTabMonitorProcess(AbstractMBeanProcessExecutor executor) {
		panel_2.startProcess(executor);
	}
	
	public void stopTabMonitorProcess() {		
		panel_2.stopProcess();
		sharedObj.releaseLockAndResumeProcess();
	}
}
