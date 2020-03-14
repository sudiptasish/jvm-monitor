package com.sc.hm.monitor.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.launcher.MBeanProcessLauncher;
import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.shared.mpool.MemoryPool;

public class MemoryThresholdInputDialog {
	
	private static MemoryThresholdInputDialog _INSTANCE_ = null;
	
	private final MBeanSharedObjectRepository mPoolShared = MBeanSharedObjectRepository.getInstance();
	
	private final JFrame frame = new JFrame();
	
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	private InputDialog inputDialog = null;
	private ConfirmDialog confirmDialog = null;
	
	private MemoryThresholdInputDialog() {
		initContainer();
		initInputDialog();
		initConfirmDialog();
	}
	
	public static synchronized MemoryThresholdInputDialog getMemoryDialog() {
		if (_INSTANCE_ == null) {
			_INSTANCE_ = new MemoryThresholdInputDialog();
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
			MBeanProcessLauncher.setThreshold(inputDialog.getMemoryPoolName(), val);
			mPoolShared.getMpool_mx_bean().setChangedPoolConfig(inputDialog.getMemoryPoolName());				
			frame.setVisible(false);
		}
	}
	
	private class InputDialog extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private JLabel inputLabel = new JLabel("Type Memory Threshold Value (in %)");
		private JLabel msgLabel = new JLabel("");
		private JTextField inputField = new JTextField();
		private JButton okButton = new JButton("Ok");
		private JButton cancelButton = new JButton("Cancel");
		
		MemoryThresholdDialogListener mThresholdDialogListener = new MemoryThresholdDialogListener();
		
		private Font font = new Font("Arial", Font.PLAIN, 11);
		
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
		
		private JLabel valueLabel = new JLabel();
		private JLabel confirmLabel = new JLabel("Are You Sure ?");
		private JButton yesButton = new JButton("Yes");
		private JButton noButton = new JButton("No");
		private long thresholdValue = 0L;
		
		private ConfirmDialog() {
			setSize(new Dimension(270, 110));
			setLayout(null);
			initDialog();
			addComponent();
		}
		
		public Dimension getSize() {
			return new Dimension(270, 110);
		}
		
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
	
	private class MemoryThresholdDialogListener implements ActionListener {
		
		public static final String ACTION_BUTTON_OK = "Ok";
		public static final String ACTION_BUTTON_CANCEL = "Cancel";
		
		private NumberFormat n_format = new DecimalFormat("0");
		
		public MemoryThresholdDialogListener() {
			n_format.setGroupingUsed(true);
		}

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
				MemoryPool memoryPool = mPoolShared.getMpool_mx_bean().getMemoryPool(inputDialog.getMemoryPoolName());
				long init = memoryPool.getInit();
				long max = memoryPool.getMax();
				val = (val * max) / 100;
				if (val <= init) {
					throw new Exception("Value should be Greater than Initial Memory");
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
