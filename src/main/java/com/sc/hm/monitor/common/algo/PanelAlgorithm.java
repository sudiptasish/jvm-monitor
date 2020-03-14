package com.sc.hm.monitor.common.algo;

public abstract class PanelAlgorithm {
	
	protected GraphPanelAlgorithmProperties algoEnv = null;

	public static final long MEMORY_DIV_MB = 1024 * 1024;	
	public static final long MEMORY_DIV_KB = 1024;
	
	protected long mFactor = 0L;
	
	public PanelAlgorithm() {
		algoEnv = new GraphPanelAlgorithmProperties();
	}
	
	public PanelAlgorithm(GraphPanelAlgorithmProperties algoEnv) {
		this.algoEnv = algoEnv;
	}
	
	public long getMFactor() {
		return mFactor;
	}

	public void setMFactor(long factor) {
		mFactor = factor;
	}

	protected void setAlgorithmProperties(long minX, long maxX, long yPixel) {
		algoEnv.setMinXProperty(minX);
		algoEnv.setMaxXProperty(maxX);
		algoEnv.setYPixel(yPixel);
	}
}
