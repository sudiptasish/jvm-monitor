package com.sc.hm.vmxd.synchui.layout.mbeans;

import java.util.HashMap;
import java.util.Map;

public final class SynchTabInfoShared {

	private static final Map<String, SynchMBeanNode> mbeansDataMap = new HashMap<String, SynchMBeanNode>();

	public static SynchMBeanNode getMbeansData(String applicationId) {
		if (mbeansDataMap.containsKey(applicationId)) {
			return mbeansDataMap.get(applicationId);
		}
		return null;
	}

	public static void setMbeansData(String applicationId, SynchMBeanNode mbeansData) {
		mbeansDataMap.put(applicationId, mbeansData);
	}
}
