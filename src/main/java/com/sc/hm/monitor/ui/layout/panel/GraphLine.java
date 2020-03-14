package com.sc.hm.monitor.ui.layout.panel;

import java.awt.geom.Line2D;

public abstract class GraphLine extends Line2D.Double {
	
	private boolean active = false;
	
	public GraphLine() {
		super();
	}
	
	public GraphLine(double X1, double Y1, double X2, double Y2) {
		super(X1, Y1, X2, Y2);
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public abstract double getLineValue();
	
	public abstract void setLineValue(double lineValue);
}
