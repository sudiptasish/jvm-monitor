package com.sc.hm.monitor.ui.layout.mbeans;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.swing.JComponent;
import javax.swing.JTable;

import com.sc.hm.monitor.ui.layout.mbeans.handler.AbstractData;
import com.sc.hm.monitor.ui.layout.mbeans.handler.ArrayDataHandler;
import com.sc.hm.monitor.ui.layout.mbeans.handler.BasicDataHandler;
import com.sc.hm.monitor.ui.layout.mbeans.handler.ComplexDataHandler;
import com.sc.hm.monitor.ui.layout.mbeans.handler.CompositeDataHandler;
import com.sc.hm.monitor.ui.layout.mbeans.handler.MapDataHandler;
import com.sc.hm.monitor.ui.layout.mbeans.handler.TabularDataHandler;

public class TableDataListener implements MouseListener {
	
	public void mouseClicked(MouseEvent me) {
		if (me.getClickCount() == 2) {
			JTable table = (JTable)me.getSource();
			int rowIndex = table.getSelectedRow();
			int columnIndex = table.getSelectedColumn();
			if (columnIndex == 0) {
				return;
			}
			manipulateCellData(table, table.getModel().getValueAt(rowIndex, 0), table.getModel().getValueAt(rowIndex, columnIndex));
		}
	}
	
	private void manipulateCellData(JComponent parent, Object title, Object cellData) {
		if (cellData instanceof long[] || cellData instanceof String[]) {
			showArrayData(parent, (String)title, cellData);
		}
		else if (cellData instanceof CompositeData) {
			showCompositeData(parent, (String)title, (CompositeData)cellData);
		}
		else if (cellData instanceof TabularData) {
			showTabularData(parent, (String)title, (TabularData)cellData);
		}
		else if (cellData instanceof Map) {
			showMapData(parent, (String)title, (TabularData)cellData);
		}
	}
	
	private void showCompositeData(JComponent parent, String title, CompositeData data) {
		ComplexDataHandler complexData = new ComplexDataHandler(parent, new CompositeDataHandler(title, "Composite", new AbstractData(data)));
		try {
			complexData.handleComplexData();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showTabularData(JComponent parent, String title, TabularData data) {
		ComplexDataHandler complexData = new ComplexDataHandler(parent, new TabularDataHandler(title, "Tabular", new AbstractData(data)));
		try {
			complexData.handleComplexData();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showArrayData(JComponent parent, String title, Object data) {
		BasicDataHandler basicData = new BasicDataHandler(parent, new ArrayDataHandler(title, "Array", new AbstractData(data)));
		try {
			basicData.handleBasicData();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showMapData(JComponent parent, String title, Object data) {
		BasicDataHandler basicData = new BasicDataHandler(parent, new MapDataHandler(title, "Map", new AbstractData(data)));
		try {
			basicData.handleBasicData();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mouseEntered(MouseEvent me) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}
