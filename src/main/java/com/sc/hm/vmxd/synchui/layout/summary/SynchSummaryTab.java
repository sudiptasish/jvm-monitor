package com.sc.hm.vmxd.synchui.layout.summary;

import javax.swing.border.BevelBorder;

import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.panel.SynchVMMonitorTab;

public class SynchSummaryTab extends SynchVMMonitorTab {
	
	private static final long serialVersionUID = 1L;
	
	public SynchSummaryTab(String applicationId) throws Exception {
		super(applicationId);
		initSharedObj();
		initPanel();
		addComponent();
	}
	
	public void initSharedObj() {
		sharedObj = new SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED();
	}
	
	public void initPanel() throws Exception {
		panel_2 = new SynchSummaryDetailsPanel(application, sharedObj, 880, 700, new BevelBorder(BevelBorder.RAISED), null);
	}
	
	public void addComponent() {
		add(panel_2);
	}
}
