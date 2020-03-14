package com.sc.hm.monitor.net.transport;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.launcher.ProcessInfo;
import com.sc.hm.monitor.net.data.NotificationObject;
import com.sc.hm.monitor.net.socket.ITransportSocketType;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.service.ExecutorServiceHelper;

public class TransportSocketNotifier {
	
	private ITransportSocketType transportSocket = null;
	
	public TransportSocketNotifier(ITransportSocketType transportSocket) {
		this.transportSocket = transportSocket;
	}
	
	public void configureHost(InetAddress hostAddress) {
		transportSocket.setHost(hostAddress);
	}
	
	public void configurePort(int port) {
		transportSocket.setPort(port);
	}
	
	public void initializeTransport() {
		transportSocket.initializeSocket();
		Logger.log("Initialized Notifier Transport....");
	}
	
	public void startNotifierTransport(String jobId) {
		try {
			Thread socket_worker = new Thread(new SocketWorker(jobId), VMConstants.NOTIFIER_THREAD_NAME.getValue() + "-" + jobId);			
			ApplicationConfiguration.getInstance().putThreadReference(socket_worker);
			socket_worker.start();
			
			//socket_worker.join();
			Logger.log("Started Job For: " + jobId);
		}
		catch(Exception e) {
			System.err.println("Error!!! startListenerTransport()" + e);
		}
	}
	
	public void startNotifierTransport(String jobId, CountDownLatch initSocketConfigLatch) {
		try {
			ExecutorService executorService = ExecutorServiceHelper.getExecutorServiceHelper().createSingleExecutorPool();
			SocketWorker socketWorker = new SocketWorker(VMConstants.NOTIFIER_THREAD_NAME.getValue() + "-" + jobId, jobId, initSocketConfigLatch);			
			executorService.execute(socketWorker);
			
			//socket_worker.join();
			Logger.log("Started Job For: " + jobId);
		}
		catch(Exception e) {
			System.err.println("Error!!! startListenerTransport()" + e);
		}
	}
	
	private class SocketWorker implements Runnable {
		private String name = "";
		private String jobId = "";
		private CountDownLatch initSocketConfigLatch = null;
		
		public SocketWorker(String jobId) {
			this(VMConstants.NOTIFIER_THREAD_NAME.getValue() + "-" + jobId, jobId);
		}
		
		public SocketWorker(String name, String jobId) {
			this(name, jobId, null);
		}
		
		public SocketWorker(String name, String jobId, CountDownLatch initSocketConfigLatch) {
			this.name = name;
			this.jobId = jobId;
			this.initSocketConfigLatch = initSocketConfigLatch;
		}
		
		public void run() {
			if (this.initSocketConfigLatch != null) {
				startSynchNotifier();
			}
			else {
				startNotifier();
			}
		}
		
		private void startSynchNotifier() {
			ObjectOutput objOutput = null;
			ObjectInput objInput = null;
			try	{
				objOutput = new ObjectOutputStream(transportSocket.getTransportOutputStream());
				objInput = new ObjectInputStream(transportSocket.getTransportInputStream());
				
				objOutput.writeObject(new String(this.jobId));
				Logger.log("Sent JobId " + this.jobId + " To Server");
				
				Object obj = objInput.readObject();
				Logger.log("Received Configuration Object for This Application: " + (EnvironmentConfigObject)obj);
				
				SynchApplicationConfiguration.getSynchInstance().setEnvironmentConfig((EnvironmentConfigObject)obj);
				initSocketConfigLatch.countDown();
				
				NotificationObject notifObject = new NotificationObject(this.jobId, ProcessInfo.PROCESS_STATUS_RUNNING);
				ProcessInfo.setProcessInfo(notifObject);
				do {
					notifObject = ProcessInfo.getProcessNotification(this.jobId);
					objOutput.writeObject(notifObject);
					objOutput.flush();
					((ObjectOutputStream)objOutput).reset();
					Logger.log("Sent Client Notification to Server...." + notifObject);
					if (!notifObject.getStatus().equals(ProcessInfo.PROCESS_STATUS_RUNNING)) {
						break;
					}
					try {
						Thread.sleep(1000 * 60 * 30);
					}
					catch (InterruptedException inte) {
						Logger.log("Notifier Thread Interrupted...");
					}
				}
				while (true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ApplicationConfiguration.getInstance().removeThreadReference(VMConstants.NOTIFIER_THREAD_NAME.getValue() + "-" + this.jobId);
			}
		}
		
		private void startNotifier() {
			ObjectOutput objOutput = null;
			ObjectInput objInput = null;
			try	{
				objOutput = new ObjectOutputStream(transportSocket.getTransportOutputStream());
				objInput = new ObjectInputStream(transportSocket.getTransportInputStream());
				
				objOutput.writeObject(new String(this.jobId));
				Logger.log("Sent JobId " + this.jobId + " To Server");
				
				Object obj = objInput.readObject();
				Logger.log("Object Content: " + (EnvironmentConfigObject)obj);
				ApplicationConfiguration.getInstance().setEnvironmentConfig((EnvironmentConfigObject)obj, this);

				NotificationObject notifObject = new NotificationObject(this.jobId, ProcessInfo.PROCESS_STATUS_RUNNING);
				ProcessInfo.setProcessInfo(notifObject);
				do {
					notifObject = ProcessInfo.getProcessNotification(this.jobId);
					objOutput.writeObject(notifObject);
					objOutput.flush();
					((ObjectOutputStream)objOutput).reset();
					Logger.log("Sent Client Notification to Server...." + notifObject);
					if (!notifObject.getStatus().equals(ProcessInfo.PROCESS_STATUS_RUNNING)) {
						break;
					}
					try {
						Thread.sleep(1000 * 60 * 30);
					}
					catch (InterruptedException inte) {
						Logger.log("Notifier Thread Interrupted...");
					}
				}
				while (true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ApplicationConfiguration.getInstance().removeThreadReference(VMConstants.NOTIFIER_THREAD_NAME.getValue() + "-" + this.jobId);
			}
		}
	}
}
