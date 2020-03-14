package com.sc.hm.vmxd.synchui.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;

import com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor;
import com.sc.hm.vmxd.process.executor.MBeanProcessExecutorFactory;
import com.sc.hm.vmxd.synchui.layout.panel.SynchVMMonitorTab;
import com.sc.hm.vmxd.synchui.layout.panel.SynchVMStaticTab;

public class SynchVMTabManager {
	
	private final String[] STATIC_TAB_NAME = {"MBeans"};
	private final String[] MONITORING_TAB_NAME = {"Summary", "Memory", "Memory Pool", "Garbage Collector", "Threads", "Top Thread", "Classes"};
	
	private final String[] STATIC_TAB_CLASS = {"com.sc.hm.vmxd.synchui.layout.mbeans.SynchMBeansTab"};
	private final String[] MONITORING_TAB_CLASS = {"com.sc.hm.vmxd.synchui.layout.summary.SynchSummaryTab"
													, "com.sc.hm.vmxd.synchui.layout.memory.SynchMemoryTab"
													, "com.sc.hm.vmxd.synchui.layout.mpool.SynchMemoryPoolTab"
													, "com.sc.hm.vmxd.synchui.layout.gbcollector.SynchGBCollectorTab"
													, "com.sc.hm.vmxd.synchui.layout.thread.SynchThreadTab"
													, "com.sc.hm.vmxd.synchui.layout.thread.SynchTopThreadTab"
													, "com.sc.hm.vmxd.synchui.layout.classes.SynchClassesTab"};
		
	private SynchVMStaticTab[] staticTab = null;
	private SynchVMMonitorTab[] monitorTab = null;
	
	private AbstractMBeanProcessExecutor mbeanProcessExecutor = MBeanProcessExecutorFactory.getExecutorFactory().getMBeanExecutor("MGRAPH_EXEC", MONITORING_TAB_CLASS.length);
	
	private transient JProgressBar progressBar = null;
	private transient JLabel progressLabel = null;

	public SynchVMTabManager() {
		staticTab = new SynchVMStaticTab[STATIC_TAB_NAME.length];
		monitorTab = new SynchVMMonitorTab[MONITORING_TAB_NAME.length];		
	}
	
	public static SynchVMTabManager newTabManager() {
		return new SynchVMTabManager();
	}
	
	public void setProgressLevelParameter(JProgressBar bar, JLabel label) {
		progressBar = bar;
		progressLabel = label;
	}
	
	public void configureAllTab(String applicationId) throws Exception {
		configureVMStaticTab(applicationId);
		configureVMMonitoringTab(applicationId);
		//Titu-progressBar = null;
		//Titu-progressLabel = null;
	}
	
	public void configureVMStaticTab(String applicationId) throws Exception {
		for (byte i = 0; i < staticTab.length; i ++) {
			//Titu-progressBar.setValue(progressBar.getValue() + 1);
			//Titu-progressLabel.setText("Loading " + STATIC_TAB_CLASS[i] + "....");
			staticTab[i] = (SynchVMStaticTab)_configureTabInternal(applicationId, STATIC_TAB_CLASS[i]);
		}
	}
	
	public void configureVMMonitoringTab(String applicationId) throws Exception {
		for (byte i = 0; i < monitorTab.length; i ++) {
			//Titu-progressBar.setValue(progressBar.getValue() + 1);
			//Titu-progressLabel.setText("Loading " + MONITORING_TAB_CLASS[i] + "....");
			monitorTab[i] = (SynchVMMonitorTab)_configureTabInternal(applicationId, MONITORING_TAB_CLASS[i]);
		}
	}
	
	private Object _configureTabInternal(String applicationId, String class_name) throws Exception {
		try {
			Constructor<?> constructor = Class.forName(class_name).getDeclaredConstructor(new Class[] {String.class});
			return constructor.newInstance(new Object[] {applicationId});
		}
		catch (NoSuchMethodException nsme) {
			throw nsme;
		}
		catch (ClassNotFoundException cnfe) {
			throw cnfe;
		}
		catch (InvocationTargetException invte) {
			throw invte;
		}
		catch (InstantiationException inste) {
			throw inste;
		}
		catch (IllegalAccessException ilae) {
			throw ilae;
		}
	}
	
	public void addAllTab(JTabbedPane tabbedPane) {		
		addMonitoringTab(tabbedPane);
		addStaticTab(tabbedPane);
	}
	
	public void addStaticTab(JTabbedPane tabbedPane) {
		for (byte i = 0; i < staticTab.length; i ++) {
			tabbedPane.addTab(STATIC_TAB_NAME[i], staticTab[i]);
		}
	}
	
	public void addMonitoringTab(JTabbedPane tabbedPane) {
		for (byte i = 0; i < monitorTab.length; i ++) {
			tabbedPane.addTab(MONITORING_TAB_NAME[i], monitorTab[i]);
		}
	}
	
	public void startVMMonitoringProcess() throws Exception {
		for (byte i = 0; i < monitorTab.length; i ++) {
			if (monitorTab[i] != null) {
				monitorTab[i].startTabMonitorProcess(mbeanProcessExecutor);
			}
		}
		for (byte i = 0; i < staticTab.length; i ++) {
			if (staticTab[i] != null) {
				staticTab[i].loadStaticVMData();
			}
		}
	}
	
	public void stopVMMonitoringProcess() {
		for (byte i = 0; i < monitorTab.length; i ++) {
			monitorTab[i].stopTabMonitorProcess();
		}
	}
}
