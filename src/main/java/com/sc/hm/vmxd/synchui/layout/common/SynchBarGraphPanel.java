package com.sc.hm.vmxd.synchui.layout.common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class SynchBarGraphPanel extends AbstractSynchGraphPlotterPanel {
	
	private static final long serialVersionUID = 1L;
	
	private long currentMinX = 0L;
	private long currentMaxX = 0L;
	private long currentValue = 0L;
	private long usageThreshold = -9L;
	
	public SynchBarGraphPanel() {
		this("BarGraph", 1, 1);
	}

	public SynchBarGraphPanel(String name, int width, int height) {
		super(name, width, height, false);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		initGraphPanel(g2d);
		drawCoordinates(g2d);
		plotValues(g2d);
	}

	public long getCurrentMaxX() {
		return currentMaxX;
	}

	public void setCurrentMaxX(long currentMaxX) {
		this.currentMaxX = currentMaxX;
	}

	public long getCurrentMinX() {
		return currentMinX;
	}

	public void setCurrentMinX(long currentMinX) {
		this.currentMinX = currentMinX;
	}

	public long getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(long currentValue) {
		this.currentValue = currentValue;
	}

	public long getUsageThreshold() {
		return usageThreshold;
	}

	public void setUsageThreshold(long usageThreshold) {
		this.usageThreshold = usageThreshold;
	}

	public void initGraphPanel(Graphics2D g) {
		g.clearRect(0, 0, this.width, this.height);
		g.setColor(this.defaultGraphBackgroundColor);
		g.fillRect(0, 0, this.width, this.height);
	}
	
	public void drawCoordinates(Graphics2D g) {
	}
	
	public void plotValues(Graphics2D g) {
		int yValue = calculateEquivalentYCoordinate(currentValue);
		if ((height - yValue) * 100 / height >= 90) {
			g.setColor(Color.RED);
		}
		else {
			g.setColor(Color.GREEN);
		}
		g.fillRect(0, yValue, width, height - yValue);
		
		if (usageThreshold > 0) {
			yValue = calculateEquivalentYCoordinate(usageThreshold);
			g.setColor(Color.ORANGE);
			g.fillRect(0, 0, width, yValue);
		}
	}
	
	public int calculateEquivalentYCoordinate(long currentValue) {
		double val = (double)height - ((double)height / (currentMaxX - currentMinX) * (currentValue - currentMinX));
		return (int)Math.round(val);
	}

	public void setMouseMotionListener() {}
}
