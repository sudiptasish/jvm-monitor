package com.sc.hm.monitor.main.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.config.manager.VMConfigurationUtil;
import com.sc.hm.monitor.launcher.ProcessArgs;
import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.launcher.ProcessLauncher;
import com.sc.hm.monitor.net.data.NotificationObject;
import com.sc.hm.monitor.ui.layout.panel.BasicGraphPanel;

public class DashboardPanel extends BasicGraphPanel {

	private static final long serialVersionUID = 1L;
	
	private final Vector<ApplicationButton> appButtons = new Vector<ApplicationButton>();
	private final Vector<JLabel> appLabels = new Vector<JLabel>();
	private final Vector<JLabel> appStatusLabels = new Vector<JLabel>();
	
	private final Font font = new Font("Arial", Font.PLAIN, 11);
	
	private BufferedImage buff_image = null;
	
	//private JPopupMenu appMenu = new JPopupMenu();
	
	private ConfigurationFrame configPropertiesFrame = new ConfigurationFrame(this);
	
	public DashboardPanel() {
		super();
		setPreferredSize(new Dimension(800, 600));
		loadImage();
	}

	public DashboardPanel(int panel_width, int panel_height) {
		this(panel_width, panel_height, new TitledBorder(""), null);
	}
	
	public DashboardPanel(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
		loadImage();
	}
	
	private void loadImage() {
		try {
			buff_image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/appImage.jpg"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initializePopupMenu() {}
	
	public void setApplicationsProperties(Vector<String> v) {
		for (Enumeration<String> enm = v.elements(); enm.hasMoreElements(); ) {
			String applicationId = enm.nextElement();
			EnvironmentConfigObject envConfigObject = VMConfigurationUtil.getEnvConfigProperty(applicationId);
			String applicationName = envConfigObject.getApplicationName();
			String server = envConfigObject.getServerId();
			String port = envConfigObject.getPort();
			appButtons.addElement(new ApplicationButton(applicationId, server, port));
			
			JLabel label = new JLabel(applicationName, JLabel.CENTER);
			label.setFont(font);
			appLabels.addElement(label);
			
			JLabel sLabel = new JLabel("Status: " + ProcessInfo.PROCESS_STATUS_STOPPED, JLabel.CENTER);
			sLabel.setFont(font);
			appStatusLabels.addElement(sLabel);
			
			inititializeListeners(applicationId);
		}
		v.clear();
		v = null;
	}
	
	public void createNewApplicationConfiguration(EnvironmentConfigObject envConfigObject) {
		VMConfigurationUtil.setEnvConfigProperty(envConfigObject.getApplicationId(), envConfigObject);
		appButtons.addElement(new ApplicationButton(envConfigObject.getApplicationId(), envConfigObject.getServerId(), envConfigObject.getPort()));
		
		JLabel label = new JLabel(envConfigObject.getApplicationName(), JLabel.CENTER);
		label.setFont(font);
		appLabels.addElement(label);
		
		JLabel sLabel = new JLabel("Status: " + ProcessInfo.PROCESS_STATUS_STOPPED, JLabel.CENTER);
		sLabel.setFont(font);
		appStatusLabels.addElement(sLabel);
		
		reinitializePanel();
		repaint();
	}
	
	public void deleteApplicationConfiguration(String applicationId) {
		VMConfigurationUtil.deleteConfiguration(applicationId);
		int i = -1;
		for (i = appButtons.size() - 1; i >= 0; i --) {
			if (appButtons.elementAt(i).getApplicationId().equals(applicationId)) {
				break;
			}
		}
		if (i != -1) {
			appButtons.remove(i);
			appLabels.remove(i);
			appStatusLabels.remove(i);
			reinitializePanel();
			repaint();
		}
	}
	
	public void inititializePanel() {
		int button_gap = 40;
		int height = 40;
		int i = 0;
		for (int k = 0; k < appButtons.size(); k ++) {
			ApplicationButton appButton = appButtons.elementAt(k);
			JLabel appLabel = appLabels.elementAt(k);
			JLabel statusLabel = appStatusLabels.elementAt(k);
			
			appButton.setBorder(new BevelBorder(BevelBorder.RAISED));
			if (button_gap + i * (120 + button_gap) + 120 + 40 <= 800) {				
			}
			else {
				height += 120 + button_gap + 10;
				i = 0;
			}
			appButton.setBounds(button_gap + i * (120 + button_gap), height, 120, 120);
			appLabel.setBounds(button_gap + i * (120 + button_gap), height + 125, 120, 18);
			statusLabel.setBounds(button_gap + i * (120 + button_gap), height + 145, 120, 18);
			i ++;
			
			add(appButton);
			add(appLabel);
			add(statusLabel);
		}
	}
	
	private void reinitializePanel() {
		removeAll();
		inititializePanel();
	}
	
	private void inititializeListeners(String appId) {
		ProcessInfo.registerListener(appId, new DashboardProcessStatusListener(this));
	}
	
	private class ApplicationButton extends JButton {
		private static final long serialVersionUID = 1L;
		
		private String applicationId = null;
		
		public ApplicationButton(String applicationId, String server, String port) {
			super(new ImageIcon(buff_image));
			addMouseListener(new ButtonMouseListner(this));
			this.applicationId = applicationId;
			setToolTipText(new StringBuilder().append("[Server: ").append(server).append(". Port: ").append(port).append("]").toString());
		}
		
		public String getApplicationId() {
			return this.applicationId;
		}
	}
	
	private class ButtonMouseListner extends MouseAdapter {
		private JComponent component = null;
		
		public ButtonMouseListner(JComponent comp) {
			component = comp;
		}
		public void mouseClicked(final MouseEvent me) {
			if (me.getClickCount() == 2) {
				_launchInternal((ApplicationButton)me.getSource());				
			}
			else {
				if (me.getButton() == MouseEvent.BUTTON3) {					 
					JPopupMenu appMenu = new JPopupMenu();
					JMenuItem startMenuItem = new JMenuItem("Launch Monitoring");
					startMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							_launchInternal((ApplicationButton)me.getSource());
						}
					});
					JMenuItem propertiesMenuItem = new JMenuItem("Show Configuration");
					propertiesMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							configPropertiesFrame.setConfigurationDetails(VMConfigurationUtil.getEnvConfigProperty(((ApplicationButton)me.getSource()).getApplicationId()));
							configPropertiesFrame.setMode(ConfigurationPanel.MODE_VIEW);
							if (!configPropertiesFrame.isVisible()) {
								configPropertiesFrame.setVisible(true);
							}
						}
					});
					JMenuItem deleteMenuItem = new JMenuItem("Delete Configuration");
					deleteMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							deleteApplicationConfiguration(((ApplicationButton)me.getSource()).getApplicationId());
						}
					});
					startMenuItem.setFont(font);
					propertiesMenuItem.setFont(font);
					deleteMenuItem.setFont(font);
					
					appMenu.add(startMenuItem);
					appMenu.add(new JSeparator());
					appMenu.add(propertiesMenuItem);
					appMenu.add(new JSeparator());
					appMenu.add(deleteMenuItem);
					
					appMenu.show(component, me.getX(), me.getY());
				}
			}
		}
		
		private void _launchInternal(ApplicationButton appButton) {
			String appId = appButton.getApplicationId();			
			/*
			int i = appButtons.indexOf(appButton);
			if (i >= 0 && i < appLabels.size()) {
				JLabel label = appLabels.elementAt(i);
				label.setToolTipText(ProcessInfo.PROCESS_STATUS_RUNNING);
			}
			*/
			EnvironmentConfigObject envConfigObject = VMConfigurationUtil.getEnvConfigProperty(appId);
			String vm_min = envConfigObject.getVmMemoryMin();
			String vm_max = envConfigObject.getVmMemoryMax();
			ProcessInfo.setProcessInfo(new NotificationObject(appId, ProcessInfo.PROCESS_STATUS_NEW));
			ProcessLauncher.launchProcess(
			        new ProcessArgs(VMConstants.MONITOR_PROCESS_MAIN.toString()
			                , new String[] {vm_min, vm_max}
							, new String[] {VMConstants.MONITOR_PROCESS.toString()
							    , appId
								, VMConfigurationUtil.getPrimeProperty(VMConstants.XML_TRANSPORT_HOST.toString())
								, VMConfigurationUtil.getPrimeProperty(VMConstants.XML_TRANSPORT_PORT.toString())
							}));
		}
	}
	
	private class DashboardProcessStatusListener implements ProcessInfo.ProcessStatusListener {
		private JComponent parent = null;
		
		public DashboardProcessStatusListener(JPanel panel) {
			parent = panel;
		}

		public void onStatus(String processId, String status, String errorString) {
			for (int i = 0; i < appButtons.size(); i ++) {
				if (processId.equals(appButtons.elementAt(i).getApplicationId())) {
					JLabel sLabel = appStatusLabels.elementAt(i);
					sLabel.setText("Status: " + status);
					if (status.equals(ProcessInfo.PROCESS_STATUS_RUNNING)) {
						sLabel.setForeground(Color.GREEN);
					}
					else if (status.equals(ProcessInfo.PROCESS_STATUS_STOPPED)) {
						sLabel.setForeground(Color.ORANGE);
					}
					else if (status.equals(ProcessInfo.PROCESS_STATUS_ERROR)) {
						sLabel.setForeground(Color.RED);
						sLabel.setToolTipText(errorString);
					}
					parent.repaint();
					break;
				}
			}			
		}		
	}
}
