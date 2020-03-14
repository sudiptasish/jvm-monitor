package com.sc.hm.vmxd.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.ui.layout.panel.BasicGraphPanel;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.DesktopApplicationConfiguration;
import com.sc.hm.vmxd.desktop.panel.AbstractComponentProcessPanel;
import com.sc.hm.vmxd.desktop.panel.CPUViewPanel;
import com.sc.hm.vmxd.desktop.panel.ClassViewPanel;
import com.sc.hm.vmxd.desktop.panel.GCViewPanel;
import com.sc.hm.vmxd.desktop.panel.MBeansViewPanel;
import com.sc.hm.vmxd.desktop.panel.MemoryPoolViewPanel;
import com.sc.hm.vmxd.desktop.panel.MemoryViewPanel;
import com.sc.hm.vmxd.desktop.panel.MonitoringPanelProps;
import com.sc.hm.vmxd.desktop.panel.ThreadViewPanel;
import com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor;
import com.sc.hm.vmxd.process.executor.MBeanProcessExecutorFactory;

public class DesktopApplicationViewer extends BasicGraphPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final DesktopApplicationConfiguration desktop_appConfig =
        DesktopApplicationConfiguration.getDesktopApplicationInstance();
	
	private String name = "";
	
	private JLabel[] areaLabelDetails = null;
	
	private final Vector<ApplicationLabel> appLabelVector = new Vector<>(5);
	private JLabel[] statusLabel = null;
	private final Map<String, List<JPanel>> monitorAreaMap = new HashMap<>();
	
	private final Map<String, String> statusMap = new HashMap<>();
	
	private final JLabel appLabel = new JLabel("Application Name", JLabel.CENTER);
	private final JLabel areaLabel = new JLabel("Application Monitoring Area", JLabel.CENTER);
	
	private final Font font = new Font("Arial", Font.PLAIN, 11);
	
	private final Font boldFont = new Font("Arial", Font.BOLD, 11);
	
	private final JPanel panel = new JPanel();
	
	private final Dimension dim = new Dimension(860, 660);

	public DesktopApplicationViewer() {
		this("Application Monitoring Grid");
	}
	
	public DesktopApplicationViewer(String title) {
		name = title;
		//initApp();
		//init();
	}
	
	public DesktopApplicationViewer(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Dimension getSize() {
		return dim;
	}
	
	public Dimension getPreferredSize() {
		return dim;
	}
	
	private void init() {
		//setSize(dim);
		//setLayout(null);
				
		areaLabelDetails = new JLabel[7];
		areaLabelDetails[0] = new JLabel("Memory Monitor", JLabel.CENTER);
		areaLabelDetails[1] = new JLabel("MemoryPool Monitor", JLabel.CENTER);
		areaLabelDetails[2] = new JLabel("Thread Monitor", JLabel.CENTER);
		areaLabelDetails[3] = new JLabel("CPU Monitor", JLabel.CENTER);
		areaLabelDetails[4] = new JLabel("GC Monitor", JLabel.CENTER);
		areaLabelDetails[5] = new JLabel("Class Monitor", JLabel.CENTER);
		areaLabelDetails[6] = new JLabel("MBeans Monitor", JLabel.CENTER);
		
		appLabel.setBounds(3, 10, 110, 20);
		areaLabel.setBounds(110, 10, areaLabelDetails.length * 190, 20);
		
		appLabel.setFont(boldFont);
		areaLabel.setFont(boldFont);
		
		appLabel.setBorder(new EtchedBorder());
		areaLabel.setBorder(new EtchedBorder());
		
		for (int i = 0; i < areaLabelDetails.length; i ++) {
			areaLabelDetails[i].setBounds(110 + (i * 190), 35, 190, 20);
			areaLabelDetails[i].setFont(boldFont);
			areaLabelDetails[i].setBorder(new EtchedBorder());
		}
		
		panel.setPreferredSize(new Dimension(110 + areaLabelDetails.length * 190, 500));
		panel.setLayout(null);
		//panel.setBounds(63, 30, 110 + areaLabelDetails.length * 190, 500);
		panel.setBorder(new BevelBorder(BevelBorder.RAISED));
	}
	
	private void initApp() {
		String[] appIds = desktop_appConfig.getAllApplicationIds();
		statusLabel = new JLabel[appIds.length];
		
		for (int i = 0; i < appIds.length; i ++) {
			EnvironmentConfigObject envConfig = desktop_appConfig.getEnvironmentConfig(appIds[i]);
			ApplicationLabel appLabel = new ApplicationLabel(appIds[i], envConfig.getApplicationName());
			appLabel.setBounds(3, 60 + (i * (120 + 5)), 110, 120);
			appLabel.setFont(font);
			appLabel.setBorder(new EtchedBorder());
			appLabelVector.addElement(appLabel);
			
			String status = getStatus(appIds[i]);
			statusLabel[i] = new JLabel(status, JLabel.CENTER);
			//statusLabel[i].setBounds(3, 60 + (i * (120 + 5)), 110, 120);
			statusLabel[i].setFont(boldFont);
			if (status.equals(ProcessInfo.PROCESS_STATUS_RUNNING)) {
				statusLabel[i].setForeground(Color.GREEN);
			}
			else {
				statusLabel[i].setForeground(Color.RED);
			}
			
			List<JPanel> monitoredPanelList = new ArrayList<JPanel>();
			int j = 0;
			if (Boolean.valueOf(envConfig.getMonitorMemory())) {
				JPanel panel = new MemoryViewPanel(appIds[i], 190, 120, new EtchedBorder(), null);
				panel.setBounds(110 + (j * 190), 60 + (i * (120 + 5)), 190, 120);
				monitoredPanelList.add(panel);
			}
			else {
				addBlankPanel(monitoredPanelList, i, j);
			}
			j ++;
			
			if (Boolean.valueOf(envConfig.getMonitorMemorypool())) {
				JPanel panel = new MemoryPoolViewPanel(appIds[i], 190, 120, new EtchedBorder(), null);
				panel.setBounds(110 + (j * 190), 60 + (i * (120 + 5)), 190, 120);
				monitoredPanelList.add(panel);
			}
			else {
				addBlankPanel(monitoredPanelList, i, j);
			}
			j ++;
			
			if (Boolean.valueOf(envConfig.getMonitorThread())) {
				JPanel panel = new ThreadViewPanel(appIds[i], 190, 120, new EtchedBorder(), null);
				panel.setBounds(110 + (j * 190), 60 + (i * (120 + 5)), 190, 120);
				monitoredPanelList.add(panel);
			}
			else {
				addBlankPanel(monitoredPanelList, i, j);
			}
			j ++;
			
			if (Boolean.valueOf(envConfig.getMonitorCpu())) {
				JPanel panel = new CPUViewPanel(appIds[i], 190, 120, new EtchedBorder(), null);
				panel.setBounds(110 + (j * 190), 60 + (i * (120 + 5)), 190, 120);
				monitoredPanelList.add(panel);
			}
			else {
				addBlankPanel(monitoredPanelList, i, j);
			}
			j ++;
			
			if (Boolean.valueOf(envConfig.getMonitorGc())) {
				JPanel panel = new GCViewPanel(appIds[i], 190, 120, new EtchedBorder(), null);
				panel.setBounds(110 + (j * 190), 60 + (i * (120 + 5)), 190, 120);
				monitoredPanelList.add(panel);
			}
			else {
				addBlankPanel(monitoredPanelList, i, j);
			}
			j ++;
			
			if (Boolean.valueOf(envConfig.getMonitorClass())) {
				JPanel panel = new ClassViewPanel(appIds[i], 190, 120, new EtchedBorder(), null);
				panel.setBounds(110 + (j * 190), 60 + (i * (120 + 5)), 190, 120);
				monitoredPanelList.add(panel);
			}
			else {
				addBlankPanel(monitoredPanelList, i, j);
			}
			j ++;
			
			if (Boolean.valueOf(envConfig.getMonitorMBean())) {
				JPanel panel = new MBeansViewPanel(appIds[i], 190, 120, new EtchedBorder(), null);
				panel.setBounds(110 + (j * 190), 60 + (i * (120 + 5)), 190, 120);
				monitoredPanelList.add(panel);
			}
			else {
				addBlankPanel(monitoredPanelList, i, j);
			}
			monitorAreaMap.put(appIds[i], monitoredPanelList);
		}
	}
	
	private void addBlankPanel(List<JPanel> monitoredPanelList, int i, int j) {
		JPanel panel = new JPanel();
		panel.setBounds(110 + (j * 190), 60 + (i * (120 + 5)), 190, 120);
		panel.setBorder(new EtchedBorder());
		monitoredPanelList.add(panel);
	}
	
	public void configureDesktop() {
		initApp();
		init();
		
		for (Map.Entry<String, List<JPanel>> me : monitorAreaMap.entrySet()) {
			List<JPanel> list = me.getValue();
			for (JPanel monitoredPanel : list) {
				panel.add(monitoredPanel);
			}
		}		
		addComponent();
	}
	
	public void startComponentMonitoringProcess(String applicationId) {
		List<JPanel> list = monitorAreaMap.get(applicationId);
		CountDownLatch doneStartLatch = new CountDownLatch(list.size());
		
		AbstractMBeanProcessExecutor monitorPanelExecutor = MBeanProcessExecutorFactory.getExecutorFactory().getMBeanExecutor("PANEL_MONITOR_EXEC_" + applicationId);
		
		for (JPanel monitoredPanel : list) {
			try {
				if (monitoredPanel instanceof AbstractComponentProcessPanel) {
					MonitoringPanelProps.MonitoringProcess mProcess = ((AbstractComponentProcessPanel)monitoredPanel).getMonitoringProcess();
					mProcess.setInitParam(doneStartLatch);
					monitorPanelExecutor.startExecuting(mProcess);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			doneStartLatch.await();
			Logger.log("Started Monitoring Process for Application " + applicationId);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void startComponentMonitoringProcess() {
		for (Map.Entry<String, List<JPanel>> me : monitorAreaMap.entrySet()) {
			String applicationId = me.getKey();
			List<JPanel> list = me.getValue();
			AbstractMBeanProcessExecutor monitorPanelExecutor = MBeanProcessExecutorFactory.getExecutorFactory().getMBeanExecutor("PANEL_MONITOR_EXEC_" + applicationId);
			
			for (JPanel monitoredPanel : list) {
				try {
					if (monitoredPanel instanceof AbstractComponentProcessPanel) {
						monitorPanelExecutor.startExecuting(((MonitoringPanelProps)monitoredPanel).getMonitoringProcess());
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setStatus(String applicationId, String status) {
		statusMap.put(applicationId, status);
	}
	
	public String getStatus(String applicationId) {
		if (statusMap.containsKey(applicationId)) {
			return statusMap.get(applicationId);
		}
		return ProcessInfo.PROCESS_STATUS_STOPPED;
	}
	
	private void addComponent() {
		panel.add(appLabel);
		panel.add(areaLabel);
		
		for (JLabel label : areaLabelDetails) {
			panel.add(label);
		}
		
		for (ApplicationLabel label : appLabelVector) {
			panel.add(label);
		}
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setBounds(50, 20, 760, 570);
		
		add(scrollPane);
	}
	
	private class ApplicationLabel extends JLabel {
		private static final long serialVersionUID = 1L;
		
		private String applicationId = "";
		
		public ApplicationLabel(String id, String name) {
			super(name, JLabel.CENTER);
			applicationId = id;
		}

		public String getApplicationId() {
			return applicationId;
		}
	}
}
