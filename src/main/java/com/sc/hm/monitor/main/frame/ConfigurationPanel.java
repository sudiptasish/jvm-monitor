package com.sc.hm.monitor.main.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.config.manager.VMConfigurationUtil;
import com.sc.hm.monitor.ui.layout.panel.BasicGraphPanel;

public class ConfigurationPanel extends BasicGraphPanel {

	private static final long serialVersionUID = 1L;
	
	private String current_application_id = "";
	
	public static final String MODE_NEW = "MODE_NEW";
	public static final String MODE_VIEW = "MODE_VIEW";
	public static final String MODE_EDIT = "MODE_EDIT";
	private String current_mode = MODE_NEW;

	private DashboardPanel dPanel = null;
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	private JLabel displayLabel = new JLabel("Fields marked with red (*) are Mandetory");
	private JLabel appLabel = new JLabel("Application Name *");
	private JTextField appField = new JTextField();
	
	private JPanel appPanel = new JPanel();
	private JPanel crPanel = new JPanel();
	private JPanel dtPanel = new JPanel();
	
	private JLabel nameLabel = new JLabel("Server Name / IP *");
	private JTextField nameField = new JTextField();
	private JLabel portLabel = new JLabel("Server Port *");
	private JTextField portField = new JTextField();
	private JLabel userLabel = new JLabel("User Name");
	private JTextField userField = new JTextField();
	private JLabel passwordLabel = new JLabel("Password");
	private JTextField passwordField = new JTextField();

	private JLabel mserverLabel = new JLabel("MBean Server");
    private JTextField mserverField = new JTextField();
	
    private JLabel roleLabel = new JLabel("Role");
    private JTextField roleField = new JTextField();
    private JLabel rpasswordLabel = new JLabel("Role Password");
    private JTextField rpasswordField = new JTextField();

    private JCheckBox enableSSL = new JCheckBox("Enable SSL");
	private JCheckBox enableLogging = new JCheckBox("Enable Logging");
	private JCheckBox enableMailing = new JCheckBox("Enable Mailing");
	private JLabel mCauseLabel = new JLabel("Choose Action");
	private JComboBox mCauseCombo = new JComboBox();
	private JLabel mailFrom = new JLabel("Mail From");
	private JTextField mFromField = new JTextField();
	private JLabel mailTo = new JLabel("Mail To");
	private JTextField mToField = new JTextField();
	private JLabel mailCc = new JLabel("Mail Cc");
	private JTextField mCcField = new JTextField();
	private JCheckBox enableConnect = new JCheckBox("Enable Auto Connect");
	private JCheckBox enablePersistence = new JCheckBox("Enable Auto Persistence");
	private JLabel persistLabel = new JLabel("File Path");
	private JTextField persistField = new JTextField();
	private JButton selectButton = new JButton("Select");
	private JLabel intervalLabel = new JLabel("Interval");
	private JTextField intervalField = new JTextField();
	private JComboBox intervalCombo = new JComboBox();
	private JCheckBox enableStartup = new JCheckBox("Start As Independent Process");
	private JLabel minLabel = new JLabel("VM Args [-Xms]");
	private JTextField minField = new JTextField();
	private JLabel maxLabel = new JLabel("VM Args [-Xmx]");
	private JTextField maxField = new JTextField();
	
	private JFileChooser fileChooser = new JFileChooser();
	
	private JButton submitButton = new JButton("Save Configuration");
	private JButton defaultButton = new JButton("Apply Default");
	
	public ConfigurationPanel(int panel_width, int panel_height) {
		this(panel_width, panel_height, new TitledBorder(""), null);
	}
	
	public ConfigurationPanel(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
	}

	public void inititializePanel(DashboardPanel panel) {
		dPanel = panel;
		initializeComponent();
		addComponent();
	}
	
	public void showPanelDetails(EnvironmentConfigObject envConfigObject) {
		current_application_id = envConfigObject.getApplicationId();
		appField.setText(envConfigObject.getApplicationName());
		nameField.setText(envConfigObject.getServerId());
		portField.setText(envConfigObject.getPort());
		userField.setText(envConfigObject.getUserName());
		passwordField.setText(envConfigObject.getPassword());
		mserverField.setText(envConfigObject.getMserver());
		roleField.setText(envConfigObject.getRole());
		rpasswordField.setText(envConfigObject.getRolePassword());
		enableSSL.setSelected(Boolean.valueOf(envConfigObject.getEnableSSL()));
		enableLogging.setSelected(Boolean.valueOf(envConfigObject.getEnableLogging()));
		enableMailing.setSelected(Boolean.valueOf(envConfigObject.getEnableMailing()));
		if (Boolean.valueOf(envConfigObject.getEnableMailing())) {
			mCauseCombo.setSelectedItem(envConfigObject.getMailCause());
			mFromField.setText(envConfigObject.getMailFrom());
			mToField.setText(envConfigObject.getMailTo());
			mCcField.setText(envConfigObject.getMailCc());
		}
		enableConnect.setSelected(Boolean.valueOf(envConfigObject.getEnableConnect()));
		enablePersistence.setSelected(Boolean.valueOf(envConfigObject.getEnablePersistence()));
		if (Boolean.valueOf(envConfigObject.getEnablePersistence())) {
			persistField.setText(envConfigObject.getPersistFilePath());
			intervalField.setText(envConfigObject.getPersistInterval());
			intervalCombo.setSelectedItem(envConfigObject.getIntervalType());
		}
		enableStartup.setSelected(Boolean.valueOf(envConfigObject.getIndependentProcess()));
		if (Boolean.valueOf(envConfigObject.getIndependentProcess())) {
			minField.setText(envConfigObject.getVmMemoryMin());
			maxField.setText(envConfigObject.getVmMemoryMax());
		}
	}
	
	public void setMode(String mode) {
		current_mode = mode;
		submitButton.setText("Save Configuration");
		if (mode.equals(MODE_VIEW)) {
			disableAllComponent();
			submitButton.setText("Edit Configuration");
		}
	}
	
	public String getCurrentMode() {
		return current_mode;
	}
	
	private void initializeComponent() {
		displayLabel.setFont(font);
		appLabel.setFont(font);
		appField.setFont(font);
		
		nameLabel.setFont(font);
		nameField.setFont(font);
		portLabel.setFont(font);
		portField.setFont(font);
		userLabel.setFont(font);
		userField.setFont(font);
		passwordLabel.setFont(font);
		passwordField.setFont(font);
		roleLabel.setFont(font);
		roleField.setFont(font);
		rpasswordLabel.setFont(font);
		rpasswordField.setFont(font);
		mserverLabel.setFont(font);
		mserverField.setFont(font);
		
		enableSSL.setFont(font);
		enableLogging.setFont(font);
		enableMailing.setFont(font);
		enablePersistence.setFont(font);
		enableConnect.setFont(font);
		enableStartup.setFont(font);
		persistLabel.setFont(font);
		persistField.setFont(font);
		selectButton.setFont(font);
		intervalLabel.setFont(font);
		intervalField.setFont(font);
		intervalCombo.setFont(font);
		minLabel.setFont(font);
		minField.setFont(font);
		maxLabel.setFont(font);
		maxField.setFont(font);
		fileChooser.setFont(font);
		submitButton.setFont(font);
		defaultButton.setFont(font);
		mCauseLabel.setFont(font);
		mCauseCombo.setFont(font);
		mailFrom.setFont(font);
		mFromField.setFont(font);
		mailTo.setFont(font);
		mToField.setFont(font);
		mailCc.setFont(font);
		mCcField.setFont(font);
		
		mToField.setToolTipText("Multiple Mail-Ids should be seperated by , or ;");
		mCcField.setToolTipText("Multiple Mail-Ids should be seperated by , or ;");
		
		displayLabel.setBounds(10, 5, 250, 15);
		displayLabel.setForeground(Color.RED);
		
		appPanel.setLayout(null);
		appPanel.setBounds(3, 30, 330, 50);
		appPanel.setBorder(new TitledBorder(new EtchedBorder(), "Application Details", 0, 0, font, Color.BLUE));
		
		crPanel.setLayout(null);
		crPanel.setBounds(3, 90, 330, 160);
		crPanel.setBorder(new TitledBorder(new EtchedBorder(), "Server Credentials", 0, 0, font, Color.BLUE));
		
		dtPanel.setLayout(null);
		dtPanel.setBounds(3, 260, 330, 310);
		dtPanel.setBorder(new TitledBorder(new EtchedBorder(), "Server Configurations", 0, 0, font, Color.BLUE));
		
		appLabel.setBounds(10, 20, 90, 15);
		appLabel.setForeground(Color.RED);
		appField.setBounds(120, 20, 160, 18);
		appField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		nameLabel.setBounds(10, 20, 100, 15);
		nameLabel.setForeground(Color.RED);
		nameField.setBounds(120, 20, 160, 18);
		nameField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		portLabel.setBounds(10, 45, 100, 15);
		portLabel.setForeground(Color.RED);
		portField.setBounds(120, 45, 160, 18);
		portField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		mserverLabel.setBounds(10, 70, 100, 15);
		mserverField.setBounds(120, 70, 160, 18);
		mserverField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		userLabel.setBounds(10, 95, 55, 15);
		userField.setBounds(70, 95, 65, 18);
		userField.setBorder(new BevelBorder(BevelBorder.LOWERED));
        passwordLabel.setBounds(155, 95, 55, 15);
		passwordField.setBounds(215, 95, 65, 18);
		passwordField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		roleLabel.setBounds(10, 120, 55, 15);
        roleField.setBounds(70, 120, 65, 18);
        roleField.setBorder(new BevelBorder(BevelBorder.LOWERED));
        rpasswordLabel.setBounds(155, 120, 55, 15);
        rpasswordField.setBounds(215, 120, 65, 18);
        rpasswordField.setBorder(new BevelBorder(BevelBorder.LOWERED));
        
		enableSSL.setBounds(5, 20, 150, 18);
		enableLogging.setBounds(170, 20, 150, 18);
		enableMailing.setBounds(5, 45, 150, 18);
		enableConnect.setBounds(170, 45, 150, 18);
		
		mCauseLabel.setBounds(10, 75, 90, 18);
		mCauseCombo.setBounds(110, 75, 170, 18);
		mailFrom.setBounds(10, 100, 45, 18);
		mFromField.setBounds(60, 100, 220, 18);
		mFromField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		mailTo.setBounds(10, 125, 45, 18);
		mToField.setBounds(60, 125, 220, 18);
		mToField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		mailCc.setBounds(10, 150, 45, 18);
		mCcField.setBounds(60, 150, 220, 18);
		mCcField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		enablePersistence.setBounds(5, 175, 200, 18);
		persistLabel.setBounds(10, 200, 45, 18);
		persistField.setBounds(60, 200, 180, 18);
		persistField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		selectButton.setBounds(250, 200, 70, 18);
		intervalLabel.setBounds(10, 225, 45, 18);
		intervalField.setBounds(60, 225, 90, 18);
		intervalField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		intervalCombo.setBounds(160, 225, 80, 18);
		enableStartup.setBounds(5, 255, 200, 18);
		minLabel.setBounds(10, 280, 80, 18);
		minField.setBounds(95, 280, 65, 18);
		minField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		maxLabel.setBounds(170, 280, 80, 18);
		maxField.setBounds(255, 280, 65, 18);
		maxField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		minLabel.setForeground(Color.RED);
		maxLabel.setForeground(Color.RED);
		
		submitButton.setBounds(30, 575, 130, 20);
		defaultButton.setBounds(180, 575, 130, 20);
		
		mCauseCombo.addItem(VMConstants.MAIL_CAUSE_MEM_THRESHOLD_EXCEED);
		mCauseCombo.addItem(VMConstants.MAIL_CAUSE_THREAD_CPU_EXCEED);
		
		intervalCombo.addItem("Seconds");
		intervalCombo.addItem("Minutes");
		intervalCombo.addItem("Hours");
		intervalCombo.addItem("Days");
		
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setDialogTitle("Select A Directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		applyDefault();
		
		CheckboxListener chkListener = new CheckboxListener();
		enablePersistence.addItemListener(chkListener);
		enableStartup.addItemListener(chkListener);
		enableSSL.addItemListener(chkListener);
		enableMailing.addItemListener(chkListener);
		
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int val = fileChooser.showSaveDialog(dtPanel);
				if (val == JFileChooser.APPROVE_OPTION) {
					persistField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		submitButton.addActionListener(new SubmitActionListener(this));
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				applyDefault();
			}
		});
	}
	
	private void addComponent() {
		add(displayLabel);
		add(appPanel);
		add(crPanel);
		add(dtPanel);
		
		appPanel.add(appLabel);
		appPanel.add(appField);
		
		crPanel.add(nameLabel);
		crPanel.add(nameField);
		crPanel.add(portLabel);
		crPanel.add(portField);
		crPanel.add(userLabel);
		crPanel.add(userField);
		crPanel.add(passwordLabel);
		crPanel.add(passwordField);
        crPanel.add(mserverLabel);
        crPanel.add(mserverField);
        crPanel.add(roleLabel);
        crPanel.add(roleField);
        crPanel.add(rpasswordLabel);
        crPanel.add(rpasswordField);
        
		dtPanel.add(enableSSL);
		dtPanel.add(enableLogging);
		dtPanel.add(enableMailing);
		dtPanel.add(enablePersistence);
		dtPanel.add(persistLabel);
		dtPanel.add(persistField);
		dtPanel.add(selectButton);
		dtPanel.add(intervalLabel);
		dtPanel.add(intervalField);
		dtPanel.add(intervalCombo);
		dtPanel.add(enableConnect);
		dtPanel.add(enableStartup);
		dtPanel.add(minLabel);
		dtPanel.add(minField);
		dtPanel.add(maxLabel);
		dtPanel.add(maxField);		
		dtPanel.add(mCauseLabel);
		dtPanel.add(mCauseCombo);
		dtPanel.add(mailFrom);
		dtPanel.add(mFromField);
		dtPanel.add(mailTo);
		dtPanel.add(mToField);
		dtPanel.add(mailCc);
		dtPanel.add(mCcField);
		
		add(submitButton);
		add(defaultButton);
	}
	
	private void applyDefault() {
		userField.setEditable(false);
		passwordField.setEditable(false);
		mserverField.setEditable(false);
		roleField.setEditable(false);
		rpasswordField.setEditable(false);
        enableSSL.setSelected(false);
		enableLogging.setSelected(false);
		enableMailing.setSelected(false);
		enableConnect.setSelected(false);
		enablePersistence.setSelected(false);
		selectButton.setEnabled(false);
		persistField.setEditable(false);
		intervalField.setEditable(false);
		intervalCombo.setEnabled(false);
		enableStartup.setSelected(true);
		mCauseCombo.setEnabled(false);
		mFromField.setEditable(false);
		mToField.setEditable(false);
		mCcField.setEditable(false);
		
		appField.setText("");
		nameField.setText("");
		portField.setText("");
		userField.setText("");
		passwordField.setText("");
		mserverField.setText("");
		roleField.setText("");
		rpasswordField.setText("");
		mFromField.setText("");
		mToField.setText("");
		mCcField.setText("");
		persistField.setText("");
		intervalField.setText("");
		minField.setEditable(true);
		maxField.setEditable(true);
		minField.setText("16M");
		maxField.setText("32M");
	}
	
	public void disableAllComponent() {
		appField.setEditable(false);
		
		nameField.setEditable(false);
		portField.setEditable(false);
		userField.setEditable(false);
		passwordField.setEditable(false);
		mserverField.setEditable(false);
		roleField.setEditable(false);
		rpasswordField.setEditable(false);
		enableSSL.setEnabled(false);
		enableLogging.setEnabled(false);
		enableMailing.setEnabled(false);
		enableConnect.setEnabled(false);
		enablePersistence.setEnabled(false);
		selectButton.setEnabled(false);
		persistField.setEditable(false);
		intervalField.setEditable(false);
		intervalCombo.setEnabled(false);
		enableStartup.setEnabled(false);
		mCauseCombo.setEnabled(false);
		mFromField.setEditable(false);
		mToField.setEditable(false);
		mCcField.setEditable(false);
		minField.setEditable(false);
		maxField.setEditable(false);
	}
	
	public void enableAllComponent() {
		appField.setEditable(true);
		
		nameField.setEditable(true);
		portField.setEditable(true);
		mserverField.setEditable(true);
        roleField.setEditable(true);
        rpasswordField.setEditable(true);
				
		enableSSL.setEnabled(true);
		if (enableSSL.isSelected()) {
			userField.setEditable(true);
			passwordField.setEditable(true);
		}
		enableLogging.setEnabled(true);
		enableMailing.setEnabled(true);
		if (enableMailing.isSelected()) {
			mCauseCombo.setEnabled(true);
			mFromField.setEditable(true);
			mToField.setEditable(true);
			mCcField.setEditable(true);
		}
		enableConnect.setEnabled(true);
		enablePersistence.setEnabled(true);
		if (enablePersistence.isSelected()) {
			selectButton.setEnabled(true);
			persistField.setEditable(true);
			intervalField.setEditable(true);
			intervalCombo.setEnabled(true);
		}
		enableStartup.setEnabled(true);
		if (enableStartup.isSelected()) {
			minField.setEditable(true);
			maxField.setEditable(true);
		}
	}
	
	private void populateEnvObject(EnvironmentConfigObject envConfigObject) {
		envConfigObject.setApplicationName(appField.getText());
		envConfigObject.setServerId(nameField.getText());
		envConfigObject.setPort(portField.getText());
		envConfigObject.setUserName(userField.getText());
		envConfigObject.setPassword(passwordField.getText());
		envConfigObject.setMserver(mserverField.getText());
		envConfigObject.setRole(roleField.getText());
		envConfigObject.setRolePassword(rpasswordField.getText());
		envConfigObject.setEnableSSL(String.valueOf(enableSSL.isSelected()));
		envConfigObject.setEnableLogging(String.valueOf(enableLogging.isSelected()));
		envConfigObject.setEnableMailing(String.valueOf(enableMailing.isSelected()));
		envConfigObject.setMailCause(String.valueOf(mCauseCombo.getSelectedItem()));
		envConfigObject.setMailFrom(mFromField.getText());
		envConfigObject.setMailTo(mToField.getText());
		envConfigObject.setMailCc(mCcField.getText());
		envConfigObject.setEnableConnect(String.valueOf(enableConnect.isSelected()));
		envConfigObject.setEnablePersistence(String.valueOf(enablePersistence.isSelected()));
		envConfigObject.setPersistFilePath(persistField.getText());
		envConfigObject.setPersistInterval(intervalField.getText());
		envConfigObject.setIntervalType((String)intervalCombo.getSelectedItem());
		envConfigObject.setIndependentProcess(String.valueOf(enableStartup.isSelected()));
		String vmMin = minField.getText();
		vmMin = !vmMin.endsWith("M") ? (vmMin + "M") : vmMin; 
		envConfigObject.setVmMemoryMin(vmMin);
		String vmMax = maxField.getText();
		vmMax = !vmMax.endsWith("M") ? (vmMax + "M") : vmMax; 
		envConfigObject.setVmMemoryMax(vmMax);
	}
	
	private class CheckboxListener implements ItemListener {
		public void itemStateChanged(ItemEvent ie) {
			if (ie.getSource() == enableSSL) {
				if (enableSSL.isSelected()) {
					userLabel.setForeground(Color.RED);
					passwordLabel.setForeground(Color.RED);
					userField.setEditable(true);
					passwordField.setEditable(true);
                    mserverField.setEditable(true);
                    roleField.setEditable(true);
                    rpasswordField.setEditable(true);
				}
				else {
					userLabel.setForeground(Color.BLACK);
					passwordLabel.setForeground(Color.BLACK);
					userField.setText("");
					passwordField.setText("");
					mserverField.setText("");
					roleField.setText("");
					rpasswordField.setText("");
					userField.setEditable(false);
					passwordField.setEditable(false);
					mserverField.setEditable(false);
					roleField.setEditable(false);
					rpasswordField.setEditable(false);
				}
			}
			else if (ie.getSource() == enableMailing) {
				if (enableMailing.isSelected()) {
					mailFrom.setForeground(Color.RED);
					mailTo.setForeground(Color.RED);
					mCauseCombo.setEnabled(true);
					mFromField.setEditable(true);
					mToField.setEditable(true);
					mCcField.setEditable(true);
				}
				else {
					mailFrom.setForeground(Color.BLACK);
					mailTo.setForeground(Color.BLACK);
					mCauseCombo.setEnabled(false);
					mFromField.setText("");
					mToField.setText("");
					mCcField.setText("");
					mFromField.setEditable(false);
					mToField.setEditable(false);
					mCcField.setEditable(false);
				}
			}
			else if (ie.getSource() == enablePersistence) {
				if (enablePersistence.isSelected()) {
					persistLabel.setForeground(Color.RED);
					intervalLabel.setForeground(Color.RED);
					selectButton.setEnabled(true);
					persistField.setEditable(true);
					intervalField.setEditable(true);
					intervalCombo.setEnabled(true);
				}
				else {
					persistLabel.setForeground(Color.BLACK);
					intervalLabel.setForeground(Color.BLACK);
					selectButton.setEnabled(false);
					persistField.setText("");
					intervalField.setText("");
					persistField.setEditable(false);
					intervalField.setEditable(false);
					intervalCombo.setEnabled(false);
				}
			}
			else if (ie.getSource() == enableStartup) {
				if (enableStartup.isSelected()) {
					minLabel.setForeground(Color.RED);
					maxLabel.setForeground(Color.RED);
					minField.setEditable(true);
					maxField.setEditable(true);
					minField.setText("16M");
					maxField.setText("32M");
				}
				else {
					minLabel.setForeground(Color.BLACK);
					maxLabel.setForeground(Color.BLACK);
					minField.setText("");
					maxField.setText("");
					minField.setEditable(false);
					maxField.setEditable(false);
				}
			}
		}
	}
	
	private class SubmitActionListener implements ActionListener {
		private JPanel parent = null;
		public SubmitActionListener(JPanel panel) {
			parent = panel;
		}
		public void actionPerformed(ActionEvent ae) {
			synchronized (this) {
				String c_mode = ((ConfigurationPanel)parent).getCurrentMode();
				
				if (c_mode.equals(MODE_VIEW)) {
					((ConfigurationPanel)parent).setMode(ConfigurationPanel.MODE_EDIT);
					((ConfigurationPanel)parent).enableAllComponent();
					return;
				}
				else if (c_mode.equals(MODE_NEW)) {
					if ("".equals(appField.getText()) || "".equals(nameField.getText()) || "".equals(passwordLabel.getText())) {
						JOptionPane.showMessageDialog(parent, "Mandetory Fields Are Required");
						return;
					}
					if (enableMailing.isSelected() && ("".equals(mFromField.getText()) || "".equals(mToField.getText()))) {
						JOptionPane.showMessageDialog(parent, "Enter Mailing From / To Details");
						return;
					}
					if (enablePersistence.isSelected() && ("".equals(persistField.getText()) || "".equals(intervalField.getText()))) {
						JOptionPane.showMessageDialog(parent, "Select Persistence File Path / Interval");
						return;
					}
					if (enableStartup.isSelected() && ("".equals(minField.getText()) || "".equals(maxField.getText()))) {
						JOptionPane.showMessageDialog(parent, "Select VM Memory Arguments");
						return;
					}
					String appName = VMConfigurationUtil.isExistConfiguration(nameField.getText().trim(), portField.getText().trim());
					if (appName != null && !"".equals(appName)) {
						JOptionPane.showMessageDialog(parent, "Application '" + appName + "' has Similar Configuration");
						return;
					}
				
					EnvironmentConfigObject envConfigObject = new EnvironmentConfigObject();
					envConfigObject.setApplicationId(String.valueOf(VMConfigurationUtil.generateNextApplicationId()));
					populateEnvObject(envConfigObject);
					VMConfigurationUtil.createNewConfiguration(envConfigObject);
					dPanel.createNewApplicationConfiguration(envConfigObject);
				}
				else {
					EnvironmentConfigObject envConfigObject = new EnvironmentConfigObject();
					envConfigObject.setApplicationId(current_application_id);
					populateEnvObject(envConfigObject);
					VMConfigurationUtil.refreshConfigurations(envConfigObject);
				}
			}
		}
	}
}
