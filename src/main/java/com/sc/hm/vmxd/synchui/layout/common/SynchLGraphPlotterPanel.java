package com.sc.hm.vmxd.synchui.layout.common;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;

import com.sc.hm.monitor.util.GraphImageProperties;
import com.sc.hm.vmxd.data.gbcollector.GarbageCollectorData;
import com.sc.hm.vmxd.synchui.layout.panel.SynchBubblePanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchXGraphLine;
import com.sc.hm.vmxd.synchui.layout.panel.SynchYGraphLine;

public class SynchLGraphPlotterPanel extends SynchSharedGraphPlotterPanel {
	
	private static final long serialVersionUID = 1L;
	
	private GarbageCollectorData gbCollector = null;
	private long currentMinX = 0L;
	private long currentMaxX = 0L;
	
	private Line2D line = new Line2D.Double();

	private SynchBubblePanel bubblePanel = null;

	public SynchLGraphPlotterPanel() {
		this("GraphPanel", 1, 1);
	}

	public SynchLGraphPlotterPanel(String name, int width, int height) {
		this(name, width, height, false);		
	}

	public SynchLGraphPlotterPanel(String name, int width, int height, boolean flag) {
		this(name, width, height, flag, new SynchBubblePanel(width, height));		
	}

	public SynchLGraphPlotterPanel(String name, int width, int height, boolean flag, SynchBubblePanel bubblePanel) {
		super(name, width, height, flag);
		this.bubblePanel = bubblePanel;
		initXYLines();
	}
	
	private void initXYLines() {
		if (isMainPanel()) {
			bubblePanel.xLine = new SynchXGraphLine[GraphImageProperties.IMAGE_WIDTH + 1];
			bubblePanel.yLine = new SynchYGraphLine[GraphImageProperties.IMAGE_WIDTH + 1];
			for (short i = 0; i < bubblePanel.xLine.length; i ++) {
				bubblePanel.yLine[i] = new SynchYGraphLine(0 + i, 0, 0 + i, GraphImageProperties.IMAGE_HEIGHT);
			}
		}
	}

	public GarbageCollectorData getGbCollector() {
		return gbCollector;
	}

	public void setGbCollector(GarbageCollectorData gbCollector) {
		this.gbCollector = gbCollector;
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

	public void paintComponent(Graphics g) {
		try {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			
			initGraphPanel(g2d);
			initDoubleInterval(xFactor);
			drawCoordinates(g2d);
			if (!fillCurve) {
				plotValues(g2d);
			}
			else {
				fillGraph(g2d);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			g.dispose();
		}
	}
	
	public void initGraphPanel(Graphics2D g) {
		g.clearRect(0, 0, this.width, this.height);
		g.setColor(this.defaultGraphBackgroundColor);
		g.fillRect(0, 0, this.width, this.height);
	}
	
	public void drawCoordinates(Graphics2D g) {
		g.setColor(this.axisColor);
		if (!isMainPanel()) {
			currentMinX = 0L;
			currentMaxX = gbCollector.getMaxCollectionCount() + gbCollector.getMaxCollectionCount() / 2;
		}
		
		bubblePanel.currentMaxX = currentMaxX;
		bubblePanel.currentMinX = currentMinX;
		
		// draw X lines
		if (showX) {
			drawXLines(g);
		}
		// draw Y lines
		if (isMainPanel()) {
			drawYLinesBubble(g);
		}
		else {
			drawYLines(g);
		}
	}
	
	private void drawXLines(Graphics2D g) {
		int tmpInterval = (int)(axisInterval < 10 ? 10 : axisInterval);
		for (int y = height; y >= tmpInterval; y -= tmpInterval) {
			g.drawLine(0, y, width, y);
		}
	}
	
	private void drawYLines(Graphics2D g) {
		int tmpInterval = (int)(axisInterval < 10 ? 10 : axisInterval);
		for (int x = tmpInterval; x <= width; x += tmpInterval) {
			g.drawLine(x, 0, x, height);
		}
	}
	
	private void drawYLinesBubble(Graphics2D g) {
		int tmpInterval = (int)(axisInterval < 10 ? 10 : axisInterval);
		bubblePanel.yLine[0].setActive(false);
		for (int x = 1; x <= width; x ++) {
			bubblePanel.yLine[x].setActive(false);
			if (showY && x % tmpInterval == 0) {
				g.draw(bubblePanel.yLine[x]);
			}
		}
	}
	
	public void plotValues(Graphics2D g) {
		g.setColor(this.graphColor);
		List<Long> usageList = gbCollector.getCollectionList();
		int totalPoints = usageList.size();
		if (totalPoints < 2) {
			return;
		}
		int maxPoints = (int)(width / axisInterval + 1);
		int toIndex = (totalPoints < maxPoints) ? 0 : totalPoints - maxPoints;
		
		g.setStroke(new BasicStroke(strokeWidth));
		double xPoint = (double)width;
		for (int i = totalPoints - 1; i > toIndex; i --, xPoint -= axisInterval) {
			long val = usageList.get(i);
			double fromYValue = calculateEquivalentYCoordinate(val);
			double toYValue = calculateEquivalentYCoordinate(usageList.get(i - 1));
			
			bubblePanel.yLine[(int)xPoint].setLineValue(val);
			bubblePanel.yLine[(int)xPoint].setActive(true);
			
			line.setLine(xPoint, fromYValue, xPoint - axisInterval, toYValue);
			g.draw(line);
		}
		bubblePanel.yLine[(int)xPoint].setLineValue(usageList.get(toIndex));
		bubblePanel.yLine[(int)xPoint].setActive(true);
	}
	
	public void fillGraph(Graphics2D g) {
		g.setColor(this.graphColor);
		int[] xPoints = null;
		int[] yPoints = null;
		
		List<Long> usageList = gbCollector.getCollectionList();
		if (usageList.size() < 2) {
			return;
		}
		int totalPoints = usageList.size();		
		int maxPoints = (int)(width / axisInterval + 1);
		int toIndex = 0;
		if (totalPoints < maxPoints) {
			toIndex = 0;
			xPoints = new int[totalPoints + 2];
			yPoints = new int[totalPoints + 2];
		}
		else {
			toIndex = totalPoints - maxPoints;
			xPoints = new int[(int)(width/axisInterval + 3)];
			yPoints = new int[(int)(width/axisInterval + 3)];
		}
		
		int k = 0;
		xPoints[k] = width;
		yPoints[k] = height;
		
		for (int i = totalPoints - 1, xPoint = width; i >= toIndex; i --, xPoint -= axisInterval) {
			int fromXValue = xPoint;
			int fromYValue = (int)Math.round(calculateEquivalentYCoordinate(usageList.get(i)));
			xPoints[++k] = fromXValue;
			yPoints[k] = fromYValue;
		}
		xPoints[++k] = xPoints[k - 1];
		yPoints[k] = height;
		
		g.fillPolygon(xPoints, yPoints, xPoints.length);
	}
	
	public double calculateEquivalentYCoordinate(long currentValue) {
		double val = (double)height - ((double)height / (currentMaxX - currentMinX) * (currentValue - currentMinX));
		return val;
	}
}
