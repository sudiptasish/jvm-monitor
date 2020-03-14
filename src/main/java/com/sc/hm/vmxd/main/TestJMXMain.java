package com.sc.hm.vmxd.main;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sc.hm.monitor.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class TestJMXMain {
    
    public static void main(String[] args) throws IOException {
        testConnect(args);
    }

    public static void testConnect(String[] args) throws IOException {
        JMXServiceURL jmxURL = null;
        JMXConnector jmxConnector = null;
        
        try {
            //System.setSecurityManager(null);
            
            //jmxURL = new JMXServiceURL("service:jmx:iiop://slc10bkv.us.oracle.com:7004/jndi/weblogic.management.mbeanservers.runtime");
            
            //jmxURL = new JMXServiceURL("t3", "slc10bkv.us.oracle.com", 7004, "/jndi/weblogic.management.mbeanservers.runtime");
            
            jmxURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://slc10bkv.us.oracle.com:7006/jmxrmi");
            
            Map<String, Object> env = new HashMap<String, Object>();
            env.put(JMXConnector.CREDENTIALS, new String[] {"monitorRole", "sysman"});
            //env.put(JMXConnector.CREDENTIALS, new String[] {"controlRole", ""});
            //env.put(Context.SECURITY_PRINCIPAL, "weblogic");
            //env.put(Context.SECURITY_CREDENTIALS, "welcome1");
            env.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
            env.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
            
            jmxConnector = JMXConnectorFactory.connect(jmxURL, env);
            MBeanServerConnection mxbeanServer = jmxConnector.getMBeanServerConnection();
            
            Logger.log(mxbeanServer.toString());
            
            String[] domains = mxbeanServer.getDomains();
            for (String domain : domains) {
                Set<ObjectName> mbean_names = mxbeanServer.queryNames(new ObjectName(new StringBuilder().append(domain).append(":*").toString()), null);
                for (ObjectName mbean_name : mbean_names) {
                    MBeanInfo mbeanInfo = mxbeanServer.getMBeanInfo(mbean_name);
                    Logger.log(mbeanInfo.toString());
                    break;
                }
                break;
            }
            ThreadMXBean threadMXBean = getThreadMXBean(mxbeanServer);
            
            long[] ids = threadMXBean.getAllThreadIds();
            mxbeanServer.invoke(new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME)
                    , "getThreadInfo"
                    , new Object[] {1L, 1}
                    , new String[] {long.class.getName(), int.class.getName()});
            
            ThreadInfo info = threadMXBean.getThreadInfo(1, 1);
            Logger.log(info);
            //ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(ids, 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (jmxConnector != null) {
                jmxConnector.close();
            }
        }
    }
    
    private static ThreadMXBean getThreadMXBean(MBeanServerConnection mxbeanServer) throws IOException {
        return ManagementFactory.newPlatformMXBeanProxy(mxbeanServer
                , ManagementFactory.THREAD_MXBEAN_NAME
                , ThreadMXBean.class);
    }
}
