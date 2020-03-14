package com.sc.hm.monitor.ui.layout.panel;

import java.awt.LayoutManager;

import javax.swing.border.Border;

import com.sc.hm.monitor.config.ApplicationConfiguration;

public abstract class AbstractGraphProcessPanel extends BasicGraphPanel implements ComponentProcessActivity {
	
	protected static ApplicationConfiguration appConfig = ApplicationConfiguration.getInstance();

	private static final long serialVersionUID = 1L;
	private boolean isRun = false;
	private boolean forceStop = false;
	protected ComponentProcess graphProcess = null;
	
	private Thread processThread = null;

	public AbstractGraphProcessPanel(int panel_width, int panel_height) {
		super(panel_width, panel_height);
	}
	
	public AbstractGraphProcessPanel(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
	}
	
	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	
	public boolean isForceStop() {
		return forceStop;
	}

	public void setForceStop(boolean forceStop) {
		this.forceStop = forceStop;
	}

	public void initializeComponentProcess(ComponentProcess graphProcess) {
		this.graphProcess = graphProcess;
	}
	
	protected void _startProcessInternal() {
		processThread = new Thread(graphProcess);
		processThread.start();
	}
	
	protected void _stopProcessInternal() {
		try {
			processThread.join();
		}
		catch (InterruptedException inte) {
			inte.printStackTrace();
		}
	}
	
	public abstract void startSynchronizedMonitoring();
	
	public abstract void startAsynchronizedMonitoring();

	public interface ComponentProcess extends Runnable {
		public void updateComponent();
		public void notifyOwner();
	}
}
