package com.sc.hm.vmxd.synchui.layout.common;

import java.awt.Graphics2D;

public interface SynchGraphPlotterProps {

	public void initGraphPanel(Graphics2D g);
	
	public void initInterval(int xFactor);
	
	public void drawCoordinates(Graphics2D g);
	
	public void plotValues(Graphics2D g);
}
