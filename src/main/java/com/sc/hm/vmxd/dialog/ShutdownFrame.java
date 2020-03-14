package com.sc.hm.vmxd.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Frame that will be visible when the remote monitoring is being stopped.
 * 
 * @author Sudiptasish Chanda
 */
public class ShutdownFrame extends JFrame {	
	private static final long serialVersionUID = 1L;
	
	public ShutdownFrame() {
		super("Shutdown Monitoring");
		setSize(new Dimension(350, 160));
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		setLocation((int)(dim.width / 2 - getWidth() / 2), (int)(dim.height / 2 - getHeight() / 2));
		//setUndecorated(true);
		getContentPane().add(new ShutdownPanel(), BorderLayout.CENTER);
	}

	private class ShutdownPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private Dimension dim = new Dimension(340, 140);		
		private JLabel label = new JLabel("Shutting down jvm monitor. Please Wait....");
	
		public ShutdownPanel() {
			super();
			setLayout(null);
			setSize(dim);
			init();
		}
		
        @Override
		public Dimension getSize() {
			return dim;
		}
		
        @Override
		public Dimension getPreferredSize() {
			return dim;
		}
		
		private void init() {
			label.setFont(new Font("Arial", Font.PLAIN, 11));
			label.setBounds(30, 40, 300, 18);
			add(label);
		}
	}
}
