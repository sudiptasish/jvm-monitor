package com.sc.hm.monitor.common.algo;

public class MaxMinResultObject extends ResultObject {

	private long minX = 0L;
	private long maxX = 0L;
	private long mFactor = 0L;
	private String factorLabel = "";
		
	public MaxMinResultObject() {}

	public MaxMinResultObject(long minX, long maxX, String factorLabel) {
		this.minX = minX;
		this.maxX = maxX;
		this.factorLabel = factorLabel;
	}

	public long getMaxX() {
		return maxX;
	}

	public void setMaxX(long maxX) {
		this.maxX = maxX;
	}

	public long getMinX() {
		return minX;
	}

	public void setMinX(long minX) {
		this.minX = minX;
	}

	public long getMFactor() {
		return mFactor;
	}

	public void setMFactor(long factor) {
		mFactor = factor;
	}

	public String getFactorLabel() {
		return factorLabel;
	}

	public void setFactorLabel(String factorLabel) {
		this.factorLabel = factorLabel;
	}
}
