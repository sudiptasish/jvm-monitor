package com.sc.hm.monitor.ui.layout.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.NumberFormat;

public class BubblePanel extends BasicGraphPanel {

	private static final long serialVersionUID = 1L;
	
	public XGraphLine[] xLine = null;
	public YGraphLine[] yLine = null;
	
	public long currentMinX = 0L;
	public long currentMaxX = 0L;
	
	private NumberFormat n_format = NumberFormat.getInstance();
	
	private final int bubbleWidth = 90;
	private final int bubbleHeight = 18;
	
	private int xPoint = 0;
	private int yPoint = 0;
	
	private boolean showBubble = false;
	
	public BubblePanel(int panel_width, int panel_height) {
		super(panel_width, panel_height);
		n_format.setMaximumFractionDigits(0);
		n_format.setMinimumFractionDigits(0);
		n_format.setGroupingUsed(true);
	}

	public void setMouseMotionListener() {
		addMouseMotionListener(new MouseMotionListenerHandler(this));
		addMouseListener(new MouseListenerHandler(this));
	}
	
	public void paintComponent(Graphics g) {
		try {
			double lineVal = yLine[xPoint].getLineValue();
			if (!showBubble || lineVal == 0 || !yLine[xPoint].isActive()) {
				return;
			}
			g.setColor(new Color(0, 125, 0));
			int[] xPoints = {xPoint - bubbleWidth - 10, xPoint - 10, xPoint - 10, xPoint - 10 - 10, xPoint, xPoint - 10 - 30, xPoint - bubbleWidth - 10};
			int[] yPoints = {yPoint - bubbleHeight - 10, yPoint - bubbleHeight - 10, yPoint - 10, yPoint - 10, yPoint, yPoint - 10, yPoint - 10};
			
			/*
			g.fillRoundRect(xPoint - bubbleWidth - 10, yPoint - bubbleHeight - 10, bubbleWidth, bubbleHeight, 5, 5);
			g.drawLine(xPoint - 10 - 30, yPoint - 10, xPoint, yPoint);
			g.drawLine(xPoint - 10 - 10, yPoint - 10, xPoint, yPoint);
			g.fillPolygon(new int[] {xPoint - 10 - 30, xPoint - 10 - 10, xPoint}, new int[] {yPoint - 10, yPoint - 10, yPoint}, 3);
			*/
			
			Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
			g.fillPolygon(polygon);
			
			g.setColor(Color.WHITE);
			g.drawString(n_format.format(lineVal), xPoint - bubbleWidth - 10 + 5, yPoint - bubbleHeight - 10 + bubbleHeight - 4);
			
			//Logger.log(xPoint + " : " + yLine[xPoint]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			g.dispose();
		}
	}
	
	private class MouseListenerHandler extends MouseAdapter {
		private BasicGraphPanel graphPanel = null;
		
		public MouseListenerHandler(BasicGraphPanel panel) {
			graphPanel = panel;
		}
		
		public void mouseEntered(MouseEvent me) {
			showBubble = true;
			graphPanel.repaint();
		}
		public void mouseExited(MouseEvent me) {
			showBubble = false;
			graphPanel.repaint();
		}
	}
	
	private class MouseMotionListenerHandler implements MouseMotionListener {
		private BasicGraphPanel graphPanel = null;
		
		public MouseMotionListenerHandler(BasicGraphPanel panel) {
			graphPanel = panel;
		}

		public void mouseDragged(MouseEvent me) {
		}

		public void mouseMoved(MouseEvent me) {
			xPoint = me.getX();
			if (yLine[xPoint].isActive()) {
				yPoint = (int)calculateEquivalentYCoordinate((long)yLine[xPoint].getLineValue());				
				graphPanel.repaint();
			}			
		}
		
		public double calculateEquivalentYCoordinate(long currentValue) {
			double val = (double)getHeight() - ((double)getHeight() / (currentMaxX - currentMinX) * (currentValue - currentMinX));
			return val;
		}
	}
}
