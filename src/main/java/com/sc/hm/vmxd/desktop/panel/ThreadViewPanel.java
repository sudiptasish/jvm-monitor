package com.sc.hm.vmxd.desktop.panel;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.LayoutManager;
import java.text.NumberFormat;
import java.util.concurrent.CountDownLatch;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.ThreadDataRepository;
import com.sc.hm.vmxd.data.thread.ThreadData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;

public class ThreadViewPanel extends MonitoredComponentViewPanel {
	
	private static final long serialVersionUID = 1L;

	private String applicationId = "";
	
	private ThreadDataRepository threadRepository = null;
	
	private CountDownLatch initLatch = new CountDownLatch(1);
	
	private FontMetrics fontMetrics = null;
	
	private JLabel tThreadLabel = new JLabel("Total Thread Count:        ");
	private JLabel cThreadLabel = new JLabel("Current Thread Count:      ");
	private JLabel pThreadLabel = new JLabel("Peak Thread Count:         ");
	private JLabel dThreadLabel = new JLabel("Daemon Thread Count:       ");
	private JLabel dlThreadLabel = new JLabel("Deadlocked Thread Count:   ");
	
	private JLabel tThreadLabel_R = new JLabel("");
	private JLabel cThreadLabel_R = new JLabel("");
	private JLabel pThreadLabel_R = new JLabel("");
	private JLabel dThreadLabel_R = new JLabel("");
	private JLabel dlThreadLabel_R = new JLabel("");
	
	private NumberFormat n_format = NumberFormat.getInstance();

	public ThreadViewPanel(String applicationId, int panel_width, int panel_height) {
		this(applicationId, panel_width, panel_height, new TitledBorder(""), null);
	}

	public ThreadViewPanel(String applicationId, int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
		this.applicationId = applicationId;
		initMonitoringProcess(initLatch);
		initializeListener(initLatch);
		initOther();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.THREAD_LOCK);
			threadRepository = (ThreadDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_THREAD);
			threadRepository.registerListener(listener, new StringBuilder().append("ThreadViewPanel - ").append(applicationId).toString());
			
			n_format.setMaximumFractionDigits(0);
			n_format.setMinimumFractionDigits(0);
			n_format.setGroupingUsed(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initializeAndAddComponent() {
		if (threadRepository.isInitializedRepository()) {
			initializeComponent();
			addAllComponent();
		}
		repaint();
	}
	
	private void initializeComponent() {
		fontMetrics = getFontMetrics(font);
		
		tThreadLabel.setFont(font);
		cThreadLabel.setFont(font);
		pThreadLabel.setFont(font);
		dThreadLabel.setFont(font);
		dlThreadLabel.setFont(font);
		
		tThreadLabel_R.setFont(font);
		cThreadLabel_R.setFont(font);
		pThreadLabel_R.setFont(font);
		dThreadLabel_R.setFont(font);
		dlThreadLabel_R.setFont(font);
		
		tThreadLabel.setBounds(10, 30, 140, 15);
		tThreadLabel_R.setBounds(10 + 140, 30, 30, 15);
		
		cThreadLabel.setBounds(10, 30 + 1 * (15 + 1), 140, 15);
		cThreadLabel_R.setBounds(10 + 140, 30 + 1 * (15 + 1), 30, 15);
		
		pThreadLabel.setBounds(10, 30 + 2 * (15 + 1), 140, 15);
		pThreadLabel_R.setBounds(10 + 140, 30 + 2 * (15 + 1), 30, 15);
		
		dThreadLabel.setBounds(10, 30 + 3 * (15 + 1), 140, 15);
		dThreadLabel_R.setBounds(10 + 140, 30 + 3 * (15 + 1), 30, 15);
		
		dlThreadLabel.setBounds(10, 30 + 4 * (15 + 1), 140, 15);
		dlThreadLabel_R.setBounds(10 + 140, 30 + 4 * (15 + 1), 30, 15);
	}
	
	private void addAllComponent() {
		add(tThreadLabel);
		add(cThreadLabel);
		add(pThreadLabel);
		add(dThreadLabel);
		add(dlThreadLabel);
		
		add(tThreadLabel_R);
		add(cThreadLabel_R);
		add(pThreadLabel_R);
		add(dThreadLabel_R);
		add(dlThreadLabel_R);
	}
	
	public void startComponentMonitoring() throws Exception {
		try {
			thirdPartyLock.startGetItem();
			ThreadData threadData = threadRepository.getThreadData();
			tThreadLabel_R.setText(n_format.format(threadData.getTotalStartedThreadCount()));
			cThreadLabel_R.setText(n_format.format(threadData.getThreadCount()));
			pThreadLabel_R.setText(n_format.format(threadData.getPeakThreadCount()));
			dThreadLabel_R.setText(n_format.format(threadData.getDaemonThreadCount()));
			dlThreadLabel_R.setText(n_format.format(threadData.getDeadlockThreadCount()));
		}
		finally {
			thirdPartyLock.endGetItem();
		}
	}
}
