package com.sc.hm.monitor.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MBeanOperationDialog {
	
	private static MBeanOperationDialog _INSTANCE_ = null;

	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	private final JFrame frame = new JFrame();
	
	private MBeanOpInfoPanel panel = null;
	
	private MBeanOperationDialog() {
		panel = new MBeanOpInfoPanel();
		initContainer();
	}
	
	public static synchronized MBeanOperationDialog getMBeanOperationDialog() {
		if (_INSTANCE_ == null) {
			_INSTANCE_ = new MBeanOperationDialog();
		}
		return _INSTANCE_;
	}
	
	public void initContainer() {
		frame.setTitle("Method Invocation");
		frame.addWindowListener(new WindowAdapter() {
			private boolean gotFocus = false;
			public void windowGainedFocus(WindowEvent we) {
				if (!gotFocus) {
					gotFocus = true;
				}
			}
		});
		frame.setSize(new Dimension(360, 140));
		frame.setLocation((int)(dim.getWidth() - frame.getWidth()) / 2, (int)(dim.getHeight() - frame.getHeight()) / 2);
		frame.getContentPane().add(panel);
	}
	
	public void showMBeanOperationDialog(String msg, String error) {
		panel.showOpInfo(msg, error);
		frame.setVisible(true);
	}
	
	private class MBeanOpInfoPanel extends JPanel {		
		private static final long serialVersionUID = 1L;
		
		private Font font = new Font("Arial", Font.PLAIN, 11);
		
		private JLabel msgLabel = new JLabel();
		private JLabel errorLabel = new JLabel();
		
		private JButton button = new JButton("Ok");
		
		private Dimension panel_dim = new Dimension(355, 120);
		
		public MBeanOpInfoPanel() {
			super();
			initPanel();
		}
		
		private void initPanel() {
			setSize(panel_dim);
			setLayout(null);
			msgLabel.setBounds(10, 10, 330, 18);
			errorLabel.setBounds(10, 35, 330, 18);
			button.setBounds(140, 70, 60, 18);
			
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					frame.dispose();
					frame.setVisible(false);
				}
			});
			
			msgLabel.setFont(font);
			errorLabel.setFont(font);
			button.setFont(font);
			
			add(msgLabel);
			add(errorLabel);
			add(button);
		}
		
		public void showOpInfo(String msg, String error) {
			msgLabel.setText(msg);
			errorLabel.setText(error);
		}
	}
}
