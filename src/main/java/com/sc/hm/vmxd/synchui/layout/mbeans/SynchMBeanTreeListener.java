/* $Header: SynchMBeanTreeListener.java Jan 20, 2017 schanda  Exp $ */

/* Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved. */

/*
   DESCRIPTION
    <short description of component this file declares/defines>

   PRIVATE CLASSES
    <list of private classes defined - with one-line descriptions>

   NOTES
    <other useful comments, qualifications, etc.>

   MODIFIED    (MM/DD/YY)
    schanda     Jan 20, 2017 - Creation
 */

/**
 * @version $Header: SynchMBeanTreeListener.java Jan 20, 2017 schanda  Exp $
 * @author  schanda
 * @since   release specific (what release of product did this appear in)
 */

package com.sc.hm.vmxd.synchui.layout.mbeans;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectName;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;

import com.sc.hm.monitor.ui.layout.mbeans.TableDataListener;
import com.sc.hm.monitor.ui.layout.mbeans.tab.OperationActionButton;
import com.sc.hm.monitor.ui.layout.mbeans.tab.OperationTextField;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;
import com.sc.hm.vmxd.synchui.layout.mbeans.model.SynchAttributeInfoTableModel;
import com.sc.hm.vmxd.synchui.layout.mbeans.model.SynchAttributeValueTableModel;
import com.sc.hm.vmxd.synchui.layout.mbeans.model.SynchMBeansTableModel;
import com.sc.hm.vmxd.synchui.layout.mbeans.model.SynchOperationInfoTableModel;

public class SynchMBeanTreeListener extends MouseAdapter {
    
    private final String application;
    private final JTree mbeans_tree;
    private final JSplitPane splitPane;
    
    public SynchMBeanTreeListener(String application, JTree mbeans_tree, JSplitPane splitPane) {
        this.application = application;
        this.mbeans_tree = mbeans_tree;
        this.splitPane = splitPane;
    }

    public void mouseClicked(MouseEvent me) {
        if (mbeans_tree.getSelectionPath() != null) {
            SynchMBeanNode mbeanNode = (SynchMBeanNode)mbeans_tree.getSelectionPath().getLastPathComponent();
            Logger.log(mbeanNode);
            
            // Now make a remote call to populate the attribute/operation info
            if (mbeanNode.getObjectName() != null && !mbeanNode.isInitialized()) {
                try {
                    final MXBeanServer mxbeanServer = MXBeanServerManager.getMXBeanServer(application);
                    MBeanInfo mbeanInfo = mxbeanServer.getMBeanInfo(mbeanNode.getObjectName());
                    MBeanAttributeInfo[] mbeanAttributes = mbeanInfo.getAttributes();
                    MBeanOperationInfo[] mbeanOperations = mbeanInfo.getOperations();
                    MBeanNotificationInfo[] mbeanNotifications = mbeanInfo.getNotifications();
                    
                    if (mbeanAttributes.length > 0) {
                        populateAttributeInfo(mbeanNode.getObjectName(), mbeanNode, mbeanAttributes);
                    }
                    if (mbeanOperations.length > 0) {
                        populateOperationInfo(mbeanNode.getObjectName(), mbeanNode, mbeanOperations);
                    }
                    if (mbeanOperations.length > 0) {
                        populateNotificationInfo(mbeanNode, mbeanNotifications, (JPanel)mbeanNode.getNode_data());
                    }
                    mbeanNode.setInitialized(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mbeanNode.getNode_name().equals("Attributes")) {
                Map<Object, Object> attributeValueMap = new LinkedHashMap<Object, Object>();
                SynchMBeansTableModel tableModel = (SynchMBeansTableModel)((JTable)mbeanNode.getNode_data()).getModel();
                String[] mbeanAttributes = ((SynchAttributeValueTableModel)tableModel).getAttributeNames();
                
                try {
                    ObjectName mbean_name = new ObjectName(tableModel.getLabel());
                    Object attributeValue = null;
                    final MXBeanServer mxbeanServer = MXBeanServerManager.getMXBeanServer(application);
                    
                    AttributeList attributeList = mxbeanServer.getAttributeList(mbean_name, mbeanAttributes);
                    
                    for (int i = 0; i < attributeList.size(); i ++) {
                        Attribute attribute = (Attribute)attributeList.get(i);
                        attributeValue = attribute.getValue();
                        if (attributeValue == null) {
                            attributeValue = new String("Unavailable");
                        }
                        attributeValueMap.put(attribute.getName(), attributeValue);                        
                    }
                    tableModel.setTableModelData(attributeValueMap);
                    //SwingUtilities.updateComponentTreeUI(component);
                    splitPane.setRightComponent(new JScrollPane(mbeanNode.getNode_data()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (mbeanNode.getNode_data() != null) {
                splitPane.setRightComponent(new JScrollPane(mbeanNode.getNode_data()));
            }
        }
    }
    
    private void populateAttributeInfo(final ObjectName mbean_name, SynchMBeanNode parent, MBeanAttributeInfo[] mbeanAttributes) throws Exception {
        Font font = new Font("Arial", Font.PLAIN, 11);
        
        SynchMBeansTableModel attrValueModel = new SynchAttributeValueTableModel(mbean_name.toString());
        JTable attributeValueTable = new JTable(attrValueModel);
        attributeValueTable.setBounds(5, 5, 650, 580);
        attributeValueTable.setFont(font);
        attributeValueTable.addMouseListener(new TableDataListener());
        
        SynchMBeanNode attributeNode = new SynchMBeanNode("Attributes", attributeValueTable);
        parent.add(attributeNode);
        
        Map<Object, Object> attributeValueMap = new LinkedHashMap<Object, Object>();
        
        JTable attributeInfoTable = null;
        Map<Object, Object> attributeInfoMap = new LinkedHashMap<Object, Object>();
        
        for (int i = 0; i < mbeanAttributes.length; i ++) {
            attributeInfoMap.put("Name", mbeanAttributes[i].getName());
            attributeInfoMap.put("Description", mbeanAttributes[i].getDescription());
            attributeInfoMap.put("Type", mbeanAttributes[i].getType());
            attributeInfoMap.put("Readable", mbeanAttributes[i].isReadable());
            attributeInfoMap.put("Writable", mbeanAttributes[i].isWritable());
            
            SynchMBeansTableModel tableModel = new SynchAttributeInfoTableModel("AttributeInfo");
            tableModel.setTableModelData(attributeInfoMap);
            attributeInfoTable = new JTable(tableModel);
            attributeInfoTable.setBounds(5, 5, 650, 580);
            attributeInfoTable.setFont(font);
            
            attributeValueMap.put(mbeanAttributes[i].getName(), "");
            
            attributeNode.add(new SynchMBeanNode(mbeanAttributes[i].getName(), attributeInfoTable));
        }
        attrValueModel.setTableModelData(attributeValueMap);        
    }
    
    private void populateOperationInfo(final ObjectName mbean_name, SynchMBeanNode parent, MBeanOperationInfo[] mbeanOperations) throws Exception {
        Font font = new Font("Arial", Font.PLAIN, 11);
        
        JPanel panel = new JPanel();
        panel.setSize(new Dimension(650, 580));
        panel.setLayout(null);
        
        SynchMBeanNode operationNode = new SynchMBeanNode("Operations", panel);
        parent.add(operationNode);
        
        JTable operationTable = null;
        Map<Object, Object> operationInfoMap = new LinkedHashMap<Object, Object>();
                
        for (int i = 0; i < mbeanOperations.length; i ++) {
            final String op_name = mbeanOperations[i].getName();
            String returnType = mbeanOperations[i].getReturnType();
            MBeanParameterInfo[] mbeanParameters = mbeanOperations[i].getSignature();
            
            operationInfoMap.put("Name", op_name);
            operationInfoMap.put("Description", mbeanOperations[i].getDescription());
            operationInfoMap.put("Return Type", returnType);
            operationInfoMap.put("Impact", mbeanOperations[i].getImpact());
            operationInfoMap.put("Signature", mbeanParameters);
            
            SynchMBeansTableModel tableModel = new SynchOperationInfoTableModel("OperationInfo");
            tableModel.setTableModelData(operationInfoMap);
            operationTable = new JTable(tableModel);
            operationTable.setBounds(5, 5, 650, 580);
            operationTable.setFont(font);
            
            operationNode.add(new SynchMBeanNode(mbeanOperations[i].getName(), operationTable));
            
            JLabel label = new JLabel(returnType.indexOf(".") >= 0 ? returnType.substring(returnType.lastIndexOf(".") + 1) : returnType);
            label.setFont(font);
            label.setBounds(30, 20 + (i + 1) * 30, 100, 18);
            
            OperationActionButton button = new OperationActionButton(op_name, returnType, mbean_name);
            button.setFont(font);
            button.setBounds(30 + 100 + 20, 20 + (i + 1) * 30, 200, 18);
            
            OperationTextField[] textFields = null;
            if (mbeanParameters != null) {
                textFields = new OperationTextField[mbeanParameters.length];
                for (int j = 0; j < mbeanParameters.length; j ++) {
                    textFields[j] = new OperationTextField(mbeanParameters[j].getType());
                    textFields[j].setFont(font);
                    textFields[j].setBounds(30 + 100 + 20 + 200 + 30 + j * (15 + 65), 20 + (i + 1) * 30, 65, 18);
                    panel.add(textFields[j]);
                }
            }           
            button.addActionListener(new SynchMBeansActionListener(application, panel, textFields));
            
            panel.add(label);
            panel.add(button);
        }
    }
    
    private void populateNotificationInfo(SynchMBeanNode parent, MBeanNotificationInfo[] mbeanNotifications, JPanel null_panel) throws Exception {
        SynchMBeanNode notificationNode = new SynchMBeanNode("Notifications", null_panel);
        parent.add(notificationNode);
        
        for (int i = 0; i < mbeanNotifications.length; i ++) {
            notificationNode.add(new SynchMBeanNode(mbeanNotifications[i].getName(), null_panel));
        }
    }
}
