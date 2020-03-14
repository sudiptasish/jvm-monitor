package com.sc.hm.monitor.ui.layout.gbcollector;

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
import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.shared.gbcollector.GBCollector;
import com.sc.hm.monitor.shared.gbcollector.GBCollectorMBeanSharedObject;
import com.sc.hm.monitor.ui.layout.common.LGraphPlotterPanel;
import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.monitor.ui.layout.panel.MonitoringGraphPanel;

public class GBCollectorGraphPanel extends MonitoringGraphPanel {
	
	private static final long serialVersionUID = 1L;

	private static GBCollectorMBeanSharedObject repos = MBeanSharedObjectRepository.getInstance().getGbcollector_mx_bean();
	
	private GBCollector gbCollector = null;
	
	private long currentMaxX = 0L;
	private long currentMinX = 0L;

	private NumberFormat n_format = new DecimalFormat("0");
	
	private LGraphPlotterPanel lGraphPlotterPanel = null;
	private LGraphPlotterPanel[] allGraphPanels = null;
	
	private JLabel[] collectorLabels = null;
	
	public GBCollectorGraphPanel(_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height) throws Exception {
		this(obj, width, height, new TitledBorder(""), null);
	}
		
	public GBCollectorGraphPanel(_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height, Border border, LayoutManager layout) throws Exception {
		super(obj, width, height, border, layout);		
		initializePrimaryPanel();
		initializeOtherComponent();
		initializeOtherGBCollectorComponent();
		addAllComponent();
	}
	
	private void initializePrimaryPanel() throws Exception {
		lGraphPlotterPanel = new LGraphPlotterPanel(sharedObj.getSelectionName(), 800, 280, true, bubblePanel);
		lGraphPlotterPanel.setGbCollector(repos.getGarbageCollectorByName(sharedObj.getSelectionName()));
		setPrimaryPlotterPanel(lGraphPlotterPanel);
		configurePrimaryPanelComponent();
	}
	
	private void initializeOtherComponent() throws Exception {
		initializeComponent();
		initializeDisplayArea(8, 70, new Dimension(620, 130));
	}
	
	public void initializeOtherGBCollectorComponent() throws Exception {
		String[] gbNames = repos.getGarbageCollectorNames();
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
		
		allGraphPanels = new LGraphPlotterPanel[gbNames.length];
		int startX = 10 + (gbNames.length * remainder) / 2;
		for (byte i = 0; i < allGraphPanels.length; i ++) {
			allGraphPanels[i] = new LGraphPlotterPanel(gbNames[i], mPoolGraph_XLength, mPoolGraph_YLength);
			allGraphPanels[i].setMainPanel(false);
			allGraphPanels[i].setGbCollector(repos.getGarbageCollectorByName(gbNames[i]));
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
		for (LGraphPlotterPanel panel : allGraphPanels) {
			add(panel);
		}		
		for (JLabel label : collectorLabels) {
			add(label);
		}
	}
	
	public void startAsynchronizedMonitoring() {
		retrieveInformation();		
		synchronized (repos) {
			gbCollector = repos.getGarbageCollectorByName(graph_name);
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
			updateOtherComponents();
		}
		checkPersistence(MonitorTask.TASK_GBCOLLECTION_PERSIST);
	}
	
	public void startSynchronizedMonitoring() {
		retrieveInformation();		
		synchronized (repos) {
			gbCollector = repos.getGarbageCollectorByName(graph_name);
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
			updateOtherComponents();
		}
		checkPersistence(MonitorTask.TASK_GBCOLLECTION_PERSIST);
	}
	
	private void updatePrimaryComponent() {
		lGraphPlotterPanel.setGbCollector(gbCollector);
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
			currentMaxX = gbCollector.getMaxCollectionCount();
			currentMinX = gbCollector.getMinCollectionCount();			
		}
		else {
			currentMaxX = gbCollector.getMaxCollectionCount() + gbCollector.getMaxCollectionCount() / 2;
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
		
		if (gbCollector.getCollectionCount().size() >= 2) {
			/*timeAlgo.setStartTime(gbCollector.getStartTime());
			timeAlgo.setSpan(Integer.parseInt(sharedObj.getSpan()));
			timeAlgo.setDelay(sharedObj.getGraphDelay());
			timeAlgo.setTotalPoints(gbCollector.getCollectionCount().size());
			
			TimeObject timeObject = timeAlgo.calculateTimes();
			displayXLabels(timeObject);*/
			displayXLabels(gbCollector.getTimeList());
		}		
	}

	private String displayGBCollectorDetailsInfo() {
		int totalRecords = gbCollector.getCollectionCount().size();
		int totalTimes = gbCollector.getCollectionTime().size();
		if (totalRecords == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder(1000);
		
		sb.append("  Garbage Collector Details:    ").append("[Current Time: " + new Date() + "]");
		sb.append("\n\n").append("  Current Collection:     ").append(n_format.format(gbCollector.getCollectionCount().get(totalRecords - 1)) + " Object(s).    ");
		sb.append("Elapsed Time:    ").append(n_format.format(gbCollector.getCollectionTime().get(totalTimes - 1)) + " Milli Seconds");
		sb.append("\n").append("  Total Collections:       ").append(n_format.format(gbCollector.getTotalCollectionCount()) + " Object(s).    ");
		sb.append("Total Elapsed Time:    ").append(n_format.format(gbCollector.getTotalCollectionTime()) + " Milli Seconds.");
		sb.append("\n");
		
		String[] memoryPools = gbCollector.getMemoryPools();
		for (byte i = 0; i < memoryPools.length; i ++) {
			sb.append("\n").append("  Memory Pool ").append(i + 1).append(":  ").append(memoryPools[i]);
		}
		return sb.toString();
	}
}
