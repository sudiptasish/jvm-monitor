package com.sc.hm.vmxd.desktop.panel;

import java.awt.LayoutManager;
import java.util.concurrent.CountDownLatch;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class MBeansViewPanel extends MonitoredComponentViewPanel {
	
	private static final long serialVersionUID = 1L;

	private String applicationId = "";
	
	private CountDownLatch initLatch = new CountDownLatch(1);
	
	public MBeansViewPanel(String applicationId, int panel_width, int panel_height) {
		this(applicationId, panel_width, panel_height, new TitledBorder(""), null);
	}

	public MBeansViewPanel(String applicationId, int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
		this.applicationId = applicationId;
		initMonitoringProcess(initLatch);
		initializeListener(initLatch);
		initOther();
	}
	
	private void initOther() {
		
	}
	
	public void initializeAndAddComponent() {
		initializeComponent();
		addAllComponent();
		repaint();
	}
	
	private void initializeComponent() {
		
	}
	
	private void addAllComponent() {
		
	}
	
	public void startComponentMonitoring() throws Exception {
		
	}
}
