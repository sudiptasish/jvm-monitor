package com.sc.hm.monitor.shared.classes;

import java.lang.management.ClassLoadingMXBean;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sc.hm.monitor.shared.IMBeanObject;

public class ClassMBeanSharedObject implements IMBeanObject {
	
	//public static final String CLASS_TYPE_ALL = "All Classes";
	public static final String CLASS_TYPE_TOTAL = "Total Class";
	public static final String CLASS_TYPE_LOADED = "Loaded Class";
	public static final String CLASS_TYPE_UNLOADED = "Unloaded Class";

	private Map<String, ClassTypeInfo> classesMap = new HashMap<String, ClassTypeInfo>();
	
	private List<Long> timeList = new ArrayList<Long>();
	
	public ClassMBeanSharedObject() {
	}
	
	public void updateClassMBean(ClassLoadingMXBean cmBean) throws Exception, UndeclaredThrowableException {
		ClassTypeInfo classInfo = classesMap.get(CLASS_TYPE_TOTAL);
		if (classInfo == null) {
			classInfo = new ClassTypeInfo(CLASS_TYPE_TOTAL);
		}
		classInfo.updateTypeInfo(cmBean.getTotalLoadedClassCount());
		classesMap.put(CLASS_TYPE_TOTAL, classInfo);
		
		classInfo = classesMap.get(CLASS_TYPE_LOADED);
		if (classInfo == null) {
			classInfo = new ClassTypeInfo(CLASS_TYPE_LOADED);
		}
		classInfo.updateTypeInfo(cmBean.getLoadedClassCount());
		classesMap.put(CLASS_TYPE_LOADED, classInfo);
		
		classInfo = classesMap.get(CLASS_TYPE_UNLOADED);
		if (classInfo == null) {
			classInfo = new ClassTypeInfo(CLASS_TYPE_UNLOADED);
		}
		classInfo.updateTypeInfo(cmBean.getUnloadedClassCount());
		classesMap.put(CLASS_TYPE_UNLOADED, classInfo);
	}
	
	public ClassTypeInfo getTargetClassType(String classType) {
		if (classesMap.containsKey(classType)) {
			return classesMap.get(classType);
		}
		return null;
	}

	public String[] getAllClassTypes() {
		return new String[] {CLASS_TYPE_TOTAL, CLASS_TYPE_LOADED, CLASS_TYPE_UNLOADED};
	}
	
	public void addTime(long current_time) {
		timeList.add(current_time);
	}
	
	public List<Long> getTimeList() {
		return timeList;
	}

	public String toString() {
		return "[" + this.classesMap + "]";
	}
}
