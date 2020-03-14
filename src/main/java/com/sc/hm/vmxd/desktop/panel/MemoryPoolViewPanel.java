package com.sc.hm.vmxd.desktop.panel;

import java.awt.LayoutManager;
import java.util.concurrent.CountDownLatch;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.MemoryPoolDataRepository;
import com.sc.hm.vmxd.data.memory.MemoryPoolData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;

public class MemoryPoolViewPanel extends MonitoredComponentViewPanel {
	
	private static final long serialVersionUID = 1L;

	private String applicationId = "";
	
	private MemoryPoolDataRepository memoryPoolRepository = null;
	
	private String[] memoryPoolNames = null;
	
	private StatisticsViewPanel[] statisticsPanel = null;

	private CountDownLatch initLatch = new CountDownLatch(1);

	public MemoryPoolViewPanel(String applicationId, int panel_width, int panel_height) {
		this(applicationId, panel_width, panel_height, new TitledBorder(""), null);
	}

	public MemoryPoolViewPanel(String applicationId, int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
		this.applicationId = applicationId;
		initMonitoringProcess(initLatch);
		initializeListener(initLatch);
		initOther();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.MEMORY_POOL_LOCK);
			memoryPoolRepository = (MemoryPoolDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY_POOL);			
			memoryPoolRepository.registerListener(listener, new StringBuilder().append("MemoryPoolViewPanel - ").append(applicationId).toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initializeAndAddComponent() {
		if (memoryPoolRepository.isInitializedRepository()) {
			initializeComponent();
			addAllComponent();
		}
		repaint();
	}
	
	private void initializeComponent() {
		memoryPoolNames = memoryPoolRepository.getMemoryPoolNames();
		statisticsPanel = new StatisticsViewPanel[memoryPoolNames.length];
		for (int i = 0; i < statisticsPanel.length; i ++) {
			statisticsPanel[i] = new StatisticsViewPanel(24, 90, new EtchedBorder(), null);
			statisticsPanel[i].setBounds(10 + (i * (24 + 4)), 20, 24, 90);
			statisticsPanel[i].setToolTipText(memoryPoolNames[i]);
		}
	}
	
	private void addAllComponent() {
		if (statisticsPanel != null) {
			for (StatisticsViewPanel sViewPanel : statisticsPanel) {
				add(sViewPanel);
			}
		}
	}
	
	public void startComponentMonitoring() throws Exception {
		long start_time = 0L;
		try {
			thirdPartyLock.startGetItem();
			start_time = System.currentTimeMillis();
			for (int i = 0; i < memoryPoolNames.length; i ++) {
				MemoryPoolData memoryPoolData = memoryPoolRepository.getMemoryPoolData(memoryPoolNames[i]);
				statisticsPanel[i].setMax_value(memoryPoolData.getMax());
				statisticsPanel[i].setCurrent_value(memoryPoolData.getCurrentUsed());
				statisticsPanel[i].repaint();
			}
		}
		finally {
			thirdPartyLock.endGetItem();
		}
		Logger.log(new StringBuilder().append("Time [MemoryViewPanel]: ").append(System.currentTimeMillis() - start_time).append(" Millis").toString());
	}
}
