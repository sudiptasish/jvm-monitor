package com.sc.hm.monitor.ui.layout.mbeans.table;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class MBeanTreeNode {

	private String name = "";
	private int level = 0;
	private Object data = null;
	
	private MBeanTreeNode parent = null;
	private Vector<MBeanTreeNode> children = null;
		
	public MBeanTreeNode() {}
	
	public MBeanTreeNode(String name, Object data) {
		this (name, data, null);
	}
	
	public MBeanTreeNode(String name, Object data, MBeanTreeNode parent) {
		this.parent = parent;
		this.name = name;		
		this.data = data;
		if (this.parent != null) {
			this.parent.addChild(this);
			this.level = this.parent.getLevel() + 1;
		}
		children = new Vector<MBeanTreeNode>();
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Vector<MBeanTreeNode> getChildren() {
		return children;
	}

	public void setChildren(Vector<MBeanTreeNode> children) {
		this.children = children;
	}

	public void addChild(MBeanTreeNode child) {
		this.children.addElement(child);
	}

	public MBeanTreeNode getParent() {
		return parent;
	}

	public void setParent(MBeanTreeNode parent) {
		this.parent = parent;
	}
	
	public boolean equals(Object obj) {
		MBeanTreeNode node = (MBeanTreeNode)obj;
		return node.getName().equals(getName());
	}
	
	public boolean isDirectChild(MBeanTreeNode child) {
		return child.getParent().equals(this);
	}
	
	public boolean isDirectChildByName(String name) {
		for (MBeanTreeNode node : children) {
			if (node.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public MBeanTreeNode getDirectChildNodeByName(String name) {
		MBeanTreeNode child = null;
		for (MBeanTreeNode node : children) {
			if (node.getName().equals(name)) {
				child = node;
				break;
			}
		}
		return child;
	}
	
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	public MBeanTreeNode getFirstChild() {
		if (children.size() > 0) {
			return children.elementAt(0);
		}
		return null;
	}
	
	public Enumeration<MBeanTreeNode> enumerate() {
		return children.elements();
	}
	
	public Set<MBeanTreeNode> getAllChildrenAtSpecifiedLevel(int level) {
		Set<MBeanTreeNode> set = new HashSet<MBeanTreeNode>();
		
		return set;
	}

	public String toString() {
		return name;
	}
}
