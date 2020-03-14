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
import com.sc.hm.vmxd.data.ClassDataRepository;
import com.sc.hm.vmxd.data.classes.ClassData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;

public class ClassViewPanel extends MonitoredComponentViewPanel {
	
	private static final long serialVersionUID = 1L;

	private String applicationId = "";
	
	private ClassDataRepository classRepository = null;
	
	private CountDownLatch initLatch = new CountDownLatch(1);
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	private FontMetrics fontMetrics = null;
	
	private JLabel tClassLabel = new JLabel("Total Loaded Class:     ");
	private JLabel cClassLabel = new JLabel("Current Loaded Class:   ");
	private JLabel uClassLabel = new JLabel("Total UnLoaded Class:   ");
	
	private JLabel tClassLabel_R = new JLabel("");
	private JLabel cClassLabel_R = new JLabel("");
	private JLabel uClassLabel_R = new JLabel("");
	
	private NumberFormat n_format = NumberFormat.getInstance();

	public ClassViewPanel(String applicationId, int panel_width, int panel_height) {
		this(applicationId, panel_width, panel_height, new TitledBorder(""), null);
	}

	public ClassViewPanel(String applicationId, int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
		this.applicationId = applicationId;
		initMonitoringProcess(initLatch);
		initializeListener(initLatch);
		initOther();
	}
	
	private void initOther() {
		try {
			thirdPartyLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.CLASS_LOADING_LOCK);
			classRepository = (ClassDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_CLASS);
			classRepository.registerListener(listener, new StringBuilder().append("ClassViewPanel - ").append(applicationId).toString());
			
			n_format.setMaximumFractionDigits(0);
			n_format.setMinimumFractionDigits(0);
			n_format.setGroupingUsed(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initializeAndAddComponent() {
		if (classRepository.isInitializedRepository()) {
			initializeComponent();
			addAllComponent();
		}
		repaint();
	}
	
	private void initializeComponent() {
		fontMetrics = getFontMetrics(font);
		
		tClassLabel.setFont(font);
		cClassLabel.setFont(font);
		uClassLabel.setFont(font);
		tClassLabel_R.setFont(font);
		cClassLabel_R.setFont(font);
		uClassLabel_R.setFont(font);
		
		tClassLabel.setBounds(10, 60, 130, 15);
		tClassLabel_R.setBounds(10 + 130, 60, 40, 15);
		
		cClassLabel.setBounds(10, 60 + 1 * (15 + 1), 130, 15);
		cClassLabel_R.setBounds(10 + 130, 60 + 1 * (15 + 1), 40, 15);
		
		uClassLabel.setBounds(10, 60 + 2 * (15 + 1), 130, 15);
		uClassLabel_R.setBounds(10 + 130, 60 + 2 * (15 + 1), 40, 15);
	}
	
	private void addAllComponent() {
		add(tClassLabel);
		add(cClassLabel);
		add(uClassLabel);
		
		add(tClassLabel_R);
		add(cClassLabel_R);
		add(uClassLabel_R);
	}
	
	public void startComponentMonitoring() throws Exception {
		try {
			thirdPartyLock.startGetItem();
			ClassData classData = classRepository.getClassData();
			tClassLabel_R.setText(n_format.format(classData.totalLoadedClass()));
			cClassLabel_R.setText(n_format.format(classData.currentLoadedClass()));
			uClassLabel_R.setText(n_format.format(classData.currentUnLoadedClass()));
		}
		finally {
			thirdPartyLock.endGetItem();
		}
	}
}
