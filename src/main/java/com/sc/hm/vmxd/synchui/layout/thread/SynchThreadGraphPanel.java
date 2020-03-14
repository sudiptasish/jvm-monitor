package com.sc.hm.vmxd.synchui.layout.thread;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.common.algo.MaxMinResultObject;
import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.OperatingSystemDataRepository;
import com.sc.hm.vmxd.data.ThreadDataRepository;
import com.sc.hm.vmxd.data.os.OperatingSystemData;
import com.sc.hm.vmxd.data.thread.ThreadData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.lock.ThirdPartySynchronizedLock;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.common.SynchDGraphPlotterPanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchMonitoringGraphPanel;

public class SynchThreadGraphPanel extends SynchMonitoringGraphPanel {
	
	private static final long serialVersionUID = 1L;
	
	private String applicationId = "";

	private OperatingSystemDataRepository osRepository = null;	
	private ThreadDataRepository threadRepository = null;
	
	private ThreadData threadData = null;
	private OperatingSystemData osData = null;
	
	private long currentMaxX = 0L;
	private long currentMinX = 0L;

	private SynchDGraphPlotterPanel dGraphPlotterPanel = null;
	
	private ThirdPartySynchronizedLock thirdPartyOSLock = null;
	
	public SynchThreadGraphPanel(String application, SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height) throws Exception {
		this(application, obj, width, height, new TitledBorder(""), null);
	}
	
	public SynchThreadGraphPanel(String application, SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int panel_width, int panel_height, Border panel_border, LayoutManager layout) throws Exception {
		super(obj, panel_width, panel_height, panel_border, layout);
		applicationId = application;
		initOther();
		initializePrimaryPanel();
		initializeOtherComponent();
		addAllComponent();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.THREAD_LOCK);
			thirdPartyOSLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.OPERATING_SYSTEM_LOCK);
			osRepository = (OperatingSystemDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_OPERATING_SYSTEM);
			threadRepository = (ThreadDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_THREAD);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initializePrimaryPanel() throws Exception {
	    dGraphPlotterPanel = new SynchDGraphPlotterPanel(sharedObj.getSelectionName(), 800, 280, true, bubblePanel);
		osData = osRepository.getOsData();
		dGraphPlotterPanel.setUsageList(osData.getCpuUsage());
		setPrimaryPlotterPanel(dGraphPlotterPanel);
		configurePrimaryPanelComponent();
	}
	
	private void initializeOtherComponent() throws Exception {
		initializeComponent();
		initializeDisplayArea(2, 70, new Dimension(800, 35));
	}
	
	private void addAllComponent() throws Exception {
		addComponent();
	}
	
	public void startSynchronizedMonitoring() throws Exception {
		retrieveInformation("com.sc.hm.vmxd.process.OperatingSystemMBeanSynchProcess");		
		try {
			thirdPartyOSLock.startGetItem();
			osData = osRepository.getOsData();
			dGraphPlotterPanel.setUsageList(osData.getCpuUsage());
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			checkPersistence(MonitorTask.TASK_CPUUSAGE_PERSIST);
		}
		finally {
			thirdPartyOSLock.endGetItem();
		}
		
		try {	
			thirdPartyLock.startGetItem();
			threadData = threadRepository.getThreadData();
			updateDetailsComponent();
		}
		finally {
			thirdPartyLock.endGetItem();
		}		
	}
	
	private void updatePrimaryComponent() {
		updatePrimaryPanel();
	}
	
	private void updateDetailsComponent() {
		updateDisplayArea(displayThreadDetailsInfo());
	}
	
	private void processMinXAndMaxX() {
		if (sharedObj.isDynmaicAxis()) {
			currentMaxX = (long)Math.ceil(osData.getMax_cpu());
			currentMinX = (long)Math.floor(osData.getMin_cpu());
		}
		else {
			currentMaxX = (long)Math.ceil(osData.getMax_cpu() + osData.getMax_cpu() / 2);
			currentMinX = 0L;
		}
		if (currentMinX == 0 && currentMaxX < 8) {
			currentMaxX += 8;
		}
		dGraphPlotterPanel.setCurrentMinX(currentMinX);
		dGraphPlotterPanel.setCurrentMaxX(currentMaxX);
		displayYLabels(new MaxMinResultObject(currentMinX, currentMaxX, "Second"), 1);

		if (osData.getCpuUsage().size() >= 2) {
			/*
			timeAlgo.setStartTime(repos_os.getStartTime());
			timeAlgo.setSpan(Integer.parseInt(sharedObj.getSpan()));
			timeAlgo.setDelay(sharedObj.getGraphDelay());
			timeAlgo.setTotalPoints(repos_os.getTimeList().size());
			
			TimeObject timeObject = timeAlgo.calculateTimes();
			displayXLabels(timeObject);*/
			displayXLabels(osData.getTimeList());
		}
	}
	
	private String displayThreadDetailsInfo() {
		StringBuilder sb = new StringBuilder(1000);		
		sb.append("  Total Started: ").append(threadData.getTotalStartedThreadCount()).append(". ");
		sb.append("  Live Thread: ").append(threadData.getThreadCount()).append(". ");
		sb.append("  Peak Thread: ").append(threadData.getPeakThreadCount()).append(". ");
		sb.append("  Daemon Thread: ").append(threadData.getDaemonThreadCount()).append(". ");
		sb.append("  Deadlocked Thread: ").append(threadData.getDeadlockThreadCount()).append(".");
		
		return sb.toString();
	}
}
