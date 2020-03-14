package com.sc.hm.monitor.shared;

import com.sc.hm.monitor.shared.classes.ClassMBeanSharedObject;
import com.sc.hm.monitor.shared.compile.CompilationMBeanSharedObject;
import com.sc.hm.monitor.shared.gbcollector.GBCollectorMBeanSharedObject;
import com.sc.hm.monitor.shared.memory.MemoryMBeanSharedObject;
import com.sc.hm.monitor.shared.mpool.MemoryPoolMBeanSharedObject;
import com.sc.hm.monitor.shared.os.OSMBeanSharedObject;
import com.sc.hm.monitor.shared.runtime.RuntimeMBeanSharedObject;
import com.sc.hm.monitor.shared.threads.ThreadMBeanSharedObject;

public class MBeanSharedObjectRepository {
	
	private static final MBeanSharedObjectRepository _INSTANCE_ = new MBeanSharedObjectRepository();

	private MemoryMBeanSharedObject memory_mx_bean = null;
	private MemoryPoolMBeanSharedObject mpool_mx_bean = null;
	private ThreadMBeanSharedObject thread_mx_bean = null;
	private ClassMBeanSharedObject class_mx_bean = null;
	private GBCollectorMBeanSharedObject gbcollector_mx_bean = null;
	private CompilationMBeanSharedObject compiler_mx_bean = null;
	private RuntimeMBeanSharedObject runtime_mx_bean = null;
	private OSMBeanSharedObject os_mx_bean = null;
	
	private MBeanSharedObjectRepository() {}
	
	public static synchronized MBeanSharedObjectRepository getInstance() {
		return _INSTANCE_;
	}
	
	public void storeMBean(IMBeanObject mbean) {
		if (mbean instanceof ClassMBeanSharedObject) {
			setClass_mx_bean((ClassMBeanSharedObject)mbean);
		}
		else if (mbean instanceof CompilationMBeanSharedObject) {
			setCompiler_mx_bean((CompilationMBeanSharedObject)mbean);
		}
		else if (mbean instanceof GBCollectorMBeanSharedObject) {
			setGbcollector_mx_bean((GBCollectorMBeanSharedObject)mbean);
		}
		else if (mbean instanceof MemoryMBeanSharedObject) {
			setMemory_mx_bean((MemoryMBeanSharedObject)mbean);
		}
		else if (mbean instanceof MemoryPoolMBeanSharedObject) {
			setMpool_mx_bean((MemoryPoolMBeanSharedObject)mbean);
		}
		else if (mbean instanceof OSMBeanSharedObject) {
			setOs_mx_bean((OSMBeanSharedObject)mbean);
		}
		else if (mbean instanceof RuntimeMBeanSharedObject) {
			setRuntime_mx_bean((RuntimeMBeanSharedObject)mbean);
		}
		else if (mbean instanceof ThreadMBeanSharedObject) {
			setThread_mx_bean((ThreadMBeanSharedObject)mbean);
		}
	}
	
	public ClassMBeanSharedObject getClass_mx_bean() {
		return class_mx_bean;
	}
	public void setClass_mx_bean(ClassMBeanSharedObject class_mx_bean) {
		this.class_mx_bean = class_mx_bean;
	}
	public CompilationMBeanSharedObject getCompiler_mx_bean() {
		return compiler_mx_bean;
	}
	public void setCompiler_mx_bean(CompilationMBeanSharedObject compiler_mx_bean) {
		this.compiler_mx_bean = compiler_mx_bean;
	}
	public GBCollectorMBeanSharedObject getGbcollector_mx_bean() {
		return gbcollector_mx_bean;
	}
	public void setGbcollector_mx_bean(GBCollectorMBeanSharedObject gbcollector_mx_bean) {
		this.gbcollector_mx_bean = gbcollector_mx_bean;
	}
	public MemoryMBeanSharedObject getMemory_mx_bean() {
		return memory_mx_bean;
	}
	public void setMemory_mx_bean(MemoryMBeanSharedObject memory_mx_bean) {
		this.memory_mx_bean = memory_mx_bean;
	}
	public MemoryPoolMBeanSharedObject getMpool_mx_bean() {
		return mpool_mx_bean;
	}
	public void setMpool_mx_bean(MemoryPoolMBeanSharedObject mpool_mx_bean) {
		this.mpool_mx_bean = mpool_mx_bean;
	}
	public OSMBeanSharedObject getOs_mx_bean() {
		return os_mx_bean;
	}
	public void setOs_mx_bean(OSMBeanSharedObject os_mx_bean) {
		this.os_mx_bean = os_mx_bean;
	}
	public RuntimeMBeanSharedObject getRuntime_mx_bean() {
		return runtime_mx_bean;
	}
	public void setRuntime_mx_bean(RuntimeMBeanSharedObject runtime_mx_bean) {
		this.runtime_mx_bean = runtime_mx_bean;
	}
	public ThreadMBeanSharedObject getThread_mx_bean() {
		return thread_mx_bean;
	}
	public void setThread_mx_bean(ThreadMBeanSharedObject thread_mx_bean) {
		this.thread_mx_bean = thread_mx_bean;
	}	
}
