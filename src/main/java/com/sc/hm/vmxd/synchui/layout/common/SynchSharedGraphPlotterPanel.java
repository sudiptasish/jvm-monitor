package com.sc.hm.vmxd.synchui.layout.common;

import java.awt.Color;
import java.awt.Graphics2D;

public class SynchSharedGraphPlotterPanel extends AbstractSynchGraphPlotterPanel {

	private static final long serialVersionUID = 1L;
	
	protected int xFactor = 1;
	protected boolean showX = true;
	protected boolean showY = true;
	protected boolean fillCurve = false;
	protected boolean dynamicAxis = false;
	protected Color axisColor = Color.GREEN;
	protected Color graphColor = Color.GREEN;	
	protected float strokeWidth = 1.0f;
	protected boolean showC = false;
	
	public SynchSharedGraphPlotterPanel(String name, int width, int height, boolean flag) {
		super(name, width, height, flag);
	}
	
	public boolean isFillCurve() {
		return fillCurve;
	}

	public void setFillCurve(boolean fillCurve) {
		this.fillCurve = fillCurve;
	}

	public Color getAxisColor() {
		return axisColor;
	}

	public void setAxisColor(Color axisColor) {
		this.axisColor = axisColor;
	}

	public boolean isDynamicAxis() {
		return dynamicAxis;
	}

	public void setDynamicAxis(boolean dynamicAxis) {
		this.dynamicAxis = dynamicAxis;
	}

	public Color getGraphColor() {
		return graphColor;
	}

	public void setGraphColor(Color graphColor) {
		this.graphColor = graphColor;
	}

	public boolean isShowX() {
		return showX;
	}

	public void setShowX(boolean showX) {
		this.showX = showX;
	}

	public boolean isShowY() {
		return showY;
	}

	public void setShowY(boolean showY) {
		this.showY = showY;
	}

	public float getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public int getXFactor() {
		return xFactor;
	}

	public void setXFactor(int factor) {
		xFactor = factor;
	}
	
	public boolean isShowC() {
		return showC;
	}

	public void setShowC(boolean showC) {
		this.showC = showC;
	}

	public void initGraphPanel(Graphics2D g) {}
	
	public void drawCoordinates(Graphics2D g) {}
	
	public void plotValues(Graphics2D g) {}

	public void setMouseMotionListener() {}
}
