package com.sc.hm.vmxd.synchui.layout.common;

import java.awt.Dimension;

import javax.swing.JPanel;

public class AbstractSynchCoordinatePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected int width = 0;
	protected int height = 0;
	protected Dimension dimension = null;
	
	protected double axisInterval = 0.0;

	public AbstractSynchCoordinatePanel(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.dimension = new Dimension(width, height);
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
	
	public double getAxisInterval() {
		return axisInterval;
	}

	public void setAxisInterval(double axisInterval) {
		this.axisInterval = axisInterval;
	}
}
