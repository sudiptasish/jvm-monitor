package com.sc.hm.vmxd.data.listener;

public class DataNotificationInfo implements DataNotification {

	public static final String DATA_INITIALIZED = "DATA_INITIALIZED";
	public static final String DATA_UN_INITIALIZED = "DATA_UN_INITIALIZED";
	
	private String type = "";
	
	public DataNotificationInfo(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
