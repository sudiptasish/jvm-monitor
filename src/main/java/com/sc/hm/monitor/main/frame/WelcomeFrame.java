package com.sc.hm.monitor.main.frame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sc.hm.monitor.common.SharedMain;
import com.sc.hm.monitor.launcher.LocalVMHandler;
import com.sc.hm.monitor.util.GraphImageProperties;

public class WelcomeFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Dimension screen_dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private Vector<LocalVMHandler._LocalVMArgs> vm_vector = null;
	
	private WelcomeTabbedPanel welcomePanel = null;

	public WelcomeFrame(SharedMain mShared, String initialMsg) {
		super("Remote VM Connection");
		loadLocalAciveVMs();
		initializeWelcomePanel(mShared, initialMsg);
		initializeFrame();
		addComponent();
	}
	
	private void loadLocalAciveVMs() {
		vm_vector = LocalVMHandler.getActiveLocalVMs();
	}
	
	private void initializeWelcomePanel(SharedMain mShared, String initialMsg) {
		welcomePanel = new WelcomeTabbedPanel(380, 300);
		welcomePanel.setMShared(mShared);
		welcomePanel.setInitialMsg(initialMsg);
		welcomePanel.setVm_vector(vm_vector);
		welcomePanel.inititializePanel();
		
		String current_os = System.getProperty("os.name");
		if (current_os.indexOf("Windows") >= 0 || current_os.indexOf("windows") >= 0) {
			try {
				UIManager.setLookAndFeel(GraphImageProperties.WINDOWS_CLASS_NAME);
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (InstantiationException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			SwingUtilities.updateComponentTreeUI(welcomePanel);
		}
	}
	
	private void initializeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(380, 300));
		setLocation((int)(screen_dimension.getWidth() / 2 - getSize().getWidth() / 2), (int)(screen_dimension.getHeight() / 2 - getSize().getHeight() / 2));
		setResizable(false);
	}
	
	private void addComponent() {
		getContentPane().add(welcomePanel);
	}
}
