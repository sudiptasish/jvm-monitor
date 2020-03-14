package com.sc.hm.vmxd.desktop.panel;

import java.awt.Font;
import java.awt.LayoutManager;
import java.util.concurrent.CountDownLatch;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.data.listener.DataInitNotificationListener;
import com.sc.hm.vmxd.data.listener.DataNotification;
import com.sc.hm.vmxd.data.listener.DataNotificationInfo;
import com.sc.hm.vmxd.process.lock.ThirdPartySynchronizedLock;

public abstract class MonitoredComponentViewPanel extends AbstractComponentProcessPanel {

	private static final long serialVersionUID = 1L;
	
	protected Font font = new Font("Arial", Font.PLAIN, 11);

	protected ThirdPartySynchronizedLock thirdPartyLock = null;
	
	protected MonitoredViewPanelNotificationListener listener = null;
	
	public MonitoredComponentViewPanel(int panel_width, int panel_height) {
		this(panel_width, panel_height, new TitledBorder(""), null);
	}

	public MonitoredComponentViewPanel(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
	}
	
	protected void initializeListener(CountDownLatch initLatch) {
		listener = new MonitoredViewPanelNotificationListener(this, initLatch);
	}
	
	protected abstract void initializeAndAddComponent();
	
	private class MonitoredViewPanelNotificationListener implements DataInitNotificationListener {
		private MonitoredComponentViewPanel mViewPanel = null;
		private CountDownLatch initLatch = null;
		
		public MonitoredViewPanelNotificationListener(MonitoredComponentViewPanel mViewPanel, CountDownLatch initLatch) {
			this.mViewPanel = mViewPanel;
			this.initLatch = initLatch;
		}

		public void dataInitialized(DataNotification notification, Object handback) {
			if (notification.getType().equals(DataNotificationInfo.DATA_INITIALIZED)) {
				mViewPanel.initializeAndAddComponent();
				Logger.log("Data Initialized for " + handback.toString());
				initLatch.countDown();
			}
		}
	}
}
