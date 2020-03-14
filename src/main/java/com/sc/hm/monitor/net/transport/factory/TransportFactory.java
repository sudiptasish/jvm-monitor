package com.sc.hm.monitor.net.transport.factory;

import com.sc.hm.monitor.common.VMConstants;


public class TransportFactory extends AbstractTransportFactory {
	
	public TransportFactory() {		
	}

	public ITransportFactory getORCTransportFactory(String FACTORY_TYPE) {
		ITransportFactory transFactory = null;
		if (VMConstants.VM_TRANSPORT.toString().equalsIgnoreCase(FACTORY_TYPE)) {
			transFactory = new VMTransportFactory(); 
		}
		return transFactory;
	}
}
