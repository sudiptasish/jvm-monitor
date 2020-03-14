package com.sc.hm.vmxd.process.lock;

public class ThirdPartyClassLoadingLock extends ThirdPartySynchronizedLock {
	
	private String lock_name = "";

	public ThirdPartyClassLoadingLock(String lock_name) {
		super();
		this.lock_name = lock_name;
	}
}
