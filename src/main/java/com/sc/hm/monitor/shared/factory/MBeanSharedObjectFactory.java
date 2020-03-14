package com.sc.hm.monitor.shared.factory;

import com.sc.hm.monitor.shared.IMBeanObject;
import com.sc.hm.monitor.shared.classes.ClassMBeanSharedObject;
import com.sc.hm.monitor.shared.compile.CompilationMBeanSharedObject;
import com.sc.hm.monitor.shared.gbcollector.GBCollectorMBeanSharedObject;
import com.sc.hm.monitor.shared.memory.MemoryMBeanSharedObject;
import com.sc.hm.monitor.shared.mpool.MemoryPoolMBeanSharedObject;
import com.sc.hm.monitor.shared.os.OSMBeanSharedObject;
import com.sc.hm.monitor.shared.runtime.RuntimeMBeanSharedObject;
import com.sc.hm.monitor.shared.threads.ThreadMBeanSharedObject;
import com.sc.hm.monitor.util.ManagementUtil;

public class MBeanSharedObjectFactory {
	
	private static MBeanSharedObjectFactory _INSTANCE_ = null;
	
	private MBeanSharedObjectFactory() {}
	
	public static MBeanSharedObjectFactory getMBeanFactory() {
		if (_INSTANCE_ == null) {
			synchronized(MBeanSharedObjectFactory.class) {
				if (_INSTANCE_ == null) {
					_INSTANCE_ = new MBeanSharedObjectFactory();
				}
			}
		}
		return _INSTANCE_;
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public IMBeanObject getSharedMBean(String beanType) throws Exception {
		if (ManagementUtil.MEMORY_MBEAN_PROCESS.equals(beanType)) {
			return new MemoryMBeanSharedObject();
		}
		else if (ManagementUtil.MEMORY_MBEAN_PROCESS.equals(beanType)) {
			return new MemoryPoolMBeanSharedObject();
		}
		else if (ManagementUtil.MEMORYPOOL_MBEAN_PROCESS.equals(beanType)) {
			return new MemoryPoolMBeanSharedObject();
		}
		else if (ManagementUtil.THREAD_MBEAN_PROCESS.equals(beanType)) {
			return new ThreadMBeanSharedObject();
		}
		else if (ManagementUtil.CLASS_MBEAN_PROCESS.equals(beanType)) {
			return new ClassMBeanSharedObject();
		}			
		else if (ManagementUtil.GBCOLLECTOR_MBEAN_PROCESS.equals(beanType)) {
			return new GBCollectorMBeanSharedObject();
		}
		else if (ManagementUtil.COMPILE_MBEAN_PROCESS.equals(beanType)) {
			return new CompilationMBeanSharedObject();
		}			
		else if (ManagementUtil.RUNTIME_MBEAN_PROCESS.equals(beanType)) {
			return new RuntimeMBeanSharedObject();
		}			
		else if (ManagementUtil.OS_MBEAN_PROCESS.equals(beanType)) {
			return new OSMBeanSharedObject();
		}
		else {
			throw new Exception ("Invalid MBean Type");
		}
	}
}
