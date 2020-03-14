package com.sc.hm.monitor.invoker;

import java.lang.reflect.Method;

public class InternalInvoker {

	public static Object invoke(Class _class, String _method, Class[] _cArray, Object _argArray) throws Exception {
		Method method = _class.getDeclaredMethod(_method, _cArray);
		return method.invoke(_class.newInstance(), _argArray);
	}
	
	public static Object invoke(Object _object, String _method, Class[] _cArray, Object _argArray) throws Exception {
		Method method = _object.getClass().getDeclaredMethod(_method, _cArray);
		return method.invoke(_object, _argArray);
	}
}
