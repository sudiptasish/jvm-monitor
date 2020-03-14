package com.sc.hm.vmxd.process.util;

import java.util.HashMap;
import java.util.Map;

import com.sc.hm.vmxd.config.SynchApplicationConfiguration;
import com.sc.hm.vmxd.process.AbstractMBeanSynchProcess;

public final class ProcessStatUtil {
	
	private static final Map<String, Integer> prIntervalMap = new HashMap<String, Integer>();
	
	public static void setProcessInterval(String process_name, int interval) {
		prIntervalMap.put(process_name, interval);
		AbstractMBeanSynchProcess synch_mbeanProcess = SynchApplicationConfiguration.getSynchInstance().getCurrentObserver().getActiveProcess(process_name);
		synch_mbeanProcess.setPauseTime(interval * 1000);
	}
	
	public static int getProcessInterval(String process_name) {
		if (prIntervalMap.containsKey(process_name)) {
			prIntervalMap.get(process_name);
		}
		return 4;
	}
}
