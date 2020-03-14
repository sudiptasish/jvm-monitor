package com.sc.hm.monitor.ui.layout.common;

import java.awt.Color;
import java.io.File;

import com.sc.hm.monitor.util.GraphImageProperties;

public class _UI_SELECTIONPANEL_GRAPHPANEL_SHARED {
	
	private Object lock = new Object();
	
	private String span = "1";
	private String selectionName = "";
	private Color axisColor = Color.GREEN;
	private Color graphColor = Color.GREEN;
	private int graphDelay = GraphImageProperties.IMAGE_DELAY;
	private float boldFactor = 1;
	private boolean dynmaicAxis = false;
	private boolean isRun = true;
	private boolean showX = true;
	private boolean showY = true;
	private boolean fillCurve = false;
	private boolean persist = false;
	private String fileLocation = "";
	private File directoryLocation = null;
	
	private boolean showCommittedUsage = false;

	public _UI_SELECTIONPANEL_GRAPHPANEL_SHARED() {
		this("1", "", Color.GREEN, Color.GREEN, 4, true);
	}

	public _UI_SELECTIONPANEL_GRAPHPANEL_SHARED(String span, String selectionName, Color axisColor, Color graphColor, int graphDelay, boolean isRun) {
		this.span = span;
		this.selectionName = selectionName;
		this.axisColor = axisColor;
		this.graphColor = graphColor;
		this.graphDelay = graphDelay;
		this.isRun = isRun;
	}

	public boolean isPersist() {
		return persist;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public Color getAxisColor() {
		return axisColor;
	}

	public void setAxisColor(Color axisColor) {
		this.axisColor = axisColor;
	}

	public Color getGraphColor() {
		return graphColor;
	}

	public void setGraphColor(Color graphColor) {
		this.graphColor = graphColor;
	}

	public int getGraphDelay() {
		return graphDelay;
	}

	public void setGraphDelay(int graphDelay) {
		this.graphDelay = graphDelay;
	}

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	public String getSelectionName() {
		return selectionName;
	}

	public void setSelectionName(String selectionName) {
		this.selectionName = selectionName;
	}

	public String getSpan() {
		return span;
	}

	public void setSpan(String span) {
		this.span = span;
	}

	public float getBoldFactor() {
		return boldFactor;
	}

	public void setBoldFactor(float boldFactor) {
		this.boldFactor = boldFactor;
	}

	public boolean isDynmaicAxis() {
		return dynmaicAxis;
	}

	public void setDynmaicAxis(boolean dynmaicAxis) {
		this.dynmaicAxis = dynmaicAxis;
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
	
	public boolean isFillCurve() {
		return fillCurve;
	}

	public void setFillCurve(boolean fillCurve) {
		this.fillCurve = fillCurve;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public File getDirectoryLocation() {
		return directoryLocation;
	}

	public void setDirectoryLocation(File directoryLocation) {
		this.directoryLocation = directoryLocation;
	}

	public boolean isShowCommittedUsage() {
		return showCommittedUsage;
	}

	public void setShowCommittedUsage(boolean showCommittedUsage) {
		this.showCommittedUsage = showCommittedUsage;
	}

	public void acquireLockAndStartWaiting() {
		synchronized (lock) {
			try {
				lock.wait();
			}
			catch (InterruptedException inte) {}
		}
	}
	
	public synchronized void releaseLockAndResumeProcess() {
		synchronized (lock) {
			lock.notify();
		}
	}

	public String toString() {
		return "[" + selectionName + " " + span + " " + axisColor + " " + graphColor + " " + graphDelay + "]";
	}
}
