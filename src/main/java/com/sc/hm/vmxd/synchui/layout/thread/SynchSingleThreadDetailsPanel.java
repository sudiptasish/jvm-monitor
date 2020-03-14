package com.sc.hm.vmxd.synchui.layout.thread;

import java.awt.Dimension;
import java.awt.Font;
import java.lang.management.ThreadInfo;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

public class SynchSingleThreadDetailsPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private int panel_width = 880;
	private int panel_height = 270;
	
	private JTextArea textArea = null;
	private JScrollPane threadDetailsPane = null;
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	private Dimension dim = new Dimension(panel_width, panel_height);
	
	private final StringBuilder builder = new StringBuilder(1000);
	
	public SynchSingleThreadDetailsPanel() throws Exception {
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
		textArea = new JTextArea(30, 12);
		textArea.setEditable(false);
		textArea.setLineWrap(false);
		textArea.setFont(font);
		
		threadDetailsPane = new JScrollPane(textArea);
		threadDetailsPane.setBounds(60, 5, 790, 205);
		
		add(threadDetailsPane);
	}
	
	public void initOther() {}
	
	public void displayThreadDetails(ThreadInfo threadInfo) {
	    textArea.setText(dumpThreadInfo(threadInfo));
	}
	
	private String dumpThreadInfo(ThreadInfo threadInfo) {	    
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
