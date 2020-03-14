package com.sc.hm.monitor.net.transport.factory;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.net.socket.ITransportSocketType;
import com.sc.hm.monitor.net.socket.TransportClientSocket;
import com.sc.hm.monitor.net.socket.TransportServerSocket;

public class VMTransportFactory implements ITransportFactory {

	public ITransportSocketType getTransportSocket() {
		return new TransportClientSocket();
	}
	
	public ITransportSocketType getTransportSocket(String SOCKET_TYPE) throws Exception {
		if (VMConstants.VM_TRANSPORT_SERVER_SOCKET.toString().equalsIgnoreCase(SOCKET_TYPE)) {
			return new TransportServerSocket();
		}
		else if (VMConstants.VM_TRANSPORT_CLIENT_SOCKET.toString().equalsIgnoreCase(SOCKET_TYPE)) {
			return new TransportClientSocket();
		}
		else {
			throw new Exception("Invalid Transport Socket Type Requested");
		}
	}
}
