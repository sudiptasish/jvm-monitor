package com.sc.hm.vmxd.desktop.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.vmxd.synchui.layout.panel.SynchBasicGraphPanel;

public class StatisticsViewPanel extends SynchBasicGraphPanel {

	private static final long serialVersionUID = 1L;
	
	private long max_value = 0L;
	private long current_value = 0L;
	
	private Color bgColor = Color.BLACK;
	private Color fillColor = Color.GREEN;
	
	private final int FACTOR = 15;

	public StatisticsViewPanel(int panel_width, int panel_height) {
		this(panel_width, panel_height, new TitledBorder(""), null);
	}
	
	public StatisticsViewPanel(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
	}
	
	public long getMax_value() {
		return max_value;
	}

	public void setMax_value(long max_value) {
		this.max_value = max_value;
	}

	public long getCurrent_value() {
		return current_value;
	}

	public void setCurrent_value(long current_value) {
		this.current_value = current_value;
	}

	public void paintComponent(Graphics g) {
		try {
			Graphics2D g2d = (Graphics2D)g;
			//int currentFillY = max_value > 0 ? Math.round(getHeight() * current_value / max_value) : 0;
			double currentFillY = max_value > 0 ? (getHeight() * current_value) / (double)max_value : 0.0;
			g2d.setBackground(bgColor);
			g2d.clearRect(0, 0, getWidth(), getHeight());
			long quotient = Math.round(currentFillY / FACTOR);
			
			if (quotient == 0) {
				g2d.setColor(fillColor);
				g2d.fillRect(0, getHeight() - (int)Math.round(currentFillY), getWidth(),  getHeight());
			}
			else {
				for (int i = 1; i <= quotient; i ++) {
					//g2d.fillRect(0, getHeight() - currentFillY, getWidth(),  getHeight());
					g2d.setColor(fillColor);
					g2d.fillRect(0, getHeight() - i * FACTOR, getWidth(), FACTOR);
					
					g2d.setColor(bgColor);
					g2d.fillRect(0, getHeight() - i * FACTOR - 1, getWidth(), 1);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
