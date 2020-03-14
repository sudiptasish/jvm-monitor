package com.sc.hm.monitor.ui.layout.mbeans.handler;

public class AbstractData {

	private Object celldata = null;
	
	public AbstractData() {}
	
	public AbstractData(Object celldata) {
		this.celldata = celldata;
	}

	public Object getCelldata() {
		return celldata;
	}

	public void setCelldata(Object celldata) {
		this.celldata = celldata;
	}
}
