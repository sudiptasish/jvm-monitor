package com.sc.hm.vmxd.synchui.layout.mbeans.model;

import java.util.Map;
import java.util.Vector;

public class SynchAttributeValueTableModel extends SynchMBeansTableModel {

	private static final long serialVersionUID = 1L;

	public SynchAttributeValueTableModel(String s) {
		super(s);
		setDefaultColumnName();
	}
	
	private void setDefaultColumnName() {
		String[] name = {"Attribute Name", "Attribute Value"};
		setColumnNames(name);
	}
	
	public void setColumnNames(String[] names) {
		super.columns = new String[names.length];
		for (byte i = 0; i < names.length; i ++) {
			super.columns[i] = names[i];
		}
	}
	
	public void setTableModelData(Map<Object, Object> values) {
		super.setTableModelData(values);
	}
	
	public String[] getAttributeNames() {		
		int length = rows.size();
		String[] attributes = new String[length];
		for (int i = 0; i < length; i ++) {
			Vector<Object> row = rows.elementAt(i);
			attributes[i] = (String)row.elementAt(0);
		}
		return attributes;
	}
}
