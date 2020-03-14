package com.sc.hm.monitor.main.frame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.config.manager.VMConfigurationUtil;
import com.sc.hm.monitor.event.ConfigUpdater;
import com.sc.hm.monitor.event.listener.DashboardWindowEventListener;

public class DashboardFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Dimension screen_dimension = Toolkit.getDefaultToolkit().getScreenSize();
	
	private DashboardPanel dPanel = null;
	private ConfigurationFrame configFrame = null;
	
	private Font font = new Font("Arial", Font.PLAIN, 12);
	
	public DashboardFrame() {
		super();
		initializeDashboardPanel();
		initializeConfigFrame();
		initializeMenu();
		initializeFrame();
	}
	
	private void initializeConfigFrame() {
		configFrame = new ConfigurationFrame(dPanel);
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
                try {
                    ConfigUpdater.updateConfig();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    System.exit(0);
                }
			}
		});
		fileMenu.add(exitItem);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(font);
		
		JMenuItem newConfig = new JMenuItem("Configure New Application");
		newConfig.setFont(font);
		newConfig.addActionListener(new MenuActionListener());
		//JMenuItem editConfig = new JMenuItem("Edit Configuration");
		//editConfig.setFont(font);
		actionMenu.add(newConfig);
		//editConfig.add(new JSeparator());
		//actionMenu.add(editConfig);
		
		menubar.add(fileMenu);
		menubar.add(actionMenu);
		menubar.add(helpMenu);
		setJMenuBar(menubar);
	}
	
	private void initializeDashboardPanel() {
		dPanel = new DashboardPanel(800, 2800, new BevelBorder(BevelBorder.RAISED), null);
		dPanel.setApplicationsProperties(VMConfigurationUtil.getAllApplicationIds());
		dPanel.inititializePanel();
		addComponent();
	}
	
	private void initializeFrame() {
		setTitle("Dashboard Application");
		addWindowListener(new DashboardWindowEventListener());
		setSize(new Dimension(800, 600));
		setLocation((int)(screen_dimension.getWidth() / 2 - getSize().width / 2), (int)(screen_dimension.getHeight() / 2 - getSize().getHeight() / 2));		
	}
	
	public void addComponent() {
		getContentPane().add(new JScrollPane(dPanel));
	}
	
	private class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			configFrame.setMode(ConfigurationPanel.MODE_NEW);
			configFrame.setVisible(true);
		}
	}
}
