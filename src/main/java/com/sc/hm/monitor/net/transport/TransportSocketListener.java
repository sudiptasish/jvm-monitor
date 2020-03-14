package com.sc.hm.monitor.net.transport;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.config.manager.VMConfigurationUtil;
import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.net.data.NotificationObject;
import com.sc.hm.monitor.net.socket.ITransportSocketType;
import com.sc.hm.monitor.net.socket.TransportServerSocket;
import com.sc.hm.monitor.util.Logger;

public class TransportSocketListener {
	
	private ITransportSocketType transportSocket = null;
	private boolean startListen = true;
	
	public TransportSocketListener(ITransportSocketType transportSocket) {
		this.transportSocket = transportSocket;
	}
	
	public void configurePort(int port) {
		transportSocket.setPort(port);
	}
	
	public void initializeTransport() {
		transportSocket.initializeSocket();
		Logger.log("Initialized Listener Transport....");
	}
	
	public boolean isStartListen() {
		return startListen;
	}

	public void setStartListen(boolean startListen) {
		this.startListen = startListen;
	}

	public void startListenerTransport() {
		new Thread(new SocketConnection()).start();
	}
	
	private class SocketConnection implements Runnable {
		public void run() {
			while (isStartListen()) {
				try {
					Socket socket = ((TransportServerSocket)transportSocket).acceptConnection();
					Thread socketWorker = new Thread(new SocketWorker(socket));
					socketWorker.start();
					
					//socketWorker.join();
				}
				catch(Exception e) {
					System.err.println("Error!!! startListenerTransport()" + e);
				}
			}
		}
	}
	
	private class SocketWorker implements Runnable {
		private Socket client = null;
		private String jobId = "";

		public SocketWorker(Socket c) {
			client = c;
		}

		public void run() {
			ObjectOutput objOutput = null;
			ObjectInput objInput = null;

			NotificationObject notificationObj = null;
			try	{
				objOutput = new ObjectOutputStream(client.getOutputStream());
				objInput = new ObjectInputStream(client.getInputStream());
				
				Object obj = objInput.readObject();
				if (obj instanceof String) {
					Logger.log("Sending Environment Configuration for Process " + (String)obj);
					jobId = (String)obj;
					EnvironmentConfigObject envConfigObject = VMConfigurationUtil.getEnvConfigProperty((String)obj);
					envConfigObject.setSmtp_host(VMConfigurationUtil.getPrimeProperty(VMConstants.XML_SMTP_HOST.toString()));
					objOutput.writeObject(envConfigObject);
					objOutput.flush();
					((ObjectOutputStream)objOutput).reset();
					Logger.log("Wrote Environment Config Object to Stream");
				}
				do {
					Logger.log("Waiting for Process Status....");
					obj = objInput.readObject();
					if (obj instanceof NotificationObject) {
						notificationObj = (NotificationObject)obj;
						Logger.log("Server Received Remote Process Info: " + notificationObj);
						//ProcessInfo.setProcessStatus(notificationObj.getJobId(), notificationObj.getStatus());
						ProcessInfo.setProcessInfo(notificationObj.getJobId(), notificationObj);
					}
				}
				while (((NotificationObject)obj).getStatus().equalsIgnoreCase((ProcessInfo.PROCESS_STATUS_RUNNING)));
				Logger.log("Remote Monioring Stopped For Process " + ((NotificationObject)obj).getJobId());
			}
			catch (Exception e) {
				System.err.println("Remote Process Terminated: " + e.getMessage());
				ProcessInfo.setProcessStatus(jobId, ProcessInfo.PROCESS_STATUS_ERROR);
			}
			finally {
				try	{
					if (objOutput != null) {
						objOutput.close();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
