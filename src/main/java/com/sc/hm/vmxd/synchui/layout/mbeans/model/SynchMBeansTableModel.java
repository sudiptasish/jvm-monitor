package com.sc.hm.vmxd.synchui.layout.mbeans.model;

import java.util.Map;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class SynchMBeansTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private String label = "";
	
	protected String[] columns = {"Name", "Value"};
	protected Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

	public SynchMBeansTableModel(String s) {
		label = s;
	}

	public void setTableModelData(Map<Object, Object> values) {
		rows.clear();
		rows = null;
		
		rows = new Vector<Vector<Object>>(values.size());
		for (Map.Entry<Object, Object> me : values.entrySet()) {
			Vector<Object> row = new Vector<Object>(columns.length);
			row.addElement(me.getKey());
			row.addElement(me.getValue());
			rows.addElement(row);
		}
		fireTableDataChanged();
	}
	
	public void setColumnNames(String[] names) {
		columns = names;
	}
	
	public String getLabel() {
		return label;
	}
	
	public int getColumnCount() {
		return columns.length;
	}

	public int getRowCount() {
		return rows.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Vector<Object> row = rows.elementAt(rowIndex);
		return row.elementAt(columnIndex);
	}

	public Class<?> getColumnClass(int columnIndex) {
		Vector<Object> row = rows.elementAt(0);
		Object cellData = row.elementAt(columnIndex);
		if (cellData instanceof Boolean) {
			return String.class;
		}
		else {
			return cellData.getClass();
		}
	}
	
	public void setValueAt(Object obj, int rowIndex, int columnIndex) {
		Vector<Object> rowData = rows.elementAt(rowIndex);
		rowData.setElementAt(obj, columnIndex);
	}
	
	public String getColumnName(int columnIndex) {
		if (columns[columnIndex] != null) {
            return columns[columnIndex];
        }
        else {
            return "";
        }
	}

	public String[] getColumnNames() {
		return columns;
	}
}
