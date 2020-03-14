package com.sc.hm.monitor.ext.jmx;

import java.io.IOException;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

public interface ManagedBeanServer {

	public int getRemoteMBeanCount() throws IOException;
	
	public String getDefaultDomain() throws IOException;
	
	public String[] getAllDomains() throws IOException;
	
	public Object getRemoteMBeanAttributeValue(ObjectName objectName, String attributeName) throws IOException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, MBeanException;
	
	public AttributeList getRemoteMBeanAttributes(ObjectName objectName, String[] attributeName) throws IOException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, MBeanException;
	
	public MBeanInfo getRemoteMBeanInfo(ObjectName objectName) throws IOException, IntrospectionException, InstanceNotFoundException, ReflectionException;
	
	public ObjectInstance getObjectInstance(ObjectName objectName) throws IOException, InstanceNotFoundException;
	
	public Object invokeRemoteMBeanOperation(ObjectName objectName, String operationName, Object[] params, String[] signature) throws Exception;
	
	public void setRemoteMBeanAttribute (ObjectName objectName, Attribute attribute) throws Exception;
	
	public void setRemoteMBeanAttributes (ObjectName objectName, AttributeList attributes) throws Exception;
	
	public boolean isMBeanRegistered(ObjectName objectName) throws IOException;
	
	public boolean isInstanceOf(ObjectName objectName, String className) throws InstanceNotFoundException,IOException;
	
	public Set<ObjectName> getRemoteMBeanName(ObjectName objectName, QueryExp qExp) throws IOException;
	
	public Set<ObjectName> getRemoteMBeanByDomainName(String domainName) throws MalformedObjectNameException, IOException;
	
	public Set<ObjectName> getAllRemoteMBean() throws MalformedObjectNameException, IOException;
	
	public Set<ObjectInstance> getRemoteMBeanByObjectName(ObjectName objectName, QueryExp qExp) throws IOException;
	
	public void createMXBean(String className, ObjectName objectName);
	
	public void createMXBean(String className, ObjectName objectName, Object[] params, String[] signature);
	
	public void unregisterRemoteMBean(ObjectName objectName);
	
	public void addRemoteMBeanNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object handback);
	
	public void removeRemoteMBeanNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object handback);
	
	public void removeRemoteMBeanNotificationListener(ObjectName objectName, NotificationListener listener);
}
