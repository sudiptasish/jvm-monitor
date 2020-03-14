package com.sc.hm.monitor.ui.layout.mbeans.table.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;

	private String modelName = "";

	private String[] columnNames = {"Name", "Value"};
	protected Vector<Vector<Object>> rows = null;
	
	public DataTableModel(String modelName) {
		this.modelName = modelName;
	}
	
	public void initializeRows(int rowCount) {
		rows = new Vector<Vector<Object>>(rowCount);
		for (int i = 0; i < rowCount; i ++) {
			Vector<Object> rowData = new Vector<Object>(columnNames.length);
			rows.addElement(rowData);
		}
	}
	
	public void initializeTableRowData(Map<String, Object> values) {
		rows = new Vector<Vector<Object>>(values.size());
		for (Iterator<Map.Entry<String, Object>> itr = values.entrySet().iterator(); itr.hasNext();) {
			Vector<Object> row = new Vector<Object>(columnNames.length);
			Map.Entry<String, Object> me = itr.next();
			row.addElement(me.getKey());
			row.addElement(me.getValue());
			rows.addElement(row);
		}
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return rows.size();
	}
	
	public Vector<Vector<Object>> getRows() {
		return rows;
	}
	
	public void setRows(Vector<Vector<Object>> rows) {
		this.rows = rows;
	}
	
	public Class<?> getColumnClass(int columnIndex) {
		Vector<Object> rowData = rows.elementAt(0);
		Object cellData = rowData.elementAt(columnIndex);
		if (cellData instanceof Boolean) {
			return String.class;
		}
		else {
			return cellData.getClass();
		}
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
		Vector<Object> rowData = rows.elementAt(rowIndex);
		return rowData.elementAt(columnIndex);
	}
	
	public void setValueAt(Object obj, int rowIndex, int columnIndex) {
		Vector<Object> rowData = rows.elementAt(rowIndex);
		rowData.setElementAt(obj, columnIndex);
	}
	
	public String getColumnName(int column) {
		if (columnNames[column] != null) {
            return columnNames[column];
        }
        else {
            return "";
        }
	}
}
