package com.sc.hm.vmxd.synchui.layout.mbeans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.management.openmbean.CompositeData;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sc.hm.monitor.dialog.MBeanOperationDialog;
import com.sc.hm.monitor.ui.layout.mbeans.handler.CompositeDataHandler;
import com.sc.hm.monitor.ui.layout.mbeans.tab.OperationActionButton;
import com.sc.hm.monitor.ui.layout.mbeans.tab.OperationTextField;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.jmx.MXBeanServer;
import com.sc.hm.vmxd.jmx.manager.MXBeanServerManager;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class SynchMBeansActionListener  implements ActionListener {
    
    private String application = "";
    private JPanel parentComponent = null;
    private OperationTextField[] textFields = null;
    
    public SynchMBeansActionListener(String application, JPanel panel, OperationTextField[] opTextFields) {
        this.application = application;
        parentComponent = panel;
        textFields = opTextFields;
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            final MXBeanServer mxbeanServer = MXBeanServerManager.getMXBeanServer(application);
            MBeanOperationDialog opDialog = MBeanOperationDialog.getMBeanOperationDialog();
            Object[] params = null;
            String[] signatures = null;
            
            OperationActionButton opActionButton = (OperationActionButton)ae.getSource();
            if (textFields != null && textFields.length > 0) {
                params = new Object[textFields.length];
                signatures = new String[textFields.length];
                
                for (int i = 0; i < textFields.length; i ++) {
                    if ("".equals(textFields[i])) {
                        JOptionPane.showMessageDialog(parentComponent, "All Fields Are Mandetory");
                        return;
                    }
                    try {
                        params[i] = textFields[i].getParameter();
                    }
                    catch (NumberFormatException nmfe) {
                        JOptionPane.showMessageDialog(parentComponent, "Invalid Argument Type");
                        return;
                    }
                    signatures[i] = textFields[i].getTextDataType();
                    Logger.log(new StringBuilder().append("Param ").append(i).append(": ").append(params[i]).append(". Signature ").append(i).append(": ").append(signatures[i]).toString());
                }
            }
            try {
                Logger.log("Invoking " + opActionButton.getObjectName().toString() + "'s method " + opActionButton.getText());
                Object obj = mxbeanServer.invokeOperation(opActionButton.getObjectName(), opActionButton.getText(), params, signatures);
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
}