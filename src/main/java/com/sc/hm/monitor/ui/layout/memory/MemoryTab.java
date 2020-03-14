package com.sc.hm.monitor.ui.layout.memory;

import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.ui.layout.common.UISelectionPanel;
import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.monitor.ui.layout.panel.VMMonitorTab;

public class MemoryTab extends VMMonitorTab {
	
	private static final long serialVersionUID = 1L;
	
	public MemoryTab() throws Exception {
		super();
		initSharedObj();
		initPanel();
		addComponent();
	}
	
	public void initSharedObj() {
		sharedObj = new _UI_SELECTIONPANEL_GRAPHPANEL_SHARED();
		sharedObj.setSelectionName(MBeanSharedObjectRepository.getInstance().getMemory_mx_bean().HEAP_MEMORY);
	}
	
	public void initPanel() throws Exception {
		panel_1 = new UISelectionPanel(UISelectionPanel.PANEL_TYPE_MEMORY, sharedObj);
		panel_2 = new MemoryGraphPanel(sharedObj, 880, 610, new BevelBorder(BevelBorder.RAISED), null);
	}
	
	public void addComponent() {
		panel_1.setBounds(4, 0, 880, 80);
		panel_2.setBounds(4, 80 + 5, panel_2.getWidth(), panel_2.getHeight());
		add(panel_1);
		add(panel_2);
	}
}
