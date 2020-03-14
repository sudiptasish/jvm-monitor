package com.sc.hm.vmxd.desktop.panel;

import java.awt.LayoutManager;
import java.text.NumberFormat;
import java.util.concurrent.CountDownLatch;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.MemoryDataRepository;
import com.sc.hm.vmxd.data.memory.MemoryData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;

public class MemoryViewPanel extends MonitoredComponentViewPanel {
	
	private static final long serialVersionUID = 1L;
	
	private String applicationId = "";
	
	private MemoryDataRepository memoryRepository = null;
	
	private StatisticsViewPanel[] statisticsPanel = null;
	
	private String[] memoryNames = null;
	
	private JLabel[] mLabels = null;
	
	private JLabel[] maxMLabel = null;
	private JLabel[] usedMLabel = null;
	
	private JLabel[] maxMLabel_R = null;
	private JLabel[] usedMLabel_R = null;
	
	private NumberFormat n_format = NumberFormat.getInstance();

	private CountDownLatch initLatch = new CountDownLatch(1);

	public MemoryViewPanel(String applicationId, int panel_width, int panel_height) {
		this(applicationId, panel_width, panel_height, new TitledBorder(""), null);
	}

	public MemoryViewPanel(String applicationId, int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
		this.applicationId = applicationId;
		initMonitoringProcess(initLatch);
		initializeListener(initLatch);
		initOther();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.MEMORY_LOCK);
			memoryRepository = (MemoryDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY);
			memoryRepository.registerListener(listener, new StringBuilder().append("MemoryViewPanel - ").append(applicationId).toString());
			
			n_format.setMaximumFractionDigits(0);
			n_format.setMinimumFractionDigits(0);
			n_format.setGroupingUsed(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initializeAndAddComponent() {
		if (memoryRepository.isInitializedRepository()) {
			initializeComponent();
			addAllComponent();
		}
		repaint();
	}
	
	private void initializeComponent() {
		memoryNames = memoryRepository.getMemoryNames();
		mLabels = new JLabel[memoryNames.length];
		maxMLabel = new JLabel[memoryNames.length];
		maxMLabel_R = new JLabel[memoryNames.length];
		usedMLabel = new JLabel[memoryNames.length];
		usedMLabel_R = new JLabel[memoryNames.length];
		
		statisticsPanel = new StatisticsViewPanel[memoryNames.length];
		for (int i = 0, j = 0; i < statisticsPanel.length; i ++) {
			mLabels[i] = new JLabel(memoryNames[i]);
			mLabels[i].setFont(font);
			maxMLabel[i] = new JLabel("Max:");
			maxMLabel[i].setFont(font);
			maxMLabel_R[i] = new JLabel("");
			maxMLabel_R[i].setFont(font);
			usedMLabel[i] = new JLabel("Used:");
			usedMLabel[i].setFont(font);
			usedMLabel_R[i] = new JLabel("");
			usedMLabel_R[i].setFont(font);
			
			mLabels[i].setBounds(75, 3 + i * 62, 110, 15);
			maxMLabel[i].setBounds(75, 3 + i * 62 + (j + 1) * (15 + 1), 40, 15);
			maxMLabel_R[i].setBounds(75 + 40, 3 + i * 62 + (j + 1) * (15 + 1), 70, 15);
			usedMLabel[i].setBounds(75, 3 + i * 62 + (j + 2) * (15 + 1), 40, 15);
			usedMLabel_R[i].setBounds(75 + 40, 3 + i * 62 + (j + 2) * (15 + 1), 70, 15);
			
			statisticsPanel[i] = new StatisticsViewPanel(24, 90, new EtchedBorder(), null);
			statisticsPanel[i].setBounds(10 + (i * (24 + 4)), 20, 24, 90);
			statisticsPanel[i].setToolTipText(memoryNames[i]);
		}
	}
	
	private void addAllComponent() {
		if (mLabels != null) {
			for (int i = 0; i < mLabels.length; i ++) {
				add(mLabels[i]);
				add(maxMLabel[i]);
				add(maxMLabel_R[i]);
				add(usedMLabel[i]);
				add(usedMLabel_R[i]);
			}
		}
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
			for (int i = 0; i < memoryNames.length; i ++) {
				MemoryData memoryData = memoryRepository.getMemoryData(memoryNames[i]);
				maxMLabel_R[i].setText(n_format.format(memoryData.getMax()));
				usedMLabel_R[i].setText(n_format.format(memoryData.getCurrentUsed()));
				statisticsPanel[i].setMax_value(memoryData.getMax());
				statisticsPanel[i].setCurrent_value(memoryData.getCurrentUsed());
				statisticsPanel[i].repaint();			}
			
		}
		finally {
			thirdPartyLock.endGetItem();
		}
		Logger.log(new StringBuilder().append("Time [MemoryViewPanel]: ").append(System.currentTimeMillis() - start_time).append(" Millis").toString());
	}
}
