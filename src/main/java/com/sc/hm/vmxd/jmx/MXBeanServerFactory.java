package com.sc.hm.vmxd.jmx;

/**
 * The factory class is responnsible for creating the mbean server and maintaining
 * the connection (socket channnel) between the parent and child VM.
 * 
 * @author Sudiptasish Chanda
 */
public abstract class MXBeanServerFactory {
	
	public static final String MX_BEAN_SERVER_FACTORY_LOCAL = "MX_BEAN_SERVER_FACTORY_LOCAL";
	public static final String MX_BEAN_SERVER_FACTORY_REMOTE = "MX_BEAN_SERVER_FACTORY_REMOTE";

	public static synchronized MXBeanServerFactory getMXBeanServerFactory(String factory_type) {
		if (MX_BEAN_SERVER_FACTORY_LOCAL.equals(factory_type)) {
			return LocalMXBeanServerFactory.getMXBeanServerFactory();
		}
		else if (MX_BEAN_SERVER_FACTORY_REMOTE.equals(factory_type)) {
			return RemoteMXBeanServerFactory.getMXBeanServerFactory();
		}
		return null;
	}
	
    /**
     * Create a new MXBean server.
     * 
     * @param application
     * @throws Exception 
     */
	public abstract void createMXBeanServer(String application) throws Exception;
	
    /**
     * Return the mbean server name cooresponding to the application id.
     * 
     * @param application
     * @return MXBeanServer
     * @throws Exception 
     */
	public abstract MXBeanServer getMXBeanServer(String application) throws Exception;
}
