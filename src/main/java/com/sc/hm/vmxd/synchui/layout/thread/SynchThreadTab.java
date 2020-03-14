package com.sc.hm.vmxd.synchui.layout.thread;

import java.awt.BorderLayout;

import javax.swing.border.BevelBorder;

import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.common.SynchUISelectionPanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchVMMonitorTab;

public class SynchThreadTab extends SynchVMMonitorTab {

	private static final long serialVersionUID = 1L;
	private SynchThreadDetailsPanel panel_3 = null;
	
	public SynchThreadTab(String applicationId) throws Exception {
		super(applicationId);
		initSharedObj();
		initPanel();
		addComponent();
	}
	
	public void initSharedObj() {
		sharedObj = new SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED();
		sharedObj.setSelectionName("Main Thread");
	}
	
	public void initPanel() throws Exception {
		panel_1 = new SynchUISelectionPanel(application, SynchUISelectionPanel.PANEL_TYPE_THREAD, sharedObj);
		panel_2 = new SynchThreadGraphPanel(application, sharedObj, 880, 375, new BevelBorder(BevelBorder.RAISED), null);
		panel_3 = new SynchThreadDetailsPanel();
	}
	
	public void addComponent() {
		add(panel_1, BorderLayout.NORTH);
		add(panel_2, BorderLayout.CENTER);
		add(panel_3, BorderLayout.SOUTH);
	}
}
