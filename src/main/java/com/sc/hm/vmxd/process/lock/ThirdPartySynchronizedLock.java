package com.sc.hm.vmxd.process.lock;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ThirdPartySynchronizedLock {

	private static final int IDLE = 0;
	private static final int WRITE = 1;
	private static final int READ = 2;
	
	private final AtomicInteger lock = new AtomicInteger(IDLE);
	
	protected ThirdPartySynchronizedLock() {}
	
	public void startAddItem() throws Exception {
	    lock.compareAndSet(IDLE, WRITE);
	}
	
	public void endAddItem() throws Exception {
		lock.compareAndSet(WRITE, IDLE);
	}
	
	public void startGetItem() throws Exception {
	    lock.compareAndSet(IDLE, READ);
	}
	
	public void endGetItem() throws Exception {
	    lock.compareAndSet(READ, IDLE);
	}
}
