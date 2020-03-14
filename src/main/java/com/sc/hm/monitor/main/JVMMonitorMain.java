package com.sc.hm.monitor.main;

import java.lang.reflect.Constructor;

public abstract class JVMMonitorMain {

	public JVMMonitorMain() {
	}

	public static void main (String[] args) {
		String className = args[0];
		String[] params = new String[4];
		System.arraycopy(args, 1, params, 0, params.length - 1);
		params[params.length - 1] = "rmi";
		
		JVMMonitorMain vmMonitor = null;
		try {
		    Constructor<?> constructor = Class.forName(className).getDeclaredConstructor(new Class[] {String[].class});
			vmMonitor = (JVMMonitorMain)constructor.newInstance(new Object[] {params});
			vmMonitor.initializeTransport();
			vmMonitor.startChildProcess();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract void startChildProcess() throws Exception;
	
	public abstract void initializeTransport();
}
