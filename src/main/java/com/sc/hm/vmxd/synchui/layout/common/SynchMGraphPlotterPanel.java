package com.sc.hm.vmxd.synchui.layout.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.lang.management.MemoryUsage;
import java.text.NumberFormat;
import java.util.List;

import com.sc.hm.monitor.common.algo.MaxMinResultObject;
import com.sc.hm.monitor.util.GraphImageProperties;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.data.memory.MemoryData;
import com.sc.hm.vmxd.synchui.layout.panel.SynchBubblePanel;
import com.sc.hm.vmxd.synchui.layout.panel.SynchXGraphLine;
import com.sc.hm.vmxd.synchui.layout.panel.SynchYGraphLine;

public class SynchMGraphPlotterPanel extends SynchSharedGraphPlotterPanel {
	
	private static final long serialVersionUID = 1L;

	private boolean draw = false;
	
	private MemoryData memoryData = null;
	private long currentMinX = 0L;
	private long currentMaxX = 0L;
	
	private MaxMinResultObject resultObject = new MaxMinResultObject();
	
	private Line2D line = new Line2D.Double();
	
	private NumberFormat n_format = NumberFormat.getInstance();
	
	private SynchBubblePanel bubblePanel = null;
	
	public SynchMGraphPlotterPanel() {
		this("GraphPanel", 1, 1);
	}

	public SynchMGraphPlotterPanel(String name, int width, int height) {
		this(name, width, height, false);
	}

	public SynchMGraphPlotterPanel(String name, int width, int height, boolean flag) {
		this(name, width, height, flag, new SynchBubblePanel(width, height));
	}

	public SynchMGraphPlotterPanel(String name, int width, int height, boolean flag, SynchBubblePanel bubblePanel) {
		super(name, width, height, flag);		
		this.bubblePanel = bubblePanel;		
		
		// Add Bubble Test
		initXYLines();
		initFormat();
	}
	
	public boolean isDraw() {
		return draw;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}

	public void setMouseMotionListener() {
		if (isMainPanel()) {
			addMouseListener(new MouseListenerHandler(this));
			addMouseMotionListener(new MouseMotionListenerHandler(this));
		}
	}

	public MemoryData getMemoryPool() {
		return memoryData;
	}

	public void setMemoryPool(MemoryData memoryData) {
		this.memoryData = memoryData;
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

	public MaxMinResultObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(MaxMinResultObject resultObject) {
		this.resultObject = resultObject;
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
	
	private void initFormat() {
		n_format.setMaximumFractionDigits(2);
		n_format.setMinimumFractionDigits(0);
		n_format.setGroupingUsed(true);
	}
	
	public SynchBubblePanel getBubblePanel() {
		return bubblePanel;
	}

	public void setBubblePanel(SynchBubblePanel bubblePanel) {
		this.bubblePanel = bubblePanel;
	}

	public void paintComponent(Graphics g) {
		try {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			
			initGraphPanel(g2d);
			initDoubleInterval(xFactor);
			drawCoordinates(g2d);
			if (!fillCurve) {
				if (isMainPanel()) {
					plotValues(g2d);
					if (showC) {
						plotBothValues(g2d);
					}
				}
				else {
					plotOtherValues(g2d);
				}
			}
			else {
				fillGraph(g2d);
				if (isMainPanel() && showC) {
					plotBothValues(g2d);
				}
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
		if (isMainPanel()) {
			currentMinX = resultObject.getMinX() * resultObject.getMFactor();
			currentMaxX = resultObject.getMaxX() * resultObject.getMFactor();
		}
		else {
			currentMinX = 0L;
			currentMaxX = memoryData.getMax();
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
		List<MemoryUsage> poolUsage = memoryData.getUsageList();
		int totalPoints = poolUsage.size();
		
		if (totalPoints < 2) {
			return;
		}
		int maxPoints = (int)(width / axisInterval + 1);
		int toIndex = (totalPoints < maxPoints) ? 0 : totalPoints - maxPoints;
		
		g.setStroke(new BasicStroke(strokeWidth));
		double xPoint = (double)width;
		for (int i = totalPoints - 1; i > toIndex; i --, xPoint -= axisInterval) {
			MemoryUsage usage = poolUsage.get(i);
			
			double fromYValue = calculateEquivalentYCoordinate(usage.getUsed());
			double toYValue = calculateEquivalentYCoordinate(poolUsage.get(i - 1).getUsed());
			
			bubblePanel.yLine[(int)xPoint].setLineValue(usage.getUsed());
			bubblePanel.yLine[(int)xPoint].setActive(true);
			
			line.setLine(xPoint, fromYValue, xPoint - axisInterval, toYValue);
			g.draw(line);
		}
		bubblePanel.yLine[(int)xPoint].setLineValue(poolUsage.get(toIndex).getUsed());
		bubblePanel.yLine[(int)xPoint].setActive(true);
	}
	
	public void plotBothValues(Graphics2D g) {
		Line2D cLine = new Line2D.Double();
		g.setColor(new Color(222, 43, 33));
		List<MemoryUsage> poolUsage = memoryData.getUsageList();
		int totalPoints = poolUsage.size();
		
		if (totalPoints < 2) {
			return;
		}
		int maxPoints = (int)(width / axisInterval + 1);
		int toIndex = (totalPoints < maxPoints) ? 0 : totalPoints - maxPoints;
		
		g.setStroke(new BasicStroke(2.0F));
		double xPoint = (double)width;
		for (int i = totalPoints - 1; i > toIndex; i --, xPoint -= axisInterval) {
			MemoryUsage usage = poolUsage.get(i);
			
			double fromYValue = calculateEquivalentYCoordinate(usage.getCommitted());
			double toYValue = calculateEquivalentYCoordinate(poolUsage.get(i - 1).getCommitted());
			
			cLine.setLine(xPoint, fromYValue, xPoint - axisInterval, toYValue);
			g.draw(cLine);
		}
	}
	
	public void plotOtherValues(Graphics2D g) {
		g.setColor(this.graphColor);
		List<MemoryUsage> poolUsage = memoryData.getUsageList();
		int totalPoints = poolUsage.size();
		
		if (totalPoints < 2) {
			return;
		}
		int maxPoints = (int)(width / axisInterval + 1);
		int toIndex = (totalPoints < maxPoints) ? 0 : totalPoints - maxPoints;
		
		g.setStroke(new BasicStroke(strokeWidth));
		double xPoint = (double)width;
		for (int i = totalPoints - 1; i > toIndex; i --, xPoint -= axisInterval) {
			double fromYValue = calculateEquivalentYCoordinate(poolUsage.get(i).getUsed());
			double toYValue = calculateEquivalentYCoordinate(poolUsage.get(i - 1).getUsed());
			line.setLine(xPoint, fromYValue, xPoint - axisInterval, toYValue);
			g.draw(line);
		}
	}
	
	public void fillGraph(Graphics2D g) {
		g.setColor(this.graphColor);
		int[] xPoints = null;
		int[] yPoints = null;
		
		List<MemoryUsage> poolUsage = memoryData.getUsageList();
		int totalPoints = poolUsage.size();
		
		if (totalPoints < 2) {
			return;
		}
		
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
			int fromYValue = (int)Math.round(calculateEquivalentYCoordinate(poolUsage.get(i).getUsed()));
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
	
	public double calculateEquivalentYValue(long yCoordinate) {
		double currentValue = (double)currentMinX + ((double)height - yCoordinate) * (currentMaxX - currentMinX) / (double)height;		
		return currentValue;
	}
	
	private class MouseListenerHandler extends MouseAdapter {
		private AbstractSynchGraphPlotterPanel graphPanel = null;
		
		public MouseListenerHandler(AbstractSynchGraphPlotterPanel panel) {
			graphPanel = panel;
		}
		public void mouseClicked(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON2) {
				Logger.log("Mouse Right Button Clicked");
			}
		}
	}
	
	private class MouseMotionListenerHandler implements MouseMotionListener {
		private AbstractSynchGraphPlotterPanel graphPanel = null;
		
		public MouseMotionListenerHandler(AbstractSynchGraphPlotterPanel panel) {
			graphPanel = panel;
		}

		public void mouseDragged(MouseEvent me) {
		}

		public void mouseMoved(MouseEvent me) {			
		}
	}
}
