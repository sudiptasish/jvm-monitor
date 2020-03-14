package com.sc.hm.monitor.common.algo;

public class GraphPanelAlgorithm extends PanelAlgorithm {
	
	private MaxMinResultObject resultObj = null;

	public GraphPanelAlgorithm() {
		super();
		resultObj = new MaxMinResultObject();
	}
	
	public GraphPanelAlgorithm(GraphPanelAlgorithmProperties algoEnv) {
		super(algoEnv);
		resultObj = new MaxMinResultObject();
	}
	
	public void setAlgorithmProperties(long minX, long maxX, long yPixel) {
		super.setAlgorithmProperties(minX, maxX, yPixel);
	}
	
	public ResultObject calculateMaxAndMinX() {
		long currentMinX = algoEnv.getMinXProperty();
		long currentMaxX = algoEnv.getMaxXProperty();
		
		if ((currentMinX == 0 && currentMaxX / MEMORY_DIV_MB >= 8) || currentMinX /MEMORY_DIV_MB >= 1) {
			currentMaxX = (currentMaxX + (MEMORY_DIV_MB - (currentMaxX % MEMORY_DIV_MB))) / MEMORY_DIV_MB;
			currentMinX /= MEMORY_DIV_MB;
			if (currentMinX == currentMaxX) {
				currentMinX --;
				currentMaxX ++;
			}
			resultObj.setMFactor(MEMORY_DIV_MB);
			resultObj.setFactorLabel("(MB)");
		}
		else if ((currentMinX == 0 && currentMaxX / MEMORY_DIV_KB >= 8) || currentMinX /MEMORY_DIV_KB >= 1) {
			currentMaxX = (currentMaxX + (MEMORY_DIV_KB - (currentMaxX % MEMORY_DIV_KB))) / MEMORY_DIV_KB;
			currentMinX /= MEMORY_DIV_KB;
			if (currentMinX == currentMaxX) {
				currentMinX --;
				currentMaxX ++;
			}
			resultObj.setMFactor(MEMORY_DIV_KB);
			resultObj.setFactorLabel("(KB)");
		}
		else {
			if (currentMinX == currentMaxX) {
				currentMinX --;
				currentMaxX ++;
			}
			resultObj.setMFactor(1);
			resultObj.setFactorLabel("(BYTE)");
		}
		resultObj.setMinX(currentMinX);
		resultObj.setMaxX(currentMaxX);
		
		return resultObj;
	}
}
