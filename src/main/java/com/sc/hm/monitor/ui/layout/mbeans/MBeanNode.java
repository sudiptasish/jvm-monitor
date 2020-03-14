package com.sc.hm.monitor.ui.layout.mbeans;

import javax.swing.tree.DefaultMutableTreeNode;

public class MBeanNode extends DefaultMutableTreeNode {
	
	public static final String NODE_TYPE_ATTRIBUTE = "Attributes";
	public static final String NODE_TYPE_OPERATION = "Operations";
	public static final String NODE_TYPE_MBEAN = "MBean";
	
	private int level = 0;
	
	private MBeansNodeData nodeData = null;
	
	public MBeanNode() {
		super();
	}
	
	public MBeanNode(String name) {
		super(name);
		level = 0;
	}
	
	public MBeanNode(String name, int level) {
		super(name);
		this.level = level;
	}
	
	public MBeanNode(MBeansNodeData data) {
		super(data.getNodeLabel());
		nodeData = data;
	}
	
	public void add(MBeanNode node) {
		node.setLevel(this.level + 1);
		super.add(node);
	}

	public MBeansNodeData getNodeData() {
		return nodeData;
	}

	public void setNodeData(MBeansNodeData nodeData) {
		this.nodeData = nodeData;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
