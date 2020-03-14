package com.sc.hm.vmxd.synchui.layout.mbeans;

import java.awt.Dimension;
import java.awt.Font;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;
import com.sc.hm.vmxd.process.executor.AbstractMBeanProcessExecutor;
import com.sc.hm.vmxd.process.executor.MBeanProcessExecutorFactory;
import com.sc.hm.vmxd.synchui.layout.panel.SynchVMStaticTab;

public class SynchMBeansTab extends SynchVMStaticTab {

	private static final long serialVersionUID = 1L;
	
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	
	public SynchMBeansTab(String applicationId) throws Exception {
		super(applicationId);
		initSharedObj();
		initPanel();
		addComponent();
	}
	
	public void initSharedObj() {}
	
	public void initPanel() throws Exception {
		setSize(new Dimension(880, 700));
		setLayout(null);
		
		splitPane.setSize(new Dimension(880, 700));
		splitPane.setDividerLocation(210);
		splitPane.setDividerSize(10);
	}
	
	public void addComponent() {
		add(splitPane);
	}
	
	public void loadStaticVMData() throws Exception {
		Future<Object> future = null;
		SynchMBeanNode rootNode = null;
		
		AbstractMBeanProcessExecutor mbeanProcessExecutor = MBeanProcessExecutorFactory
		        .getExecutorFactory().getMBeanExecutor("MBEAN_EXEC_APP_" + application, 1);
		
		final MXBeanServer mxbeanServer = MXBeanServerManager.getMXBeanServer(application);
		
		try {
			future = mbeanProcessExecutor.submitFutureTask(mxbeanServer
			        , application
			        , "com.sc.hm.vmxd.synchui.layout.mbeans.SynchMBeansHandler");
			
			Logger.log("Started MBeans Tab Data Loading Process....");
			rootNode = (SynchMBeanNode)future.get();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		JPanel left_panel = new JPanel();
		left_panel.setLayout(null);
		left_panel.setPreferredSize(new Dimension(210, 660));
		
		final JTree mbeans_tree = new JTree(rootNode);
		mbeans_tree.setFont(new Font("Arial", Font.PLAIN, 11));
		
		mbeans_tree.addMouseListener(new SynchMBeanTreeListener(application, mbeans_tree, splitPane));
		
		JScrollPane scrollPane = new JScrollPane(mbeans_tree);
		scrollPane.setBounds(0, 0, 210, 580);
		JButton button = new JButton("Refresh");
		button.setFont(new Font("Arial", Font.PLAIN, 11));
		button.setBounds(65, 600, 80, 18);
		
		left_panel.add(scrollPane);
		left_panel.add(button);
		
		splitPane.setLeftComponent(left_panel);
		splitPane.setRightComponent(new JPanel());
		//splitPane.repaint();
	}
}
