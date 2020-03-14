package com.sc.hm.monitor.main.frame;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class MonitorFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Dimension screen_dimension = Toolkit.getDefaultToolkit().getScreenSize();

	public MonitorFrame() {
		initializeFrame();
	}
	
	private void initializeFrame() {
		setTitle("VM Monitor");
		setSize(new Dimension(890, 725));
		setLocation((int)(screen_dimension.getWidth() / 2 - getSize().width / 2), (int)(screen_dimension.getHeight() / 2 - getSize().getHeight() / 2));		
	}
	
	public void addComponent(JComponent component) {
		getContentPane().add(component);
	}
}
