package com.sc.hm.monitor.main.frame;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;

public class ConfigurationFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private Dimension screen_dimension = Toolkit.getDefaultToolkit().getScreenSize();
	
	private ConfigurationPanel configPanel = null;
	
	private DashboardPanel dPanel = null;

	public ConfigurationFrame() {
		super();
		configPanel = new ConfigurationPanel(350, 640, new BevelBorder(BevelBorder.RAISED), null);
	}
	
	public ConfigurationFrame(DashboardPanel panel) {
		super();
		dPanel = panel;
		initializeConfigurationPanel();
		initializeFrame();
	}
	
	private void initializeConfigurationPanel() {
		configPanel = new ConfigurationPanel(350, 640, new BevelBorder(BevelBorder.RAISED), null);
		configPanel.inititializePanel(dPanel);
		addComponent();
	}
	
	private void initializeFrame() {
		setTitle("Configure Application");
		setSize(new Dimension(350, 640));
		setLocation((int)(screen_dimension.getWidth() / 2 - getSize().width / 2), (int)(screen_dimension.getHeight() / 2 - getSize().getHeight() / 2));
	}
	
	public void setConfigurationDetails(EnvironmentConfigObject envConfigObject) {
		configPanel.showPanelDetails(envConfigObject);
	}
	
	public void setMode(String mode) {
		configPanel.setMode(mode);
	}
	
	private void addComponent() {
		getContentPane().add(configPanel);
	}
}
