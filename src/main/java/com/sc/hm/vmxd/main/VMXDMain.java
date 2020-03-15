package com.sc.hm.vmxd.main;

import java.awt.Font;
import java.io.File;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.main.JVMMonitorMain;
import com.sc.hm.monitor.main.frame.MonitorFrame;
import com.sc.hm.monitor.main.frame.ProgressFrame;
import com.sc.hm.monitor.net.data.NotificationObject;
import com.sc.hm.monitor.net.socket.SocketConfiguration;
import com.sc.hm.monitor.net.transport.TransportHandler;
import com.sc.hm.monitor.persistence.PersistentManager;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.jmx.MXBeanServerFactory;
import com.sc.hm.vmxd.jmx.config.JMXConfigurationSingleton;
import com.sc.hm.vmxd.jmx.connector.ServerConnectorConfig;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;
import com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor;
import com.sc.hm.vmxd.process.executor.MBeanProcessExecutorFactory;
import com.sc.hm.vmxd.process.observer.SynchProcessObserver;
import com.sc.hm.vmxd.service.ExecutorServiceHelper;
import com.sc.hm.vmxd.synchui.manager.SynchVMTabManager;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class VMXDMain extends JVMMonitorMain {
	
	private static SynchApplicationConfiguration synch_appConfig = null;
	
	private ExecutorServiceHelper exeServiceHelper = null;
	
	private String[] params = null;
	
	private SynchVMTabManager tabManager = null;
	
	private MonitorFrame monitorFrame = null;
	
	private ProgressFrame progressFrame = null;
	
	private JTabbedPane pane = null;
	
	public VMXDMain(String[] params) {
		super();
		this.params = params;
		loadProgressBar();
		loadTabManager();
		initOther();
	}
	
	private void loadProgressBar() {
		progressFrame = new ProgressFrame();
		JOptionPane.setRootFrame(progressFrame);
		progressFrame.setVisible(true);
	}
	
	private void initOther() {
		synch_appConfig = SynchApplicationConfiguration.getSynchInstance();
		exeServiceHelper = ExecutorServiceHelper.getExecutorServiceHelper();
		
		monitorFrame = new MonitorFrame();
		monitorFrame.addWindowListener(new SynchMonitorWindowEventListener(monitorFrame));
		pane = new JTabbedPane();
		pane.setFont(new Font("Arial", Font.BOLD, 11));
		
		progressFrame.setProgressValue(1);
		progressFrame.setProgressText("Initialized Executor Service....");
	}
	
	private void loadTabManager() {
		tabManager = SynchVMTabManager.newTabManager();
		progressFrame.setProgressValue(2);
		progressFrame.setProgressText("Loaded Tab Manager....");
	}
	
    @Override
	public void initializeTransport() {
	}

    @Override
	public void startChildProcess() throws Exception {
		CountDownLatch initSocketConfigLatch = new CountDownLatch(1);
		TransportHandler.createNotifierSocket(initSocketConfigLatch
		        , new SocketConfiguration(params[1], Integer.parseInt(params[2]))
		        , params[0]);
		
		try {
			initSocketConfigLatch.await();
		}
		catch (InterruptedException inte) {}
		
		progressFrame.setProgressValue(5);
		progressFrame.setProgressText("Established Remote Socket Connection....");
		
		EnvironmentConfigObject envConfigObject = synch_appConfig.getEnvironmentConfig();
		if (envConfigObject == null) {
			throw new Exception("Environment Configuration Object for Application " + params[0]
			        + " Not Transferred.\nCan Not Launch Monitoring....");
		}
		
		try {
			monitorFrame.setTitle("VM Monitor [Application: " + envConfigObject.getApplicationName() + "]");
			startMXBeanProcess(envConfigObject);
			startMonitoring(envConfigObject.getApplicationId());
			
			if (Boolean.valueOf(envConfigObject.getEnablePersistence())) {
				File file = new File(envConfigObject.getPersistFilePath());
				if (file.exists()) {
					PersistentManager.startDataPersistenceProcess(
					        "com.sc.hm.monitor.persistence.task.SynchPersistentTask"
					        , file
					        , envConfigObject.getPersistInterval()
					        , envConfigObject.getIntervalType());
				}
			}
			synch_appConfig.waitUntilExceptionOrEnd();
			exeServiceHelper.shutdownAllExecutorPools();
			
			if (ProcessInfo.getProcessNotification(envConfigObject.getApplicationId())
			        .getStatus().equals(ProcessInfo.PROCESS_STATUS_ERROR)) {
			    
				int response = JOptionPane.showConfirmDialog(monitorFrame
				        , "Remote Connection Broken.\nDo You Want to take a Backup of your Data?"
				        , "Error"
				        , JOptionPane.YES_NO_OPTION);
				
				if (response == JOptionPane.YES_OPTION) {
					File file = new File(".");
					PersistentManager.persistDataImmediate(null, file, "com.sc.hm.monitor.persistence.task.SynchPersistentTask");
					JOptionPane.showMessageDialog(null, "Data backed-up at Location " + file.getAbsolutePath());
				}
				System.err.println("Exited Main program....");
			}
		}
		catch (Exception e) {
			System.err.println("Internal Error!!! " + e.getMessage());
			performShutdown(e);
		}
		//System.exit(0);
	}

	/**
	 * Start all the background process, that will periodically connect
	 * to the remote mbean server to fetch required metrics.
	 * 
	 * @param envConfigObject
	 * @throws Exception
	 */
	private void startMXBeanProcess(EnvironmentConfigObject envConfigObject) throws Exception {
		progressFrame.setProgressText("Starting Monitoring Process....");
		String application = envConfigObject.getApplicationId();
		
		String serverType = envConfigObject.getPort().equals("0")
		        ? MXBeanServerFactory.MX_BEAN_SERVER_FACTORY_LOCAL
		                : MXBeanServerFactory.MX_BEAN_SERVER_FACTORY_REMOTE;
		
		SynchProcessObserver observer = new SynchProcessObserver(application);
		synch_appConfig.setCurrentObserver(observer);
		
		JMXConfigurationSingleton.getInstance().setJMXConfiguration(application
		        , new ServerConnectorConfig(application
		                , envConfigObject.getServerId()
		                , envConfigObject.getPort()
		                , envConfigObject.getMserver()
		                , envConfigObject.getUserName()
		                , envConfigObject.getPassword()
		                , envConfigObject.getRole()
		                , envConfigObject.getRolePassword()));
		
		MXBeanServerManager.initializeMXBeanServer(application, serverType);
		
		AbstractMBeanProcessExecutor mbeanProcessExecutor = MBeanProcessExecutorFactory
		        .getExecutorFactory().getMBeanExecutor("MBEAN_EXEC");
		
		CountDownLatch latch = new CountDownLatch(mbeanProcessExecutor.getExecutorProcessCount());
		mbeanProcessExecutor.startExecuting(MXBeanServerManager.getMXBeanServer(application)
		        , application
		        , latch
		        , observer);
		
		try {
			Logger.log("Waiting For All Processes to up");
			latch.await();
			Logger.log("All Processes up....");
			progressFrame.setProgressValue(12);			
		}
		catch (InterruptedException inte) {}
	}

	public void startMonitoring(String applicationId) throws Exception {
		createAndShowGUI(applicationId);
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException inte) {}
        
        _startMonitoringInternal();
	}

	private void createAndShowGUI(String applicationId) throws Exception {
		progressFrame.setProgressText("Loading GUI....");
		initTabbedComponent(applicationId);
		progressFrame.setProgressValue(15);
		clearFrame(progressFrame);
		
		monitorFrame.getContentPane().add(pane);
		monitorFrame.setVisible(true);
	}

	private void initTabbedComponent(String applicationId) throws Exception {
		tabManager.configureAllTab(applicationId);
		tabManager.addAllTab(pane);
	}
	
	private void _startMonitoringInternal() throws Exception {
        Logger.log("Started Monitoring....");
		tabManager.startVMMonitoringProcess();
	}
	
	private void performShutdown(Exception e) {
		try {
			NotificationObject notifObject = ProcessInfo.getProcessNotification(synch_appConfig.getEnvironmentConfig().getApplicationId());
			notifObject.setException(e);
			notifObject.setNotification(ProcessInfo.PROCESS_STATUS_ERROR);
			exeServiceHelper.shutdownAllExecutorPools();
			
			System.err.println("Exited Main program....");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void clearFrame(JFrame f) {
		f.getContentPane().removeAll();
		f.setVisible(false);
		f.dispose();
		f = null;
	}
}
