package com.sc.hm.monitor.ui.layout.thread;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.shared.threads.ThreadMBeanSharedObject;
import com.sc.hm.monitor.shared.threads.VMThreadInfo;

public class ThreadDetailsPanel extends JPanel implements ActionListener, ListSelectionListener {
	
	private static final long serialVersionUID = 1L;

	private ThreadMBeanSharedObject repos = MBeanSharedObjectRepository.getInstance().getThread_mx_bean();
	
	private int panel_width = 880;
	private int panel_height = 220;
	
	private JList list = null;
	private JTextArea textArea = null;
	private JScrollPane threadListPane = null;
	private JScrollPane threadDetailsPane = null;
	private JButton button = new JButton("Refresh List");
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	private Dimension dim = new Dimension(panel_width, panel_height);

	public ThreadDetailsPanel() throws Exception {
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
	
	public void refreshList() throws Exception {
		String[] threadNames = null;
		synchronized (repos) {
			Long[] threadIds = repos.getAllThreadIds();
			threadNames = new String[threadIds.length];
			for (int i = 0; i < threadIds.length; i ++) {
				VMThreadInfo vmThreadInfo = repos.getThreadInfo(threadIds[i]);
				threadNames[i] = threadIds[i] + ":" + vmThreadInfo.getThreadName();
			}
		}
		synchronized (list) {
			list.setListData(threadNames);
		}
	}
	
	public void initComponent() throws Exception {
		String[] threadNames = null;
		synchronized (repos) {
			Long[] threadIds = repos.getAllThreadIds();
			threadNames = new String[threadIds.length];
			for (int i = 0; i < threadIds.length; i ++) {
				VMThreadInfo vmThreadInfo = repos.getThreadInfo(threadIds[i]);
				threadNames[i] = threadIds[i] + ":" + vmThreadInfo.getThreadName();
			}
		}
		list = new JList(threadNames);
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
	
	public void actionPerformed(ActionEvent ae) {
		try {
			refreshList();
		}
		catch (Exception e) {
			System.err.println("Error!!! Refreshing Thread List.... " + e.getMessage());
		}
	}
	
	public void valueChanged(ListSelectionEvent lse) {
		synchronized (list) {
			Object obj = list.getSelectedValue();
			if (obj != null) {
				String s = obj.toString();
				long id = Long.parseLong(s.substring(0, s.indexOf(":")));
				VMThreadInfo vmThreadInfo = repos.getThreadInfo(id);
				textArea.setText(vmThreadInfo.toString());
			}
		}
	}
}
