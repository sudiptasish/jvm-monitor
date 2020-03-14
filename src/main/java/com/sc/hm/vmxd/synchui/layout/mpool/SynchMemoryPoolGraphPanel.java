package com.sc.hm.vmxd.synchui.layout.mpool;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.text.NumberFormat;
import java.util.Date;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.common.algo.MaxMinResultObject;
import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.GarbageCollectorDataRepository;
import com.sc.hm.vmxd.data.MemoryPoolDataRepository;
import com.sc.hm.vmxd.data.gbcollector.GarbageCollectorData;
import com.sc.hm.vmxd.data.memory.MemoryPoolData;
import com.sc.hm.vmxd.dialog.SynchMemoryThresholdInputDialog;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.common.SynchBarGraphPanel;
import com.sc.hm.vmxd.synchui.layout.common.SynchMGraphPlotterPanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchBasicGraphPanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchMonitoringGraphPanel;
import java.util.Collection;

/**
 * Memory pool panel.
 * 
 * @author Sudiptasish Chanda
 */
public class SynchMemoryPoolGraphPanel extends SynchMonitoringGraphPanel {
	
	private static final long serialVersionUID = 1L;

	private String applicationId = "";

	private MemoryPoolDataRepository memoryPoolRepository = null;
	private GarbageCollectorDataRepository garbageCollectorRepository = null;
	
	private MemoryPoolData memoryPoolData = null;
	
	private long currentMaxX = -1L;
	private long currentMinX = -1L;
	
	private long previousMaxX = 1L;
	private long previousMinX = 1L;
	
	private SynchMGraphPlotterPanel mGraphPlotterPanel = null;
	private SynchMGraphPlotterPanel[] allGraphPanels = null;
	private SynchBarGraphPanel barPanel = null;
	
	private JLabel[] poolLabels = null;
	
	private final JButton thresholdButton = new JButton("Set");
	private final JButton unthresholdButton = new JButton("Reset");
	
	private final NumberFormat n_format = NumberFormat.getInstance();
	
	private MPoolConfigurationActionListener listener = null;
	
	private final Font font = new Font("Arial", Font.PLAIN, 11);
	
	private final StringBuilder _buff = new StringBuilder(512);
		
    public SynchMemoryPoolGraphPanel(String application
        , SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj
        , int width
        , int height) throws Exception {
        
		this(application
            , obj
            , width
            , height
            , new TitledBorder("")
            , null);
	}
	
	public SynchMemoryPoolGraphPanel(String application
        , SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj
        , int width
        , int height
        , Border border
        , LayoutManager layout) throws Exception {
        
		super(obj, width, height, border, layout);
        
		applicationId = application;
		initOther();
		initializePrimaryPanel();
		initializeOtherComponent();
		initializeBarComponent();
		initializeOtherPoolComponent();
		initializeListener();
		addAllComponent();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId)
                .getSynchLockByName(AbstractLockRepository.MEMORY_POOL_LOCK);
            
			memoryPoolRepository = (MemoryPoolDataRepository)
                AbstractMBeanDataRepositoryFactory
                    .getDataRepositoryFactory(applicationId)
                    .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY_POOL);
			garbageCollectorRepository = (GarbageCollectorDataRepository)
                AbstractMBeanDataRepositoryFactory
                    .getDataRepositoryFactory(applicationId)
                    .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initializePrimaryPanel() throws Exception {
		mGraphPlotterPanel = new SynchMGraphPlotterPanel(
            sharedObj.getSelectionName()
            , 800
            , 280
            , true
            , bubblePanel);
        
		mGraphPlotterPanel.setMemoryPool(memoryPoolRepository.getMemoryPoolData(sharedObj.getSelectionName()));
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
		barPanel = new SynchBarGraphPanel(sharedObj.getSelectionName(), 40, 130);
		barPanel.setPanelBounds(820, topIndent + 47);
	}
	
	private void initializeOtherPoolComponent() throws Exception {
		String[] memoryPools = memoryPoolRepository.getMemoryPoolNames();
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
		
		allGraphPanels = new SynchMGraphPlotterPanel[memoryPools.length];
		int startX = 10 + (memoryPools.length * remainder) / 2;
		for (byte i = 0; i < allGraphPanels.length; i ++) {
			allGraphPanels[i] = new SynchMGraphPlotterPanel(memoryPools[i], mPoolGraph_XLength, mPoolGraph_YLength);
			allGraphPanels[i].setMainPanel(false);
			allGraphPanels[i].setMemoryPool(memoryPoolRepository.getMemoryPoolData(memoryPools[i]));
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
		for (SynchMGraphPlotterPanel panel : allGraphPanels) {
			add(panel);
		}		
		for (JLabel label : poolLabels) {
			add(label);
		}
		add(thresholdButton);
		add(unthresholdButton);
	}
	
    @Override
	public void startSynchronizedMonitoring() throws Exception {
		long start_time = 0L;
		retrieveInformation("com.sc.hm.vmxd.process.MemoryPoolMBeanSynchProcess");
		try {
			thirdPartyLock.startGetItem();
			start_time = System.currentTimeMillis();
			memoryPoolData = memoryPoolRepository.getMemoryPoolData(graph_name);
			if (memoryPoolData.getUsageThreshold() != -9) {
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
		finally {
			thirdPartyLock.endGetItem();
		}
		checkPersistence(MonitorTask.TASK_MEMORYPOOL_PERSIST);
		Logger.log("Time [SynchMemoryPoolGraphPanel]: " + (System.currentTimeMillis() - start_time) + " Millis");
	}
	
	private void updatePrimaryComponent() {
		mGraphPlotterPanel.setMemoryPool(memoryPoolData);
		updatePrimaryPanel();
	}
	
	private void updateDetailsComponent() throws Exception {
		updateDisplayArea(displayPoolDetailsInfo());
	}
	
	private void updateBarComponent() {
		barPanel.setCurrentMinX(0);
		barPanel.setCurrentMaxX(memoryPoolData.getMax());
		barPanel.setCurrentValue(memoryPoolData.getCurrentUsed());
		barPanel.setUsageThreshold(memoryPoolData.getUsageThreshold());
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
			currentMaxX = memoryPoolData.getMax();
			if (currentMaxX < memoryPoolData.getUsed_max()) {
				currentMaxX = memoryPoolData.getUsed_max();
			}
		}
		else {
			currentMinX = memoryPoolData.getUsed_min();
			currentMaxX = memoryPoolData.getUsed_max();
			
			if (currentMinX == 0 && currentMaxX == 0) {
				currentMaxX ++;
			}
		}
		if (previousMaxX != currentMaxX || previousMinX != currentMinX) {
			graphAlgo.setAlgorithmProperties(currentMinX, currentMaxX, 280);
			MaxMinResultObject resultObject = (MaxMinResultObject)graphAlgo.calculateMaxAndMinX();
			displayYLabels(resultObject);			
			
			mGraphPlotterPanel.setResultObject(resultObject);			
			previousMinX = currentMinX;
			previousMaxX = currentMaxX;
		}
		if (memoryPoolData.getUsageCount() >= 2) {
			/*timeAlgo.setStartTime(memoryPoolData.getStartTime());
			timeAlgo.setSpan(Integer.parseInt(sharedObj.getSpan()));
			timeAlgo.setDelay(sharedObj.getGraphDelay());
			timeAlgo.setTotalPoints(memoryPoolData.getPoolUsage().size());
			
			TimeObject timeObject = timeAlgo.calculateTimes();
			displayXLabels(timeObject);*/
			displayXLabels(memoryPoolData.getDateList());
		}
	}

	private String displayPoolDetailsInfo() throws Exception {
		MemoryUsage usage = memoryPoolData.getCurrentUsage();
		
		_buff.append("  Memory Pool Name:     ")
            .append(memoryPoolData.getName())
            .append(".\tPool Type:    ")
            .append(memoryPoolData.getType())
            .append("        [Current Time: ")
            .append(new Date())
            .append("]");
        
		_buff.append("\n\n")
            .append("  Initial Memory:\t")
            .append(n_format.format(usage.getInit()))
            .append(" BYTE.");
        
		_buff.append("\t\tUsed Memory:\t")
            .append(n_format.format(usage.getUsed()))
            .append(" BYTE.");
        
		_buff.append("\n")
            .append("  Committed Memory:\t")
            .append(n_format.format(usage.getCommitted()))
            .append(" BYTE.");
        
		_buff.append("\t\tMaximum Memory:\t")
            .append(n_format.format(usage.getMax()))
            .append(" BYTE.");
        
		_buff.append("\n\n")
            .append("  Usage Threshold:\t")
            .append(memoryPoolData.getUsageThreshold() != -9
                ? n_format.format(memoryPoolData.getUsageThreshold()) + " BYTE."
                : "Not Supported");
        
		_buff.append(".\t")
            .append("Usage Threshold Count:\t")
            .append(memoryPoolData.getUsageThresholdCount() != -9
                ? memoryPoolData.getUsageThresholdCount()
                : "Not Supported");
        
		_buff.append("\n  Collection Usage Threshold:\t")
            .append(memoryPoolData.getCollectionUsageThreshold() != -9
                ? n_format.format(memoryPoolData.getCollectionUsageThreshold()) + " BYTE."
                : "Not Supported");
        
		_buff.append(".    ")
            .append("Collection Usage Threshold Count:\t")
            .append(memoryPoolData.getCollectionUsageThresholdCount() != -9
                ? memoryPoolData.getCollectionUsageThresholdCount()
                : "Not Supported")
            .append("\n");
		
		Collection<GarbageCollectorData> collectors =
            garbageCollectorRepository.getGarbageCollectorByMemory(graph_name);
        
        byte i = 1;
		for (GarbageCollectorData collector : collectors) {
            _buff.append("\n")
                .append("  Garbage Collector ")
                .append(i ++)
                .append(":  ")
                .append(collector.toString());
		}
		return _buff.toString();
	}
	
	private class MPoolConfigurationActionListener implements ActionListener {
		SynchBasicGraphPanel graphPanel = null;
		
		public MPoolConfigurationActionListener(SynchBasicGraphPanel panel) {
			graphPanel = panel;
		}
		
        @Override
		public void actionPerformed(ActionEvent ae) {
			if ("Set".equals(ae.getActionCommand())) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							SynchMemoryThresholdInputDialog.getMemoryDialog().showInputDialog(applicationId, graph_name);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				unthresholdButton.setEnabled(true);
			}
			else {
				try {
					MXBeanServer mxbeanServer = MXBeanServerManager.getMXBeanServer(applicationId);
					if (mxbeanServer != null) {
						try {
							mxbeanServer.setAttribute(new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + ",name=" + graph_name), new Attribute("UsageThreshold", 0));
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					//Titu-mpoolMBeanShared.setChangedPoolConfig(graph_name);
					unthresholdButton.setEnabled(false);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
