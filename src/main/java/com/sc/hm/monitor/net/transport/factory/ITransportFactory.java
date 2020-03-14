package com.sc.hm.monitor.net.transport.factory;

import com.sc.hm.monitor.net.socket.ITransportSocketType;

public interface ITransportFactory {

	public ITransportSocketType getTransportSocket(String SOCKET_TYPE) throws Exception;
}
