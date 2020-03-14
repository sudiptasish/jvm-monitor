package com.sc.hm.monitor.ui.layout.mpool;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.common.algo.MaxMinResultObject;
import com.sc.hm.monitor.dialog.MemoryThresholdInputDialog;
import com.sc.hm.monitor.launcher.MBeanProcessLauncher;
import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.shared.gbcollector.GBCollector;
import com.sc.hm.monitor.shared.gbcollector.GBCollectorMBeanSharedObject;
import com.sc.hm.monitor.shared.mpool.MemoryPool;
import com.sc.hm.monitor.shared.mpool.MemoryPoolMBeanSharedObject;
import com.sc.hm.monitor.ui.layout.common.BarGraphPanel;
import com.sc.hm.monitor.ui.layout.common.MGraphPlotterPanel;
import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.monitor.ui.layout.panel.BasicGraphPanel;
import com.sc.hm.monitor.ui.layout.panel.MonitoringGraphPanel;
import com.sc.hm.monitor.util.Logger;

public class MemoryPoolGraphPanel extends MonitoringGraphPanel {
	
	private static final long serialVersionUID = 1L;

	private static MemoryPoolMBeanSharedObject mpoolMBeanShared = MBeanSharedObjectRepository.getInstance().getMpool_mx_bean();
	
	private MemoryPool memoryPool = null;
	
	private long currentMaxX = -1L;
	private long currentMinX = -1L;
	
	private long previousMaxX = 1L;
	private long previousMinX = 1L;
	
	private MGraphPlotterPanel mGraphPlotterPanel = null;
	private MGraphPlotterPanel[] allGraphPanels = null;
	private BarGraphPanel barPanel = null;
	
	private JLabel[] poolLabels = null;
	
	private JButton thresholdButton = new JButton("Set");
	private JButton unthresholdButton = new JButton("Reset");
	
	private NumberFormat n_format = NumberFormat.getInstance();
	
	private MPoolConfigurationActionListener listener = null;
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	public MemoryPoolGraphPanel(_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height) throws Exception {
		this(obj, width, height, new TitledBorder(""), null);
	}
	
	public MemoryPoolGraphPanel(_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height, Border border, LayoutManager layout) throws Exception {
		super(obj, width, height, border, layout);		
		initializePrimaryPanel();
		initializeOtherComponent();
		initializeBarComponent();
		initializeOtherPoolComponent();
		initializeListener();
		addAllComponent();
	}
	
	private void initializePrimaryPanel() throws Exception {
		mGraphPlotterPanel = new MGraphPlotterPanel(sharedObj.getSelectionName(), 800, 280, true, bubblePanel);
		mGraphPlotterPanel.setMemoryPool(mpoolMBeanShared.getMemoryPool(sharedObj.getSelectionName()));
		setPrimaryPlotterPanel(mGraphPlotterPanel);
		configurePrimaryPanelComponent();
	}
	
	private void initializeOtherComponent() throws Exception {
		initializeComponent();
		initializeDisplayArea(8, 70, new Dimension(620, 130));
		
		thresholdButton.setBounds(727, mGraphPlotterPanel.getHeight() + 75, 65, 18);
		thresholdButton.setFont(font);
		thresholdButton.setToolTipText("Set Threshold Value");		
		
		unthresholdButton.setBounds(727, mGraphPlotterPanel.getHeight() + 105, 65, 18);
		unthresholdButton.setToolTipText("ReSet Threshold Value");
		unthresholdButton.setFont(font);
		unthresholdButton.setEnabled(false);
	}
	
	private void initializeBarComponent() throws Exception {
		int topIndent = mGraphPlotterPanel.getHeight();
		barPanel = new BarGraphPanel(sharedObj.getSelectionName(), 40, 130);
		barPanel.setPanelBounds(820, topIndent + 47);
	}
	
	private void initializeOtherPoolComponent() throws Exception {
		String[] memoryPools = mpoolMBeanShared.getMemoryPoolNames();
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
			allGraphPanels[i].setMemoryPool(mpoolMBeanShared.getMemoryPool(memoryPools[i]));
			allGraphPanels[i].setXFactor(4);
			allGraphPanels[i].setPanelBounds(startX + (i * mPoolGraph_XLength) + (i * 10), 490);
			allGraphPanels[i].setFillCurve(true);
			
			poolLabels[i] = new JLabel(memoryPools[i]);
			poolLabels[i].setBounds(startX + (i * mPoolGraph_XLength) + (i * 10), 490 - 15, mPoolGraph_XLength, 15);
			poolLabels[i].setFont(font);
		}
	}
	
	private void initializeListener() {
		listener = new MPoolConfigurationActionListener(this);
		thresholdButton.addActionListener(listener);
		unthresholdButton.addActionListener(listener);
	}
	
	private void addAllComponent() throws Exception {
		addComponent();		
		add(barPanel);		
		for (MGraphPlotterPanel panel : allGraphPanels) {
			add(panel);
		}		
		for (JLabel label : poolLabels) {
			add(label);
		}
		add(thresholdButton);
		add(unthresholdButton);
	}
	
	public void startAsynchronizedMonitoring() {
		long start_time = 0L;
		retrieveInformation();
		synchronized (mpoolMBeanShared) {
			start_time = System.currentTimeMillis();
			memoryPool = mpoolMBeanShared.getMemoryPool(graph_name);
			if (memoryPool.getUsageThreshold() != -9) {
				thresholdButton.setVisible(true);
				unthresholdButton.setVisible(true);
			}
			else {
				thresholdButton.setVisible(false);
				unthresholdButton.setVisible(false);
			}
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
			updateBarComponent();
			updateOtherComponents();
		}
		checkPersistence(MonitorTask.TASK_MEMORYPOOL_PERSIST);
		Logger.log("Time [MemoryPoolGraphPanel]: " + (System.currentTimeMillis() - start_time) + " Millis");
	}
	
	public void startSynchronizedMonitoring() {
		long start_time = System.currentTimeMillis();
		retrieveInformation();
		try {
			memoryPool = mpoolMBeanShared.useMemoryInfo(graph_name);
			if (memoryPool.getUsageThreshold() != -9) {
				thresholdButton.setVisible(true);
				unthresholdButton.setVisible(true);
			}
			else {
				thresholdButton.setVisible(false);
				unthresholdButton.setVisible(false);
			}
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
			updateBarComponent();
			updateOtherComponents();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				mpoolMBeanShared.completedMemoryInfo(memoryPool.getMemoryName());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		checkPersistence(MonitorTask.TASK_MEMORYPOOL_PERSIST);
		Logger.log("Time [MemoryPoolGraphPanel]: " + (System.currentTimeMillis() - start_time) + " Millis");
	}
	
	private void updatePrimaryComponent() {
		mGraphPlotterPanel.setMemoryPool(memoryPool);
		updatePrimaryPanel();
	}
	
	private void updateDetailsComponent() {
		updateDisplayArea(displayPoolDetailsInfo());
	}
	
	private void updateBarComponent() {
		barPanel.setCurrentMinX(0);
		barPanel.setCurrentMaxX(memoryPool.getMax());
		barPanel.setCurrentValue(memoryPool.getPoolUsage().get(memoryPool.getPoolUsage().size() - 1));
		barPanel.setUsageThreshold(memoryPool.getUsageThreshold());
		barPanel.repaint();
	}
	
	private void updateOtherComponents() {
		for (byte i = 0; i < allGraphPanels.length; i ++) {
			allGraphPanels[i].repaint();
		}
	}
	
	private void processMinXAndMaxX() {
		if (!sharedObj.isDynmaicAxis()) {
			currentMinX = 0L;
			currentMaxX = memoryPool.getMax();
			if (currentMaxX < memoryPool.getUsed_max()) {
				currentMaxX = memoryPool.getUsed_max();
			}
			if (sharedObj.isShowCommittedUsage()) {
				if (currentMaxX < memoryPool.getCommitted_max()) {
					currentMaxX = memoryPool.getCommitted_max();
				}
			}
		}
		else {
			currentMinX = memoryPool.getUsed_min();
			currentMaxX = memoryPool.getUsed_max();
			if (sharedObj.isShowCommittedUsage()) {
				if (currentMaxX < memoryPool.getCommitted_max()) {
					currentMaxX = memoryPool.getCommitted_max();
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
		if (memoryPool.getPoolUsage().size() >= 2) {
			/*timeAlgo.setStartTime(memoryPool.getStartTime());
			timeAlgo.setSpan(Integer.parseInt(sharedObj.getSpan()));
			timeAlgo.setDelay(sharedObj.getGraphDelay());
			timeAlgo.setTotalPoints(memoryPool.getPoolUsage().size());
			
			TimeObject timeObject = timeAlgo.calculateTimes();
			displayXLabels(timeObject);*/
			displayXLabels(mpoolMBeanShared.getTimeList());
		}
	}

	private String displayPoolDetailsInfo() {
		StringBuilder sb = new StringBuilder(1000);
		
		sb.append("  Memory Pool Name:     ").append(memoryPool.getMemoryName()).append(".\tMemory Pool Type:    ").append(memoryPool.getMemoryType().name());
		sb.append("        [Current Time: ").append(new Date()).append("]");
		sb.append("\n\n").append("  Initial Memory:\t").append(n_format.format(memoryPool.getInit()) + " BYTE.");
		sb.append("\t\tUsed Memory:\t").append(n_format.format(memoryPool.getUsed()) + " BYTE.");
		sb.append("\n").append("  Committed Memory:\t").append(n_format.format(memoryPool.getCommitted()) + " BYTE.");
		sb.append("\t\tMaximum Memory:\t").append(n_format.format(memoryPool.getMax()) + " BYTE.");
		sb.append("\n\n").append("  Usage Threshold:\t").append(memoryPool.getUsageThreshold() != -9 ? memoryPool.getUsageThreshold() : "Not Supported");
		sb.append(".\t").append("Usage Threshold Count:\t").append(memoryPool.getUsageThresholdCount() != -9 ? memoryPool.getUsageThresholdCount() : "Not Supported");
		sb.append("\n  Collection Usage Threshold:\t").append(memoryPool.getCollectionUsageThreshold() != -9 ? memoryPool.getCollectionUsageThreshold() : "Not Supported");
		sb.append(".    ").append("Collection Usage Threshold Count:\t").append(memoryPool.getCollectionUsageThresholdCount() != -9 ? memoryPool.getCollectionUsageThresholdCount() : "Not Supported").append("\n");
		
		GBCollectorMBeanSharedObject repos_gb = MBeanSharedObjectRepository.getInstance().getGbcollector_mx_bean();
		GBCollector[] collectors = repos_gb.getGarbageCollectorByMemory(graph_name);
		for (byte i = 0; i < collectors.length; i ++) {
			sb.append("\n").append("  Garbage Collector ").append(i + 1).append(":  ").append(collectors[i].toString());
		}
		return sb.toString();
	}
	
	private class MPoolConfigurationActionListener implements ActionListener {
		BasicGraphPanel graphPanel = null;
		
		public MPoolConfigurationActionListener(BasicGraphPanel panel) {
			graphPanel = panel;
		}
		
		public void actionPerformed(ActionEvent ae) {
			if ("Set".equals(ae.getActionCommand())) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						MemoryThresholdInputDialog.getMemoryDialog().showInputDialog(graph_name);
					}
				});
				unthresholdButton.setEnabled(true);			
			}
			else {
				MBeanProcessLauncher.setThreshold(graph_name, new Long(0));
				mpoolMBeanShared.setChangedPoolConfig(graph_name);
				unthresholdButton.setEnabled(false);
			}
		}
	}
}
