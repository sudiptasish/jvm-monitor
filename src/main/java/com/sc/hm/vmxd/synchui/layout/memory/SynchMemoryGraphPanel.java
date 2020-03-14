package com.sc.hm.vmxd.synchui.layout.memory;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.lang.management.MemoryUsage;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.common.algo.MaxMinResultObject;
import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.GarbageCollectorDataRepository;
import com.sc.hm.vmxd.data.MemoryDataRepository;
import com.sc.hm.vmxd.data.gbcollector.GarbageCollectorData;
import com.sc.hm.vmxd.data.memory.MemoryData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.common.SynchBarGraphPanel;
import com.sc.hm.vmxd.synchui.layout.common.SynchMGraphPlotterPanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchMonitoringGraphPanel;
import java.util.Collection;

/**
 * Memory panel.
 * 
 * @author Sudiptasish Chanda
 */
public class SynchMemoryGraphPanel extends SynchMonitoringGraphPanel {

	private static final long serialVersionUID = 1L;
	
	private String applicationId = "";

	private MemoryDataRepository memoryRepository = null;
	private GarbageCollectorDataRepository garbageCollectorRepository = null;
	
	private MemoryData memoryData = null;
	private String[] memoryNames = null;
	
	private long currentMaxX = -1L;
	private long currentMinX = -1L;
	
	private long previousMaxX = 1L;
	private long previousMinX = 1L;
	
	private SynchMGraphPlotterPanel mGraphPlotterPanel = null;
	private SynchMGraphPlotterPanel[] allGraphPanels = null;
	private SynchBarGraphPanel[] barPanel = null;
	
	private JLabel[] barLabels = null;	
	private JLabel[] poolLabels = null;
	
	private final NumberFormat n_format = NumberFormat.getInstance();
	
	private final Font font = new Font("Arial", Font.PLAIN, 11);
    
    private final StringBuilder _buff = new StringBuilder(512);
	
	public SynchMemoryGraphPanel(String application
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
		
	public SynchMemoryGraphPanel(String application
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
		addAllComponent();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId)
                .getSynchLockByName(AbstractLockRepository.MEMORY_LOCK);
			memoryRepository = (MemoryDataRepository)AbstractMBeanDataRepositoryFactory
                .getDataRepositoryFactory(applicationId).getRepositoryByName(
                    AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY);
			garbageCollectorRepository = (GarbageCollectorDataRepository)AbstractMBeanDataRepositoryFactory
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
        
		mGraphPlotterPanel.setMemoryPool(memoryRepository.getMemoryData(sharedObj.getSelectionName()));
		setPrimaryPlotterPanel(mGraphPlotterPanel);
		configurePrimaryPanelComponent();
	}
	
	private void initializeOtherComponent() throws Exception {
		initializeComponent();
		initializeDisplayArea(8, 70, new Dimension(620, 130));
	}
	
	private void initializeBarComponent() throws Exception {
		memoryNames = memoryRepository.getMemoryNames();
		barPanel = new SynchBarGraphPanel[memoryNames.length];
		barLabels = new JLabel[memoryNames.length];
		
		int topIndent = mGraphPlotterPanel.getHeight();
		for (int i = 0; i < memoryNames.length; i ++) {
			barPanel[i] = new SynchBarGraphPanel(memoryNames[i], 40, 130);
			barPanel[i].setPanelBounds(760 + i * (20 + 40), topIndent + 47);
			barLabels[i] = new JLabel(memoryNames[i].equals(MemoryDataRepository.HEAP_MEMORY) ? "H" : "NH");
			barLabels[i].setFont(font);
			barLabels[i].setBounds(760 + 10 + i * (20 + 40), topIndent + 47 + 130 + 2, 20, 10);
		}
	}
	
	private void initializeOtherPoolComponent() throws Exception {
		String[] memoryPools = memoryRepository.getMemoryNames();
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
			allGraphPanels[i].setMemoryPool(memoryRepository.getMemoryData(memoryPools[i]));
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
		for (SynchBarGraphPanel panel : barPanel) {
			add(panel);
		}
		for (JLabel label : barLabels) {
			add(label);
		}
		for (SynchMGraphPlotterPanel panel : allGraphPanels) {
			add(panel);
		}		
		for (JLabel label : poolLabels) {
			add(label);
		}
	}
	
    @Override
	public void startSynchronizedMonitoring() throws Exception {
		long start_time = 0L;
		retrieveInformation("com.sc.hm.vmxd.process.MemoryMBeanSynchProcess");		
		try {
			thirdPartyLock.startGetItem();
			start_time = System.currentTimeMillis();
			memoryData = memoryRepository.getMemoryData(graph_name);
			processMinXAndMaxX();
			
			updatePrimaryComponent();
			updateDetailsComponent();
			updateBarComponent();
			updateOtherComponents();

			checkPersistence(MonitorTask.TASK_MEMORY_PERSIST);
		}
		finally {
			thirdPartyLock.endGetItem();
		}
		Logger.log("Time [SynchMemoryGraphPanel]: " + (System.currentTimeMillis() - start_time) + " Millis");
	}
	
	private void updatePrimaryComponent() {
		mGraphPlotterPanel.setMemoryPool(memoryData);
		updatePrimaryPanel();
	}
	
	private void updateDetailsComponent() throws Exception {
		updateDisplayArea(displayPoolDetailsInfo());
	}
	
	private void updateBarComponent() {
		for (byte i = 0; i < memoryNames.length; i ++) {
			MemoryData memory = memoryRepository.getMemoryData(memoryNames[i]);
			barPanel[i].setCurrentMinX(0);
			barPanel[i].setCurrentMaxX(memory.getMax());
			barPanel[i].setCurrentValue(memory.getCurrentUsed());
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
			currentMaxX = memoryData.getMax();
			if (currentMaxX < memoryData.getUsed_max()) {
				currentMaxX = memoryData.getUsed_max();
			}
			/*
			if (sharedObj.isShowCommittedUsage()) {
				if (currentMaxX < memory.getCommitted_max()) {
					currentMaxX = memory.getCommitted_max();
				}
			}
			*/
		}
		else {
			currentMinX = memoryData.getUsed_min();
			currentMaxX = memoryData.getUsed_max();
			/*
			if (sharedObj.isShowCommittedUsage()) {
				if (currentMaxX < memory.getCommitted_max()) {
					currentMaxX = memory.getCommitted_max();
				}
			}
			*/
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
		if (memoryData.getUsageList().size() >= 2) {
			/*timeAlgo.setStartTime(memory.getStartTime());
			timeAlgo.setSpan(Integer.parseInt(sharedObj.getSpan()));
			timeAlgo.setDelay(sharedObj.getGraphDelay());
			timeAlgo.setTotalPoints(memory.getPoolUsage().size());
			
			TimeObject timeObject = timeAlgo.calculateTimes();
			displayXLabels(timeObject);*/
			displayXLabels(memoryData.getDateList());
		}
	}

	private String displayPoolDetailsInfo() throws Exception {
		MemoryUsage usage = memoryData.getCurrentUsage();
		
		_buff.append("  Memory Name:     ")
            .append(memoryData.getName())
            .append(".\tMemory Type:    ")
            .append(memoryData.getType())
            .append(".")
            .append("\t[Current Time: ")
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
            .append(" BYTE.\n");
		
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
}