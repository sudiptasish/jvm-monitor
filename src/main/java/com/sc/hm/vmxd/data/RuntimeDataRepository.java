package com.sc.hm.vmxd.data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;

import com.sc.hm.vmxd.data.runtime.RuntimeData;

/**
 * Repository to store the runtime details of remote jvm.
 * @author Sudiptasish Chanda
 */
public class RuntimeDataRepository extends DataRepository {

	private RuntimeData runtimeData = null;
	
	private Map<String, Method> methods = new HashMap<>();
	
	public RuntimeDataRepository(String name) {
		super(name);
	}

    @Override
	public void initializeRepository(Object data) throws Exception {
		Method method = null;
		runtimeData = new RuntimeData();
		
		method = runtimeData.getClass().getDeclaredMethod("setUptime", Long.class);
		methods.put("Uptime", method);
		
		AttributeList attributeList = (AttributeList)data;
		for (int i = 0; i < attributeList.size(); i ++) {
            Attribute attribute = (Attribute)attributeList.get(i);
            Object val = attribute.getValue();
            method = runtimeData.getClass().getDeclaredMethod("set" + attribute.getName(), val.getClass());
            method.invoke(runtimeData, val);
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
			methods.get(attribute.getName()).invoke(runtimeData, attribute.getValue());
		}
	}
	
	public RuntimeData getRuntimeData() {
		return runtimeData;
	}
}
