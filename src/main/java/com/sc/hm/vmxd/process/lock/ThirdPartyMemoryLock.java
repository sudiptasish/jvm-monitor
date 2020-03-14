package com.sc.hm.vmxd.process.lock;

public class ThirdPartyMemoryLock extends ThirdPartySynchronizedLock {
	
	private String lock_name = "";

	public ThirdPartyMemoryLock(String lock_name) {
		super();
		this.lock_name = lock_name;
	}
}
