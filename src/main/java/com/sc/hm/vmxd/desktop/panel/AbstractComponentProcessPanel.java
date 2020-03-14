package com.sc.hm.vmxd.desktop.panel;

import java.awt.LayoutManager;
import java.util.concurrent.CountDownLatch;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.vmxd.synchui.layout.panel.SynchBasicGraphPanel;

public abstract class AbstractComponentProcessPanel extends SynchBasicGraphPanel implements MonitoringPanelProps {

	private static final long serialVersionUID = 1L;

	protected MonitorViewProcess mViewProcess = null;

	public AbstractComponentProcessPanel(int panel_width, int panel_height) {
		this(panel_width, panel_height, new TitledBorder(""), null);
	}

	public AbstractComponentProcessPanel(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
	}
	
	protected void initMonitoringProcess(CountDownLatch initLatch) {
		mViewProcess = new MonitorViewProcess(this, initLatch);
	}
	
	public MonitoringPanelProps.MonitoringProcess getMonitoringProcess() throws Exception {
		return mViewProcess;
	}
	
	protected abstract void startComponentMonitoring() throws Exception;
	
	private class MonitorViewProcess implements MonitoringPanelProps.MonitoringProcess {
		private AbstractComponentProcessPanel mProcessPanel = null;
		private CountDownLatch initLatch = null;
		private CountDownLatch doneStartLatch = null;
		
		public MonitorViewProcess(AbstractComponentProcessPanel mProcessPanel, CountDownLatch initLatch) {
			this.mProcessPanel = mProcessPanel;
			this.initLatch = initLatch;
		}
		
		public void setInitParam(Object obj) {
			doneStartLatch = (CountDownLatch)obj;
		}
		
		public void start() throws Exception {
			mProcessPanel.startComponentMonitoring();
		}

		public void run() {
			try {
				doneStartLatch.countDown();
				initLatch.await();
				for (; ;) {
					start();
					Thread.sleep(1000);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
}
