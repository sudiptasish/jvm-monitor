package com.sc.hm.monitor.ui.layout.thread;

import java.awt.BorderLayout;

import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.ui.layout.common.UISelectionPanel;
import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.monitor.ui.layout.panel.VMMonitorTab;

public class ThreadTab extends VMMonitorTab {

	private static final long serialVersionUID = 1L;
	private ThreadDetailsPanel panel_3 = null;
	
	public ThreadTab() throws Exception {
		super();
		initSharedObj();
		initPanel();
		addComponent();
	}
	
	public void initSharedObj() {
		sharedObj = new _UI_SELECTIONPANEL_GRAPHPANEL_SHARED();
		sharedObj.setSelectionName("Main Thread");
	}
	
	public void initPanel() throws Exception {
		panel_1 = new UISelectionPanel(UISelectionPanel.PANEL_TYPE_THREAD, sharedObj);
		panel_2 = new ThreadGraphPanel(sharedObj, 880, 375, new BevelBorder(BevelBorder.RAISED), null);
		panel_3 = new ThreadDetailsPanel();
	}
	
	public void addComponent() {
		add(panel_1, BorderLayout.NORTH);
		add(panel_2, BorderLayout.CENTER);
		add(panel_3, BorderLayout.SOUTH);
	}
}
