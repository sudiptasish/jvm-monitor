package com.sc.hm.vmxd.synchui.layout.mbeans.model;

import java.util.Map;

public class SynchOperationInfoTableModel extends SynchMBeansTableModel {

	private static final long serialVersionUID = 1L;

	public SynchOperationInfoTableModel(String s) {
		super(s);
		setDefaultColumnName();
	}
	
	private void setDefaultColumnName() {
		String[] name = {"OperationInfo Name", "OperationInfo Value"};
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
	
}
