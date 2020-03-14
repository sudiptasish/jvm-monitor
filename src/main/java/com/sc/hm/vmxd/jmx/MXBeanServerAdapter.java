package com.sc.hm.vmxd.jmx;

import java.io.IOException;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

/**
 * An adapter class to forward the rpc call to underlying local/remote mbean server.
 * 
 * @author Sudiptasish Chanda
 */
public abstract class MXBeanServerAdapter {

	protected String mbeanServerName = "";
	protected MBeanServerConnection mbeanServer = null;
	
	public MXBeanServerAdapter(String name, MBeanServerConnection server) {
		mbeanServerName = name;
		mbeanServer = server;
	}
	
	public String getMBeanServerName() {
		return mbeanServerName;
	}
	
	public String getDefaultDomain() throws IOException {
		return mbeanServer.getDefaultDomain();
	}
	
	public String[] getAllDomains() throws IOException {
		return mbeanServer.getDomains();
	}
	
	public MBeanInfo getMBeanInfo(ObjectName name)
        throws InstanceNotFoundException
            , IntrospectionException
            , ReflectionException
            , IOException {
        
		return mbeanServer.getMBeanInfo(name);
	}
	
	public Set<ObjectName> queryNames(ObjectName name, QueryExp query) throws IOException {
		return mbeanServer.queryNames(name, query);
	}
	
	public Set<?> queryMBeans(ObjectName name, QueryExp query) throws IOException {
		return mbeanServer.queryMBeans(name, query);
	}
	
	public ObjectInstance createMBean(String className, ObjectName name)
        throws InstanceAlreadyExistsException
            , MBeanRegistrationException
            , NotCompliantMBeanException
            , ReflectionException
            , MBeanException
            , IOException {
        
		return mbeanServer.createMBean(className, name);
	}
	
	public void destroyMBean(ObjectName name)
        throws InstanceNotFoundException
        , MBeanRegistrationException
        , IOException {
        
		mbeanServer.unregisterMBean(name);
	}
	
	public int getMBeanCount() throws IOException {
		return mbeanServer.getMBeanCount();
	}
	
	public Object getAttribute(ObjectName name, String attribute)
        throws AttributeNotFoundException
            , InstanceNotFoundException
            , MBeanException
            , ReflectionException
            , IOException {
        
		return mbeanServer.getAttribute(name, attribute);
	}
	
	public AttributeList getAttributeList(ObjectName name, String[] attributes)
        throws AttributeNotFoundException
            , InstanceNotFoundException
            , MBeanException
            , ReflectionException
            , IOException {
        
		return mbeanServer.getAttributes(name, attributes);
	}
	
	public void setAttribute(ObjectName name, Attribute attribute)
        throws InstanceNotFoundException
            , AttributeNotFoundException
            , InvalidAttributeValueException
            , MBeanException
            , ReflectionException
            , IOException {
        
		mbeanServer.setAttribute(name, attribute);
	}
	
	public void setAttributeList(ObjectName name, AttributeList attributes)
        throws InstanceNotFoundException
            , AttributeNotFoundException
            , InvalidAttributeValueException
            , MBeanException
            , ReflectionException
            , IOException {
        
		mbeanServer.setAttributes(name, attributes);
	}
	
	public Object invokeOperation(ObjectName name
        , String operation
        , Object[] params
        , String[] signature) throws InstanceNotFoundException
                                    , MBeanException
                                    , ReflectionException
                                    , IOException {
        
		return mbeanServer.invoke(name, operation, params, signature);
	}
	
	public void addNotificationListener(ObjectName name
        , NotificationListener listener
        , NotificationFilter filter
        , Object handback) throws InstanceNotFoundException, IOException {
        
		mbeanServer.addNotificationListener(name, listener, filter, handback);
	}
	
	public void removeNotificationListener(ObjectName name, NotificationListener listener)
        throws InstanceNotFoundException
            , ListenerNotFoundException
            , IOException {
        
		mbeanServer.removeNotificationListener(name, listener);
	}
}
