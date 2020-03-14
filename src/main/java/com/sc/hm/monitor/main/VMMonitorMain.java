package com.sc.hm.monitor.main;

import java.awt.Font;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import com.sc.hm.monitor.common.SharedMain;
import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.config.ServerConfig;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.event.listener.MonitorWindowEventListener;
import com.sc.hm.monitor.launcher.MBeanProcessLauncher;
import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.main.frame.MonitorFrame;
import com.sc.hm.monitor.main.frame.ProgressFrame;
import com.sc.hm.monitor.main.frame.WelcomeFrame;
import com.sc.hm.monitor.net.data.NotificationObject;
import com.sc.hm.monitor.net.socket.SocketConfiguration;
import com.sc.hm.monitor.net.transport.TransportHandler;
import com.sc.hm.monitor.persistence.PersistentManager;
import com.sc.hm.monitor.ui.manager.VMTabManager;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.ManagementUtil;

public class VMMonitorMain extends JVMMonitorMain {
	
	private static ApplicationConfiguration appConfig = null;
	
	private MonitorFrame monitorFrame = null;
	private ProgressFrame progressFrame = null;
	private JTabbedPane pane = null;
	
	private VMTabManager tabManager = null;
	
	private String[] params = null;
	
	static {
		appConfig = ApplicationConfiguration.getInstance();
	}

	public VMMonitorMain(String[] params) {
		super();
		this.params = params;
		loadTabManager();
		initOther();
	}
	
	private void loadTabManager() {
		tabManager = VMTabManager.newTabManager();
	}
	
	private void initOther() {
		monitorFrame = new MonitorFrame();
		monitorFrame.addWindowListener(new MonitorWindowEventListener());
		pane = new JTabbedPane();
		pane.setFont(new Font("Arial", Font.BOLD, 11));
	}
	
	public void initializeTransport() {		
		TransportHandler.createNotifierSocket(new SocketConfiguration(params[1], Integer.parseInt(params[2])), params[0]);
	}
	
	public void startChildProcess() throws Exception {
		appConfig.acquiredEnvNotificationLock(this);
		EnvironmentConfigObject envConfigObject = appConfig.getEnvironmentConfig();
		if (envConfigObject == null) {
			throw new Exception("Environment Configuration Object for Application " + params[0] + " Not Transferred.\nCan Not Launch Monitoring....");
		}
		
		try {
			monitorFrame.setTitle("VM Monitor [Application: " + envConfigObject.getApplicationName() + "]");
			startIndependentProcess(envConfigObject);
			startMonitoring();
			if (Boolean.valueOf(envConfigObject.getEnablePersistence())) {
				File file = new File(envConfigObject.getPersistFilePath());
				if (file.exists()) {
					PersistentManager.startDataPersistenceProcess(file, envConfigObject.getPersistInterval(), envConfigObject.getIntervalType());
				}
			}
		}
		catch (Exception e) {
			System.err.println("Internal Error!!! While Starting Monitoring. \nMessage: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Exited Main program....");
			NotificationObject notifObject = ProcessInfo.getProcessNotification(appConfig.getEnvironmentConfig().getApplicationId());
			notifObject.setException(e.getMessage());
			notifObject.setNotification(ProcessInfo.PROCESS_STATUS_ERROR);
			Thread notifThread = appConfig.getThreadReference(VMConstants.NOTIFIER_THREAD_NAME.getValue() + "-" + notifObject.getJobId());
			if (notifThread != null) {
				notifThread.interrupt();
			}
			System.exit(-1);
		}
		try {
			waitUntilProgramEnd();
			_stopMonitoringInternal();
			
			performCleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		NotificationObject notifObject = ProcessInfo.getProcessNotification(appConfig.getEnvironmentConfig().getApplicationId());
		notifObject.setNotification(ProcessInfo.PROCESS_STATUS_TERMINATED);
		Thread notifThread = appConfig.getThreadReference(VMConstants.NOTIFIER_THREAD_NAME.getValue() + "-" + notifObject.getJobId());
		if (notifThread != null) {
			notifThread.interrupt();
		}
		Logger.log("Exited Main program....");
	}
	
	public void startProcess(String initialMsg) throws Exception {
		try {
			start(initialMsg);		
		}
		catch (Exception e) {
			System.err.println("Internal Error!!! Connecting JVM " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		try {
			startMonitoring();
		}
		catch (Exception e) {
			System.err.println("Internal Error!!! While Starting Monitoring. \nMessage: " + e.getMessage());
			Logger.log("Exited Main program....");
			System.exit(-1);
		}
		waitUntilProgramEnd();
		_stopMonitoringInternal();
		
		performCleanup();
		Logger.log("Exited Main program....");
	}
	
	private void startIndependentProcess(EnvironmentConfigObject envConfigObject) throws Exception {
		try {
			boolean thisVM = false;
			if (envConfigObject.getApplicationId().equals(VMConstants.DEFAULT_CONFIG.getValue())) {
				thisVM = true;
			}
			connectToJVMAndStartNecessaryProcess(thisVM, new ServerConfig(envConfigObject.getServerId(), envConfigObject.getPort(), params[params.length - 1]));
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	private void reStartProcess() throws Exception {
		loadTabManager();
		initOther();
		startProcess("Connection Destroyed");
	}
	
	private void start(String initialMsg) throws Exception {
		SharedMain mShared = new SharedMain();
		
		WelcomeFrame welcomeFrame = new WelcomeFrame(mShared, initialMsg);
		welcomeFrame.setVisible(true);
		
		mShared.acquireLockAndWaitForNotification();
		if (!mShared.isProceed()) {
			System.exit(0);
		}
		ServerConfig serverConfig = mShared.getServerConfig();
		boolean thisVM = mShared.isThisVM();
		
		clearFrame(welcomeFrame);
		
		connectToJVMAndStartNecessaryProcess(thisVM, serverConfig);
	}
	
	public void connectToJVMAndStartNecessaryProcess(boolean thisVM, ServerConfig serverConfig) throws Exception {
		progressFrame = new ProgressFrame();
		JOptionPane.setRootFrame(progressFrame);
		progressFrame.setVisible(true);
		
		MBeanProcessLauncher.setProgressLevelParameter(progressFrame.getProgressBar(), progressFrame.getProgressLabel());
        tabManager.setProgressLevelParameter(progressFrame.getProgressBar(), progressFrame.getProgressLabel());
        
        try {
	        if (thisVM) {
	        	appConfig.putProperty("JVM_TYPE", ManagementUtil.JVM_TYPE_LOCAL);
				appConfig.putProperty(ManagementUtil.HOST, "localhost");
				appConfig.putProperty(ManagementUtil.PORT, "0");
	        	initRemoteMBeanProcess(ManagementUtil.JVM_TYPE_LOCAL, serverConfig);
	        }
	        else {
	        	appConfig.putProperty("JVM_TYPE", ManagementUtil.JVM_TYPE_REMOTE);
				appConfig.putProperty(ManagementUtil.HOST, serverConfig.getServerName());
				appConfig.putProperty(ManagementUtil.PORT, serverConfig.getServerPort());
				appConfig.putProperty(ManagementUtil.URL, serverConfig.getUrl());
				initRemoteMBeanProcess(ManagementUtil.JVM_TYPE_REMOTE, serverConfig);
	        }
		}
		catch (Exception e) {
			throw e;
		}
	}
        
    public void initRemoteMBeanProcess(String vmType, ServerConfig serverConfig) throws Exception {
        MBeanProcessLauncher.launchAllProcess(vmType, serverConfig);
    }

	public void startMonitoring() throws Exception {
		createAndShowGUI();
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException inte) {}
        
        _startMonitoringInternal();
	}

	private void createAndShowGUI() throws Exception {
		initTabbedComponent();
		progressFrame.getProgressBar().setValue(progressFrame.getProgressBar().getValue() + 1);
		progressFrame.getProgressLabel().setText("Loading GUI....");
		clearFrame(progressFrame);
		
		monitorFrame.addComponent(pane);
		monitorFrame.setVisible(true);
	}

	private void initTabbedComponent() throws Exception {
		tabManager.configureAllTab();
		tabManager.addAllTab(pane);
		/*
		if (current_os.indexOf("Windows") >= 0 || current_os.indexOf("windows") >= 0) {
			UIManager.setLookAndFeel(GraphImageProperties.WINDOWS_CLASS_NAME);
			SwingUtilities.updateComponentTreeUI(pane);
		}
		*/
	}
	
	private void _startMonitoringInternal() throws Exception {
        Logger.log("Started Monitoring....");
		tabManager.startVMMonitoringProcess();
	}
	
	private void _stopMonitoringInternal() {
        Logger.log("Stopping Monitoring....");
		tabManager.stopVMMonitoringProcess();
		NotificationObject notifObject = ProcessInfo.getProcessNotification(appConfig.getEnvironmentConfig().getApplicationId());
		if (!notifObject.getStatus().equalsIgnoreCase(ProcessInfo.PROCESS_STATUS_STOPPED)) {
			notifObject.setStatus(ProcessInfo.PROCESS_STATUS_ERROR);
			Thread notifThread = appConfig.getThreadReference(VMConstants.NOTIFIER_THREAD_NAME.getValue() + "-" + notifObject.getJobId());
			if (notifThread != null) {
				notifThread.interrupt();
			}
			int response = JOptionPane.showConfirmDialog(monitorFrame, "Remote Connection Broken.\nDo You Want to take a Backup of your Data?", "Error", JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
				File file = new File(".");
				PersistentManager.persistData(file);
				JOptionPane.showMessageDialog(null, "Data backed-up at Location " + file.getAbsolutePath());
			}
		}
	}
	
	private void waitUntilProgramEnd() throws Exception {
		appConfig.acquiredMainLock(this);
	}
	
	private void performCleanup() throws Exception {
		MBeanProcessLauncher.destroyMBeanConnection();
		monitorFrame.remove(pane);
		monitorFrame.dispose();
		monitorFrame = null;
		Runtime.getRuntime().gc();
		
		reStartProcess();
	}
	
	private void clearFrame(JFrame f) {
		f.getContentPane().removeAll();
		f.setVisible(false);
		f.dispose();
		f = null;
	}
}
