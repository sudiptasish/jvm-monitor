package com.sc.hm.vmxd.synchui.layout.common;

import javax.swing.JTextArea;

public class SynchDisplayArea extends JTextArea {
	
	private static final long serialVersionUID = 1L;
	
	private String areaName = "";

	public SynchDisplayArea(int row, int col) {
		super(row, col);		
		setEditable(false);
		setLineWrap(false);
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
