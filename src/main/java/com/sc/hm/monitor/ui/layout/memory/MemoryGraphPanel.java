package com.sc.hm.monitor.ui.layout.memory;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
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
import com.sc.hm.monitor.shared.memory.Memory;
import com.sc.hm.monitor.shared.memory.MemoryMBeanSharedObject;
import com.sc.hm.monitor.ui.layout.common.BarGraphPanel;
import com.sc.hm.monitor.ui.layout.common.MGraphPlotterPanel;
import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.monitor.ui.layout.panel.MonitoringGraphPanel;
import com.sc.hm.monitor.util.Logger;

public class MemoryGraphPanel extends MonitoringGraphPanel {

	private static final long serialVersionUID = 1L;
	
	private static MemoryMBeanSharedObject repos = MBeanSharedObjectRepository.getInstance().getMemory_mx_bean();
	private Memory memory = null;
	private String[] memoryNames = null;
	
	private long currentMaxX = -1L;
	private long currentMinX = -1L;
	
	private long previousMaxX = 1L;
	private long previousMinX = 1L;
	
	private MGraphPlotterPanel mGraphPlotterPanel = null;
	private MGraphPlotterPanel[] allGraphPanels = null;
	private BarGraphPanel[] barPanel = null;
	
	private JLabel[] barLabels = null;	
	private JLabel[] poolLabels = null;
	
	private NumberFormat n_format = NumberFormat.getInstance();
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	public MemoryGraphPanel(_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height) throws Exception {
		this(obj, width, height, new TitledBorder(""), null);
	}
		
	public MemoryGraphPanel(_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height, Border border, LayoutManager layout) throws Exception {
		super(obj, width, height, border, layout);		
		initializePrimaryPanel();
		initializeOtherComponent();
		initializeBarComponent();
		initializeOtherPoolComponent();
		addAllComponent();
	}
	
	private void initializePrimaryPanel() throws Exception {
		mGraphPlotterPanel = new MGraphPlotterPanel(sharedObj.getSelectionName(), 800, 280, true, bubblePanel);
		mGraphPlotterPanel.setMemoryPool(repos.getMemory(sharedObj.getSelectionName()));
		setPrimaryPlotterPanel(mGraphPlotterPanel);
		configurePrimaryPanelComponent();
	}
	
	private void initializeOtherComponent() throws Exception {
		initializeComponent();
		initializeDisplayArea(8, 70, new Dimension(620, 130));
	}
	
	private void initializeBarComponent() throws Exception {
		memoryNames = repos.getMemoryNames();
		barPanel = new BarGraphPanel[memoryNames.length];
		barLabels = new JLabel[memoryNames.length];
		
		int topIndent = mGraphPlotterPanel.getHeight();
		for (int i = 0; i < memoryNames.length; i ++) {
			barPanel[i] = new BarGraphPanel(memoryNames[i], 40, 130);
			barPanel[i].setPanelBounds(760 + i * (20 + 40), topIndent + 47);
			barLabels[i] = new JLabel(memoryNames[i].equals(MemoryMBeanSharedObject.HEAP_MEMORY) ? "H" : "NH");
			barLabels[i].setFont(font);
			barLabels[i].setBounds(760 + 10 + i * (20 + 40), topIndent + 47 + 130 + 2, 20, 10);
		}
	}
	
	private void initializeOtherPoolComponent() throws Exception {
		String[] memoryPools = repos.getMemoryNames();
		poolLabels = new JLabel[memoryPools.length];
		
		int pools_consolidated_width = getWidth() - 10 - 10;
		int gap_between_2_pools = 10;
		int each_pool_width = (pools_consolidated_width - (memoryPools.length - 1) * gap_between_2_pools) / memoryPools.length;
		
		int mPoolGraph_XLength = each_pool_width;
		int remainder = mPoolGraph_XLength % 10;
		if (remainder != 0) {
			mPoolGraph_XLength -= remainder;
		}
		int mPoolGraph_YLength = 70;
		
		allGraphPanels = new MGraphPlotterPanel[memoryPools.length];
		int startX = 10 + (memoryPools.length * remainder) / 2;
		for (byte i = 0; i < allGraphPanels.length; i ++) {
			allGraphPanels[i] = new MGraphPlotterPanel(memoryPools[i], mPoolGraph_XLength, mPoolGraph_YLength);
			allGraphPanels[i].setMainPanel(false);
			allGraphPanels[i].setMemoryPool(repos.getMemory(memoryPools[i]));
			allGraphPanels[i].setXFactor(4);
			allGraphPanels[i].setPanelBounds(startX + (i * mPoolGraph_XLength) + (i * 10), 490);
			allGraphPanels[i].setFillCurve(true);
			
			poolLabels[i] = new JLabel(memoryPools[i]);
			poolLabels[i].setBounds(startX + (i * mPoolGraph_XLength) + (i * 10), 490 - 15, mPoolGraph_XLength, 15);
			poolLabels[i].setFont(font);
		}
	}
	
	private void addAllComponent() throws Exception {
		addComponent();		
		for (BarGraphPanel panel : barPanel) {
			add(panel);
		}
		for (JLabel label : barLabels) {
			add(label);
		}
		for (MGraphPlotterPanel panel : allGraphPanels) {
			add(panel);
		}		
		for (JLabel label : poolLabels) {
			add(label);
		}
	}
	
	public void startAsynchronizedMonitoring() {
		long start_time = System.currentTimeMillis();
		retrieveInformation();		
		synchronized (repos) {
			memory = repos.getMemory(graph_name);
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
			updateBarComponent();
			updateOtherComponents();
		}
		checkPersistence(MonitorTask.TASK_MEMORY_PERSIST);
		Logger.log("Time [MemoryGraphPanel]: " + (System.currentTimeMillis() - start_time) + " Millis");
	}
	
	public void startSynchronizedMonitoring() {
		long start_time = System.currentTimeMillis();
		retrieveInformation();		
		synchronized (repos) {
			memory = repos.getMemory(graph_name);
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
			updateBarComponent();
			updateOtherComponents();
		}
		checkPersistence(MonitorTask.TASK_MEMORY_PERSIST);
		Logger.log("Time [MemoryGraphPanel]: " + (System.currentTimeMillis() - start_time) + " Millis");
	}
	
	private void updatePrimaryComponent() {
		mGraphPlotterPanel.setMemoryPool(memory);
		updatePrimaryPanel();
	}
	
	private void updateDetailsComponent() {
		updateDisplayArea(displayPoolDetailsInfo());
	}
	
	private void updateBarComponent() {
		for (byte i = 0; i < memoryNames.length; i ++) {
			Memory mem = repos.getMemory(memoryNames[i]);
			barPanel[i].setCurrentMinX(0);
			barPanel[i].setCurrentMaxX(mem.getMax());
			barPanel[i].setCurrentValue(mem.getPoolUsage().get(mem.getPoolUsage().size() - 1));
			barPanel[i].repaint();
		}
	}
	
	private void updateOtherComponents() {
		for (byte i = 0; i < allGraphPanels.length; i ++) {
			allGraphPanels[i].repaint();
		}
	}
	
	private void processMinXAndMaxX() {
		if (!sharedObj.isDynmaicAxis()) {
			currentMinX = 0L;
			currentMaxX = memory.getMax();
			if (currentMaxX < memory.getUsed_max()) {
				currentMaxX = memory.getUsed_max();
			}
			if (sharedObj.isShowCommittedUsage()) {
				if (currentMaxX < memory.getCommitted_max()) {
					currentMaxX = memory.getCommitted_max();
				}
			}
		}
		else {
			currentMinX = memory.getUsed_min();
			currentMaxX = memory.getUsed_max();
			if (sharedObj.isShowCommittedUsage()) {
				if (currentMaxX < memory.getCommitted_max()) {
					currentMaxX = memory.getCommitted_max();
				}
			}
			if (currentMinX == 0 && currentMaxX == 0) {
				currentMaxX ++;
			}
		}	
		if (previousMaxX != currentMaxX || previousMinX != currentMinX) {
			graphAlgo.setAlgorithmProperties(currentMinX, currentMaxX, 320);
			MaxMinResultObject resultObject = (MaxMinResultObject)graphAlgo.calculateMaxAndMinX();
			displayYLabels(resultObject);			
			
			mGraphPlotterPanel.setResultObject(resultObject);			
			previousMinX = currentMinX;
			previousMaxX = currentMaxX;
		}
		if (memory.getPoolUsage().size() >= 2) {
			/*timeAlgo.setStartTime(memory.getStartTime());
			timeAlgo.setSpan(Integer.parseInt(sharedObj.getSpan()));
			timeAlgo.setDelay(sharedObj.getGraphDelay());
			timeAlgo.setTotalPoints(memory.getPoolUsage().size());
			
			TimeObject timeObject = timeAlgo.calculateTimes();
			displayXLabels(timeObject);*/
			displayXLabels(repos.getTimeList());
		}
	}

	private String displayPoolDetailsInfo() {
		StringBuilder sb = new StringBuilder(1000);
		
		sb.append("  Memory Name:     ").append(memory.getMemoryName()).append(".\tMemory Type:    ").append(memory.getMemoryType().name());
		sb.append("\t[Current Time: ").append(new Date()).append("]");
		sb.append("\n\n").append("  Initial Memory:\t").append(n_format.format(memory.getInit()) + " BYTE.");
		sb.append("\t\tUsed Memory:\t").append(n_format.format(memory.getUsed()) + " BYTE.");
		sb.append("\n").append("  Committed Memory:\t").append(n_format.format(memory.getCommitted()) + " BYTE.");
		sb.append("\t\tMaximum Memory:\t").append(n_format.format(memory.getMax()) + " BYTE.\n");
		
		GBCollectorMBeanSharedObject repos_gb = MBeanSharedObjectRepository.getInstance().getGbcollector_mx_bean();
		GBCollector[] collectors = repos_gb.getAllGarbageCollectors();
		for (byte i = 0; i < collectors.length; i ++) {
			sb.append("\n").append("  Garbage Collector ").append(i + 1).append(":  ").append(collectors[i].toString());
		}
		return sb.toString();
	}
}