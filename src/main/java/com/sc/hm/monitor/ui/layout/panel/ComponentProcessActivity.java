package com.sc.hm.monitor.ui.layout.panel;

import com.sc.hm.monitor.ui.layout.panel.AbstractGraphProcessPanel;

public interface ComponentProcessActivity {
	
	public void initializeComponentProcess(AbstractGraphProcessPanel.ComponentProcess graphProcess);

	public void startProcess();

	public void stopProcess();

	public boolean isRun();
}
