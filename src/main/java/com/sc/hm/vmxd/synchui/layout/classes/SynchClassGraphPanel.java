package com.sc.hm.vmxd.synchui.layout.classes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.common.algo.MaxMinResultObject;
import com.sc.hm.monitor.persistence.task.MonitorTask;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.ClassDataRepository;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.common.SynchCGraphPlotterPanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchMonitoringGraphPanel;

public class SynchClassGraphPanel extends SynchMonitoringGraphPanel {

	private static final long serialVersionUID = 1L;
	
	private String applicationId = "";

	private ClassDataRepository classDataRepository = null;
	
	private List<Long> classUsage = null;
	
	private long currentMaxX = 0L;
	private long currentMinX = 0L;

	private SynchCGraphPlotterPanel cGraphPlotterPanel = null;
	private SynchCGraphPlotterPanel[] allGraphPanels = null;
	
	private JLabel[] classLabels = null;
	
	public SynchClassGraphPanel(String application, SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int panel_width, int panel_height) throws Exception {
		this(application, obj, panel_width, panel_height, new TitledBorder(""), null);
	}

	public SynchClassGraphPanel(String application, SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int panel_width, int panel_height, Border panel_border, LayoutManager layout) throws Exception {
		super(obj, panel_width, panel_height, panel_border, layout);
		applicationId = application;
		initOther();
		initializePrimaryPanel();
		initializeOtherComponent();
		initializeOtherClassTypeComponent();
		addAllComponent();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.CLASS_LOADING_LOCK);
			classDataRepository = (ClassDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_CLASS);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initializePrimaryPanel() throws Exception {
		cGraphPlotterPanel = new SynchCGraphPlotterPanel(sharedObj.getSelectionName(), 800, 280, true, bubblePanel);
		cGraphPlotterPanel.setUsageList(classDataRepository.getTargetClassTypeUsage(graph_name));
		setPrimaryPlotterPanel(cGraphPlotterPanel);
		configurePrimaryPanelComponent();
	}
	
	private void initializeOtherComponent() throws Exception {
		initializeComponent();
		initializeDisplayArea(8, 70, new Dimension(620, 130));
	}
	
	private void initializeOtherClassTypeComponent() throws Exception {
		String[] classTypes = classDataRepository.getAllClassTypes();
		classLabels = new JLabel[classTypes.length];
		
		int pools_consolidated_width = getWidth() - 10 - 10;
		int gap_between_2_pools = 10;
		int each_pool_width = (pools_consolidated_width - (classTypes.length - 1) * gap_between_2_pools) / classTypes.length;
		
		int mPoolGraph_XLength = each_pool_width;
		int remainder = mPoolGraph_XLength % 10;
		if (remainder != 0) {
			mPoolGraph_XLength -= remainder;
		}
		int mPoolGraph_YLength = 70;
		
		allGraphPanels = new SynchCGraphPlotterPanel[classTypes.length];
		int startX = 10 + (classTypes.length * remainder) / 2;
		for (byte i = 0; i < allGraphPanels.length; i ++) {
			allGraphPanels[i] = new SynchCGraphPlotterPanel(classTypes[i], mPoolGraph_XLength, mPoolGraph_YLength);
			allGraphPanels[i].setUsageList(classDataRepository.getTargetClassTypeUsage(classTypes[i]));
			allGraphPanels[i].setXFactor(4);
			allGraphPanels[i].setPanelBounds(startX + (i * mPoolGraph_XLength) + (i * 10), 490);
			allGraphPanels[i].setFillCurve(true);
			
			classLabels[i] = new JLabel(classTypes[i]);
			classLabels[i].setBounds(startX + (i * mPoolGraph_XLength) + (i * 10), 490 - 15, mPoolGraph_XLength, 15);
			classLabels[i].setFont(new Font("Arial", Font.PLAIN, 11));
		}
	}
	
	public void addAllComponent() throws Exception {
		addComponent();
		for (SynchCGraphPlotterPanel panel : allGraphPanels) {
			add(panel);
		}		
		for (JLabel label : classLabels) {
			add(label);
		}
	}
	
	public void startSynchronizedMonitoring() throws Exception {
		try {
			thirdPartyLock.startGetItem();
			retrieveInformation("com.sc.hm.vmxd.process.ClassMBeanSynchProcess");
			classUsage = classDataRepository.getTargetClassTypeUsage(graph_name);
			processMinXAndMaxX();
				
			updatePrimaryComponent();
			updateDetailsComponent();
			updateOtherComponents();
			checkPersistence(MonitorTask.TASK_CLASSES_PERSIST);
		}
		finally {
			thirdPartyLock.endGetItem();
		}
	}
	
	private void updatePrimaryComponent() {
		cGraphPlotterPanel.setUsageList(classUsage);
		updatePrimaryPanel();
	}
	
	private void updateDetailsComponent() {
		updateDisplayArea(displayClassDetailsInfo());
	}
	
	private void updateOtherComponents() {
		for (byte i = 0; i < allGraphPanels.length; i ++) {
			long maxType = classDataRepository.getMaxCount();				
			allGraphPanels[i].setCurrentMaxX(maxType + (maxType / 2));
			allGraphPanels[i].setCurrentMinX(0L);
			allGraphPanels[i].repaint();
		}
	}
	
	private void processMinXAndMaxX() {
		int yPoint = 8;
		if (sharedObj.isDynmaicAxis()) {
			currentMaxX = classDataRepository.getMaxCount();
			currentMinX = classDataRepository.getMinCount();
		}
		else {
			currentMaxX = classDataRepository.getMaxCount() + classDataRepository.getMaxCount() / 2;
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
		cGraphPlotterPanel.setCurrentMinX(currentMinX);
		cGraphPlotterPanel.setCurrentMaxX(currentMaxX);
		
		displayYLabels(new MaxMinResultObject(currentMinX, currentMaxX, "Count"));
		
		if (classUsage.size() >= 2) {
			/*timeAlgo.setStartTime(classInfo.getStartTime());
			timeAlgo.setSpan(Integer.parseInt(sharedObj.getSpan()));
			timeAlgo.setDelay(sharedObj.getGraphDelay());
			timeAlgo.setTotalPoints(classInfo.getCount().size());
			
			TimeObject timeObject = timeAlgo.calculateTimes();
			displayXLabels(timeObject);*/
			displayXLabels(classDataRepository.getTimeList());
		}
	}
	
	private String displayClassDetailsInfo() {
		long classCount = 0L;
		StringBuilder sb = new StringBuilder(1000);		
		sb.append("  Class Loading Details:    ").append("[Current Time: " + new Date() + "]");
		
		List<Long> temp = classDataRepository.getTargetClassTypeUsage(ClassDataRepository.CLASS_TYPE_TOTAL);
		if (temp.size() > 0) {
			classCount = temp.get(temp.size() - 1);
		}
		sb.append("\n\n  Total Class: ").append(classCount).append(".\t\t");		
		temp = classDataRepository.getTargetClassTypeUsage(ClassDataRepository.CLASS_TYPE_LOADED);
		if (temp.size() > 0) {
			classCount = temp.get(temp.size() - 1);
		}
		sb.append("  Loaded Class: ").append(classCount).append(".\t\t");		
		temp = classDataRepository.getTargetClassTypeUsage(ClassDataRepository.CLASS_TYPE_UNLOADED);
		if (temp.size() > 0) {
			classCount = temp.get(temp.size() - 1);
		}
		sb.append("  Unloaded Class: ").append(classCount).append(".");
		return sb.toString();
	}
}
