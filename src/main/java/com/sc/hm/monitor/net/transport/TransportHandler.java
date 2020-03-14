package com.sc.hm.monitor.net.transport;

import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.manager.VMConfigurationUtil;
import com.sc.hm.monitor.net.socket.ITransportSocketType;
import com.sc.hm.monitor.net.socket.SocketConfiguration;
import com.sc.hm.monitor.net.transport.factory.AbstractTransportFactory;
import com.sc.hm.monitor.net.transport.factory.ITransportFactory;
import com.sc.hm.monitor.util.Logger;

public class TransportHandler {
	
	public static void createListenerSocket() {
		try {
			ITransportFactory transFactory = AbstractTransportFactory.getTransportFactory().getORCTransportFactory(VMConstants.VM_TRANSPORT.toString());
			ITransportSocketType transportSocket = transFactory.getTransportSocket(VMConstants.VM_TRANSPORT_SERVER_SOCKET.toString());
			
			TransportSocketListener transportListener = new TransportSocketListener(transportSocket);
			transportListener.configurePort(VMConfigurationUtil.getPrimePropertyAsInt(VMConstants.XML_TRANSPORT_PORT.toString()));
			Logger.log("Starting Listener Transport Socket....");
			transportListener.initializeTransport();
			transportListener.startListenerTransport();
		}
		catch(Exception e) {
			Logger.log("Error!!! Creating Listener Socket" + e);
		}
	}

	public static void createNotifierSocket(SocketConfiguration socketConfig, String jobId) {
		try {
			ITransportFactory transFactory = AbstractTransportFactory.getTransportFactory().getORCTransportFactory(VMConstants.VM_TRANSPORT.toString());
			ITransportSocketType transportSocket = transFactory.getTransportSocket(VMConstants.VM_TRANSPORT_CLIENT_SOCKET.toString());
			
			TransportSocketNotifier transportNotifier = new TransportSocketNotifier(transportSocket);
			if (socketConfig != null) {
				transportNotifier.configureHost(InetAddress.getByName(socketConfig.getHost()));
				transportNotifier.configurePort(socketConfig.getPort());
			}
			else {
				transportNotifier.configureHost(InetAddress.getByName(VMConfigurationUtil.getPrimeProperty(VMConstants.XML_TRANSPORT_HOST.toString())));
				transportNotifier.configurePort(VMConfigurationUtil.getPrimePropertyAsInt(VMConstants.XML_TRANSPORT_PORT.toString()));
			}
			Logger.log("Starting Notifier Transport Socket....");
			transportNotifier.initializeTransport();
			transportNotifier.startNotifierTransport(jobId);
		}
		catch(Exception e) {
			Logger.log("Error!!! Creating Notifier Socket" + e);
		}		
	}

	public static void createNotifierSocket(CountDownLatch initSocketConfigLatch, SocketConfiguration socketConfig, String jobId) {
		try {
			ITransportFactory transFactory = AbstractTransportFactory.getTransportFactory().getORCTransportFactory(VMConstants.VM_TRANSPORT.toString());
			ITransportSocketType transportSocket = transFactory.getTransportSocket(VMConstants.VM_TRANSPORT_CLIENT_SOCKET.toString());
			
			TransportSocketNotifier transportNotifier = new TransportSocketNotifier(transportSocket);
			if (socketConfig != null) {
				transportNotifier.configureHost(InetAddress.getByName(socketConfig.getHost()));
				transportNotifier.configurePort(socketConfig.getPort());
			}
			else {
				transportNotifier.configureHost(InetAddress.getByName(VMConfigurationUtil.getPrimeProperty(VMConstants.XML_TRANSPORT_HOST.toString())));
				transportNotifier.configurePort(VMConfigurationUtil.getPrimePropertyAsInt(VMConstants.XML_TRANSPORT_PORT.toString()));
			}
			Logger.log("Starting Notifier Transport Socket....");
			transportNotifier.initializeTransport();
			transportNotifier.startNotifierTransport(jobId, initSocketConfigLatch);
		}
		catch(Exception e) {
			Logger.log("Error!!! Creating Notifier Socket" + e);
		}		
	}
}
