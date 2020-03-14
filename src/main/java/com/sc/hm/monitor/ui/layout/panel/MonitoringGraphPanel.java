package com.sc.hm.monitor.ui.layout.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.sc.hm.monitor.common.algo.GraphPanelAlgorithm;
import com.sc.hm.monitor.common.algo.MaxMinResultObject;
import com.sc.hm.monitor.common.algo.TimeAlgorithm;
import com.sc.hm.monitor.common.algo.TimeObject;
import com.sc.hm.monitor.persistence.PersistentManager;
import com.sc.hm.monitor.ui.layout.common.DisplayArea;
import com.sc.hm.monitor.ui.layout.common.SharedGraphPlotterPanel;
import com.sc.hm.monitor.ui.layout.common._UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.monitor.util.GraphImageProperties;
import com.sc.hm.monitor.util.Logger;

public abstract class MonitoringGraphPanel extends AbstractGraphProcessPanel {

	private static final long serialVersionUID = 1L;
	
	protected _UI_SELECTIONPANEL_GRAPHPANEL_SHARED sharedObj = null;
	protected String graph_name = "";
	
	private final Font DEFAULT_FONT = new Font("Verdana", Font.PLAIN, 10);
	private final int X_LABEL_INTERVAL = 80;
	
	private SharedGraphPlotterPanel graphPlotterPanel = null;
	private DisplayArea displayArea = null;
	
	private JLabel factorLabel = new JLabel("");
	private JLabel[] symbolLabels = new JLabel[10];
	private JLabel[] xLabels = new JLabel[10];
	private JLabel[] yLabels = new JLabel[9];
	
	protected GraphPanelAlgorithm graphAlgo = new GraphPanelAlgorithm();
	protected TimeAlgorithm timeAlgo = new TimeAlgorithm();
	
	protected final NumberFormat d_format = NumberFormat.getInstance();
	private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private Date date = new Date();
	
	private JScrollPane scrollPane = null;
	
	protected BubblePanel bubblePanel = null;

	private JLayeredPane layeredPane = null;
	
	protected MonitoringGraphPanel(int panel_width, int panel_height) throws Exception {
		super(panel_width, panel_height);
	}

	protected MonitoringGraphPanel(_UI_SELECTIONPANEL_GRAPHPANEL_SHARED sharedObj , int panel_width, int panel_height, Border panel_border, LayoutManager layout) throws Exception {
		super(panel_width, panel_height, panel_border, layout);
		this.sharedObj = sharedObj;
		setGraph_name(this.sharedObj.getSelectionName());
		initBubbledPanel(800, 280);
		initLayeredPane(800, 280);		
		initOther();
	}
	
	private void initBubbledPanel(int panel_width, int panel_height) {
		bubblePanel = new BubblePanel(panel_width, panel_height);
		//bubblePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		bubblePanel.setOpaque(false);
		bubblePanel.setMouseMotionListener();
	}
	
	private void initLayeredPane(int panel_width, int panel_height) {
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(60, 10, panel_width, panel_height);
		//layeredPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}
	
	private void initOther() {
		d_format.setGroupingUsed(false);
		d_format.setMaximumFractionDigits(2);
		d_format.setMinimumFractionDigits(0);
	}
	
	protected void setPrimaryPlotterPanel(SharedGraphPlotterPanel primaryPlotterPanel) {
		graphPlotterPanel = primaryPlotterPanel;
	}
	
	protected void configurePrimaryPanelComponent() throws Exception {
		//graphPlotterPanel.setPanelBounds(60, 10);
		//graphPlotterPanel.setMouseMotionListener();
		graphPlotterPanel.setOpaque(true);
	}
	
	protected void initializeComponent() throws Exception {
		int height = graphPlotterPanel.getHeight();
		factorLabel.setBounds(8, 2 + height + 15, 50, 15);
		factorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		
		for (int i = 0, interval = 280; i < yLabels.length; i ++, interval -= GraphImageProperties.DEFAULT_Y_INTERVAL) {
			yLabels[i] = new JLabel();
			yLabels[i].setFont(DEFAULT_FONT);
			yLabels[i].setBounds(8, 2 + interval, 50, 15);
		}
		for (int i = 0, interval = 75; i < xLabels.length; i ++, interval += X_LABEL_INTERVAL) {
			symbolLabels[i] = new JLabel("^");
			symbolLabels[i].setBounds(interval + 23, height + 10, 70, 10);
			xLabels[i] = new JLabel();
			xLabels[i].setFont(DEFAULT_FONT);
			xLabels[i].setBounds(interval, height + 15, 70, 13);			
		}
	}
	
	protected void initializeDisplayArea(int row, int col, Dimension dimension) throws Exception {
		int height = graphPlotterPanel.getHeight();
		
		displayArea = new DisplayArea(row, col);
		displayArea.setEditable(false);
		displayArea.setBorder(new BevelBorder(BevelBorder.LOWERED));
		displayArea.setFont(new Font("Arial", Font.PLAIN, 11));
		displayArea.setBackground(Color.LIGHT_GRAY);
		displayArea.setForeground(Color.BLACK);
		
		scrollPane = new JScrollPane(displayArea);
		scrollPane.setBounds(60, height + 47, (int)dimension.getWidth(), (int)dimension.getHeight());
	}
	
	protected void addComponent() throws Exception {
		//add(graphPlotterPanel);
		add(factorLabel);
		
		for (JLabel label : xLabels) {
			add(label);
		}
		for (JLabel label : symbolLabels) {
			add(label);
		}
		for (JLabel label : yLabels) {
			add(label);
		}
		add(scrollPane);
		
		layeredPane.add(graphPlotterPanel, new Integer(0));
		layeredPane.add(bubblePanel, new Integer(1));
		add(layeredPane);
	}

	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
	}
	
	public String getGraph_name() {
		return graph_name;
	}

	public void setGraph_name(String graph_name) {
		this.graph_name = graph_name;
	}
	
	protected void retrieveInformation() {
		synchronized (sharedObj) {
			setGraph_name(sharedObj.getSelectionName());
			setRun(sharedObj.isRun());
			setMainGraphPanelParameter();
		}
	}
	
	protected void checkPersistence(String taskId) {
		if (sharedObj.isPersist()) {
			PersistentManager.persistData(taskId, sharedObj.getDirectoryLocation());
			sharedObj.setPersist(false);
		}
	}
	
	protected void setMainGraphPanelParameter() {
		graphPlotterPanel.setXFactor(Integer.parseInt(sharedObj.getSpan()));
		graphPlotterPanel.setShowX(sharedObj.isShowX());
		graphPlotterPanel.setShowY(sharedObj.isShowY());
		graphPlotterPanel.setDynamicAxis(sharedObj.isDynmaicAxis());
		graphPlotterPanel.setStrokeWidth(sharedObj.getBoldFactor());
		graphPlotterPanel.setAxisColor(sharedObj.getAxisColor());
		graphPlotterPanel.setGraphColor(sharedObj.getGraphColor());
		graphPlotterPanel.setFillCurve(sharedObj.isFillCurve());
		graphPlotterPanel.setShowC(sharedObj.isShowCommittedUsage());
	}

	public void updatePrimaryPanel() {
		graphPlotterPanel.repaint();
	}
	
	protected void updateDisplayArea(String text) {
		displayArea.setText(text);
	}
	
	protected void displayYLabels(MaxMinResultObject resultObject) {
		displayYLabels(resultObject, 1);
	}
	
	protected void displayYLabels(MaxMinResultObject resultObject, long diviser) {
		int yPoint = 8;
		long xMax = resultObject.getMaxX();
		long xMin = resultObject.getMinX();
		factorLabel.setText(resultObject.getFactorLabel());
		float interval = (xMax - xMin) / (float)yPoint;
		for (int i = 0; i < yPoint; i ++) {
			yLabels[i].setText(d_format.format((xMin + i * interval) / diviser));
		}
		yLabels[8].setText(d_format.format(xMax / diviser));
	}
	
	protected void displayXLabels(TimeObject timeObject) {
		for (int i = 0; i < timeObject.getFirstXLabelIndex(); i ++) {
			xLabels[i].setText("");
		}
		for (int i = timeObject.getFirstXLabelIndex(), k = 0; i < xLabels.length; i ++, k ++) {
			date.setTime(timeObject.getFirstTimeToPlot() + k * timeObject.getTimeInterval());
			xLabels[i].setText(dateFormat.format(date));
		}
	}
	
	protected void displayXLabels(List<Long> timeList) {
		try {	
			int totalPoints = timeList.size();
			int xFactor = graphPlotterPanel.getXFactor();
			double timeInterval = (double)GraphImageProperties.DEFAULT_X_INTERVAL / xFactor;
			int maxPoints = (int)(graphPlotterPanel.getWidth() / timeInterval + 1);		
			int toIndex = (totalPoints < maxPoints) ? 0 : totalPoints - maxPoints;
			
			for (JLabel label : xLabels) {
				label.setText("");
			}
			
			// Skip last few points = xFactor
			int pointToStart = totalPoints - xFactor;
			if (pointToStart <= 0) {
				return;
			}
			int labelIndex = 9;
			for (int i = pointToStart; i > toIndex; i -= xFactor * 2, labelIndex --) {
				date.setTime(timeList.get(i));
				xLabels[labelIndex].setText(dateFormat.format(date));
			}
			while (labelIndex >= 0) {
				xLabels[labelIndex--].setText("");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void startProcess() {
		setRun(sharedObj.isRun());
		MonitoringGraphProcess monitoringProcess = new MonitoringGraphProcess(this);
		initializeComponentProcess(monitoringProcess);
		_startProcessInternal();
	}

	public synchronized void stopProcess() {
		setRun(false);
		setForceStop(true);
		_stopProcessInternal();
	}
	
	public class MonitoringGraphProcess implements AbstractGraphProcessPanel.ComponentProcess {
		private AbstractGraphProcessPanel graphProcessPanel = null;
		
		public MonitoringGraphProcess(AbstractGraphProcessPanel panel) {
			graphProcessPanel = panel;
		}
		
		public synchronized void run() {
			updateComponent();
		}

		public void updateComponent() {
			while (true) {
				if (isRun()) {
					if (appConfig.getGraphType().equals(GraphImageProperties.GRAPH_TYPE_SYNCH)) {
						graphProcessPanel.startSynchronizedMonitoring();
					}
					else {
						graphProcessPanel.startAsynchronizedMonitoring();
						try {
							wait(sharedObj.getGraphDelay() * 1000);
						}
						catch (InterruptedException inte) {
							Logger.log("Graph Process Thread Interrupted");
						}
					}
				}
				else {
					if (isForceStop()) {
						Logger.log("Exiting Graph Process Thread....");
						break;
					}
					Logger.log("Thread slept....");
					sharedObj.acquireLockAndStartWaiting();
					Logger.log("Thread Awaked....");
					setRun(true);
				}
			}
		}

		public synchronized void notifyOwner() {
			Logger.log("Trying to awake Owner....");
			notify();
		}
	}
}
