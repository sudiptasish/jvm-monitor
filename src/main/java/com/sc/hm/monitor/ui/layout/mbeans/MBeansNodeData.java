package com.sc.hm.monitor.ui.layout.mbeans;

import javax.swing.JComponent;

public class MBeansNodeData {

	private String nodeLabel = "";
	private JComponent table = null;
	
	public MBeansNodeData(String label, JComponent table) {
		nodeLabel = label;
		this.table = table;
	}

	public String getNodeLabel() {
		return nodeLabel;
	}

	public void setNodeLabel(String nodeLabel) {
		this.nodeLabel = nodeLabel;
	}

	public JComponent getTable() {
		return table;
	}

	public void setTable(JComponent table) {
		this.table = table;
	}
	
	public String toString() {
		return nodeLabel;
	}
}
