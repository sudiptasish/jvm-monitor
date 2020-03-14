package com.sc.hm.monitor.ui.layout.mbeans.table.adapter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.dialog.MBeanOperationDialog;
import com.sc.hm.monitor.ui.layout.mbeans.MBeanNode;
import com.sc.hm.monitor.ui.layout.mbeans.handler.CompositeDataHandler;
import com.sc.hm.monitor.ui.layout.mbeans.tab.OperationActionButton;
import com.sc.hm.monitor.ui.layout.mbeans.tab.OperationTextField;
import com.sc.hm.monitor.ui.layout.mbeans.table.MBeanTreeNode;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.ManagementBeanTableModel;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.common.AttributeInfoTableModel;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.common.MBeanTableModel;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.common.OperationInfoTableModel;
import com.sc.hm.monitor.ui.tree.MXBeanTableColumnValueAttribute;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.ManagementUtil;

public class MXBeanDomainAdapter extends AbstractDomainAdapter {
	
	public MXBeanDomainAdapter() {
		super();
	}
	
	public void configureMXBeanServerHandler(String host, String port) {
		super.configureMXBeanServerHandler(host, Integer.parseInt(port));
	}
	
	public void configureMXBeanServerHandler(String jmxURL) {
		super.configureMXBeanServerHandler(jmxURL);
	}
	
	public void refreshRemoteMXBeanInfo() throws Exception {
		if (ManagementUtil.JVM_TYPE_LOCAL.equals(ApplicationConfiguration.getInstance().getProperty("JVM_TYPE", ManagementUtil.JVM_TYPE_LOCAL))) {
			initializeLocalMXBeanInfo();
		}
		else {
			initializeRemoteMXBeanInfo();
		}
	}
	
	public void refreshRemoteMXBeanInfoByName(ObjectName objectName) throws Exception {
		if (ManagementUtil.JVM_TYPE_LOCAL.equals(ApplicationConfiguration.getInstance().getProperty("JVM_TYPE", ManagementUtil.JVM_TYPE_LOCAL))) {
			//initializeLocalMXBeanInfoByName(objectName);
		}
		else {
			initializeRemoteMXBeanInfoByName(objectName);
		}
	}
	
	public Set<ObjectName> getLocalMBeanByDomainName(MBeanServer mbeanServer, String domainName) {
		ObjectName objectName = null;
		try {
			objectName = new ObjectName(domainName + ":*");
		}
		catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		return mbeanServer.queryNames(objectName, null);
	}
	
	public MBeanInfo getLocalMBeanInfo(MBeanServer mbeanServer, ObjectName objectName) {
		MBeanInfo mbeanInfo = null;
		try {
			mbeanInfo = mbeanServer.getMBeanInfo(objectName);
		}
		catch (InstanceNotFoundException e) {
			e.printStackTrace();
		}
		catch (IntrospectionException e) {
			e.printStackTrace();
		}
		catch (ReflectionException e) {
			e.printStackTrace();
		}
		return mbeanInfo;
	}
	
	public MBeanTableModel createEquivalentAttributeValueTableModelForLocalMBean(MBeanServer mbeanServer, ObjectName objectName, MBeanAttributeInfo[] attributeInfos) {
		Map<Object, Object> attributeValueMap = new LinkedHashMap<Object, Object>();
		MBeanTableModel attributeValueTableModel = null;
		
		try {
			int index = 0;
			for (MBeanAttributeInfo attributeInfo : attributeInfos) {
				String name = attributeInfo.getName();
				Object attributeValue = null;
				try {					
					attributeValue = mbeanServer.getAttribute(objectName, name);
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
	
	public void initializeLocalMXBeanInfo() throws Exception {
		Font font = new Font("Arial", Font.PLAIN, 11);
		try {
			MBeanTreeNode tree = new MBeanTreeNode("Management Beans", null, null);
			MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
							
			String[] domains = mbeanServer.getDomains();
			
			for (String domain : domains) {
				MBeanTreeNode domainNode = new MBeanTreeNode(domain, null, tree);								
				Set<ObjectName> names = getLocalMBeanByDomainName(mbeanServer, domain);
				MBeanTreeNode typeNode = null;
				
				populateEditableColumns(domain, names);
				
				for (ObjectName objectName : names) {
					String type = objectName.getKeyProperty("type");
					if (domainNode.isDirectChildByName(type)) {
						typeNode = domainNode.getDirectChildNodeByName(type);
					}
					else {
						typeNode = new MBeanTreeNode(type, null, domainNode);
					}
					
					if (objectName.getKeyProperty("name") != null) {
						typeNode = new MBeanTreeNode(objectName.getKeyProperty("name"), null, typeNode);
					}
					MBeanInfo mbeanInfo = getLocalMBeanInfo(mbeanServer, objectName);
					MBeanAttributeInfo[] attributeInfos = mbeanInfo.getAttributes();
					MBeanOperationInfo[] operationInfos = mbeanInfo.getOperations();
					
					// Get Attribute Values [consolidated]
					MBeanTableModel attributeValueModel = createEquivalentAttributeValueTableModelForLocalMBean(mbeanServer, objectName, attributeInfos);
					
					// Got Operation Values [consolidated] - New
					//MBeanTableModel attributeValueModel_o = createEquivalentOperationValueTableModel(objectName, operationInfos);
					final JPanel panel = new JPanel();
					panel.setSize(new Dimension(680, 500));
					panel.setLayout(null);
					for (int i = 0; i < operationInfos.length; i ++) {
						MBeanParameterInfo[] paramInfos = operationInfos[i].getSignature();
						String returnType = operationInfos[i].getReturnType();
						returnType = returnType.indexOf(".") >= 0 ? returnType.substring(returnType.lastIndexOf(".") + 1) : returnType;
						
						JLabel label = new JLabel(returnType);
						label.setFont(font);
						label.setBounds(30, 20 + (i + 1) * 30, 100, 18);
						
						OperationActionButton button = new OperationActionButton(operationInfos[i].getName(), returnType, objectName);
						button.setFont(font);
						button.setBounds(30 + 100 + 20, 20 + (i + 1) * 30, 200, 18);
						
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								try {
									MBeanOperationDialog opDialog = MBeanOperationDialog.getMBeanOperationDialog();
									Object[] params = null;
									String[] signatures = null;
									
									OperationActionButton opActionButton = (OperationActionButton)ae.getSource();
									OperationTextField[] textFields = opActionButton.getTextFields();
									if (textFields != null && textFields.length > 0) {
										params = new Object[textFields.length];
										signatures = new String[textFields.length];
										
										for (int i = 0; i < textFields.length; i ++) {
											if ("".equals(textFields[i])) {
												JOptionPane.showMessageDialog(panel, "All Fields Are Mandetory");
											}
											params[i] = textFields[i].getParameter();
											signatures[i] = textFields[i].getTextDataType();
											Logger.log(new StringBuilder().append("Param ").append(i).append(": ").append(params[i]).append(". Signature ").append(i).append(": ").append(signatures[i]).toString());
										}
									}
									try {
										Logger.log("Invoking " + opActionButton.getObjectName().toString() + "'s method " + opActionButton.getText());
										Object obj = invokeOperation(opActionButton.getObjectName(), opActionButton.getText(), params, signatures);
										if (obj != null) {
											if (obj instanceof CompositeData) {
												CompositeDataHandler compDataHandler = new CompositeDataHandler(opActionButton.getText());
												compDataHandler.handleData(new CompositeData[] {(CompositeData)obj});
											}
											else if (obj instanceof CompositeData[]) {
												CompositeDataHandler compDataHandler = new CompositeDataHandler(opActionButton.getText());
												compDataHandler.handleData((CompositeData[])obj);
											}
											else {
												opDialog.showMBeanOperationDialog("Method Successfully Invoked....", "Result: " + obj.toString());
											}
										}
										else {
											opDialog.showMBeanOperationDialog("Method Successfully Invoked....", "None");
										}
									}
									catch (Exception e) {
										e.printStackTrace();
										opDialog.showMBeanOperationDialog("Error Invoking Method....", e.getMessage());
									}
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						
						panel.add(label);
						panel.add(button);
						
						if (paramInfos != null) {
							OperationTextField[] textFields = new OperationTextField[paramInfos.length];
							for (int j = 0; j < paramInfos.length; j ++) {
								textFields[j] = new OperationTextField(paramInfos[j].getType());
								textFields[j].setFont(font);
								textFields[j].setBounds(30 + 100 + 20 + 200 + 30 + j * (15 + 80), 20 + (i + 1) * 30, 80, 18);
								panel.add(textFields[j]);								
							}
							button.setTextFields(textFields);
						}
					}
					
					// Create Attributes Node
					MBeanTreeNode attributesNode = new MBeanTreeNode(MBeanNode.NODE_TYPE_ATTRIBUTE, attributeValueModel, typeNode);
					
					for (MBeanAttributeInfo attributeInfo : attributeInfos) {
						MBeanTableModel attributeInfoModel = createEquivalentAttributeInfoTableModel(objectName, attributeInfo);
						MBeanTreeNode attributeNode = new MBeanTreeNode(attributeInfo.getName(), attributeInfoModel, attributesNode);
					}
					
					if (operationInfos.length > 0) {
						MBeanTreeNode operationsNode = new MBeanTreeNode(MBeanNode.NODE_TYPE_OPERATION, panel, typeNode);
						
						for (MBeanOperationInfo operationInfo : operationInfos) {
							MBeanTableModel operationModel = createEquivalentOperationTableModel(objectName, operationInfo);
							MBeanTreeNode operationNode = new MBeanTreeNode(operationInfo.getName(), operationModel, operationsNode);
						}						
					}
				}
			}
			MXBeanAdapterDataCache.putMBeanTreeStructure(tree);
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public void initializeRemoteMXBeanInfo() throws Exception {
		Font font = new Font("Arial", Font.PLAIN, 11);
		try {
			MBeanTreeNode tree = new MBeanTreeNode("Management Beans", null, null);
			String[] domains = mxBeanServerHandler.getManagedMBeanServer().getAllDomains();
			
			for (String domain : domains) {
				MBeanTreeNode domainNode = new MBeanTreeNode(domain, null, tree);								
				Set<ObjectName> names = mxBeanServerHandler.getManagedMBeanServer().getRemoteMBeanByDomainName(domain);
				MBeanTreeNode typeNode = null;
				
				populateEditableColumns(domain, names);
				
				for (ObjectName objectName : names) {
					String type = objectName.getKeyProperty("type");
					if (type == null) {
						type = objectName.getKeyProperty("Type");
					}
					if (domainNode.isDirectChildByName(type)) {
						typeNode = domainNode.getDirectChildNodeByName(type);
					}
					else {
						typeNode = new MBeanTreeNode(type, null, domainNode);
					}
					
					String name = objectName.getKeyProperty("name");
					if (type == null) {
						type = objectName.getKeyProperty("Name");
					}
					if (name != null) {
						typeNode = new MBeanTreeNode(name, null, typeNode);
					}
					MBeanInfo mbeanInfo = getRemoteMXBeanInfo(objectName);
					MBeanAttributeInfo[] attributeInfos = mbeanInfo.getAttributes();
					MBeanOperationInfo[] operationInfos = mbeanInfo.getOperations();
					
					// Get Attribute Values [consolidated]
					MBeanTableModel attributeValueModel = createEquivalentAttributeValueTableModel(objectName, attributeInfos);
					
					// Got Operation Values [consolidated] - New
					final JPanel panel = new JPanel();
					panel.setSize(new Dimension(680, 500));
					panel.setLayout(null);
					for (int i = 0; i < operationInfos.length; i ++) {
						MBeanParameterInfo[] paramInfos = operationInfos[i].getSignature();
						String returnType = operationInfos[i].getReturnType();
						returnType = returnType.indexOf(".") >= 0 ? returnType.substring(returnType.lastIndexOf(".") + 1) : returnType;
						
						JLabel label = new JLabel(returnType);
						label.setFont(font);
						label.setBounds(30, 20 + (i + 1) * 30, 100, 18);
						
						OperationActionButton button = new OperationActionButton(operationInfos[i].getName(), returnType, objectName);
						button.setFont(font);
						button.setBounds(30 + 100 + 20, 20 + (i + 1) * 30, 200, 18);
						
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								try {
									MBeanOperationDialog opDialog = MBeanOperationDialog.getMBeanOperationDialog();
									Object[] params = null;
									String[] signatures = null;
									
									OperationActionButton opActionButton = (OperationActionButton)ae.getSource();
									OperationTextField[] textFields = opActionButton.getTextFields();
									if (textFields != null && textFields.length > 0) {
										params = new Object[textFields.length];
										signatures = new String[textFields.length];
										
										for (int i = 0; i < textFields.length; i ++) {
											if ("".equals(textFields[i])) {
												JOptionPane.showMessageDialog(panel, "All Fields Are Mandetory");
											}
											params[i] = textFields[i].getParameter();
											signatures[i] = textFields[i].getTextDataType();
											Logger.log(new StringBuilder().append("Param ").append(i).append(": ").append(params[i]).append(". Signature ").append(i).append(": ").append(signatures[i]).toString());
										}
									}
									try {
										Logger.log("Invoking " + opActionButton.getObjectName().toString() + "'s method " + opActionButton.getText());
										Object obj = invokeOperation(opActionButton.getObjectName(), opActionButton.getText(), params, signatures);
										if (obj != null) {
											if (obj instanceof CompositeData) {
												CompositeDataHandler compDataHandler = new CompositeDataHandler(opActionButton.getText());
												compDataHandler.handleData(new CompositeData[] {(CompositeData)obj});
											}
											else if (obj instanceof CompositeData[]) {
												CompositeDataHandler compDataHandler = new CompositeDataHandler(opActionButton.getText());
												compDataHandler.handleData((CompositeData[])obj);
											}
											else {
												opDialog.showMBeanOperationDialog("Method Successfully Invoked....", "Result: " + obj.toString());
											}
										}
										else {
											opDialog.showMBeanOperationDialog("Method Successfully Invoked....", "None");
										}
									}
									catch (Exception e) {
										e.printStackTrace();
										opDialog.showMBeanOperationDialog("Error Invoking Method....", e.getMessage());
									}
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						
						panel.add(label);
						panel.add(button);
						
						if (paramInfos != null) {
							OperationTextField[] textFields = new OperationTextField[paramInfos.length];
							for (int j = 0; j < paramInfos.length; j ++) {
								textFields[j] = new OperationTextField(paramInfos[j].getType());
								textFields[j].setFont(font);
								textFields[j].setBounds(30 + 100 + 20 + 200 + 30 + j * (15 + 80), 20 + (i + 1) * 30, 80, 18);
								panel.add(textFields[j]);								
							}
							button.setTextFields(textFields);
						}
					}
					
					// Create Attributes Node
					MBeanTreeNode attributesNode = new MBeanTreeNode(MBeanNode.NODE_TYPE_ATTRIBUTE, attributeValueModel, typeNode);
					
					for (MBeanAttributeInfo attributeInfo : attributeInfos) {
						MBeanTableModel attributeInfoModel = createEquivalentAttributeInfoTableModel(objectName, attributeInfo);
						MBeanTreeNode attributeNode = new MBeanTreeNode(attributeInfo.getName(), attributeInfoModel, attributesNode);
					}
					
					if (operationInfos.length > 0) {
						MBeanTreeNode operationsNode = new MBeanTreeNode(MBeanNode.NODE_TYPE_OPERATION, panel, typeNode);
						
						for (MBeanOperationInfo operationInfo : operationInfos) {
							MBeanTableModel operationModel = createEquivalentOperationTableModel(objectName, operationInfo);
							MBeanTreeNode operationNode = new MBeanTreeNode(operationInfo.getName(), operationModel, operationsNode);
						}						
					}
				}
			}
			MXBeanAdapterDataCache.putMBeanTreeStructure(tree);
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public void initializeRemoteMXBeanInfoByName(ObjectName objectName) throws Exception {
		MBeanInfo mbeanInfo = getRemoteMXBeanInfo(objectName);
		MBeanAttributeInfo[] attributeInfos = mbeanInfo.getAttributes();
		
		MBeanTableModel attributeValueModel = createEquivalentAttributeValueTableModel(objectName, attributeInfos);
	}
	
	public Set<ObjectName> getObjectNamesByDomain(String domainName) throws IOException, MalformedObjectNameException {
		return mxBeanServerHandler.getManagedMBeanServer().getRemoteMBeanByDomainName(domainName);
	}

	private MBeanInfo getRemoteMXBeanInfo(ObjectName objectName) {
		MBeanInfo mbeanInfo = null;
		
		try {
			mbeanInfo = mxBeanServerHandler.getManagedMBeanServer().getRemoteMBeanInfo(objectName);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return mbeanInfo;
	}
	
	private MBeanTableModel createEquivalentAttributeInfoTableModel(ObjectName objectName, MBeanAttributeInfo attributeInfo) {
		Map<Object, Object> attributeInfoMap = new LinkedHashMap<Object, Object>();
		MBeanTableModel attributeInfoTableModel = null;
		
		try {
			attributeInfoMap.put("Name", attributeInfo.getName());
			attributeInfoMap.put("Description", attributeInfo.getDescription());
			attributeInfoMap.put("Type", attributeInfo.getType());
			attributeInfoMap.put("Is", attributeInfo.isIs());
			attributeInfoMap.put("Readable", attributeInfo.isReadable());
			attributeInfoMap.put("Writable", attributeInfo.isWritable());
			
			attributeInfoTableModel = new AttributeInfoTableModel(attributeInfo.getName());
			attributeInfoTableModel.initializeTableRowData(attributeInfoMap);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return attributeInfoTableModel;
	}
	
	private MBeanTableModel createEquivalentOperationTableModel(ObjectName objectName, MBeanOperationInfo operationInfo) {
		Map<Object, Object> operationInfoMap = new LinkedHashMap<Object, Object>();
		MBeanTableModel operationTableModel = null;
		
		try {
			operationInfoMap.put("Name", operationInfo.getName());
			operationInfoMap.put("Description", operationInfo.getDescription());
			operationInfoMap.put("ReturnType", operationInfo.getReturnType());
			operationInfoMap.put("Signature", operationInfo.getSignature());
			operationInfoMap.put("Impact", operationInfo.getImpact());
				
			operationTableModel = new OperationInfoTableModel(operationInfo.getName());
			operationTableModel.initializeTableRowData(operationInfoMap);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return operationTableModel;
	}
	
	public String[] getAllLiveDomains() throws IOException {
		return mxBeanServerHandler.getManagedMBeanServer().getAllDomains();
	}
}
