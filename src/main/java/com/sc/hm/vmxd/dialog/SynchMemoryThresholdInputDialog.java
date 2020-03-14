package com.sc.hm.vmxd.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.text.NumberFormat;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.MemoryPoolDataRepository;
import com.sc.hm.vmxd.data.memory.MemoryPoolData;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class SynchMemoryThresholdInputDialog {
	
	private static SynchMemoryThresholdInputDialog _INSTANCE_ = null;
	
	private MemoryPoolDataRepository memoryPoolRepository = null;
	
	private final JFrame frame = new JFrame();
	
	private final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	private final Font font = new Font("Arial", Font.PLAIN, 11);
	
	private InputDialog inputDialog = null;
	private ConfirmDialog confirmDialog = null;
	
	private SynchMemoryThresholdInputDialog() {
		initContainer();
		initInputDialog();
		initConfirmDialog();
	}
	
	public static synchronized SynchMemoryThresholdInputDialog getMemoryDialog() {
		if (_INSTANCE_ == null) {
			_INSTANCE_ = new SynchMemoryThresholdInputDialog();
		}
		return _INSTANCE_;
	}
	
	public void initContainer() {
		frame.setTitle("Set Threshold Value");
		frame.addWindowListener(new WindowAdapter() {
			private boolean gotFocus = false;
			public void windowGainedFocus(WindowEvent we) {
				if (!gotFocus) {
					gotFocus = true;
				}
			}
		});
	}
	
	private void initInputDialog() {
		inputDialog = new InputDialog();
	}
	
	private void initConfirmDialog() {
		confirmDialog = new ConfirmDialog();
	}
	
	public void showInputDialog(String applicationId, String mPoolName) throws Exception {
		memoryPoolRepository = (MemoryPoolDataRepository)AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId).getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY_POOL);
		inputDialog.setApplicationId(applicationId);
		inputDialog.setMemoryPoolName(mPoolName);
		inputDialog.setUserInput("");
		inputDialog.setMessage("");
		
		//frame.removeAll();
		frame.remove(confirmDialog);
		frame.setSize(new Dimension(360, 140));
		frame.setLocation((int)(dim.getWidth() - frame.getWidth()) / 2, (int)(dim.getHeight() - frame.getHeight()) / 2);
		frame.getContentPane().add(inputDialog);
		frame.setVisible(true);
	}
	
	public void showInputDialog(String mPoolName) {
		inputDialog.setMemoryPoolName(mPoolName);
		inputDialog.setUserInput("");
		inputDialog.setMessage("");
		
		//frame.removeAll();
		frame.remove(confirmDialog);
		frame.setSize(new Dimension(360, 140));
		frame.setLocation((int)(dim.getWidth() - frame.getWidth()) / 2, (int)(dim.getHeight() - frame.getHeight()) / 2);
		frame.getContentPane().add(inputDialog);
		frame.setVisible(true);
	}
	
	public void showConfirmDialog(long value, String message) {
		confirmDialog.setInitialMessage(message);
		confirmDialog.setThresholdValue(value);
		
		//frame.removeAll();
		frame.remove(inputDialog);
		frame.setSize(new Dimension(300, 130));
		frame.setLocation((int)(dim.getWidth() - frame.getWidth()) / 2, (int)(dim.getHeight() - frame.getHeight()) / 2);
		frame.getContentPane().add(confirmDialog);
		frame.setVisible(true);
	}
	
	public void setThresholdValueForMemoryPool(long val) {
		if (val > 0) {
			try {
				MXBeanServer mxbeanServer = MXBeanServerManager.getMXBeanServer(inputDialog.getApplicationId());
				if (mxbeanServer != null) {
					try {
						mxbeanServer.setAttribute(new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + ",name=" + inputDialog.getMemoryPoolName()), new Attribute("UsageThreshold", val));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				frame.setVisible(false);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class InputDialog extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private final JLabel inputLabel = new JLabel("Type Memory Threshold Value (in %)");
		private final JLabel msgLabel = new JLabel("");
		private final JTextField inputField = new JTextField();
		private final JButton okButton = new JButton("Ok");
		private final JButton cancelButton = new JButton("Cancel");
		
		SynchMemoryThresholdDialogListener mThresholdDialogListener = new SynchMemoryThresholdDialogListener();
		
		private final Font font = new Font("Arial", Font.PLAIN, 11);
		
		private String applicationId = "";
		private String memoryPoolName = "";
		
		public InputDialog() {
			setSize(new Dimension(350, 130));
			setLayout(null);
			initDialog();
			addComponent();	
		}
		
		public Dimension getSize() {
			return new Dimension(350, 130);
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(350, 130);
		}
		
		public void setMemoryPoolName(String mPoolName) {
			memoryPoolName = mPoolName;
		}
		
		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public String getMemoryPoolName() {
			return memoryPoolName;
		}
		
		public String getUserInput() {
			return inputField.getText();
		}
		
		public void setUserInput(String input) {
			inputField.setText(input);
		}
		
		public void setMessage(String msg) {
			msgLabel.setText(msg);
		}
		
		private void initDialog() {
			msgLabel.setForeground(Color.RED);
			inputField.setBorder(new BevelBorder(BevelBorder.LOWERED));
			
			int width = 300;
			inputLabel.setBounds(20, 5, width, 18);
			inputField.setBounds(20, 30, width, 18);
			msgLabel.setBounds(20, 52, width, 15);
			okButton.setBounds(85, 74, 80, 18);
			cancelButton.setBounds(180, 74, 80, 18);
			
			okButton.addActionListener(mThresholdDialogListener);
			cancelButton.addActionListener(mThresholdDialogListener);
			
			inputLabel.setFont(font);
			msgLabel.setFont(new Font("Arial", Font.PLAIN, 10));
			inputField.setFont(font);
			okButton.setFont(font);
			cancelButton.setFont(font);
		}
		
		private void addComponent() {
			add(inputLabel);
			add(inputField);
			add(msgLabel);
			add(okButton);
			add(cancelButton);
		}
	}
	
	private class ConfirmDialog extends JPanel {		
		private static final long serialVersionUID = 1L;
		
		private final JLabel valueLabel = new JLabel();
		private final JLabel confirmLabel = new JLabel("Are You Sure ?");
		private final JButton yesButton = new JButton("Yes");
		private final JButton noButton = new JButton("No");
		private long thresholdValue = 0L;
		
		private ConfirmDialog() {
			setSize(new Dimension(270, 110));
			setLayout(null);
			initDialog();
			addComponent();
		}
		
        @Override
		public Dimension getSize() {
			return new Dimension(270, 110);
		}
		
        @Override
		public Dimension getPreferredSize() {
			return new Dimension(270, 110);
		}
		
		public void setThresholdValue(long v) {
			thresholdValue = v;
		}
		
		public void setInitialMessage(String msg) {
			valueLabel.setText(msg);
		}
		
		private void initDialog() {
			valueLabel.setBounds(20, 10, 240, 18);
			confirmLabel.setBounds(110, 35, 140, 18);
			yesButton.setBounds(60, 62, 80, 18);
			noButton.setBounds(155, 62, 80, 18);
			
			yesButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					setThresholdValueForMemoryPool(thresholdValue);
				}
			});
			
			noButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					showInputDialog(inputDialog.getMemoryPoolName());
				}
			});
			
			valueLabel.setFont(font);
			confirmLabel.setFont(font);
			yesButton.setFont(font);
			noButton.setFont(font);
		}
		
		private void addComponent() {
			add(valueLabel);
			add(confirmLabel);
			add(yesButton);
			add(noButton);
		}
	}
	
	private class SynchMemoryThresholdDialogListener implements ActionListener {
		
		public static final String ACTION_BUTTON_OK = "Ok";
		public static final String ACTION_BUTTON_CANCEL = "Cancel";
		
		private final NumberFormat n_format = NumberFormat.getInstance();
		
		public SynchMemoryThresholdDialogListener() {
			n_format.setGroupingUsed(true);
		}

        @Override
		public void actionPerformed(ActionEvent ae) {
			String actionCommand = ae.getActionCommand();
			if (ACTION_BUTTON_OK.equals(actionCommand)) {
				performTaskSetThreshold(inputDialog.getUserInput());
			}
			else if (ACTION_BUTTON_CANCEL.equals(actionCommand)) {
				performTaskCancel();
			}
		}
		
		private void performTaskSetThreshold(final String input) {
			String temp = input.endsWith("%") ? input.substring(0, input.indexOf("%")) : input;
			try {
				long val = Long.parseLong(temp);
				if (val < 1 || val > 99) {
					throw new Exception("Value should be within 1% to 99%");
				}
				MemoryPoolData memoryPoolData = memoryPoolRepository.getMemoryPoolData(inputDialog.getMemoryPoolName());
				MemoryUsage usage = memoryPoolData.getCurrentUsage();
				long init = usage.getInit();
				long max = usage.getMax();
				val = (val * max) / 100;
				if (val <= init) {
				    // It is possible that min and max memory are same, so don't throw any exception.
					//throw new Exception("Value should be Greater than Initial Memory");
				}
				showConfirmDialog(val, n_format.format(val) + " Byte will be set as Threshold Value");
			}
			catch (NumberFormatException nfe) {
				inputDialog.setMessage("Input should be Numeric");
			}
			catch (Exception e) {
				inputDialog.setMessage(e.getMessage());
			}
		}
		
		private void performTaskCancel() {
			frame.setVisible(false);
		}
	}
}

