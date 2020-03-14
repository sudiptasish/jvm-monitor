package com.sc.hm.monitor.common.algo;

public class PixelAlgorithm extends PanelAlgorithm {
	
	public PixelAlgorithm() {
		super();
	}
	
	public PixelAlgorithm(GraphPanelAlgorithmProperties algoEnv) {
		super(algoEnv);
	}
	
	public void setAlgorithmProperties(long minX, long maxX, long yPixel) {
		super.setAlgorithmProperties(maxX, minX, yPixel);
	}
	
	public int convertToPixelValue(long actualValue) {
		double val = (double)algoEnv.getYPixel() - ((double)algoEnv.getYPixel() / (algoEnv.getMaxXProperty() - algoEnv.getMinXProperty()) * (actualValue - algoEnv.getMinXProperty()));
		return (int)Math.round(val);
	}
}
