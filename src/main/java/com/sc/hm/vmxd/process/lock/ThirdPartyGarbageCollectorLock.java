package com.sc.hm.vmxd.process.lock;

public class ThirdPartyGarbageCollectorLock extends ThirdPartySynchronizedLock {
	
	private String lock_name = "";

	public ThirdPartyGarbageCollectorLock(String lock_name) {
		super();
		this.lock_name = lock_name;
	}
}
