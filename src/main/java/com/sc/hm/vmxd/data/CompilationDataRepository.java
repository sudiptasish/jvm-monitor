package com.sc.hm.vmxd.data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;

import com.sc.hm.vmxd.data.compile.CompilationData;

/**
 * Repository to hold the compilation data of remote JVM.
 * 
 * @author Sudiptasish Chanda
 */
public class CompilationDataRepository extends DataRepository {

	private CompilationData cData = null;
	
	private final Map<String, Method> methods = new HashMap<>();
	
	public CompilationDataRepository(String name) {
		super(name);
	}

    @Override
	public void initializeRepository(Object data) throws Exception {
		Method method = null;
		cData = new CompilationData();
		
		method = cData.getClass().getDeclaredMethod("setName", String.class);
        methods.put("Name", method);
        
        method = cData.getClass().getDeclaredMethod("setTotalCompilationTime", long.class);
		methods.put("TotalCompilationTime", method);
		
		addData("CompilationMX", data);
		
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
			methods.get(attribute.getName()).invoke(cData, attribute.getValue());
		}
	}
	
	public CompilationData getCompilationData() {
		return cData;
	}
}
