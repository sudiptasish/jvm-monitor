package com.sc.hm.monitor.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

import com.sc.hm.monitor.util.Logger;


public class TestMonitor {
	private static Thread[] t = new Thread[5];
	
	public static void main(String[] args) throws Exception {		
		Logger.log(max("HI", 1,2,3));
	}
	
	public static int max(String s, int... k) {
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void testFutureTask() throws Exception {
		Callable<String> c = new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep(5000);
				return new String("YES");
			}
		};
		ExecutorService exeService = Executors.newCachedThreadPool();
		//Future<String> f = exeService.submit(c);
		exeService.shutdown();
		
		//Logger.log("Returned Value: " + f.get());
		
		FutureTask<String> fTask = new FutureTask<String>(c);
		new Thread(fTask).start();
		
		Logger.log("Returned Value: " + fTask.get());
	}
	
	public static void testBarrier() throws Exception {
		final CyclicBarrier cBarrier = new CyclicBarrier(2);
		Thread[] t = new Thread[2];	
		Runnable[] players = new Runnable[] {
				new Runnable() {
					public void run() {
						try {
							Logger.log("Player 1 Getting Ready .....");
							Thread.sleep(10000);
							Logger.log("Done [Player - 1]!!!");
							Logger.log(cBarrier.getNumberWaiting());
							Logger.log("Player - 1 Arrived Position: " + cBarrier.await());
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				},
				new Runnable() {
					public void run() {
						try {
							Logger.log("Player 2 Getting Ready .....");
							Thread.sleep(4000);
							Logger.log("Done [Player - 2]!!!");
							Logger.log(cBarrier.getNumberWaiting());
							Logger.log("Player - 2 Arrived Position: " + cBarrier.await());
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
		};
		
		for (int i = 0; i < t.length; i ++) {
			t[i] = new Thread(players[i]);
			t[i].start();
		}
		Logger.log("HERE: " + cBarrier.getNumberWaiting());
		Logger.log("About To Start Game...");
		Thread.sleep(2000);
		t[0].interrupt();
	}
	
	public static void testSemaphore() throws Exception {
		final Semaphore semaphore = new Semaphore(2);
		
		Runnable[] r = new Runnable[] {
				new Runnable() {
					public void run() {
						try {
							Thread.sleep(10000);
							Logger.log("Thread - 1 Completed Job");
							semaphore.release();							
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				},
				new Runnable() {
					public void run() {
						try {
							Thread.sleep(10000);
							Logger.log("Thread - 2 Completed Job");
							semaphore.release();
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
		};
		
		Thread[] th = new Thread[r.length];
		
		for (int i = 0; i < th.length; i ++) {
			semaphore.acquire();
			th[i] = new Thread(r[i]);
			th[i].start();
			Logger.log("Available Permits: " + semaphore.availablePermits());
		}
		Thread t = new Thread(new Runnable() {
			public void run() {
				Logger.log("Additional Work...");
			}
		});
		Logger.log("Available Permits: " + semaphore.availablePermits());
		semaphore.acquire();
		Logger.log("Available Permits: " + semaphore.availablePermits());
		t.start();
	}
	
	public static void testLatch() throws Exception {
		final CountDownLatch beginLatch = new CountDownLatch(1);
		final CountDownLatch endThreadLatch = new CountDownLatch(t.length);
		
		Runnable[] r = new Runnable[] {
				new Runnable() {
					public void run() {
						try {
							beginLatch.await();
							Logger.log("Thread - 1 Started Job");
							Thread.sleep(1000);
						}
						catch(InterruptedException inte) {}
						Logger.log("Thread - 1 Completed Job");
						endThreadLatch.countDown();
					}
				},
				new Runnable() {
					public void run() {
						try {
							beginLatch.await();
							Logger.log("Thread - 2 Started Job");
							Thread.sleep(1000);
						}
						catch(InterruptedException inte) {}
						Logger.log("Thread - 2 Completed Job");
						endThreadLatch.countDown();
					}
				},
				new Runnable() {
					public void run() {
						try {
							beginLatch.await();
							Logger.log("Thread - 3 Started Job");
							Thread.sleep(1000);
						}
						catch(InterruptedException inte) {}
						Logger.log("Thread - 3 Completed Job");
						endThreadLatch.countDown();
					}
				},
				new Runnable() {
					public void run() {
						try {
							beginLatch.await();
							Logger.log("Thread - 4 Started Job");
							Thread.sleep(1000);
						}
						catch(InterruptedException inte) {}
						Logger.log("Thread - 4 Completed Job");
						endThreadLatch.countDown();
					}
				},
				new Runnable() {
					public void run() {
						try {
							beginLatch.await();
							Logger.log("Thread - 5 Started Job");
							Thread.sleep(1000);
						}
						catch(InterruptedException inte) {}
						Logger.log("Thread - 5 Completed Job");
						endThreadLatch.countDown();
					}
				}
		};
		
		for (int i = 0; i < t.length; i ++) {
			t[i] = new Thread(r[i]);
			t[i].start();
		}
		beginLatch.countDown();
		endThreadLatch.await();
		startMainTask();
	}
	
	private static void startMainTask() {
		Logger.log("Main Task Started....");
		try {
			Thread.sleep(3000);
		}
		catch(InterruptedException inte) {}
		Logger.log("Main Task Completed....");	
	}	
}
