package com.sc.hm.monitor.ui.layout.panel;

public class YGraphLine extends GraphLine {

	private double lineValue = 0.0D;
	
	public YGraphLine() {
		super();
	}
	
	public YGraphLine(double X1, double Y1, double X2, double Y2) {
		super(X1, Y1, X2, Y2);
	}

	public double getLineValue() {
		return lineValue;
	}

	public void setLineValue(double lineValue) {
		this.lineValue = lineValue;
	}
	
	public void resetLineValue() {
		this.lineValue = 0.0;
	}
	
	public String toString() {
		return new StringBuilder().append("[").append(lineValue).append(" : ").append(isActive()).append("]").toString();
	}
}
