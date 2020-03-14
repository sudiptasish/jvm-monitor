package com.sc.hm.monitor.ext.jmx;

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
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

public class ManagedBeanServerImpl implements ManagedBeanServer {

	private MBeanServerConnection mbeanServer = null;
	
	public ManagedBeanServerImpl (MBeanServerConnection mbeanServer) {
		this.mbeanServer = mbeanServer;
	}
	
	public int getRemoteMBeanCount() throws IOException {
		return mbeanServer.getMBeanCount();
	}
	
	public String getDefaultDomain() throws IOException {
		return mbeanServer.getDefaultDomain();
	}
	
	public String[] getAllDomains() throws IOException {
		return mbeanServer.getDomains();
	}
	
	public Object getRemoteMBeanAttributeValue(ObjectName objectName, String attributeName) throws IOException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, MBeanException {
		return mbeanServer.getAttribute(objectName, attributeName);
	}
	
	public AttributeList getRemoteMBeanAttributes(ObjectName objectName, String[] attributeName) throws IOException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, MBeanException {
		return mbeanServer.getAttributes(objectName, attributeName);
	}
	
	public MBeanInfo getRemoteMBeanInfo(ObjectName objectName) throws IOException, IntrospectionException, InstanceNotFoundException, ReflectionException {
		return mbeanServer.getMBeanInfo(objectName);
	}
	
	public ObjectInstance getObjectInstance(ObjectName objectName) throws IOException, InstanceNotFoundException {
		return mbeanServer.getObjectInstance(objectName);
	}
	
	public Object invokeRemoteMBeanOperation(ObjectName objectName, String operationName, Object[] params, String[] signature) throws Exception {
		try {
			return mbeanServer.invoke(objectName, operationName, params, signature);
		}
		catch (IOException ie) {
			throw ie;
		}
		catch (MBeanException mbe) {
			throw mbe;
		}
		catch (ReflectionException rfle) {
			throw rfle;
		}
		catch (InstanceNotFoundException inste) {
			throw inste;
		}
	}
	
	public void setRemoteMBeanAttribute (ObjectName objectName, Attribute attribute) throws Exception {
		try {
			//objectName.setMBeanServer((MBeanServer)mbeanServer);
			mbeanServer.setAttribute(objectName, attribute);
		}
		catch (InstanceNotFoundException inste) {
			throw inste;
		}
		catch (AttributeNotFoundException attne) {
			throw attne;
		}
		catch (MBeanException mbe) {
			throw mbe;
		}
		catch (IOException ie) {
			throw ie;
		}
		catch (ReflectionException rfle) {
			throw rfle;
		}
		catch (InvalidAttributeValueException invle) {
			throw invle;
		}
	}
	
	public void setRemoteMBeanAttributes (ObjectName objectName, AttributeList attributes) throws Exception {
		try {
			mbeanServer.setAttributes(objectName, attributes);
		}
		catch (InstanceNotFoundException inste) {
			throw inste;
		}
		catch (IOException ie) {
			throw ie;
		}
		catch (ReflectionException rfle) {
			throw rfle;
		}
	}
	
	public boolean isMBeanRegistered(ObjectName objectName) throws IOException {
		return mbeanServer.isRegistered(objectName);
	}
	
	public boolean isInstanceOf(ObjectName objectName, String className) throws InstanceNotFoundException,IOException {
		return mbeanServer.isInstanceOf(objectName, className);
	}
	
	public Set<ObjectName> getRemoteMBeanName(ObjectName objectName, QueryExp qExp) throws IOException {
		return mbeanServer.queryNames(objectName, qExp);
	}
	
	public Set<ObjectName> getRemoteMBeanByDomainName(String domainName) throws MalformedObjectNameException, IOException {
		ObjectName objectName = new ObjectName(domainName + ":*");
		return mbeanServer.queryNames(objectName, null);
	}
	
	public Set<ObjectName> getAllRemoteMBean() throws MalformedObjectNameException, IOException {
		return mbeanServer.queryNames(null, null);
	}
	
	public Set<ObjectInstance> getRemoteMBeanByObjectName(ObjectName objectName, QueryExp qExp) throws IOException {
		return mbeanServer.queryMBeans(objectName, qExp);
	}
	
	public void createMXBean(String className, ObjectName objectName) {
		try {
			mbeanServer.createMBean(className, objectName);
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		catch (InstanceAlreadyExistsException instae) {
			instae.printStackTrace();
		}
		catch (MBeanException mbe) {
			mbe.printStackTrace();
		}
		catch (ReflectionException rfle) {
			rfle.printStackTrace();
		}
		catch (NotCompliantMBeanException ncmbe) {
			ncmbe.printStackTrace();
		}
	}
	
	public void createMXBean(String className, ObjectName objectName, Object[] params, String[] signature) {
		try {
			mbeanServer.createMBean(className, objectName, params, signature);
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		catch (InstanceAlreadyExistsException instae) {
			instae.printStackTrace();
		}
		catch (MBeanException mbe) {
			mbe.printStackTrace();
		}
		catch (ReflectionException rfle) {
			rfle.printStackTrace();
		}
		catch (NotCompliantMBeanException ncmbe) {
			ncmbe.printStackTrace();
		}
	}
	
	public void unregisterRemoteMBean(ObjectName objectName) {
		try {
			if (isMBeanRegistered(objectName)) {
				mbeanServer.unregisterMBean(objectName);
			}
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		catch (MBeanRegistrationException mbge) {
			mbge.printStackTrace();
		}
		catch (InstanceNotFoundException inste) {
			inste.printStackTrace();
		}
	}
	
	public void addRemoteMBeanNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object handback) {
		try {
			mbeanServer.addNotificationListener(objectName, listener, filter, handback);
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		catch (InstanceNotFoundException inste) {
			inste.printStackTrace();
		}
	}
	
	public void removeRemoteMBeanNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object handback) {
		try {
			mbeanServer.removeNotificationListener(objectName, listener, filter, handback);
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		catch (InstanceNotFoundException inste) {
			inste.printStackTrace();
		}
		catch (ListenerNotFoundException lsnfe) {
			lsnfe.printStackTrace();
		}
	}
	
	public void removeRemoteMBeanNotificationListener(ObjectName objectName, NotificationListener listener) {
		try {
			mbeanServer.removeNotificationListener(objectName, listener);
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		catch (InstanceNotFoundException inste) {
			inste.printStackTrace();
		}
		catch (ListenerNotFoundException lsnfe) {
			lsnfe.printStackTrace();
		}
	}
}
