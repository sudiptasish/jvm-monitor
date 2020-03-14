package com.sc.hm.vmxd.synchui.layout.mbeans;

import java.util.Set;
import java.util.concurrent.Callable;

import javax.management.ObjectName;
import javax.swing.JPanel;

import com.sc.hm.vmxd.jmx.MXBeanServer;

public class SynchMBeansHandler implements Callable<SynchMBeanNode> {
	
	private String applicationId = "";
	private MXBeanServer mxbeanServer = null;
	
	public SynchMBeansHandler(MXBeanServer mxbeanServer, String applicationId) {
		this.applicationId = applicationId;
		this.mxbeanServer = mxbeanServer;
	}
	
	public SynchMBeanNode call() throws Exception {
		try {
			createMBeansTree();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return SynchTabInfoShared.getMbeansData(applicationId);
	}
	
	public void createMBeansTree() throws Exception {
		JPanel null_panel = new JPanel();
		
		SynchMBeanNode rootNode = new SynchMBeanNode("MBeans", null_panel);
		String[] domains = mxbeanServer.getAllDomains();
		StringBuilder _buff = new StringBuilder(20);
		
		for (int i = 0; i < domains.length; i ++) {
			SynchMBeanNode domainNode = new SynchMBeanNode(domains[i], null_panel);
			rootNode.add(domainNode);
			
			SynchMBeanNode typeNode = null;
			SynchMBeanNode nameNode = null;
			
			Set<ObjectName> mbean_names = mxbeanServer.queryNames(
			        new ObjectName(_buff.append(domains[i]).append(":*").toString()), null);
			_buff.delete(0, _buff.length());
			
			for (ObjectName mbean_name : mbean_names) {
				String type = mbean_name.getKeyProperty("type");
				type = (type != null) ? type : mbean_name.getKeyProperty("Type");
				
				String name = mbean_name.getKeyProperty("name");
				name = (name != null) ? name : mbean_name.getKeyProperty("Name");
				
				//Logger.log("Root Node [Domain Name]: " + domain);
				if (type == null && name == null) {
					continue;
				}
				if (type != null && !domainNode.isDirectDescendentByNodeName(type)) {
					typeNode = new SynchMBeanNode(mbean_name, type, null_panel);
					domainNode.add(typeNode);
				}
				else {
					if (type != null) {
						typeNode = domainNode.getChildByName(type);
					}
				}
				if (name != null) {
					nameNode = new SynchMBeanNode(mbean_name, name, null_panel);
					if (typeNode != null && !typeNode.isDirectDescendentByNodeName(name)) {
						typeNode.add(nameNode);
					}
					else if (!domainNode.isDirectDescendentByNodeName(name)) {
						domainNode.add(nameNode);
					}
				}
				else {
					nameNode = typeNode;
				}
			}
		}
		SynchTabInfoShared.setMbeansData(applicationId, rootNode);
	}
}
