package com.sc.hm.vmxd.synchui.layout.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.util.GraphImageProperties;

public abstract class AbstractSynchGraphPlotterPanel extends JPanel implements SynchGraphPlotterProps {
	
	private static final long serialVersionUID = 1L;

	private String panelName = "";
	
	protected int width = 0;
	protected int height = 0;
	protected Dimension dimension = null;
	
	protected Color defaultGraphBackgroundColor = Color.BLACK;
	
	protected double axisInterval = 0.0;
	
	private boolean mainPanel = true;

	public AbstractSynchGraphPlotterPanel(String name, int width, int height, boolean flag) {
		super();
		panelName = name;
		this.width = width;
		this.height = height;
		this.mainPanel = flag;
		setDimension(new Dimension(this.width, this.height));
		initOther();
	}
	
	public void initOther() {
		setSize(dimension);
		setBorder(new BevelBorder(BevelBorder.LOWERED));
	}
	
	public void setPanelBounds(int startX, int startY) {
		setBounds(startX, startY, this.width, this.height);
	}
	
	public void initInterval(int xFactor) {
		setAxisInterval(GraphImageProperties.DEFAULT_X_INTERVAL / xFactor);
	}
	
	public void initDoubleInterval(int xFactor) {
		setDoubleAxisInterval((double)GraphImageProperties.DEFAULT_X_INTERVAL / xFactor);
	}

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(boolean mainPanel) {
		this.mainPanel = mainPanel;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
		this.width = (int)this.dimension.getWidth();
		this.height = (int)this.dimension.getHeight();
	}
	
	public Dimension getSize() {
		return dimension;
	}
	
	public Dimension getPreferredSize() {
		return dimension;
	}
	
	public Color getGraphBackgroundColor() {
		return defaultGraphBackgroundColor;
	}

	public void setGraphBackgroundColor(Color graphBackgroundColor) {
		this.defaultGraphBackgroundColor = graphBackgroundColor;
	}
	
	public int getAxisInterval() {
		return (int)axisInterval;
	}
	
	public double getDoubleAxisInterval() {
		return axisInterval;
	}

	public void setAxisInterval(int axisInterval) {
		this.axisInterval = axisInterval;
	}

	public void setDoubleAxisInterval(double axisInterval) {
		this.axisInterval = axisInterval;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	public abstract void setMouseMotionListener();
}
