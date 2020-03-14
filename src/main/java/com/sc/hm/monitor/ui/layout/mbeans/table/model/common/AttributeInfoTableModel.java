package com.sc.hm.monitor.ui.layout.mbeans.table.model.common;

import java.util.Map;
import java.util.Vector;

public class AttributeInfoTableModel extends MBeanTableModel {

	private static final long serialVersionUID = 1L;

	public AttributeInfoTableModel(String label) {
		super(label);
		setDefaultColumnName();
	}
	
	public AttributeInfoTableModel(String label, String[] columnNames, Vector<Vector<Object>> rows) {
		super(label, columnNames, rows);
	}
	
	private void setDefaultColumnName() {
		String[] name = {"AttributeInfo Name", "AttributeInfo Value"};
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
