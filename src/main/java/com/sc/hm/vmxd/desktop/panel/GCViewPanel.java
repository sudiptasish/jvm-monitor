package com.sc.hm.vmxd.desktop.panel;

import java.awt.LayoutManager;
import java.text.NumberFormat;
import java.util.concurrent.CountDownLatch;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.GarbageCollectorDataRepository;
import com.sc.hm.vmxd.data.gbcollector.GarbageCollectorData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;

public class GCViewPanel extends MonitoredComponentViewPanel {
	
	private static final long serialVersionUID = 1L;

	private String applicationId = "";
	
	private GarbageCollectorDataRepository gcRepository = null;
	
	private String[] gcNames = null;

	private CountDownLatch initLatch = new CountDownLatch(1);
	
	private JLabel[] gcName = null;
	private JLabel[] gcCollection = null;
	private JLabel[] gcTime = null;
	private JLabel[] gcCollection_R = null;
	private JLabel[] gcTime_R = null;
	
	private NumberFormat n_format = NumberFormat.getInstance();

	public GCViewPanel(String applicationId, int panel_width, int panel_height) {
		this(applicationId, panel_width, panel_height, new TitledBorder(""), null);
	}

	public GCViewPanel(String applicationId, int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
		this.applicationId = applicationId;
		initMonitoringProcess(initLatch);
		initializeListener(initLatch);
		initOther();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.GARBAGE_COLLECTOR_LOCK);
			gcRepository = (GarbageCollectorDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR);
			gcRepository.registerListener(listener, new StringBuilder().append("GCViewPanel - ").append(applicationId).toString());
			
			n_format.setMaximumFractionDigits(0);
			n_format.setMinimumFractionDigits(0);
			n_format.setGroupingUsed(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initializeAndAddComponent() {
		if (gcRepository.isInitializedRepository()) {
			initializeComponent();
			addAllComponent();
		}
		repaint();
	}
	
	private void initializeComponent() {
		gcNames = gcRepository.getGarbageCollectorNames();
		gcName = new JLabel[gcNames.length];
		gcCollection = new JLabel[gcNames.length];
		gcTime = new JLabel[gcNames.length];
		gcCollection_R = new JLabel[gcNames.length];
		gcTime_R = new JLabel[gcNames.length];
		
		for (int i = 0, j = 0; i < gcName.length; i ++) {
			gcName[i] = new JLabel(gcNames[i]);
			gcName[i].setFont(font);
			
			gcCollection[i] = new JLabel("Total Collection:");
			gcCollection[i].setFont(font);
			
			gcTime[i] = new JLabel("Collection Time:");
			gcTime[i].setFont(font);
			
			gcCollection_R[i] = new JLabel("");
			gcCollection_R[i].setFont(font);
			
			gcTime_R[i] = new JLabel("");
			gcTime_R[i].setFont(font);
			
			gcName[i].setBounds(10, 3 + i * 62, 110, 15);
			gcCollection[i].setBounds(10, 3 + i * 62 + (j + 1) * (15 + 1), 100, 15);
			gcCollection_R[i].setBounds(10 + 100, 3 + i * 62 + (j + 1) * (15 + 1), 70, 15);
			gcTime[i].setBounds(10, 3 + i * 62 + (j + 2) * (15 + 1), 100, 15);
			gcTime_R[i].setBounds(10 + 100, 3 + i * 62 + (j + 2) * (15 + 1), 70, 15);
		}
	}
	
	private void addAllComponent() {
		for (int i = 0; i < gcName.length; i ++) {
			add(gcName[i]);
			add(gcCollection[i]);
			add(gcTime[i]);
			add(gcCollection_R[i]);
			add(gcTime_R[i]);
		}
	}
	
	public void startComponentMonitoring() throws Exception {
		try {
			thirdPartyLock.startGetItem();
			for (int i = 0; i < gcNames.length; i ++) {
				GarbageCollectorData gcData = gcRepository.getGarbageCollectorData(gcNames[i]);
				gcCollection_R[i].setText(n_format.format(gcData.getTotalCollectionCount()));
				gcTime_R[i].setText(n_format.format(gcData.getTotalCollectionTime()));
			}
		}
		finally {
			thirdPartyLock.endGetItem();
		}
	}
}
