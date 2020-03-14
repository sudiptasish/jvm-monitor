package com.sc.hm.vmxd.synchui.layout.gbcollector;

import javax.swing.border.BevelBorder;

import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.GarbageCollectorDataRepository;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.common.SynchUISelectionPanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchVMMonitorTab;

public class SynchGBCollectorTab extends SynchVMMonitorTab {

	private static final long serialVersionUID = 1L;
	
	public SynchGBCollectorTab(String applicationId) throws Exception {
		super(applicationId);
		initSharedObj();
		initPanel();
		addComponent();
	}
	
	public void initSharedObj() {
		sharedObj = new SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED();
		try {
			sharedObj.setSelectionName(((GarbageCollectorDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(application).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR)).getGarbageCollectorNames()[0]);
		}
		catch (Exception e) {}
	}
	
	public void initPanel() throws Exception {
		panel_1 = new SynchUISelectionPanel(application, SynchUISelectionPanel.PANEL_TYPE_GBCOLLECTOR, sharedObj);
		panel_2 = new SynchGBCollectorGraphPanel(application, sharedObj, 880, 610, new BevelBorder(BevelBorder.RAISED), null);
	}
	
	public void addComponent() {
		panel_1.setBounds(4, 0, 880, 80);
		panel_2.setBounds(4, 80 + 5, panel_2.getWidth(), panel_2.getHeight());
		add(panel_1);
		add(panel_2);
	}
}
