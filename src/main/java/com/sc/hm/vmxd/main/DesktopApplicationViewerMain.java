package com.sc.hm.vmxd.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.config.manager.VMConfigurationUtil;
import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.DesktopApplicationConfiguration;
import com.sc.hm.vmxd.desktop.DesktopApplicationViewer;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.MXBeanServerFactory;
import com.sc.hm.vmxd.jmx.config.JMXConfigurationSingleton;
import com.sc.hm.vmxd.jmx.connector.ServerConnectorConfig;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;
import com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor;
import com.sc.hm.vmxd.process.executor.MBeanProcessExecutorFactory;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class DesktopApplicationViewerMain {
	
	private static DesktopApplicationConfiguration desktop_appConfig = DesktopApplicationConfiguration.getDesktopApplicationInstance();
	
	private final JFrame frame = new JFrame("Grid View [Application]");
	
	private DesktopApplicationViewer desktopViewer = null;
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	public DesktopApplicationViewerMain() {
		initializeMenu();
		initializeDesktopView();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(870, 680));
		frame.getContentPane().add(desktopViewer);
		frame.setVisible(true);
		
		try {
			_startIndividualMonitoring();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initializeDesktopView() {
		desktopViewer = new DesktopApplicationViewer(860, 2800, new BevelBorder(BevelBorder.RAISED), null);
		Vector<String> applications = VMConfigurationUtil.getAllApplicationIds();
		int app_count = applications.size();
		
		final CountDownLatch latch = new CountDownLatch(app_count);
		AbstractMBeanProcessExecutor processStartupExecutor = MBeanProcessExecutorFactory.getExecutorFactory().getMBeanExecutor("PROCESS_EXEC_STARTUP", app_count);
		
		for (Enumeration<String> enm = applications.elements(); enm.hasMoreElements();) {
			final String applicationId = enm.nextElement();
			final EnvironmentConfigObject envConfigObject = VMConfigurationUtil.getEnvConfigProperty(applicationId);
			desktop_appConfig.setEnvironmentConfig(applicationId, envConfigObject);
			
			processStartupExecutor.startExecuting(new Runnable() {
				public void run() {
					try {
						configureMXBeanServer(envConfigObject);
						desktopViewer.setStatus(envConfigObject.getApplicationId(), ProcessInfo.PROCESS_STATUS_RUNNING);
					}
					catch (Exception e) {
						Logger.log("Remote Process " + applicationId + " Not Running. MBean Server Can Not be Created");
						desktopViewer.setStatus(envConfigObject.getApplicationId(), ProcessInfo.PROCESS_STATUS_STOPPED);
					}
					finally {
						latch.countDown();
					}
				}
			});			
		}
		try {
			latch.await();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}		
		desktopViewer.configureDesktop();
	}
	
	private void configureMXBeanServer(EnvironmentConfigObject envConfigObject) throws Exception {
		String application = envConfigObject.getApplicationId();
		String serverType = envConfigObject.getPort().equals("0") ? MXBeanServerFactory.MX_BEAN_SERVER_FACTORY_LOCAL : MXBeanServerFactory.MX_BEAN_SERVER_FACTORY_REMOTE;
		
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
	}
	
	private void _startIndividualMonitoring() throws Exception {
		Vector<String> applications = VMConfigurationUtil.getAllApplicationIds();
		for (String applicationId : applications) {
			if (ProcessInfo.PROCESS_STATUS_RUNNING.equals(desktopViewer.getStatus(applicationId))) {
				desktopViewer.startComponentMonitoringProcess(applicationId);
				startMXBeanProcess(desktop_appConfig.getEnvironmentConfig(applicationId));
			}
		}
	}
	
	private void startMXBeanProcess(EnvironmentConfigObject envConfigObject) throws Exception {
		String application = envConfigObject.getApplicationId();
		
		AbstractMBeanProcessExecutor mbeanProcessExecutor = MBeanProcessExecutorFactory.getExecutorFactory().getMBeanExecutor("MBEAN_EXEC_APP_" + application);
		MXBeanServer mxbeanServer = MXBeanServerManager.getMXBeanServer(application);
		
		if (Boolean.valueOf(envConfigObject.getMonitorMemory())) {
			mbeanProcessExecutor.startCustomExecuting(mxbeanServer, application, AbstractMBeanProcessExecutor.MEMORY_MBEAN_PROCESS);
		}
		if (Boolean.valueOf(envConfigObject.getMonitorMemorypool())) {
			mbeanProcessExecutor.startCustomExecuting(mxbeanServer, application, AbstractMBeanProcessExecutor.MPOOL_MBEAN_PROCESS);
		}
		if (Boolean.valueOf(envConfigObject.getMonitorThread())) {
			mbeanProcessExecutor.startCustomExecuting(mxbeanServer, application, AbstractMBeanProcessExecutor.THREAD_MBEAN_PROCESS);
		}
		if (Boolean.valueOf(envConfigObject.getMonitorCpu())) {
			mbeanProcessExecutor.startCustomExecuting(mxbeanServer, application, AbstractMBeanProcessExecutor.OS_MBEAN_PROCESS);
		}
		if (Boolean.valueOf(envConfigObject.getMonitorGc())) {
			mbeanProcessExecutor.startCustomExecuting(mxbeanServer, application, AbstractMBeanProcessExecutor.GB_MBEAN_PROCESS);
		}
		if (Boolean.valueOf(envConfigObject.getMonitorClass())) {
			mbeanProcessExecutor.startCustomExecuting(mxbeanServer, application, AbstractMBeanProcessExecutor.CLASS_MBEAN_PROCESS);
		}
	}
	
	private void initializeMenu() {
		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(font);
		fileMenu.add(new JSeparator());
				
		JMenu actionMenu = new JMenu("Action");
		actionMenu.setFont(font);
		actionMenu.add(new JSeparator());
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setFont(font);
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(font);
		
		JMenuItem newConfig = new JMenuItem("Configure New Application");
		newConfig.setFont(font);
		//newConfig.addActionListener(new MenuActionListener());
		JMenuItem editConfig = new JMenuItem("Edit Configuration");
		editConfig.setFont(font);
		actionMenu.add(newConfig);
		editConfig.add(new JSeparator());
		actionMenu.add(editConfig);
		
		menubar.add(fileMenu);
		menubar.add(actionMenu);
		menubar.add(helpMenu);
		frame.setJMenuBar(menubar);
	}

	public static void main(String[] args) throws Exception {
		Thread.sleep(5000);
		VMConfigurationUtil.loadConfigurations();
		
		new DesktopApplicationViewerMain();
	}
}
