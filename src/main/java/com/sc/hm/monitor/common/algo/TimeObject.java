package com.sc.hm.monitor.common.algo;

public class TimeObject {

	private long startTime = 0L;
	private int firstXLabelIndex = 0;
	private long firstTimeToPlot = 0L;
	private long timeInterval = 0L;
	
	public TimeObject() {}
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getFirstXLabelIndex() {
		return firstXLabelIndex;
	}
	public void setFirstXLabelIndex(int firstXLabelIndex) {
		this.firstXLabelIndex = firstXLabelIndex;
	}
	public long getFirstTimeToPlot() {
		return firstTimeToPlot;
	}
	public void setFirstTimeToPlot(long firstTimeToPlot) {
		this.firstTimeToPlot = firstTimeToPlot;
	}
	public long getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}	
}
