package com.sc.hm.monitor.common.algo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sc.hm.monitor.util.GraphImageProperties;

public class TimeAlgorithm {
	
	private long startTime = 0L;
	private int span = 0;
	private int delay = 0;
	private int totalPoints = 0;
	
	private TimeObject timeObject = null;
	
	public TimeAlgorithm() {
		timeObject = new TimeObject();
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getSpan() {
		return span;
	}

	public void setSpan(int span) {
		this.span = span;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public TimeObject calculateEstimatedTimes() {
		double xInterval = (double)GraphImageProperties.DEFAULT_X_INTERVAL / span;		
		int maxPoints = (int)(800 / xInterval + 1);
		int lastPointToShow = (totalPoints < maxPoints) ? totalPoints : totalPoints - maxPoints + 1;
		long timeForLastPointToShow = startTime + (lastPointToShow - 1) * delay * 1000;
		
		double xPixelValueForFirstPoint = 0.0D;
		if (totalPoints < maxPoints) {
			xPixelValueForFirstPoint = 800 - (lastPointToShow - 1) * xInterval;
		}
		else {
			xPixelValueForFirstPoint = 0;
		}
		double nearestXLabelPixel = xPixelValueForFirstPoint + (760 - xPixelValueForFirstPoint) % 80;
		int pointToSkip = 0;
		if (totalPoints < maxPoints) {
			pointToSkip = (int)((nearestXLabelPixel - xPixelValueForFirstPoint) / xInterval);
		}
		else {
			pointToSkip = totalPoints - maxPoints;
		}
		long firstTimeToPlot = startTime + pointToSkip * delay * 1000;
		int firstXLabelIndex = (int)(9 - (760 - nearestXLabelPixel) / 80);
		long timeInterval = (int)((double)80 / xInterval) * delay * 1000;
		
		timeObject.setFirstXLabelIndex(firstXLabelIndex);
		timeObject.setFirstTimeToPlot(firstTimeToPlot);
		timeObject.setTimeInterval(timeInterval);
		
		return timeObject;
	}
	
	public TimeObject calculateTimes() {
		List<Long> timeList = new ArrayList<Long>();
		
		long start_time = timeList.get(0);
		long end_time = timeList.get(timeList.size() - 1);
		
		double xInterval = (double)GraphImageProperties.DEFAULT_X_INTERVAL / span;		
		int maxPoints = (int)(800 / xInterval + 1);
		int lastPointToShow = (totalPoints < maxPoints) ? totalPoints : totalPoints - maxPoints + 1;
		
		return null;
	}
}
