package com.sc.hm.monitor.ui.layout.mbeans.table.model.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.management.ObjectName;
import javax.swing.table.AbstractTableModel;

import com.sc.hm.monitor.ui.tree.MXBeanTableColumnValueAttribute;

public abstract class MBeanTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	
	private ObjectName mbeansObjectName = null;
	
	private String tableModelLabel = "";
	protected String[] columnNames = {"Name", "Value"};
	protected Vector<Vector<Object>> rows = null;
	
	public MBeanTableModel(String label) {
		tableModelLabel = label;
	}
	
	public MBeanTableModel(String label, String[] columnNames, Vector<Vector<Object>> rows) {
		tableModelLabel = label;
		this.columnNames = columnNames;
		this.rows = rows;
	}

	public void initializeAndSetRowCount(int rowCount) {
		rows = new Vector<Vector<Object>>(rowCount);
		for (int i = 0; i < rowCount; i ++) {
			Vector<Object> row = new Vector<Object>(columnNames.length);
			rows.addElement(row);
		}
	}
	
	public void initializeTableRowData(Map<Object, Object> values) {
		rows = new Vector<Vector<Object>>(values.size());
		for (Iterator<Map.Entry<Object, Object>> itr = values.entrySet().iterator(); itr.hasNext();) {
			Vector<Object> row = new Vector<Object>(columnNames.length);
			Map.Entry<Object, Object> me = itr.next();
			row.addElement(me.getKey());
			row.addElement(me.getValue());
			rows.addElement(row);
		}
	}
	
	public void initializeTableRowData(ObjectName mbeansObject, Map<Object, Object> values) {
		mbeansObjectName = mbeansObject;
		rows = new Vector<Vector<Object>>(values.size());
		for (Iterator<Map.Entry<Object, Object>> itr = values.entrySet().iterator(); itr.hasNext();) {
			Vector<Object> row = new Vector<Object>(columnNames.length);
			Map.Entry<Object, Object> me = itr.next();
			row.addElement(me.getKey());
			row.addElement(me.getValue());
			rows.addElement(row);
		}
	}

	public String getTableModelLabel() {
		return tableModelLabel;
	}

	public void setTableModelLabel(String tableModelLabel) {
		this.tableModelLabel = tableModelLabel;
	}

	public String[] getColumnNames() {
		return columnNames;
	}
	
	public String getColumnName(int column) {
        if (columnNames[column] != null) {
            return columnNames[column];
        }
        else {
            return "";
        }
    }
	
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	public Class getColumnClass(int column) {
		Class c = null;
		Vector<Object> rowVector = rows.elementAt(0);
		if (column == 0) {
			Object val = (String)rowVector.elementAt(column);
			if (val instanceof Boolean) {
				c = String.class;
			}
			else {
				c = val.getClass();
			}
		}
		else {
			Object obj = rowVector.elementAt(column);
			if (obj instanceof MXBeanTableColumnValueAttribute) {
				MXBeanTableColumnValueAttribute columnAttribute = (MXBeanTableColumnValueAttribute)obj;
				if (columnAttribute.getColumnData() instanceof Boolean) {
					c = String.class;
				}
				else {
					c = columnAttribute.getColumnClass();
				}
			}
			else {
				if (obj instanceof Boolean) {
					c = String.class;
				}
				else {
					c = obj.getClass();
				}
			}
		}
		return c;
	}
	
	public Class getOriginalColumnClass(int row, int column) {
		Class c = null;
		Vector<Object> rowVector = rows.elementAt(row);
		if (column == 0) {
			Object val = (String)rowVector.elementAt(column);
			c = val.getClass();
		}
		else {
			Object obj = rowVector.elementAt(column);
			if (obj instanceof MXBeanTableColumnValueAttribute) {
				MXBeanTableColumnValueAttribute columnAttribute = (MXBeanTableColumnValueAttribute)obj;
				c = columnAttribute.getColumnClass();
			}
			else {
				c = obj.getClass();
			}
		}
		return c;
	}
	
	public Vector<Vector<Object>> getRows() {
		return rows;
	}
	
	public void setRows(Vector<Vector<Object>> rows) {
		this.rows = rows;
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public int getRowCount() {
		return rows.size();
	}
	
	public Object getValueAt(int row, int col) {
		Vector<Object> rowVector = rows.elementAt(row);
		if (col == 0) {
			return rowVector.elementAt(col);
		}
		else {
			Object obj = rowVector.elementAt(col);
			if (obj instanceof MXBeanTableColumnValueAttribute) {
				MXBeanTableColumnValueAttribute columnAttribute = (MXBeanTableColumnValueAttribute)obj;
				return columnAttribute.getColumnData();
			}
			else {
				return obj;
			}
		}
	}
	
	public void setValueAt(Object val, int row, int col) {
		Vector<Object> rowVector = rows.elementAt(row);
		if (col == 1) {
			Object obj = rowVector.elementAt(col);
			if (obj instanceof MXBeanTableColumnValueAttribute) {
				MXBeanTableColumnValueAttribute columnAttribute = (MXBeanTableColumnValueAttribute)obj;
				columnAttribute.setColumnData(val);
			}
			else {
				rowVector.setElementAt(val, col);
			}
		}
	}
	
	public boolean isCellEditable(int row, int column) {
		if (column == 0) {
			return false;
		}
		Vector<Object> rowVector = rows.elementAt(row);
		
		Object obj = rowVector.elementAt(column);
		if (obj instanceof MXBeanTableColumnValueAttribute) {
			MXBeanTableColumnValueAttribute columnAttribute = (MXBeanTableColumnValueAttribute)obj;
			return columnAttribute.isEditable();
		}
		else {
			return false;
		}
	}

	public ObjectName getMbeansObjectName() {
		return mbeansObjectName;
	}

	public void setMbeansObjectName(ObjectName mbeansObjectName) {
		this.mbeansObjectName = mbeansObjectName;
	}
}
