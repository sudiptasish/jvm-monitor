package com.sc.hm.monitor.ui.layout.panel;

import javax.swing.JPanel;

import com.sc.hm.monitor.ui.layout.common.UISelectionPanel;
import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;

public abstract class VMMonitorTab extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected _UI_SELECTIONPANEL_GRAPHPANEL_SHARED sharedObj = null;
	
	protected UISelectionPanel panel_1 = null;
	protected MonitoringGraphPanel panel_2 = null;

	public VMMonitorTab() {
		super();
	}
	
	public abstract void initSharedObj();
	
	public abstract void initPanel() throws Exception;
	
	public abstract void addComponent();
	
	public void startTabMonitorProcess() {
		panel_2.startProcess();
	}
	
	public void stopTabMonitorProcess() {		
		panel_2.stopProcess();
		sharedObj.releaseLockAndResumeProcess();
	}
}
