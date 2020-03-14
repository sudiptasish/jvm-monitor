package com.sc.hm.monitor.ui.layout.mbeans.table.adapter;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.management.ObjectName;

import com.sc.hm.monitor.ui.layout.mbeans.table.MBeanTreeNode;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.common.AttributeInfoTableModel;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.common.MBeanTableModel;

public class MXBeanAdapterDataCache {

	private static Vector<MBeanTreeNode> mbeanTree = new Vector<MBeanTreeNode>();
	private static Map<String, MBeanTableModel> map = new HashMap<String, MBeanTableModel>();
	private static boolean editableAttributeConfiguration = false;
	private static Map<String, List<String>> editableAttributeList = new HashMap<String, List<String>>();
	
	public static void putMBeanTreeStructure(MBeanTreeNode tree) {
		mbeanTree.clear();
		mbeanTree.addElement(tree);
	}
	
	public static synchronized void replaceInternalTableModelofMBeansTreeNode(MBeanTableModel tableModel) {
		MBeanTreeNode mbeanNode = mbeanTree.elementAt(0);
		MBeanTreeNode childNodeNode = mbeanNode.getFirstChild();
		
	}
	
	public static synchronized void checkAndAddEditableAttribute(Set<ObjectName> objectNames) {
		if (isCompleteEditableAttributeConfiguration()) {
			return;
		}
		for (ObjectName objectName : objectNames) {
			String name = objectName.getCanonicalName();
			if (name.equalsIgnoreCase(ManagementFactory.CLASS_LOADING_MXBEAN_NAME)) {
				List<String> list = new ArrayList<String>();
				list.add("Verbose");
				editableAttributeList.put(name, list);
			}
			else if (name.equalsIgnoreCase(ManagementFactory.MEMORY_MXBEAN_NAME)) {
				List<String> list = new ArrayList<String>();
				list.add("Verbose");
				editableAttributeList.put(name, list);
			}
			else if (name.startsWith(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE) || objectName.getKeyProperty("type").equalsIgnoreCase("MemoryPool")) {
				List<String> list = new ArrayList<String>();
				list.add("UsageThreshold");
				list.add("CollectionUsageThreshold");
				editableAttributeList.put(name, list);
			}
			else if (name.equalsIgnoreCase(ManagementFactory.THREAD_MXBEAN_NAME)) {
				List<String> list = new ArrayList<String>();
				list.add("ThreadContentionMonitoringEnabled");
				list.add("ThreadCpuTimeEnabled");
				editableAttributeList.put(name, list);
			}
		}
		setEditableAttributeConfiguration(true);
	}
	
	public static synchronized boolean isEditableAttributeByName(ObjectName objectName, String columnName) {
		if (!editableAttributeList.containsKey(objectName.getCanonicalName()) || !objectName.getDomain().equalsIgnoreCase("java.lang")) {
			return false;
		}
		String name = objectName.getCanonicalName();
		List<String> list = editableAttributeList.get(name);
		if (list.contains(columnName)) {
			return true;
		}
		return false;
	}
	
	public static MBeanTreeNode getDefaultMBeanTreeStructure() {
		if (mbeanTree.size() == 0) {
			return new MBeanTreeNode("MBeans", new AttributeInfoTableModel("Un-Initialized"));
		}
		return mbeanTree.elementAt(mbeanTree.size() - 1);
	}
	
	public static void putTableModelData(String nodeName, MBeanTableModel model) {
		map.put(nodeName, model);
	}
	
	public static MBeanTableModel getTableModelData(String nodeName) {
		return map.get(nodeName);
	}
	
	public static MBeanTableModel[] getTableModelDataByDomainTypeOrSubTypeKey(String key) {
		Vector<MBeanTableModel> v = new Vector<MBeanTableModel>();
		for (Iterator<Map.Entry<String, MBeanTableModel>> itr = map.entrySet().iterator(); itr.hasNext();) {
			Map.Entry<String, MBeanTableModel> me = itr.next();
			if (me.getKey().startsWith(key)) {
				v.addElement(me.getValue());
			}
		}
		return (MBeanTableModel[])v.toArray(new MBeanTableModel[v.size()]);
	}
	
	public static boolean isCompleteEditableAttributeConfiguration() {
		return editableAttributeConfiguration;
	}

	public static void setEditableAttributeConfiguration(boolean editableAttributeConfiguration) {
		MXBeanAdapterDataCache.editableAttributeConfiguration = editableAttributeConfiguration;
	}
}
