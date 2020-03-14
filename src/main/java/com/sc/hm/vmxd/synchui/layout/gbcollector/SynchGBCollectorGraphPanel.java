package com.sc.hm.vmxd.synchui.layout.gbcollector;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.common.algo.MaxMinResultObject;
import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.GarbageCollectorDataRepository;
import com.sc.hm.vmxd.data.gbcollector.GarbageCollectorData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.common.SynchLGraphPlotterPanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchMonitoringGraphPanel;

public class SynchGBCollectorGraphPanel extends SynchMonitoringGraphPanel {
	
	private static final long serialVersionUID = 1L;

	private String applicationId = "";

	private GarbageCollectorDataRepository garbageCollectorRepository = null;
	
	private GarbageCollectorData gbCollectorData = null;
	
	private long currentMaxX = 0L;
	private long currentMinX = 0L;

	private NumberFormat n_format = new DecimalFormat("0");
	
	private SynchLGraphPlotterPanel lGraphPlotterPanel = null;
	private SynchLGraphPlotterPanel[] allGraphPanels = null;
	
	private JLabel[] collectorLabels = null;
	
	public SynchGBCollectorGraphPanel(String application, SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height) throws Exception {
		this(application, obj, width, height, new TitledBorder(""), null);
	}
		
	public SynchGBCollectorGraphPanel(String application, SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height, Border border, LayoutManager layout) throws Exception {
		super(obj, width, height, border, layout);
		applicationId = application;
		initOther();
		initializePrimaryPanel();
		initializeOtherComponent();
		initializeOtherGBCollectorComponent();
		addAllComponent();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.GARBAGE_COLLECTOR_LOCK);
			garbageCollectorRepository = (GarbageCollectorDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initializePrimaryPanel() throws Exception {
		lGraphPlotterPanel = new SynchLGraphPlotterPanel(sharedObj.getSelectionName(), 800, 280, true, bubblePanel);
		lGraphPlotterPanel.setGbCollector(garbageCollectorRepository.getGarbageCollectorData(sharedObj.getSelectionName()));
		setPrimaryPlotterPanel(lGraphPlotterPanel);
		configurePrimaryPanelComponent();
	}
	
	private void initializeOtherComponent() throws Exception {
		initializeComponent();
		initializeDisplayArea(8, 70, new Dimension(620, 130));
	}
	
	public void initializeOtherGBCollectorComponent() throws Exception {
		String[] gbNames = garbageCollectorRepository.getGarbageCollectorNames();
		collectorLabels = new JLabel[gbNames.length];
		
		int pools_consolidated_width = getWidth() - 10 - 10;
		int gap_between_2_pools = 10;
		int each_pool_width = (pools_consolidated_width - (gbNames.length - 1) * gap_between_2_pools) / gbNames.length;
		
		int mPoolGraph_XLength = each_pool_width;
		int remainder = mPoolGraph_XLength % 10;
		if (remainder != 0) {
			mPoolGraph_XLength -= remainder;
		}
		int mPoolGraph_YLength = 70;
		
		allGraphPanels = new SynchLGraphPlotterPanel[gbNames.length];
		int startX = 10 + (gbNames.length * remainder) / 2;
		for (byte i = 0; i < allGraphPanels.length; i ++) {
			allGraphPanels[i] = new SynchLGraphPlotterPanel(gbNames[i], mPoolGraph_XLength, mPoolGraph_YLength);
			allGraphPanels[i].setMainPanel(false);
			allGraphPanels[i].setGbCollector(garbageCollectorRepository.getGarbageCollectorData(gbNames[i]));
			allGraphPanels[i].setXFactor(4);
			allGraphPanels[i].setPanelBounds(startX + (i * mPoolGraph_XLength) + (i * 10), 490);
			allGraphPanels[i].setFillCurve(true);
			
			collectorLabels[i] = new JLabel(gbNames[i]);
			collectorLabels[i].setBounds(startX + (i * mPoolGraph_XLength) + (i * 10), 490 - 20, mPoolGraph_XLength, 15);
			collectorLabels[i].setFont(new Font("Arial", Font.PLAIN, 11));
		}
	}
	
	private void addAllComponent() throws Exception {
		addComponent();
		for (SynchLGraphPlotterPanel panel : allGraphPanels) {
			add(panel);
		}		
		for (JLabel label : collectorLabels) {
			add(label);
		}
	}
	
	public void startSynchronizedMonitoring() throws Exception {
		retrieveInformation("com.sc.hm.vmxd.process.GarbageCollectorMBeanSynchProcess");		
		try {
			thirdPartyLock.startGetItem();
			gbCollectorData = garbageCollectorRepository.getGarbageCollectorData(graph_name);
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
			updateOtherComponents();
		}
		finally {
			thirdPartyLock.endGetItem();
		}
		checkPersistence(MonitorTask.TASK_GBCOLLECTION_PERSIST);
	}
	
	private void updatePrimaryComponent() {
		lGraphPlotterPanel.setGbCollector(gbCollectorData);
		updatePrimaryPanel();
	}
	
	private void updateDetailsComponent() {
		updateDisplayArea(displayGBCollectorDetailsInfo());
	}
	
	private void updateOtherComponents() {
		for (byte i = 0; i < allGraphPanels.length; i ++) {
			allGraphPanels[i].repaint();
		}
	}
	
	private void processMinXAndMaxX() {
		int yPoint = 8;
		if (sharedObj.isDynmaicAxis()) {
			currentMaxX = gbCollectorData.getMaxCollectionCount();
			currentMinX = gbCollectorData.getMinCollectionCount();			
		}
		else {
			currentMaxX = gbCollectorData.getMaxCollectionCount() + gbCollectorData.getMaxCollectionCount() / 2;
			currentMinX = 0L;
		}
		if (currentMaxX == 0 && currentMinX == 0) {
			currentMaxX = 8;
		}
		else if ((currentMaxX - currentMinX) % yPoint != 0) {
			long diff = (currentMaxX - currentMinX);
			diff += (yPoint - diff % yPoint);
			currentMaxX = currentMinX + diff;
		}
		lGraphPlotterPanel.setCurrentMaxX(currentMaxX);
		lGraphPlotterPanel.setCurrentMinX(currentMinX);
		displayYLabels(new MaxMinResultObject(currentMinX, currentMaxX, "No."));
		
		if (gbCollectorData.getCollectionList().size() >= 2) {
			/*timeAlgo.setStartTime(gbCollector.getStartTime());
			timeAlgo.setSpan(Integer.parseInt(sharedObj.getSpan()));
			timeAlgo.setDelay(sharedObj.getGraphDelay());
			timeAlgo.setTotalPoints(gbCollector.getCollectionCount().size());
			
			TimeObject timeObject = timeAlgo.calculateTimes();
			displayXLabels(timeObject);*/
			displayXLabels(gbCollectorData.getDateList());
		}		
	}

	private String displayGBCollectorDetailsInfo() {
		int totalRecords = gbCollectorData.getCollectionList().size();
		long currentCollection = 0;
		if (totalRecords > 0) {
			currentCollection = gbCollectorData.getCollectionList().get(totalRecords - 1);
		}
		
		StringBuilder sb = new StringBuilder(1000);
		
		sb.append("  Garbage Collector Details:    ").append("[Current Time: " + new Date() + "]");
		sb.append("\n\n").append("  Current Collection:     ").append(n_format.format(currentCollection) + " Object(s).    ");
		sb.append("Elapsed Time:    ").append(n_format.format(gbCollectorData.getCurrentCollectionTime()) + " Milli Seconds");
		sb.append("\n").append("  Total Collections:       ").append(n_format.format(gbCollectorData.getTotalCollectionCount()) + " Object(s).    ");
		sb.append("Total Elapsed Time:    ").append(n_format.format(gbCollectorData.getTotalCollectionTime()) + " Milli Seconds.");
		sb.append("\n");
		
		String[] memoryPools = gbCollectorData.getMemoryPools();
		for (byte i = 0; i < memoryPools.length; i ++) {
			sb.append("\n").append("  Memory Pool ").append(i + 1).append(":  ").append(memoryPools[i]);
		}
		return sb.toString();
	}
}
