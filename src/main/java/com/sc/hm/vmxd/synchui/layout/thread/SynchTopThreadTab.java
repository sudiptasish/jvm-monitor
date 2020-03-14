package com.sc.hm.vmxd.synchui.layout.thread;

import java.awt.BorderLayout;

import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.panel.SynchVMMonitorTab;

public class SynchTopThreadTab extends SynchVMMonitorTab {

	private static final long serialVersionUID = 1L;
	private SynchSingleThreadDetailsPanel panel_3 = null;
	
	public SynchTopThreadTab(String applicationId) throws Exception {
		super(applicationId);
		initSharedObj();
		initPanel();
		addComponent();
	}
	
	public void initSharedObj() {
		sharedObj = new SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED();
		sharedObj.setSelectionName("Top Thread");
	}
	
	public void initPanel() throws Exception {
		panel_2 = new SynchTopThreadPanel(application, sharedObj, 880, 450);
		panel_3 = new SynchSingleThreadDetailsPanel();
		
		((SynchTopThreadPanel)panel_2).setDetailsPanel(panel_3);
	}
	
	public void addComponent() {
		add(panel_2, BorderLayout.NORTH);
		add(panel_3, BorderLayout.SOUTH);
	}
}
