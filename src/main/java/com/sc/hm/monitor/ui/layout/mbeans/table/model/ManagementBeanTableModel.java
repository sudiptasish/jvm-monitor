package com.sc.hm.monitor.ui.layout.mbeans.table.model;

import java.util.Map;
import java.util.Vector;

import com.sc.hm.monitor.ui.layout.mbeans.table.model.common.MBeanTableModel;

public class ManagementBeanTableModel extends MBeanTableModel {

	public ManagementBeanTableModel(String label) {
		super(label);
		setDefaultColumnName();
	}
	
	public ManagementBeanTableModel(String label, String[] columnNames, Vector<Vector<Object>> rows) {
		super(label, columnNames, rows);
	}
	
	private void setDefaultColumnName() {
		String[] name = {"Attribute Name", "Attribute Value"};
		setColumnNames(name);
	}
	
	public void setColumnNames(String[] names) {
		super.columnNames = new String[names.length];
		for (byte i = 0; i < names.length; i ++) {
			super.columnNames[i] = names[i];
		}
	}
	
	public void initializeTableRowData(Map<Object, Object> values) {
		super.initializeTableRowData(values);
	}
}
