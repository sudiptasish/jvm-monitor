package com.sc.hm.monitor.ui.tree;

public class MXBeanTableColumnValueAttribute {

	private String columnName = "";
	private int columnIndex = -1;
	private Class columnClass = null;
	private Object columnData = null;
	private boolean editable = false;
	
	public MXBeanTableColumnValueAttribute() {}

	public MXBeanTableColumnValueAttribute(String columnName, int columnIndex, Class columnClass, Object columnData, boolean editable) {
		this.columnName = columnName;
		this.columnIndex = columnIndex;
		this.columnClass = columnClass;
		this.columnData = columnData;
		this.editable = editable;
	}

	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public int getColumnIndex() {
		return columnIndex;
	}
	
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	public Class getColumnClass() {
		return columnClass;
	}
	
	public void setColumnClass(Class columnClass) {
		this.columnClass = columnClass;
	}
	
	public Object getColumnData() {
		return columnData;
	}
	
	public void setColumnData(Object columnData) {
		this.columnData = columnData;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	
}
