package com.sc.hm.vmxd.jmx;

import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;
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
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

/**
 * Interface that represents a mbean server.
 * 
 * @author Sudiptasish Chanda
 */
public interface MXBeanServer {

    /**
     * Return the default domain of this mbean server.
     * @return String
     * @throws IOException 
     */
	public String getDefaultDomain() throws IOException;
	
    /**
     * Return all the available domains in this mbean server.
     * @return String[]
     * @throws IOException 
     */
	public String[] getAllDomains() throws IOException;
	
    /**
     * Return the mbean info object for this object name.
     * 
     * @param name
     * @return MBeanInfo
     * @throws InstanceNotFoundException
     * @throws IntrospectionException
     * @throws ReflectionException
     * @throws IOException 
     */
	public MBeanInfo getMBeanInfo(ObjectName name)
        throws InstanceNotFoundException
            , IntrospectionException
            , ReflectionException
            , IOException;
	
    /**
     * Query for an object name. Optionally, user can pass the query expression (if any).
     * 
     * @param name
     * @param query
     * @return Set
     * @throws IOException 
     */
	public Set<ObjectName> queryNames(ObjectName name, QueryExp query) throws IOException;
	
    /**
     * Query the mbean server to retrieve the mbean names matching with the
     * object name.
     * 
     * @param name
     * @param query
     * @return Set
     * @throws IOException 
     */
	public Set<?> queryMBeans(ObjectName name, QueryExp query) throws IOException;
	
    /**
     * Create a new MBean.
     * Post the MBean creation, the ObjectInstance for this MBean will be returned.
     * 
     * @param className
     * @param name
     * @return ObjectInstance
     * 
     * @throws InstanceAlreadyExistsException
     * @throws MBeanRegistrationException
     * @throws NotCompliantMBeanException
     * @throws ReflectionException
     * @throws MBeanException
     * @throws IOException 
     */
	public ObjectInstance createMBean(String className, ObjectName name)
        throws InstanceAlreadyExistsException
            , MBeanRegistrationException
            , NotCompliantMBeanException
            , ReflectionException
            , MBeanException
            , IOException;
	
    /**
     * Destroy the MBean as identified by this ObjectName.
     * 
     * @param name
     * @throws InstanceNotFoundException
     * @throws MBeanRegistrationException
     * @throws IOException 
     */
	public void destroyMBean(ObjectName name)
        throws InstanceNotFoundException
            , MBeanRegistrationException
            , IOException;
	
    /**
     * Return the total number MBeans available in the MBean server.
     * @return int
     * @throws IOException 
     */
	public int getMBeanCount() throws IOException;
	
	public Object getAttribute(ObjectName name, String attribute)
        throws AttributeNotFoundException
            , InstanceNotFoundException
            , MBeanException
            , ReflectionException
            , IOException;
	
	public AttributeList getAttributeList(ObjectName name, String[] attributes)
        throws AttributeNotFoundException
            , InstanceNotFoundException
            , MBeanException
            , ReflectionException
            , IOException;
	
	public void setAttribute(ObjectName name, Attribute attribute)
        throws InstanceNotFoundException
            , AttributeNotFoundException
            , InvalidAttributeValueException
            , MBeanException
            , ReflectionException
            , IOException;
	
	public void setAttributeList(ObjectName name, AttributeList attributes)
        throws InstanceNotFoundException
            , AttributeNotFoundException
            , InvalidAttributeValueException
            , MBeanException
            , ReflectionException
            , IOException;
	
	public Object invokeOperation(ObjectName name
        , String operation
        , Object[] params
        , String[] signature) throws InstanceNotFoundException
                                , MBeanException
                                , ReflectionException
                                , IOException;
	
	public void addNotificationListener(ObjectName name
        , NotificationListener listener
        , NotificationFilter filter
        , Object handback) throws InstanceNotFoundException, IOException;
	
	public void removeNotificationListener(ObjectName name
        , NotificationListener listener) throws InstanceNotFoundException
                                            , ListenerNotFoundException
                                            , IOException;
	
	
	public MemoryMXBean getMemoryMXBean() throws Exception;
	
	public RuntimeMXBean getRuntimeMXBean() throws Exception;
	
	public OperatingSystemMXBean getOperatingSystemMXBean() throws Exception;
	
	public CompilationMXBean getCompilationMXBean() throws Exception;
	
	public ThreadMXBean getThreadMXBean() throws Exception;
	
	public ClassLoadingMXBean getClassLoadingMXBean() throws Exception;
	
	public List<MemoryPoolMXBean> getMemoryPoolMXBean() throws Exception;
	
	public List<GarbageCollectorMXBean> getGarbageCollectorMXBean() throws Exception;
	
	public List<MemoryManagerMXBean> getMemoryManagerMXBean() throws Exception;
}
