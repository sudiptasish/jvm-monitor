package com.sc.hm.monitor.ui.layout.mbeans.tab;

import javax.swing.JTextField;

public class OperationTextField extends JTextField {

	private static final long serialVersionUID = 1L;
	
	private String textDataType = "";
	
	public OperationTextField(String text) {
		super(text);
		this.textDataType = text;
	}

	public String getTextDataType() {
		return textDataType;
	}
	
	public Object getParameter() throws Exception {
		String s = super.getText();
		if (textDataType.indexOf("int") >= 0 || textDataType.indexOf("Integer") >= 0) {
			return Integer.parseInt(s);
		}
		else if (textDataType.indexOf("long") >= 0 || textDataType.indexOf("Long") >= 0) {
			return Long.parseLong(s);
		}
		else if (textDataType.indexOf("float") >= 0 || textDataType.indexOf("Float") >= 0) {
			return Float.parseFloat(s);
		}
		else if (textDataType.indexOf("double") >= 0 || textDataType.indexOf("Double") >= 0) {
			return Double.parseDouble(s);
		}
		else if (textDataType.indexOf("boolean") >= 0 || textDataType.indexOf("Boolean") >= 0) {
			return Boolean.valueOf(s);
		}
		else if (textDataType.indexOf("[J") >= 0) {
			s = s.trim();
			s = s.replaceAll("\\s{1,}", "");
			String[] values = s.split(",");
			long[] l = new long[values.length];
			for (int i = 0; i < l.length; i ++) {
				l[i] = Long.parseLong(values[i]);				
			}
			return l;
		}
		else {
			return s;
		}
	}
}
