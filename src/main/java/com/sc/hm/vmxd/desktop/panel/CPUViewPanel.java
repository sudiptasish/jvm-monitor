package com.sc.hm.vmxd.desktop.panel;

import java.awt.LayoutManager;
import java.text.NumberFormat;
import java.util.concurrent.CountDownLatch;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.OperatingSystemDataRepository;
import com.sc.hm.vmxd.data.os.OperatingSystemData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;

public class CPUViewPanel extends MonitoredComponentViewPanel {
	
	private static final long serialVersionUID = 1L;

	private String applicationId = "";
	
	private OperatingSystemDataRepository osRepository = null;
	
	private CountDownLatch initLatch = new CountDownLatch(1);
	
	private StatisticsViewPanel statisticsPanel = null;
	
	private JLabel tCPULabel = new JLabel("CPU Usage:");	
	private JLabel tCPULabel_R = new JLabel("");
	
	private NumberFormat n_format = NumberFormat.getInstance();

	public CPUViewPanel(String applicationId, int panel_width, int panel_height) {
		this(applicationId, panel_width, panel_height, new TitledBorder(""), null);
	}

	public CPUViewPanel(String applicationId, int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
		this.applicationId = applicationId;
		initMonitoringProcess(initLatch);
		initializeListener(initLatch);
		initOther();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.OPERATING_SYSTEM_LOCK);
			osRepository = (OperatingSystemDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_OPERATING_SYSTEM);
			osRepository.registerListener(listener, new StringBuilder().append("CPUViewPanel - ").append(applicationId).toString());
			
			n_format.setMaximumFractionDigits(2);
			n_format.setMinimumFractionDigits(2);
			n_format.setGroupingUsed(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initializeAndAddComponent() {
		if (osRepository.isInitializedRepository()) {
			initializeComponent();
			addAllComponent();
		}
		repaint();
	}
	
	private void initializeComponent() {		
		tCPULabel.setFont(font);
		tCPULabel_R.setFont(font);
		
		statisticsPanel = new StatisticsViewPanel(24, 90, new EtchedBorder(), null);
		statisticsPanel.setBounds(10, 20, 24, 90);
		
		tCPULabel.setBounds(50, 20, 100, 15);
		tCPULabel_R.setBounds(50, 20 + 15 + 1, 120, 15);		
	}
	
	private void addAllComponent() {
		add(statisticsPanel);
		
		add(tCPULabel);
		add(tCPULabel_R);
	}
	
	public void startComponentMonitoring() throws Exception {
		try {
			thirdPartyLock.startGetItem();
			OperatingSystemData osData = osRepository.getOsData();
			double cpuTime = osData.getCurrentCpuTime() / 1000000000.0D;
			tCPULabel_R.setText(n_format.format(cpuTime) + " Seconds");
			statisticsPanel.repaint();
		}
		finally {
			thirdPartyLock.endGetItem();
		}
	}
}
