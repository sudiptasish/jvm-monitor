package com.sc.hm.monitor.net.transport;

import com.sc.hm.monitor.net.socket.SocketConfiguration;
import com.sc.hm.monitor.util.Logger;

public class ExternalNotifierMain {
	
	public ExternalNotifierMain() {}

	private static void loadProperties(String propertyFile) throws Exception {
		
	}
	
	private static void sendNotification(SocketConfiguration socketConfig, String job) {
		TransportHandler.createNotifierSocket(socketConfig, job);
		Logger.log("Notifier Exiting !!!");
	}
	
	public static void main(String[] args) {
		try {
			loadProperties(args[0]);
		}
		catch(Exception e) {
			System.err.println("Error!!! Loading Properties" + e);
		}
		String[] param = new String[args.length - 1];
		System.arraycopy(args, 1, param, 0, param.length);
		
		if (param.length != 3) {
			Logger.log("Invalid Argument. Usage: java JCExternalNotifierMain <machine_name> <port> <job_id>");
			System.exit(-1);
		}
		
		sendNotification(new SocketConfiguration(param[0], Integer.parseInt(param[1])), param[2]);
	}
}
