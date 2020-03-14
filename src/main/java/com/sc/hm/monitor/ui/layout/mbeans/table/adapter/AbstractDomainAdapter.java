package com.sc.hm.monitor.ui.layout.mbeans.table.adapter;

import java.awt.Font;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ObjectName;

import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.ext.jmx.AbstractMXBeanServerHandler;
import com.sc.hm.monitor.ext.jmx.MXBeanServerHandler;
import com.sc.hm.monitor.ui.layout.mbeans.tab.OperationActionButton;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.ManagementBeanOperationTableModel;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.ManagementBeanTableModel;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.common.MBeanTableModel;
import com.sc.hm.monitor.ui.tree.MXBeanTableColumnValueAttribute;
import com.sc.hm.monitor.util.ManagementUtil;

public abstract class AbstractDomainAdapter {
	
	protected AbstractMXBeanServerHandler mxBeanServerHandler = null;

	public AbstractDomainAdapter() {}
	
	protected void configureMXBeanServerHandler(String host, int port) {
		mxBeanServerHandler = new MXBeanServerHandler();
		mxBeanServerHandler.initializeMBeanServer(host, port);
	}
	
	protected void configureMXBeanServerHandler(String jmxURL) {
		mxBeanServerHandler = new MXBeanServerHandler();
		mxBeanServerHandler.initializeMBeanServer(jmxURL);
	}
	
	public abstract void initializeRemoteMXBeanInfo() throws Exception;
	
	protected MBeanTableModel createEquivalentAttributeValueTableModel(ObjectName objectName, MBeanAttributeInfo[] attributeInfos) {
		Map<Object, Object> attributeValueMap = new LinkedHashMap<Object, Object>();
		MBeanTableModel attributeValueTableModel = null;
		
		try {
			int index = 0;
			for (MBeanAttributeInfo attributeInfo : attributeInfos) {
				String name = attributeInfo.getName();
				Object attributeValue = null;
				try {					
					attributeValue = mxBeanServerHandler.getManagedMBeanServer().getRemoteMBeanAttributeValue(objectName, name);
					if (attributeValue == null) {
						attributeValue = new String("Unavailable");
					}
				}
				catch (Exception e) {
					System.err.println(e.getMessage() + " :: " + name);
					attributeValue = new String("Unavailable");
				}
				boolean editable = checkForPreDefinedEditableColumn(objectName, name);
				attributeValueMap.put(name, new MXBeanTableColumnValueAttribute(name, index, attributeValue.getClass(), attributeValue, editable));
				index ++;
			}
			attributeValueTableModel = new ManagementBeanTableModel(null);
			attributeValueTableModel.initializeTableRowData(objectName, attributeValueMap);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return attributeValueTableModel;
	}
	
	protected MBeanTableModel createEquivalentOperationValueTableModel(ObjectName objectName, MBeanOperationInfo[] operationInfos) {
		Map<Object, Object> operationValueMap = new LinkedHashMap<Object, Object>();
		MBeanTableModel operationValueTableModel = null;
		
		try {
			Font font = new Font("Arial", Font.PLAIN, 11);
			int index = 0;
			for (MBeanOperationInfo operationInfo : operationInfos) {
				String name = operationInfo.getReturnType();
				OperationActionButton button = new OperationActionButton(operationInfo.getName(), name, objectName);
				button.setFont(font);
				Object operationValue = button;
				//String operationValue = operationInfo.getName() + "-" + name + "-" + objectName.toString();
				operationValueMap.put(name, new MXBeanTableColumnValueAttribute(name, index, operationValue.getClass(), operationValue, false));
				index ++;
			}
			operationValueTableModel = new ManagementBeanOperationTableModel(null);
			operationValueTableModel.initializeTableRowData(objectName, operationValueMap);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return operationValueTableModel;
	}
	
	protected boolean checkForPreDefinedEditableColumn(ObjectName objectName, String columnName) {
		return MXBeanAdapterDataCache.isEditableAttributeByName(objectName, columnName);
	}
	
	protected void populateEditableColumns(String domain, Set<ObjectName> names) {
		if (domain.equalsIgnoreCase("java.lang")) {
			MXBeanAdapterDataCache.checkAndAddEditableAttribute(names);
		}
	}
	
	public void invokeAttributeChanged(ObjectName objectName, String attributeName, Class attributeValueClass, Object attributeValue) throws Exception {
		Attribute attribute = null;
		if (attributeValueClass.getName().equals("java.lang.String")) {
			attribute = new Attribute(attributeName, (String)attributeValue);
		}
		else if (attributeValueClass.getName().equals("java.lang.Long")) {
			attribute = new Attribute(attributeName, new Long((String)attributeValue));
		}
		else if (attributeValueClass.getName().equals("java.lang.Integer")) {
			attribute = new Attribute(attributeName, new Integer((String)attributeValue));
		}
		else if (attributeValueClass.getName().equals("java.lang.Float")) {
			attribute = new Attribute(attributeName, new Float((String)attributeValue));
		}
		else if (attributeValueClass.getName().equals("java.lang.Double")) {
			attribute = new Attribute(attributeName, new Double((String)attributeValue));
		}
		else if (attributeValueClass.getName().equals("java.lang.Boolean")) {
			attribute = new Attribute(attributeName, new Boolean((String)attributeValue));
		}
		else {
			attribute = new Attribute(attributeName, (Object)attributeValue);
		}				
		
		if (ManagementUtil.JVM_TYPE_LOCAL.equals(ApplicationConfiguration.getInstance().getProperty("JVM_TYPE", ManagementUtil.JVM_TYPE_LOCAL))) {
			ManagementFactory.getPlatformMBeanServer().setAttribute(objectName, attribute);
		}
		else {
			mxBeanServerHandler.getManagedMBeanServer().setRemoteMBeanAttribute(objectName, attribute);
		}
	}
	
	public Object invokeOperation(ObjectName objectName, String operationName, Object[] params) throws Exception {
		String[] signature = null;
		if (params != null) {
			signature = new String[params.length];
			for (int i = 0; i < params.length; i ++) {
				signature[i] = params[i].getClass().getName();
			}
		}
		if (ManagementUtil.JVM_TYPE_LOCAL.equals(ApplicationConfiguration.getInstance().getProperty("JVM_TYPE", ManagementUtil.JVM_TYPE_LOCAL))) {
			return ManagementFactory.getPlatformMBeanServer().invoke(objectName, operationName, params, signature);
		}
		else {
			return mxBeanServerHandler.getManagedMBeanServer().invokeRemoteMBeanOperation(objectName, operationName, params, signature);
		}
	}
	
	public Object invokeOperation(ObjectName objectName, String operationName, Object[] params, String[] signature) throws Exception {
		if (ManagementUtil.JVM_TYPE_LOCAL.equals(ApplicationConfiguration.getInstance().getProperty("JVM_TYPE", ManagementUtil.JVM_TYPE_LOCAL))) {
			return ManagementFactory.getPlatformMBeanServer().invoke(objectName, operationName, params, signature);
		}
		else {
			return mxBeanServerHandler.getManagedMBeanServer().invokeRemoteMBeanOperation(objectName, operationName, params, signature);
		}
	}
}
