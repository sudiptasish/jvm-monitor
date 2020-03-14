package com.sc.hm.vmxd.data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;

import com.sc.hm.vmxd.data.os.OperatingSystemData;

/**
 * Repository to hold the operatingn system details.
 * 
 * @author Sudiptasish Chanda
 */
public class OperatingSystemDataRepository extends DataRepository {

	private OperatingSystemData osData = null;
	
	private Map<String, Method> methods = new HashMap<>();
	
	public OperatingSystemDataRepository(String name) {
		super(name);
	}

    @Override
	public void initializeRepository(Object data) throws Exception {
	    Method method = null;
        osData = new OperatingSystemData("OperatingSystemMX");
        
	    method = osData.getClass().getDeclaredMethod("setProcessCpuLoad", Double.class);
        //methods.put("ProcessCpuTime", method);
        methods.put("ProcessCpuLoad", method);
        
        method = osData.getClass().getDeclaredMethod("setFreePhysicalMemorySize", Long.class);
        methods.put("FreePhysicalMemorySize", method);
        
        method = osData.getClass().getDeclaredMethod("setFreeSwapSpaceSize", Long.class);
        methods.put("FreeSwapSpaceSize", method);
        
        method = osData.getClass().getDeclaredMethod("setCommittedVirtualMemorySize", Long.class);
        methods.put("CommittedVirtualMemorySize", method);
        
		AttributeList attributeList = (AttributeList)data;
		for (int i = 0; i < attributeList.size(); i ++) {
            Attribute attribute = (Attribute)attributeList.get(i);
            Object val = attribute.getValue();
            method = osData.getClass().getDeclaredMethod("set" + attribute.getName(), val.getClass());
            method.invoke(osData, val);
        }		
		setInitializedRepository(true);
	}

    @Override
	public void addData(String name, Object data) throws Exception {
	    if (data == null) {
	        return;
	    }
		AttributeList attributeList = (AttributeList)data;
		
		for (int i = 0; i < attributeList.size(); i ++) {
			Attribute attribute = (Attribute)attributeList.get(i);
			methods.get(attribute.getName()).invoke(osData, attribute.getValue());
		}
	}
	
	public OperatingSystemData getOsData() {
		return osData;
	}
}
