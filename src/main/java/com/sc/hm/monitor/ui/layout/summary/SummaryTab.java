package com.sc.hm.monitor.ui.layout.summary;

import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.monitor.ui.layout.panel.VMMonitorTab;

public class SummaryTab extends VMMonitorTab {
	
	private static final long serialVersionUID = 1L;
	
	public SummaryTab() throws Exception {
		super();
		initSharedObj();
		initPanel();
		addComponent();
	}
	
	public void initSharedObj() {
		sharedObj = new _UI_SELECTIONPANEL_GRAPHPANEL_SHARED();
	}
	
	public void initPanel() throws Exception {
		panel_2 = new SummaryDetailsPanel(sharedObj, 880, 700, new BevelBorder(BevelBorder.RAISED), null);
	}
	
	public void addComponent() {
		add(panel_2);
	}
}
