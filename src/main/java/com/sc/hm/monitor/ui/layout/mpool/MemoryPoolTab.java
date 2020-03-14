package com.sc.hm.monitor.ui.layout.mpool;

import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.ui.layout.common.UISelectionPanel;
import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.monitor.ui.layout.panel.VMMonitorTab;

public class MemoryPoolTab extends VMMonitorTab {
	
	private static final long serialVersionUID = 1L;
	
	public MemoryPoolTab() throws Exception {
		super();
		initSharedObj();
		initPanel();
		addComponent();
	}
	
	public void initSharedObj() {
		sharedObj = new _UI_SELECTIONPANEL_GRAPHPANEL_SHARED();
		sharedObj.setSelectionName(MBeanSharedObjectRepository.getInstance().getMpool_mx_bean().getFirstMemoryPoolName());
	}
	
	public void initPanel() throws Exception {
		panel_1 = new UISelectionPanel(UISelectionPanel.PANEL_TYPE_MEMORYPOOL, sharedObj);
		panel_2 = new MemoryPoolGraphPanel(sharedObj, 880, 610, new BevelBorder(BevelBorder.RAISED), null);
	}
	
	public void addComponent() {
		panel_1.setBounds(4, 0, 880, 80);
		panel_2.setBounds(4, 80 + 5, panel_2.getWidth(), panel_2.getHeight());
		add(panel_1);
		add(panel_2);
	}
}
