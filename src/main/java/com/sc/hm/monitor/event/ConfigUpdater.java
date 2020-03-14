/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sc.hm.monitor.event;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.config.manager.VMConfigurationUtil;
import com.sc.hm.monitor.util.FileHandler;
import java.util.Vector;

/**
 *
 * @author schan280
 */
public class ConfigUpdater {
    
    public static void updateConfig() throws Exception {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("\n");

        sBuilder.append("<").append(VMConstants.XML_MONITOR_APP).append(">");

        sBuilder.append(VMConfigurationUtil.getPrimePropertiesConverted());

        Vector<String> appIds = VMConfigurationUtil.getAllApplicationIds();
        for (String appId : appIds) {
            EnvironmentConfigObject envConfigObject = VMConfigurationUtil.getEnvConfigProperty(appId);
            sBuilder.append(envConfigObject.toXML());
        }
        sBuilder.append("\n</").append(VMConstants.XML_MONITOR_APP).append(">");

        String filename = System.getProperty("monitor.config.path");
        if (filename == null) {
            filename = VMConfigurationUtil.DEFAULT_CONFIG;
        }
        FileHandler.writeFileData(filename, sBuilder.toString());
    }
}
