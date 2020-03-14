package com.sc.hm.vmxd.synchui.layout.thread;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.ThreadDataRepository;
import com.sc.hm.vmxd.data.thread.ThreadUsageData;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;

public class SynchThreadDetailsPanel extends JPanel implements ActionListener, ListSelectionListener {
	
	private static final long serialVersionUID = 1L;

	private int panel_width = 880;
	private int panel_height = 220;
	
	private JList<String> list = null;
	private JTextArea textArea = null;
	private JScrollPane threadListPane = null;
	private JScrollPane threadDetailsPane = null;
	private JButton button = new JButton("Refresh List");
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	private Dimension dim = new Dimension(panel_width, panel_height);
	
	private final StringBuilder builder = new StringBuilder(1000);
	
	public SynchThreadDetailsPanel() throws Exception {
		super();
		setLayout(null);
		setSize(dim);
		setBorder(new BevelBorder(BevelBorder.RAISED));
		setFont(font);
		initComponent();
		initOther();
	}

	public Dimension getSize() {
		return dim;
	}

	public Dimension getPreferredSize() {
		return dim;
	}

	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
	}
	
	public void initComponent() throws Exception {
		list = new JList(prepareThreadList());
		list.setFont(font);
		list.setValueIsAdjusting(true);
		list.addListSelectionListener(this);
				
		textArea = new JTextArea(30, 12);
		textArea.setEditable(false);
		textArea.setLineWrap(false);
		textArea.setFont(font);
		
		threadListPane = new JScrollPane(list);
		threadListPane.setBounds(60, 5, 200, 155);
		
		threadDetailsPane = new JScrollPane(textArea);
		threadDetailsPane.setBounds(270, 5, 590, 155);
		
		button.setFont(font);
		button.setBounds(110, 165, 105, 18);
		button.addActionListener(this);
		
		add(threadListPane);
		add(threadDetailsPane);
		add(button);
	}
	
	public void initOther() {}
    
	/**
	 * Fetch the list of thread ids and their names from the remote MBean server.
	 * @return String[]
	 * @throws Exception
	 */
    private String[] prepareThreadList() throws Exception {
        String applicationId = SynchApplicationConfiguration.getSynchInstance()
                .getEnvironmentConfig().getApplicationId();
        
        ThreadDataRepository threadRepository = (ThreadDataRepository)AbstractMBeanDataRepositoryFactory
                .getDataRepositoryFactory(applicationId)
                .getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_THREAD);
        
        Collection<ThreadUsageData> usages = threadRepository.allUsages();
        
        boolean errorOccured = false;
        String[] threadNames = null;
        
        do {
            try {
                threadNames = new String[usages.size()];
                int i = 0;
                for (Iterator<ThreadUsageData> itr = usages.iterator(); itr.hasNext(); i ++) {
                    ThreadUsageData usageData = itr.next();
                    threadNames[i] = usageData.getId() + ":" + usageData.getName();
                }
                errorOccured = false;
            }
            catch (ConcurrentModificationException e) {
                // May be new thread info got added. Redo everything...
                errorOccured = true;
            }
        }
        while (errorOccured);
        
        return threadNames;
    }
	
	public void actionPerformed(ActionEvent ae) {
		try {
		    synchronized (list) {
	            list.setListData(prepareThreadList());
	        }
		}
		catch (Exception e) {
			System.err.println("Error!!! Refreshing Thread List.... " + e.getMessage());
		}
	}
	
	public void valueChanged(ListSelectionEvent lse) {
		try {
			MXBeanServer mxbeanServer = MXBeanServerManager.getMXBeanServer(SynchApplicationConfiguration
			        .getSynchInstance().getEnvironmentConfig().getApplicationId());
			
			synchronized (list) {
				Object obj = list.getSelectedValue();
				if (obj != null) {
					String s = obj.toString();
					long id = Long.parseLong(s.substring(0, s.indexOf(":")));
					
					Object cData = mxbeanServer.invokeOperation(
					        new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME)
					        , "getThreadInfo"
					        , new Object[] {id, Integer.MAX_VALUE}
					        , new String[] {long.class.getName(), int.class.getName()});
					
					if (cData != null) {
						ThreadInfo threadInfo = ThreadInfo.from((CompositeData)cData);
						/*cData = mxbeanServer.invokeOperation(
						        new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME)
						        , "getThreadCpuTime"
						        , new Object[] {id}
						        , new String[] {long.class.getName()});*/
						textArea.setText(dumpThreadInfo(threadInfo, -1));
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String dumpThreadInfo(ThreadInfo threadInfo, long cpuTime) {
	    
		builder.append(" Name: ")
		    .append(threadInfo.getThreadName())
		    .append("      [Thread Id: ")
		    .append(threadInfo.getThreadId())
		    .append("]")
		    .append("\n");
		
		builder.append(" Current State: ").append(threadInfo.getThreadState()).append("\t");
		if (threadInfo.getThreadState().equals(Thread.State.BLOCKED.toString())) {
			builder.append("[Waiting For Lock ")
			    .append("'" + threadInfo.getLockName() + "'")
			    .append(" Owned By Thread ")
			    .append(threadInfo.getLockName())
			    .append("]\n");
		}
		builder.append("\t[Blocked Count: ")
		    .append(threadInfo.getBlockedCount())
		    .append("     [Blocked Time: ")
		    .append(threadInfo.getBlockedTime())
		    .append("]")
		    .append("\n");
		
		builder.append(" Waited Count  : ")
		    .append(threadInfo.getWaitedCount())
		    .append("\t[Waiting Time: ")
		    .append(threadInfo.getWaitedTime())
		    .append("]")
		    .append("\n");
		
		builder.append(" CPU Usage    : ")
		    .append(cpuTime)
		    .append(" Nano Seconds")
		    .append("\n");
		
		builder.append("\n\n");
		builder.append(" Stack Trace:").append("\n");
		StackTraceElement[] ste = threadInfo.getStackTrace();
		for (StackTraceElement s : ste) {
			builder.append(" ").append(s.toString()).append("\n");
		}
		
		String s = builder.toString();
		builder.delete(0, builder.length());
		
		return s;
	}
}
