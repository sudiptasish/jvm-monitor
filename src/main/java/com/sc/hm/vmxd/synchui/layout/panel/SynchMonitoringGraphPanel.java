package com.sc.hm.vmxd.synchui.layout.panel;

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
import com.sc.hm.monitor.util.GraphImageProperties;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor;
import com.sc.hm.vmxd.process.lock.ThirdPartySynchronizedLock;
import com.sc.hm.vmxd.process.util.ProcessStatUtil;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.common.SynchDisplayArea;
import com.sc.hm.vmxd.synchui.layout.common.SynchSharedGraphPlotterPanel;

/**
 * Main panel that holds all the ui components.
 * 
 * @author Sudiptasish Chanda
 */
public abstract class SynchMonitoringGraphPanel extends AbstractSynchGraphProcessPanel {

	private static final long serialVersionUID = 1L;
	
	protected ThirdPartySynchronizedLock thirdPartyLock = null;
	
	protected SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED sharedObj = null;
	protected String graph_name = "";
	
	private final Font DEFAULT_FONT = new Font("Verdana", Font.PLAIN, 10);
	private final int X_LABEL_INTERVAL = 80;
	
	private SynchSharedGraphPlotterPanel graphPlotterPanel = null;
	private SynchDisplayArea displayArea = null;
	
	private final JLabel factorLabel = new JLabel("");
	private final JLabel[] symbolLabels = new JLabel[10];
	private final JLabel[] xLabels = new JLabel[10];
	private final JLabel[] yLabels = new JLabel[9];
	
	protected final GraphPanelAlgorithm graphAlgo = new GraphPanelAlgorithm();
	protected final TimeAlgorithm timeAlgo = new TimeAlgorithm();
	
	protected final NumberFormat d_format = NumberFormat.getInstance();
	private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private final Date date = new Date();
	
	private JScrollPane scrollPane = null;
	
	protected SynchBubblePanel bubblePanel = null;

	private JLayeredPane layeredPane = null;
	
	private final long pauseTime = 4000;
	
	protected SynchMonitoringGraphPanel(int panel_width, int panel_height) throws Exception {
		super(panel_width, panel_height);
	}

    protected SynchMonitoringGraphPanel(SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED sharedObj
            , int panel_width
            , int panel_height) throws Exception {
        
        super(panel_width, panel_height);
        this.sharedObj = sharedObj;
    }

	protected SynchMonitoringGraphPanel(SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED sharedObj
	        , int panel_width
	        , int panel_height
	        , Border panel_border
	        , LayoutManager layout) throws Exception {
	    
		super(panel_width, panel_height, panel_border, layout);
		this.sharedObj = sharedObj;
		setGraph_name(this.sharedObj.getSelectionName());
		initBubbledPanel(800, 280);
		initLayeredPane(800, 280);		
		initOther();
	}
	
	private void initBubbledPanel(int panel_width, int panel_height) {
		bubblePanel = new SynchBubblePanel(panel_width, panel_height);
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
	
	protected void setPrimaryPlotterPanel(SynchSharedGraphPlotterPanel primaryPlotterPanel) {
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
		
		displayArea = new SynchDisplayArea(row, col);
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
	
	protected void retrieveInformation(String process_name) {
		synchronized (sharedObj) {
			setGraph_name(sharedObj.getSelectionName());
			setRun(sharedObj.isRun());
			int earlier_interval = ProcessStatUtil.getProcessInterval(process_name);
			if (earlier_interval != sharedObj.getGraphDelay()) {
				ProcessStatUtil.setProcessInterval(process_name, sharedObj.getGraphDelay());
			}
			if (graphPlotterPanel != null) {
			    setMainGraphPanelParameter();
			}
		}
	}
	
	protected void checkPersistence(String taskId) {
		if (sharedObj.isPersist()) {
			PersistentManager.persistData(taskId, sharedObj.getDirectoryLocation(), "com.sc.hm.monitor.persistence.task.SynchPersistentTask");
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
	
	protected void displayXLabels(List<Date> timeList) {
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
				xLabels[labelIndex].setText(dateFormat.format(timeList.get(i)));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Override
	public synchronized void startProcess() {
		setRun(sharedObj.isRun());
		MonitoringGraphProcess monitoringProcess = new MonitoringGraphProcess(this);
		initializeComponentProcess(monitoringProcess);
		_startProcessInternal();
	}

	public synchronized void startProcess(AbstractMBeanProcessExecutor executor) {
		setRun(sharedObj.isRun());
		MonitoringGraphProcess monitoringProcess = new MonitoringGraphProcess(this);
		initializeComponentProcess(monitoringProcess);
		_startProcessInternal(executor);
	}

    @Override
	public synchronized void stopProcess() {
		setRun(false);
		setForceStop(true);
		_stopProcessInternal();
	}
	
	public class MonitoringGraphProcess implements AbstractSynchGraphProcessPanel.ComponentProcess {
		private AbstractSynchGraphProcessPanel graphProcessPanel = null;
		
		public MonitoringGraphProcess(AbstractSynchGraphProcessPanel panel) {
			graphProcessPanel = panel;
		}
		
        @Override
		public synchronized void run() {
			try {
				updateComponent();
				wait(100);
			}
            catch (InterruptedException e) {
                Logger.log("Process " + getClass().getName() + " Interrupted. Exiting....");
            }
			catch (Exception e) {
				Logger.log("Process " + getClass().getName() + " encountered error");
				e.printStackTrace();
			}
		}

        @Override
		public void updateComponent() throws Exception {
			while (!Thread.currentThread().isInterrupted()) {
				if (isRun()) {
					graphProcessPanel.startSynchronizedMonitoring();
					Thread.sleep(pauseTime);
				}
				else {
					if (isForceStop()) {
						Logger.log("Exiting Graph Process Thread....");
						break;
					}
					Logger.log("Thread " + Thread.currentThread() + " going to sleep ....");
					sharedObj.acquireLockAndStartWaiting();
					Logger.log("Thread " + Thread.currentThread() + " Awaked ....");
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
