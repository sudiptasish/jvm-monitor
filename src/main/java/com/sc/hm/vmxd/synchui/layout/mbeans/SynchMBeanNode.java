package com.sc.hm.vmxd.synchui.layout.mbeans;

import java.util.Enumeration;
import java.util.Vector;

import javax.management.ObjectName;
import javax.swing.JComponent;
import javax.swing.tree.TreeNode;

public class SynchMBeanNode implements TreeNode {
	
	private SynchMBeanNode parent = null;

	private ObjectName objectName = null;
	private String node_name = "";
	private JComponent node_data = null;
	
	private boolean allowChildren = true;
	
	private Vector<SynchMBeanNode> children = null;
	private boolean initialized = false;
	
	public SynchMBeanNode(String node_name, JComponent node_data) {
	    this(null, node_name, node_data);
	}
	
	public SynchMBeanNode(ObjectName objectName, String node_name, JComponent node_data) {
		this(objectName, node_name, node_data, true);
	}
	
	public SynchMBeanNode(ObjectName objectName, String node_name, JComponent node_data, boolean allowChildren) {
	    this.parent = null;
        this.objectName = objectName;
		this.node_name = node_name;
		this.node_data = node_data;
		this.allowChildren = allowChildren;
		children = new Vector<SynchMBeanNode>(2);
	}

	/**
     * @return the objectName
     */
    public ObjectName getObjectName() {
        return objectName;
    }

    /**
     * @param objectName the objectName to set
     */
    public void setObjectName(ObjectName objectName) {
        this.objectName = objectName;
    }

    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public JComponent getNode_data() {
		return node_data;
	}

	public void setNode_data(JComponent node_data) {
		this.node_data = node_data;
	}
	
	public boolean isAllowChildren() {
		return allowChildren;
	}

	public void setAllowChildren(boolean allowChildren) {
		this.allowChildren = allowChildren;
	}
	
	public boolean hasChildren() {
		return children.size() > 0;
	}

	public void setParent(SynchMBeanNode parent) {
		this.parent = parent;
	}

	public Enumeration<SynchMBeanNode> children() {
		return children.elements();
	}
	
	public void add(SynchMBeanNode node) throws Exception {
		if (!isAllowChildren()) {
			throw new Exception("Does not allow Children");
		}
		if (node == this) {
			return;
		}
		node.setParent(this);
		children.addElement(node);		
	}
	
	public boolean isDirectDescendentByNodeName(String name) {
		for (SynchMBeanNode mbeanNode : children) {
			if (name.equals(mbeanNode.getNode_name())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDescendentByNodeName(SynchMBeanNode node) {
		return isDescendentByName(this, node.getNode_name());
	}
	
	private boolean isDescendentByName(SynchMBeanNode parent, String nodeName) {
		boolean flag = false;
		for (Enumeration<SynchMBeanNode> enm = parent.children(); enm.hasMoreElements();) {
			SynchMBeanNode node = enm.nextElement();
			if (node.isLeaf()) {
				if (nodeName.equals(node.getNode_name())) {
					flag = true;
					break;
				}
			}
			else {
				return isDescendentByName(node, nodeName);
			}
		}
		return flag;
	}
	
	public boolean isDirectDescendentByNode(SynchMBeanNode node) {
		return children.contains(node);
	}

	public boolean getAllowsChildren() {
		return true;
	}
	
	public SynchMBeanNode getChildByName(String name) {
		for (SynchMBeanNode node : children) {
			if (name.equals(node.getNode_name())) {
				return node;
			}
		}
		return null;
	}

	public TreeNode getChildAt(int childIndex) {
		if (childIndex >= children.size()) {
			return null;
		}
		return children.elementAt(childIndex);
	}

	public int getChildCount() {
		return children.size();
	}

	public int getIndex(TreeNode node) {
		for (int i = children.size() - 1; i >= 0; i --) {
			if (children.elementAt(i) == node) {
				return i;
			}
		}
		return -1;
	}

	public TreeNode getParent() {
		return parent;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public String toString() {
		return node_name;
	}
}
