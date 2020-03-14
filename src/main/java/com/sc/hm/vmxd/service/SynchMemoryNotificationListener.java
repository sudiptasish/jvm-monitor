package com.sc.hm.vmxd.service;

import java.lang.management.MemoryNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.mail.VMMailUtil;
import com.sc.hm.monitor.mail.config.MailConfiguration;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.SynchApplicationConfiguration;

public class SynchMemoryNotificationListener implements NotificationListener {

	public void handleNotification(Notification notification, Object handback) {
		Logger.log("Notification Received: " + notification.getType());
		if (notification.getType().equals(MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED)) {
			performNecessaryActivity(notification, handback);
		}
	}
	
	private void performNecessaryActivity(Notification notification, Object handback) {
		try {
			CompositeData compositeData = (CompositeData)notification.getUserData();
			MemoryNotificationInfo memoryNotificationInfo = MemoryNotificationInfo.from(compositeData);
			Logger.log("Handback Object: " + handback);
			
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("\nMemory Threshold Exceeded for Pool: ").append(memoryNotificationInfo.getPoolName()).append(". ");
			sBuilder.append("\n").append("Current Usage: ").append(memoryNotificationInfo.getUsage());
			Logger.log(sBuilder.toString());
			
			EnvironmentConfigObject envConfig = SynchApplicationConfiguration.getSynchInstance().getEnvironmentConfig();
			if (Boolean.valueOf(envConfig.getEnableMailing().trim())) {
				String from = envConfig.getMailFrom().trim();
				String to = envConfig.getMailTo().trim();
				String cc = envConfig.getMailCc().trim();
				
				String[] arrTo = null;
				String seperator = ",";
				int seperatorIndex = to.indexOf(",");
				if (to != null && !"".equals(to)) {
						if (seperatorIndex == -1) {
						seperatorIndex = to.indexOf(";");
						if (seperatorIndex > 0) {
							seperator = ";";
						}
					}
					arrTo = to.split(seperator);
				}
				
				String[] arrCc = null;
				if (cc != null && !"".equals(cc)) {
					seperatorIndex = cc.indexOf(",");
					if (seperatorIndex == -1) {
						seperatorIndex = cc.indexOf(";");
						if (seperatorIndex > 0) {
							seperator = ";";
						}
					}
					arrCc = cc.split(seperator);
				}				
				
				MailConfiguration mailConfig = new MailConfiguration(from, arrTo, arrCc, null, envConfig.getMailCause(), sBuilder.toString());
				VMMailUtil.sendMail(mailConfig);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
