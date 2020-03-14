package com.sc.hm.monitor.ui.layout.mbeans.handler;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

import com.sc.hm.monitor.ui.layout.mbeans.tab.DataTreePanel;

public abstract class AbstractDataHandler {
	
	private String dataHandlerName = "";
	private String dataTypeName = "";
	
	private AbstractData data = null;
	
	private static final Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

	public AbstractDataHandler() {}

	public AbstractDataHandler(String dataHandlerName, String dataTypeName, AbstractData data) {
		this.dataHandlerName = dataHandlerName;
		this.dataTypeName = dataTypeName;
		this.data = data;
	}

	public String getDataHandlerName() {
		return dataHandlerName;
	}

	public void setDataHandlerName(String dataHandlerName) {
		this.dataHandlerName = dataHandlerName;
	}

	public String getDataTypeName() {
		return dataTypeName;
	}

	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}
	
	public AbstractData getData() {
		return data;
	}

	public void setData(AbstractData data) {
		this.data = data;
	}

	public abstract void handleData() throws Exception;
	
	protected void createAndShowAbstractDataPanel(AbstractTableModel model) {
		DataTreePanel panel = new DataTreePanel(400, 300);
		panel.setModel(model);
		panel.configureAndAddComponent();
		
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(400, 300));
		frame.getContentPane().add(panel);
		frame.setVisible(true);
	}
	
	protected void createAndShowAbstractDataPanel(String title, AbstractTableModel model, List<Map<String, Object>> list) {
		DataTreePanel panel = new DataTreePanel(400, 300);
		panel.setModel(model);
		panel.setTabularDataMap(list);
		panel.configureAndAddComponent();
		
		final JFrame frame = new JFrame(title);
		frame.setSize(new Dimension(400, 300));
		frame.setLocation((int)(screen_size.getWidth() - frame.getWidth()) / 2, (int)(screen_size.getHeight() - frame.getHeight()) / 2);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent we) {
				frame.dispose();
			}
		});
		frame.getContentPane().add(panel);
		frame.setVisible(true);
	}
}
