package com.sc.hm.monitor.ui.layout.mbeans.handler;

import javax.swing.JComponent;

public class BasicDataHandler extends AbstractDataHandler {

	private AbstractDataHandler dataHandler = null;
	
	private JComponent component = null;

	public BasicDataHandler(JComponent component, AbstractDataHandler dataHandler) {
		this(dataHandler.getDataHandlerName(), dataHandler.getDataTypeName(), dataHandler.getData());
		this.component = component;
		this.dataHandler = dataHandler;
	}

	public BasicDataHandler(String dataHandlerName, String dataTypeName, AbstractData data) {
		super(dataHandlerName, dataTypeName, data);
	}
	
	public JComponent getComponent() {
		return component;
	}

	public void setComponent(JComponent component) {
		this.component = component;
	}

	public void handleBasicData() throws Exception {
		dataHandler.handleData();
	}

	public void handleData() throws Exception {}
}
