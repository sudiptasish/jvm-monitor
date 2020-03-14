package com.sc.hm.vmxd.process.lock;

public class ThirdPartyThreadLock extends ThirdPartySynchronizedLock {
	
	private String lock_name = "";

	public ThirdPartyThreadLock(String lock_name) {
		super();
		this.lock_name = lock_name;
	}
}
