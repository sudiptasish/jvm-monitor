package com.sc.hm.vmxd.synchui.layout.panel;

import javax.swing.JPanel;

public abstract class SynchVMStaticTab extends JPanel {

	private static final long serialVersionUID = 1L;
	
	protected String application = "";

	public SynchVMStaticTab(String applicationId) {
		super();
		application = applicationId;
	}
	
	public abstract void initSharedObj();
	
	public abstract void initPanel() throws Exception;
	
	public abstract void addComponent();
	
	public abstract void loadStaticVMData() throws Exception;
}
