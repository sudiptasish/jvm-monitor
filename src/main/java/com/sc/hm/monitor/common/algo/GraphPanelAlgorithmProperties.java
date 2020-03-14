package com.sc.hm.monitor.common.algo;

public class GraphPanelAlgorithmProperties {

	private long maxXProperty = 0L;
	private long minXProperty = 0L;
	private long yPixel = 0L;
	
	public GraphPanelAlgorithmProperties() {}

	public GraphPanelAlgorithmProperties(long maxXProperty, long minXProperty, long yPixel) {
		this.maxXProperty = maxXProperty;
		this.minXProperty = minXProperty;
		this.yPixel = yPixel;
	}

	public final long getMaxXProperty() {
		return maxXProperty;
	}

	public void setMaxXProperty(long maxXProperty) {
		this.maxXProperty = maxXProperty;
	}

	public final long getMinXProperty() {
		return minXProperty;
	}

	public void setMinXProperty(long minXProperty) {
		this.minXProperty = minXProperty;
	}

	public final long getYPixel() {
		return yPixel;
	}

	public void setYPixel(long pixel) {
		yPixel = pixel;
	}
}
