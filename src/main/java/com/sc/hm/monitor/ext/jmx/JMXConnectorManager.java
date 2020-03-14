package com.sc.hm.monitor.ext.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.MessageFormat;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sc.hm.monitor.config.ServerConfig;

public class JMXConnectorManager {

	public static JMXConnector getJMXConnector (ServerConfig config, String url_template) throws Exception {
		JMXConnector jmxConnector = null;
		JMXServiceURL jmxURL = null;
		try {
			
			if (config.getUrl() == null || "".equals(config.getUrl())) {
				_configureURL(config, url_template);
				jmxURL = new JMXServiceURL(config.getProtocol(), config.getServerName(), Integer.parseInt(config.getServerPort()), config.getUrl());
			}
			else {
				jmxURL = new JMXServiceURL(config.getUrl());
			}
			jmxConnector = JMXConnectorFactory.connect(jmxURL);
		}
		catch(MalformedURLException murle) {
			throw new Exception("Invalid URL Specification [" + config.getUrl() + "]");
		}
		catch (IOException e) {
			throw new Exception("Unable to Connect Remote Host");
		}
		return jmxConnector;
	}
	
	private static void _configureURL (ServerConfig config, String url_template) {
		String temp_url = url_template;
//		String[] args = new String[] {config.getProtocol(), config.getServerName(), config.getServerPort()};
		
		temp_url = MessageFormat.format(temp_url, config.getProtocol(), config.getServerName(), config.getServerPort());
		config.setUrl(temp_url);
	}
}
