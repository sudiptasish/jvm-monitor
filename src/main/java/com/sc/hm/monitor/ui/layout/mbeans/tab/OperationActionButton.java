package com.sc.hm.monitor.ui.layout.mbeans.tab;

import javax.management.ObjectName;
import javax.swing.JButton;

public class OperationActionButton extends JButton {
	
	private static final long serialVersionUID = 1L;
	
	private String returnType = "";
	private ObjectName objectName = null;
	
	private OperationTextField[] textFields = null;

	public OperationActionButton(String name, String returnType, ObjectName objectName) {
		super(name);
		this.returnType = returnType;
		this.objectName = objectName;
	}

	public String getReturnType() {
		return returnType;
	}

	public ObjectName getObjectName() {
		return objectName;
	}
	
	public void setTextFields(OperationTextField[] textFields) {
		this.textFields = textFields;
	}
	
	public OperationTextField[] getTextFields() {
		return textFields;
	}
}
