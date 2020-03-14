package com.sc.hm.monitor.net.data;

public class JCReflectionMethodInfo {

	private String name = "";
	private int modifier = -999;
	private String returnType = "";
	private Class[] parameters = null;
	private Class[] exceptions = null;

	public JCReflectionMethodInfo() {}

	public JCReflectionMethodInfo(String name, int modifier, String returnType, Class[] parameters, Class[] exceptions) {
		this.name = name;
		this.modifier = modifier;
		this.returnType = returnType;
		this.parameters = parameters;
		this.exceptions = exceptions;
	}

	public Class[] getExceptions() {
		return exceptions;
	}

	public void setExceptions(Class[] exceptions) {
		this.exceptions = exceptions;
	}

	public int getModifier() {
		return modifier;
	}

	public void setModifier(int modifier) {
		this.modifier = modifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class[] getParameters() {
		return parameters;
	}

	public void setParameters(Class[] parameters) {
		this.parameters = parameters;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
}
