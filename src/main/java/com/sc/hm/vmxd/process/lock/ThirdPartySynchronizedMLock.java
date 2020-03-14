package com.sc.hm.vmxd.process.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThirdPartySynchronizedMLock {

	private final Lock addLock = new ReentrantLock();
	private final Lock getLock = new ReentrantLock();
	
	private final Condition newTaskCondition = addLock.newCondition();
	private final Condition noTaskCondition = getLock.newCondition();
	
	private boolean addTask = false;
	private boolean gettingTask = false;
	
	private int consumer_participation = 0;	
	private int consumer_take_count = 0;
	
	protected ThirdPartySynchronizedMLock(int consumer_participation) {
		this.consumer_participation = consumer_participation;
	}
	
	public void startAddItem() throws Exception {
		try {
			addLock.lock();
			if (addTask) {
				newTaskCondition.await();
			}
			gettingTask = false;
			addTask = true;
		}
		finally {
			addLock.unlock();
		}
	}
	
	public void endAddItem() throws Exception {
		try {
			getLock.lock();			
			gettingTask = true;
			consumer_take_count = 0;
			noTaskCondition.signalAll();
		}
		finally {
			getLock.unlock();
		}
		try {
			addLock.lock();
			addTask = false;
			newTaskCondition.signalAll();
		}
		finally {
			addLock.unlock();
		}
	}
	
	public void startGetItem() throws Exception {
		try {
			getLock.lock();
			if (!gettingTask) {
				noTaskCondition.await();
			}
			addTask = true;
			gettingTask = false;
		}
		finally {
			getLock.unlock();
		}
	}
	
	public void endGetItem() throws Exception {
		try {
			addLock.lock();
			addTask = false;
			consumer_take_count ++;
			newTaskCondition.signalAll();
		}
		finally {
			addLock.unlock();
		}
		if (consumer_take_count < consumer_participation) {
			try {
				getLock.lock();
				gettingTask = true;
				noTaskCondition.signalAll();
			}
			finally {
				getLock.unlock();
			}
		}
		else {
			gettingTask = false;
		}
	}

	public int getConsumer_participation() {
		return consumer_participation;
	}

	public void setConsumer_participation(int consumer_participation) {
		this.consumer_participation = consumer_participation;
	}

	public int getConsumer_take_count() {
		return consumer_take_count;
	}
}
