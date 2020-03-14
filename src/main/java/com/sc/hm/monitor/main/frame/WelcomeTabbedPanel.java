package com.sc.hm.monitor.main.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import com.sc.hm.monitor.common.SharedMain;
import com.sc.hm.monitor.config.ServerConfig;
import com.sc.hm.monitor.launcher.LocalVMHandler;
import com.sc.hm.monitor.mbean.factory.ManagementBeanFactory;
import com.sc.hm.monitor.mbean.factory.RemoteManagementBeanFactory;
import com.sc.hm.monitor.ui.layout.panel.BasicGraphPanel;

public class WelcomeTabbedPanel extends BasicGraphPanel {

	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane = null;
	private LocalPane localPane = null;
	private RemotePane remotePane = null;
	
	private SharedMain mShared = null;
	private String initialMsg = "";
	
	private JButton submitButton = new JButton("Connect");
	private JButton cancelButton = new JButton("Cancel");
	
	private Vector<LocalVMHandler._LocalVMArgs> vm_vector = null;
	
	public WelcomeTabbedPanel(int panel_width, int panel_height) {
		this(panel_width, panel_height, new TitledBorder(""), null);
	}
	
	public WelcomeTabbedPanel(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		super(panel_width, panel_height, panel_border, layout);
	}
	
	public void inititializePanel() {
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(10, 10, 355, 200);
		tabbedPane.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		localPane = new LocalPane(345, 170);
		remotePane = new RemotePane(345, 170);
		
		tabbedPane.addTab("Local", localPane);
		tabbedPane.addTab("Remote", remotePane);
		
		submitButton.setBounds(90, 225, 90, 20);
		submitButton.setMnemonic(KeyEvent.VK_C);
		cancelButton.setBounds(190, 225, 90, 20);
		cancelButton.setMnemonic(KeyEvent.VK_A);
		
		CCButtonActionListener buttonListener = new CCButtonActionListener();
		submitButton.addActionListener(buttonListener);
		cancelButton.addActionListener(buttonListener);
		
		addMainComponent();
	}
	
	private void addMainComponent() {
		add(tabbedPane);
		add(submitButton);
		add(cancelButton);
		requestFocus(true);		
	}
	
	public SharedMain getMShared() {
		return mShared;
	}

	public void setMShared(SharedMain shared) {
		mShared = shared;
	}

	public String getInitialMsg() {
		return initialMsg;
	}

	public void setInitialMsg(String initialMsg) {
		this.initialMsg = initialMsg;
	}

	public Vector<LocalVMHandler._LocalVMArgs> getVm_vector() {
		return vm_vector;
	}

	public void setVm_vector(Vector<LocalVMHandler._LocalVMArgs> vm_vector) {
		this.vm_vector = vm_vector;
	}

	private class LocalPane extends BasicGraphPanel {
		private static final long serialVersionUID = 1L;
		
		private LocalVMHandler._LocalVMArgs selectedVMArgs = null;
		
		private JTable table = null;
		private JScrollPane scrollpane = null;

		public LocalPane(int panel_width, int panel_height) {
			this(panel_width, panel_height, new TitledBorder(""), null);
		}
		
		public LocalPane(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
			super(panel_width, panel_height, panel_border, layout);
			initialize();
			addComponent();
		}
		
		private void initialize() {
			table = new JTable(new LocalVMTableModel());
			if (vm_vector != null && vm_vector.size() > 0) {
				Vector<LocalVMHandler._LocalVMArgs> vector = new Vector<LocalVMHandler._LocalVMArgs>(vm_vector);
				((LocalVMTableModel)table.getModel()).initializeTableData(vector);
				vm_vector.clear();
			}
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					int rowIndex = ((JTable)me.getSource()).getSelectedRow();
					String PID = (String)table.getModel().getValueAt(rowIndex, 0);
					String PNAME = (String)table.getModel().getValueAt(rowIndex, 1);
					String url = (String)table.getModel().getValueAt(rowIndex, 2);
					selectedVMArgs = new LocalVMHandler._LocalVMArgs(PID, PNAME, url);
					
					if (me.getClickCount() == 1) {						
					}
					else if (me.getClickCount() == 2) {
						submitButton.doClick();
					}
				}
			});
			scrollpane = new JScrollPane(table);
			scrollpane.setBounds(0, 0, 345, 170);
		}
		
		private void addComponent() {
			add(scrollpane);
		}
		
		public LocalVMHandler._LocalVMArgs getSelectedVMArgs() {
			return selectedVMArgs;
		}

		public void setSelectedVMArgs(LocalVMHandler._LocalVMArgs selectedVMArgs) {
			this.selectedVMArgs = selectedVMArgs;
		}

		private class LocalVMTableModel extends AbstractTableModel {
			private static final long serialVersionUID = 1L;
			
			private String[] columnNames = {"PID", "Image Name"};
			private Vector<Vector<String>> rowData = new Vector<Vector<String>>();
			
			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {
				return rowData.size();
			}
			
			public String getColumnName(int index) {
				return columnNames[index];
			}
			
			public void initializeTableData(Vector<LocalVMHandler._LocalVMArgs> vector) {
				rowData = new Vector<Vector<String>>(vector.size());
				for (LocalVMHandler._LocalVMArgs vmArg : vector) {
					Vector<String> row = new Vector<String>(columnNames.length + 1);
					row.addElement(vmArg.getProcessId());
					row.addElement(vmArg.getProcessName());
					row.addElement(vmArg.getJmxURL());
					rowData.addElement(row);
				}
				vector.clear();
			}
			
			public void initializeTableData(Map<String, String> data) {
				rowData = new Vector<Vector<String>>(data.size());
				for (Map.Entry<String, String> me : data.entrySet()) {
					Vector<String> row = new Vector<String>(columnNames.length);
					row.addElement(me.getKey());
					row.addElement(me.getValue());
					rowData.addElement(row);
				}
				data.clear();
			}
			
			public void setValueAt(Object obj, int rowIndex, int colIndex) {
				Vector<String> row = rowData.elementAt(rowIndex);
				row.add(colIndex, obj.toString());
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				Vector<String> row = rowData.elementAt(rowIndex);
				return row.elementAt(columnIndex);
			}
			
			public Class<?> getColumnClass(int columnIndex) {
				Vector<String> row = rowData.elementAt(0);
				String cellData = row.elementAt(columnIndex);
				return cellData.getClass();
		    }
		}
	}
	
	private class RemotePane extends BasicGraphPanel {
		private static final long serialVersionUID = 1L;
		
		private JLabel serverLabel = new JLabel("Server IP/Name *");
		private JLabel portLabel = new JLabel("Server Port *");
		private JLabel nameLabel = new JLabel("User Name");
		private JLabel pwdLabel = new JLabel("Password");
		
		private JTextField ipField = new JTextField();
		private JTextField portField = new JTextField();
		private JTextField nameField = new JTextField();
		private JTextField pwdField = new JTextField();
		
		private JCheckBox sslCheckBox = new JCheckBox("SSL Connection");
		
		private JLabel msgField = new JLabel();

		public RemotePane(int panel_width, int panel_height) {
			this(panel_width, panel_height, new TitledBorder(""), null);
		}
		
		public RemotePane(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
			super(panel_width, panel_height, panel_border, layout);
			initializeComponent();
			addComponent();
		}
		
		private void initializeComponent() {
			serverLabel.setBounds(20, 20, 120, 20);
			portLabel.setBounds(20, 45, 120, 20);
			nameLabel.setBounds(20, 70, 120, 20);
			pwdLabel.setBounds(20, 95, 120, 20);
			
			ipField.setBounds(150, 20, 120, 20);
			portField.setBounds(150, 45, 120, 20);
			nameField.setBounds(150, 70, 120, 20);
			nameField.setEditable(false);
			pwdField.setBounds(150, 95, 120, 20);
			pwdField.setEditable(false);
			
			msgField.setBounds(20, 125, 130, 15);
			msgField.setForeground(Color.RED);
			msgField.setFont(new Font("Verdana", Font.PLAIN, 11));
			msgField.setText(initialMsg);
			
			sslCheckBox.setBounds(150, 125, 130, 20);
			sslCheckBox.setEnabled(true);
			
			sslCheckBox.addItemListener(new SSLCheckboxListener());
		}
		
		private void addComponent() {
			add(serverLabel);
			add(portLabel);
			add(nameLabel);
			add(pwdLabel);
			
			add(ipField);
			add(portField);
			add(nameField);
			add(pwdField);
			
			add(msgField);
			add(sslCheckBox);
		}
		
		public String getIpValue() {
			return ipField.getText().trim();
		}
		
		public String getPortValue() {
			return portField.getText().trim();
		}
		
		public String getNameValue() {
			return nameField.getText().trim();
		}
		
		public String getPasswordValue() {
			return pwdField.getText().trim();
		}
		
		public void setMessageValue(String text) {
			msgField.setText(text);
		}
		
		public boolean getCheckBoxStatus() {
			return sslCheckBox.isSelected();
		}
		
		public void enableNameField() {
			nameField.setEditable(true);
		}
		
		public void enablePasswordField() {
			pwdField.setEditable(true);
		}
		
		public void disableNameField() {
			nameField.setEditable(false);
		}
		
		public void disablePasswordField() {
			pwdField.setEditable(false);
		}
	}
	
	private class SSLCheckboxListener implements ItemListener {
		public void itemStateChanged(ItemEvent ie) {
			if (ie.getStateChange() == ItemEvent.SELECTED) {
				remotePane.enableNameField();
				remotePane.enablePasswordField();
			}
			else {
				remotePane.disableNameField();
				remotePane.disablePasswordField();
			}
		}
	}
	
	private class CCButtonActionListener implements ActionListener {		
		public void actionPerformed(ActionEvent ae) {
			if (ae.getActionCommand().equals("Connect")) {
				mShared.setProceed(true);
				if (tabbedPane.getSelectedComponent() == localPane) {
					// LocalPane Selected
					if (localPane.getSelectedVMArgs() == null) {
						return;
					}
					LocalVMHandler._LocalVMArgs selectedVMArgs = localPane.getSelectedVMArgs();
					if (selectedVMArgs.getProcessName().indexOf("JVMMonitorMain") >= 0 || selectedVMArgs.getProcessName().indexOf("jvm_monitor") >= 0) {
						mShared.setThisVM(true);
					}
					else {
						mShared.setThisVM(false);
						mShared.setServerConfig(new ServerConfig(selectedVMArgs.getJmxURL()));
					}
					mShared.notifyLockOwner();
				}
				else if (tabbedPane.getSelectedComponent() == remotePane) {
					// RemotePane Selected
					mShared.setThisVM(false);
					if ("".equals(remotePane.getIpValue())) {
						remotePane.setMessageValue("Empty Server Field");
						return;
					}
					if ("".equals(remotePane.getPortValue())) {
						remotePane.setMessageValue("Empty Port Field");
						return;
					}
					mShared.setServerConfig(new ServerConfig(remotePane.getIpValue(), remotePane.getPortValue(), "rmi"));
					
					if (remotePane.getCheckBoxStatus()) {
						if ("".equals(remotePane.getNameValue())) {
							remotePane.setMessageValue("Empty Name Field");
							return;
						}
						if ("".equals(remotePane.getPasswordValue())) {
							remotePane.setMessageValue("Password Name Field");
							return;
						}
						mShared.getServerConfig().setName(remotePane.getNameValue());
						mShared.getServerConfig().setPassword(remotePane.getPasswordValue());
					}
					try {
						ManagementBeanFactory mbeanFactory = ManagementBeanFactory.getManagementBeanFactory(ManagementBeanFactory.FACTORY_TYPE_REOMTE);
						((RemoteManagementBeanFactory)mbeanFactory).checkRemoteConnection(mShared.getServerConfig().getServerName(), mShared.getServerConfig().getServerPort());
						remotePane.setMessageValue("");
						mShared.notifyLockOwner();
					}
					catch (Exception e) {
						System.err.println("Unable to Connect. Error: " + e.getMessage());
						remotePane.setMessageValue("Invalid Argument(s)");
					}
				}
			}
			else if (ae.getActionCommand().equals("Cancel")) {
				mShared.setProceed(false);
				mShared.notifyLockOwner();
			}
		}
	}
}
