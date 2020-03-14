package com.sc.hm.monitor.ui.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;

import com.sc.hm.monitor.ui.layout.panel.VMMonitorTab;
import com.sc.hm.monitor.ui.layout.panel.VMStaticTab;

public class VMTabManager {
	
	private final String[] STATIC_TAB_NAME = {"MBeans"};
	private final String[] MONITORING_TAB_NAME = {"Summary", "Memory", "Memory Pool", "Garbage Collector", "Threads", "Classes"};
	
	private final String[] STATIC_TAB_CLASS = {"com.sc.hm.monitor.ui.layout.mbeans.MBeansTab"};
	private final String[] MONITORING_TAB_CLASS = {"com.sc.hm.monitor.ui.layout.summary.SummaryTab"
													, "com.sc.hm.monitor.ui.layout.memory.MemoryTab"
													, "com.sc.hm.monitor.ui.layout.mpool.MemoryPoolTab"
													, "com.sc.hm.monitor.ui.layout.gbcollector.GBCollectorTab"
													, "com.sc.hm.monitor.ui.layout.thread.ThreadTab"
													, "com.sc.hm.monitor.ui.layout.classes.ClassesTab"};
	
	private VMStaticTab[] staticTab = null;
	private VMMonitorTab[] monitorTab = null;
	
	private transient JProgressBar progressBar = null;
	private transient JLabel progressLabel = null;

	public VMTabManager() {
		staticTab = new VMStaticTab[STATIC_TAB_NAME.length];
		monitorTab = new VMMonitorTab[MONITORING_TAB_NAME.length];		
	}
	
	public static VMTabManager newTabManager() {
		return new VMTabManager();
	}
	
	public void setProgressLevelParameter(JProgressBar bar, JLabel label) {
		progressBar = bar;
		progressLabel = label;
	}
	
	public void configureAllTab() throws Exception {
		configureVMStaticTab();
		configureVMMonitoringTab();
		progressBar = null;
		progressLabel = null;
	}
	
	public void configureVMStaticTab() throws Exception {
		for (byte i = 0; i < staticTab.length; i ++) {
			progressBar.setValue(progressBar.getValue() + 1);
			progressLabel.setText("Loading " + STATIC_TAB_CLASS[i] + "....");
			staticTab[i] = (VMStaticTab)_configureTabInternal(STATIC_TAB_CLASS[i]);
		}
	}
	
	public void configureVMMonitoringTab() throws Exception {
		for (byte i = 0; i < monitorTab.length; i ++) {
			progressBar.setValue(progressBar.getValue() + 1);
			progressLabel.setText("Loading " + MONITORING_TAB_CLASS[i] + "....");
			monitorTab[i] = (VMMonitorTab)_configureTabInternal(MONITORING_TAB_CLASS[i]);
		}
	}
	
	private Object _configureTabInternal(String class_name) throws Exception {
		try {
			Constructor<?> constructor = Class.forName(class_name).getDeclaredConstructor(new Class[] {});
			return constructor.newInstance(new Object[] {});
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
	
	public void startVMMonitoringProcess() {
		for (byte i = 0; i < monitorTab.length; i ++) {
			if (monitorTab[i] != null) {
				monitorTab[i].startTabMonitorProcess();
			}
		}
	}
	
	public void stopVMMonitoringProcess() {
		for (byte i = 0; i < monitorTab.length; i ++) {
			monitorTab[i].stopTabMonitorProcess();
		}
	}
}
