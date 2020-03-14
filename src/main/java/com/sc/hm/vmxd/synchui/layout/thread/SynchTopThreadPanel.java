package com.sc.hm.vmxd.synchui.layout.thread;

import java.awt.Color;
import java.awt.Font;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.ThreadDataRepository;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.panel.SynchMonitoringGraphPanel;
import com.sc.hm.vmxd.synchui.layout.thread.model.SynchTopThreadTableModel;

public class SynchTopThreadPanel extends SynchMonitoringGraphPanel {
	
	private static final long serialVersionUID = 1L;
	
	private String applicationId = "";
    
	private ThreadDataRepository threadRepository = null;
	
	private JScrollPane scroller = null;
	private JTable threadTable = null;
	
	private SynchSingleThreadDetailsPanel detailsPanel;
	
	public SynchTopThreadPanel(String application
	        , SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED sharedObj
	        , int panel_width
	        , int panel_height) throws Exception {
	    
        super(sharedObj, panel_width, panel_height);
        applicationId = application;
        initOther();
        initializePrimaryPanel();
    }
	
	private void initOther() {
		try {
			threadRepository = (ThreadDataRepository)AbstractMBeanDataRepositoryFactory
			        .getDataRepositoryFactory(applicationId)
			        .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_THREAD);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initializePrimaryPanel() throws Exception {
	    threadTable = new JTable(new SynchTopThreadTableModel("Top Thread"));
	    threadTable.setBounds(60, 20, 790, 410);
        threadTable.setFont(new Font("Arial", Font.PLAIN, 11));
        threadTable.getSelectionModel().addListSelectionListener(new ThreadTableSelectionListener());
        
        threadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        threadTable.setBackground(Color.BLACK);
        threadTable.setForeground(Color.GREEN);
        
        scroller = new JScrollPane(threadTable);
        scroller.setBounds(60, 20, 790, 410);
        
        add(scroller);
	}
	
	/**
     * @param detailsPanel the detailsPanel to set
     */
    public void setDetailsPanel(SynchSingleThreadDetailsPanel detailsPanel) {
        this.detailsPanel = detailsPanel;
    }

    public void startSynchronizedMonitoring() throws Exception {
        retrieveInformation("com.sc.hm.vmxd.process.ThreadMBeanSynchProcess");
        ((SynchTopThreadTableModel)threadTable.getModel()).setTableModelData(threadRepository.allUsages());
	}
    
    private class ThreadTableSelectionListener implements ListSelectionListener {
        
        /* (non-Javadoc)
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (SynchTopThreadPanel.this.threadTable.getSelectedRow() == -1) {
                return;
            }
            try {
                Long id = (Long)SynchTopThreadPanel.this.threadTable
                        .getValueAt(SynchTopThreadPanel.this.threadTable.getSelectedRow(), 1);
                
                MXBeanServer mxbeanServer = MXBeanServerManager.getMXBeanServer(SynchApplicationConfiguration
                        .getSynchInstance().getEnvironmentConfig().getApplicationId());
                
                Object cData = mxbeanServer.invokeOperation(
                        new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME)
                        , "getThreadInfo"
                        , new Object[] {id, Integer.MAX_VALUE}
                        , new String[] {long.class.getName(), int.class.getName()});
                
                if (cData != null) {
                    ThreadInfo threadInfo = ThreadInfo.from((CompositeData)cData);
                    detailsPanel.displayThreadDetails(threadInfo);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }        
    }
}
