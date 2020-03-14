package com.sc.hm.vmxd.synchui.layout.panel;

public class SynchXGraphLine extends SynchGraphLine {

	private double lineValue = 0.0D;
	
	public SynchXGraphLine() {
		super();
	}
	
	public SynchXGraphLine(double X1, double Y1, double X2, double Y2) {
		super(X1, Y1, X2, Y2);
	}

	public double getLineValue() {
		return lineValue;
	}

	public void setLineValue(double lineValue) {
		this.lineValue = lineValue;
	}	
}
