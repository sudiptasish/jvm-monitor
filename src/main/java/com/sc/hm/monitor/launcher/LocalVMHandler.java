package com.sc.hm.monitor.launcher;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
//import sun.jvmstat.monitor.HostIdentifier;
//import sun.jvmstat.monitor.MonitoredHost;
//import sun.jvmstat.monitor.MonitoredVmUtil;
//import sun.jvmstat.monitor.VmIdentifier;
import sun.management.ConnectorAddressLink;

public class LocalVMHandler {

	public static Vector<_LocalVMArgs> getActiveLocalVMs() {
		Vector<_LocalVMArgs> vector = new Vector<_LocalVMArgs>();
        Set<Integer> set = null;
        String processName = "";
		
		try {
			//MonitoredHost monitoredhost = MonitoredHost.getMonitoredHost(new HostIdentifier((String)null));
	        //set = monitoredhost.activeVms();
			//Logger.log(set);
	
			for (Iterator<Integer> itr = set.iterator(); itr.hasNext(); ) {
				Integer VM_ID = itr.next();
				try {
					String jmxURL = ConnectorAddressLink.importFrom(VM_ID.intValue());
					//VmIdentifier vmidentifier = new VmIdentifier(String.valueOf(VM_ID.intValue()));
					//processName = MonitoredVmUtil.commandLine(monitoredhost.getMonitoredVm(vmidentifier));
					
					if (processName != null && processName.length() > 0) {						
						if (processName.indexOf("JVMMonitorMain") >= 0 || processName.indexOf("jvm_monitor") >= 0) {
							vector.addElement(new _LocalVMArgs(String.valueOf(VM_ID.intValue()), processName, jmxURL));
						}
						else if (jmxURL != null && jmxURL.length() > 0) {
							vector.addElement(new _LocalVMArgs(String.valueOf(VM_ID.intValue()), processName, jmxURL));
						}
					}					
					//Logger.log(VM_ID.intValue() + " :: " + processName + " :: " + jmxURL);					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return vector;
	}
	
	public static class _LocalVMArgs {
		private String processId = "";
		private String processName = "";
		private String jmxURL = "";
		
		public _LocalVMArgs(String processId, String processName, String jmxURL) {
			this.processId = processId;
			this.processName = processName;
			this.jmxURL = jmxURL;
		}

		public String getProcessId() {
			return processId;
		}

		public void setProcessId(String processId) {
			this.processId = processId;
		}

		public String getProcessName() {
			return processName;
		}

		public void setProcessName(String processName) {
			this.processName = processName;
		}

		public String getJmxURL() {
			return jmxURL;
		}

		public void setJmxURL(String jmxURL) {
			this.jmxURL = jmxURL;
		}
		
		public String toString() {
			return new StringBuilder().append("[").append(processId).append(" ").append(processName).append("]").toString();
		}
	}
}
