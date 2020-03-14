package com.sc.hm.monitor.net.transport.factory;

public abstract class AbstractTransportFactory {
	
	public static synchronized AbstractTransportFactory getTransportFactory() {
		return new TransportFactory();
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public abstract ITransportFactory getORCTransportFactory(String FACTORY_TYPE);
}
