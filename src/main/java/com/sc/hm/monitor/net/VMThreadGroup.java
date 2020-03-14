package com.sc.hm.monitor.net;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VMThreadGroup extends ThreadGroup {
	
	 private static Reference<VMThreadGroup> threadGroupRef = null;
	 private Map<String, SoftReference<Thread>> ref_childThread_map;

	 public VMThreadGroup(String groupName) {
		 super(groupName);
	     ref_childThread_map = new HashMap<String, SoftReference<Thread>>();
	 }

	 public static synchronized VMThreadGroup getThreadGroup() {
		 if(threadGroupRef == null) {
			 threadGroupRef = new SoftReference<VMThreadGroup>(new VMThreadGroup("VM_THREAD_GROUP"));
		 }
		 return (VMThreadGroup)threadGroupRef.get();
	 }

	 public synchronized void putThreadReference(String thread_name, Thread t) {
		 ref_childThread_map.put(thread_name, new SoftReference<Thread>(t));
	 }

	 public synchronized void removeThreadReference(String thread_name) {
		 if(ref_childThread_map.containsKey(thread_name))
			 ref_childThread_map.remove(thread_name);
	 }

	 public synchronized boolean isRunning(String thread_name) {
		 return ref_childThread_map.containsKey(thread_name);
	 }

	 public synchronized Thread getThread(String thread_name) {
		 Thread t = null;
		 if(!ref_childThread_map.containsKey(thread_name)) {
			 return null;
		 }
		 Reference<Thread> ref = ref_childThread_map.get(thread_name);
		 if(ref == null) {
			 return null;
		 }
		 else {
			 t = (Thread)ref.get();
			 return t;
		 }
	 }

	 public synchronized Iterator getAllThreads() {
		 return ref_childThread_map.keySet().iterator();
	 }
}
