package com.sc.hm.vmxd.data;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;

import com.sc.hm.vmxd.data.classes.ClassData;

/**
 * Repository to store the class loading information.
 * Classes are loaded and/or unloaded over a period of time. Such trend will be
 * stored in this repository.
 * 
 * @author Sudiptasish Chanda
 */
public class ClassDataRepository extends DataRepository {
	
	public static final String CLASS_TYPE_TOTAL = "Total Class";
	public static final String CLASS_TYPE_LOADED = "Loaded Class";
	public static final String CLASS_TYPE_UNLOADED = "Unloaded Class";

	private ClassData classData = null;
	
	private final Map<String, Method> methods = new HashMap<>();

	public ClassDataRepository(String name) {
		super(name);
	}

    @Override
	public void initializeRepository(Object obj) throws Exception {
		Method method = null;        
        classData = new ClassData("ClassLoading");
        
		method = classData.getClass().getMethod("setTotalLoadedClassCount", Long.class);
		methods.put("TotalLoadedClassCount", method);
		
		method = classData.getClass().getMethod("setUnloadedClassCount", Long.class);
		methods.put("UnloadedClassCount", method);
		
		method = classData.getClass().getMethod("setLoadedClassCount", Integer.class);
		methods.put("LoadedClassCount", method);
		
		addData("ClassLoading", obj);
		
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
			methods.get(attribute.getName()).invoke(classData, attribute.getValue());
		}
	}
	
	public List<Long> getTargetClassTypeUsage(String classType) {
		if (classType.equals(CLASS_TYPE_TOTAL)) {
			return classData.getTotalLoadedClassCount();
		}
		else if (classType.equals(CLASS_TYPE_LOADED)) {
			return classData.getLoadedClassCount();
		}
		else {
			return classData.getUnloadedClassCount();
		}
	}
	
	public ClassData getClassData() {
		return classData;
	}
	
	public long getMaxCount() {
		return classData.getMax_count();
	}
	
	public long getMinCount() {
		return classData.getMin_count();
	}
	
	public List<Date> getTimeList() {
		return classData.getTimeList();
	}
	
	public String[] getAllClassTypes() {
		return new String[] {CLASS_TYPE_TOTAL, CLASS_TYPE_LOADED, CLASS_TYPE_UNLOADED};
	}
}
