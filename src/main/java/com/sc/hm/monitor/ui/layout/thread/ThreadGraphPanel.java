package com.sc.hm.monitor.ui.layout.thread;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.common.algo.MaxMinResultObject;
import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.shared.os.OSMBeanSharedObject;
import com.sc.hm.monitor.shared.threads.ThreadMBeanSharedObject;
import com.sc.hm.monitor.ui.layout.common.CGraphPlotterPanel;
import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.monitor.ui.layout.panel.MonitoringGraphPanel;

public class ThreadGraphPanel extends MonitoringGraphPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static OSMBeanSharedObject repos_os = MBeanSharedObjectRepository.getInstance().getOs_mx_bean();	
	private static ThreadMBeanSharedObject repos = MBeanSharedObjectRepository.getInstance().getThread_mx_bean();
	
	private long currentMaxX = 0L;
	private long currentMinX = 0L;

	private CGraphPlotterPanel cGraphPlotterPanel = null;
	
	public ThreadGraphPanel(_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height) throws Exception {
		this(obj, width, height, new TitledBorder(""), null);
	}
	
	public ThreadGraphPanel(_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int panel_width, int panel_height, Border panel_border, LayoutManager layout) throws Exception {
		super(obj, panel_width, panel_height, panel_border, layout);
		initializePrimaryPanel();
		initializeOtherComponent();
		addAllComponent();
	}
	
	private void initializePrimaryPanel() throws Exception {
		cGraphPlotterPanel = new CGraphPlotterPanel(sharedObj.getSelectionName(), 800, 280, true, bubblePanel);
		cGraphPlotterPanel.setUsageList(repos_os.getProcessCPUTime());
		setPrimaryPlotterPanel(cGraphPlotterPanel);
		configurePrimaryPanelComponent();
	}
	
	private void initializeOtherComponent() throws Exception {
		initializeComponent();
		initializeDisplayArea(2, 70, new Dimension(800, 35));
	}
	
	private void addAllComponent() throws Exception {
		addComponent();
	}
	
	public void startAsynchronizedMonitoring() {
		retrieveInformation();		
		synchronized (repos) {
			cGraphPlotterPanel.setUsageList(repos_os.getProcessCPUTime());
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
		}
		checkPersistence(MonitorTask.TASK_CPUUSAGE_PERSIST);		
	}
	
	public void startSynchronizedMonitoring() {
		retrieveInformation();		
		synchronized (repos) {
			cGraphPlotterPanel.setUsageList(repos_os.getProcessCPUTime());
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
		}
		checkPersistence(MonitorTask.TASK_CPUUSAGE_PERSIST);		
	}
	
	private void updatePrimaryComponent() {
		updatePrimaryPanel();
	}
	
	private void updateDetailsComponent() {
		updateDisplayArea(displayThreadDetailsInfo());
	}
	
	private void processMinXAndMaxX() {
		if (sharedObj.isDynmaicAxis()) {
			currentMaxX = repos_os.getMaxCPUTime();
			currentMinX = repos_os.getMinCPUTime();
		}
		else {
			currentMaxX = repos_os.getMaxCPUTime() + repos_os.getMaxCPUTime() / 2;
			currentMinX = 0L;
		}
		if (currentMinX == 0 && currentMaxX < 8) {
			currentMaxX += 8;
		}
		cGraphPlotterPanel.setCurrentMinX(currentMinX);
		cGraphPlotterPanel.setCurrentMaxX(currentMaxX);
		displayYLabels(new MaxMinResultObject(currentMinX, currentMaxX, "Second"), 1000000000);

		if (repos_os.getProcessCPUTime().size() >= 2) {
			/*
			timeAlgo.setStartTime(repos_os.getStartTime());
			timeAlgo.setSpan(Integer.parseInt(sharedObj.getSpan()));
			timeAlgo.setDelay(sharedObj.getGraphDelay());
			timeAlgo.setTotalPoints(repos_os.getTimeList().size());
			
			TimeObject timeObject = timeAlgo.calculateTimes();
			displayXLabels(timeObject);*/
			displayXLabels(repos_os.getTimeList());
		}
	}
	
	private String displayThreadDetailsInfo() {
		StringBuilder sb = new StringBuilder(1000);		
		sb.append("  Total Started: ").append(repos.getStartedThreads()).append(". ");
		sb.append("  Live Thread: ").append(repos.getLiveThreads()).append(". ");
		sb.append("  Peak Thread: ").append(repos.getPeakThreads()).append(". ");
		sb.append("  Daemon Thread: ").append(repos.getDaemonThreads()).append(". ");
		sb.append("  Deadlocked Thread: ").append(repos.getDeadlockedThreads()).append(".");
		
		return sb.toString();
	}
}
