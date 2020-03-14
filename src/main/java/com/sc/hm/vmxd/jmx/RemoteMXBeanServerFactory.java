package com.sc.hm.vmxd.jmx;

import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

import com.sc.hm.vmxd.jmx.config.JMXConfigurationSingleton;
import com.sc.hm.vmxd.jmx.connector.ServerConnectorConfig;

/**
 * Remote MXBean server factory class.
 * The factory class is responnsible for creating the mbean server and maintaining
 * the connection (socket channnel) between the parent and child VM.
 * 
 * @author Sudiptasish Chanda
 */
public class RemoteMXBeanServerFactory extends MXBeanServerFactory {

	private static RemoteMXBeanServerFactory rSeverFactory = null;
	
	private final Map<String, MXBeanServer> mxBeanServerMap = new HashMap<>();
	
	private RemoteMXBeanServerFactory() {
		super();
	}
	
	public static MXBeanServerFactory getMXBeanServerFactory() {
		if (rSeverFactory == null) {
			synchronized (LocalMXBeanServerFactory.class) {
				if (rSeverFactory == null) {
					rSeverFactory = new RemoteMXBeanServerFactory();
				}
			}
		}
		return rSeverFactory;
	}
	
    @Override
	public synchronized void createMXBeanServer(String application) throws Exception {
		if (!mxBeanServerMap.containsKey(application)) {
		    ServerConnectorConfig serverConfig = JMXConfigurationSingleton.getInstance().getJMXConfiguration(application);
			if (serverConfig.getUser() != null && serverConfig.getUser().trim().length() > 0) {
			    createCustomMXBeanServer(application);
			}
			else {
    		    String url = new StringBuilder().append("service:jmx:rmi:///jndi/rmi://").append(serverConfig.getHost()).append(":").append(serverConfig.getPort()).append("/jmxrmi").toString();
    			try {
    				JMXServiceURL jmxURL = new JMXServiceURL(url);
    				JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxURL);
    				MBeanServerConnection mbeanServer = jmxConnector.getMBeanServerConnection();
    				MXBeanServer mxbeanServer = new RemoteMXBeanServer(application, mbeanServer);
    				mxBeanServerMap.put(application, mxbeanServer);
    			}
    			catch (Exception e) {
    				throw e;
    			}
			}
		}
	}
	
	private void createCustomMXBeanServer(String application) throws Exception {
	    ServerConnectorConfig serverConfig = JMXConfigurationSingleton.getInstance().getJMXConfiguration(application);
	    JMXServiceURL jmxURL = null;;
        
        try {
            if (serverConfig.getMserver() != null && serverConfig.getMserver().trim().length() > 0) {
                if (serverConfig.getProtocol().equals("iiop")) {
                    String url = new StringBuilder().append("service:jmx:iiop://")
                            .append(serverConfig.getHost()).append(":").append(serverConfig.getPort())
                            .append(serverConfig.getJndiRoot()).append(serverConfig.getMserver()).toString();
                    
                    jmxURL = new JMXServiceURL(url);
                }
                else {
                    jmxURL = new JMXServiceURL(serverConfig.getProtocol()
                            , serverConfig.getHost()
                            , Integer.parseInt(serverConfig.getPort())
                            , serverConfig.getJndiRoot() + serverConfig.getMserver());
                }
            }
            else {
                String url = new StringBuilder().append("service:jmx:rmi:///jndi/rmi://")
                        .append(serverConfig.getHost()).append(":").append(serverConfig.getPort())
                        .append("/jmxrmi").toString();
                
                jmxURL = new JMXServiceURL(url);
            }            		            
            Map<String, Object> env = new HashMap<String, Object>();
            if (serverConfig.getRoleName() != null && serverConfig.getRoleName().trim().length() > 0
                    && serverConfig.getRolePassword() != null && serverConfig.getRolePassword().trim().length() > 0) {
                env.put(JMXConnector.CREDENTIALS, new String[] {serverConfig.getRoleName(), serverConfig.getRolePassword()});
            }
            env.put(Context.SECURITY_PRINCIPAL, serverConfig.getUser());
            env.put(Context.SECURITY_CREDENTIALS, serverConfig.getPassword());
            env.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, serverConfig.getProviderPackage());
            
            /*Subject delegationSubject = 
                    new Subject(true, 
                        Collections.singleton(new JMXPrincipal("delegate")), 
                        Collections.EMPTY_SET, 
                        Collections.EMPTY_SET);*/
            
            JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxURL, env);
            MBeanServerConnection mbeanServer = jmxConnector.getMBeanServerConnection();
            MXBeanServer mxbeanServer = new RemoteMXBeanServer(application, mbeanServer);
            mxBeanServerMap.put(application, mxbeanServer);
        }
        catch (Exception e) {
            throw e;
        }
    }
	
    @Override
	public synchronized MXBeanServer getMXBeanServer(String application) throws Exception {
		if (!mxBeanServerMap.containsKey(application)) {
			createMXBeanServer(application);
		} 
		return mxBeanServerMap.get(application);
	}
}
