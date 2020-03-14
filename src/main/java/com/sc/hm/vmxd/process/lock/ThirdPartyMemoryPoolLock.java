package com.sc.hm.vmxd.process.lock;

public class ThirdPartyMemoryPoolLock extends ThirdPartySynchronizedLock {
	
	private String lock_name = "";

	public ThirdPartyMemoryPoolLock(String lock_name) {
		super();
		this.lock_name = lock_name;
	}
}
