package com.sc.hm.monitor.event;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventObject {

	private final Lock eventLock = new ReentrantLock();
	private final Condition eventCondition = eventLock.newCondition();
	
	private Object object = null;
	
	public EventObject() {}
	
	public void setObject(Object obj) throws Exception {
		try {
			eventLock.lock();
			object = obj;
			if (obj != null) {
				eventCondition.signal();
			}
		}
		finally {
			eventLock.unlock();
		}
	}
	
	public Object getObject() throws Exception {
		try {
			eventLock.lock();
			if (object == null) {
				eventCondition.await();
			}
		}
		finally {
			eventLock.unlock();
		}
		return object;
	}
}
