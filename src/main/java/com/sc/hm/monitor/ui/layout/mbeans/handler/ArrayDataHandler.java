package com.sc.hm.monitor.ui.layout.mbeans.handler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sc.hm.monitor.ui.layout.mbeans.table.model.DataTableModel;

public class ArrayDataHandler extends BasicDataHandler {

	public ArrayDataHandler(String dataHandlerName, String dataTypeName, AbstractData data) {
		super(dataHandlerName, dataTypeName, data);
	}
	
	public void handleData() throws Exception {
		List<Map<String, Object>> basicDataList = new ArrayList<Map<String,Object>>(1);
		
		Object arr = getData().getCelldata();
		int length = Array.getLength(arr);
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>(length);
		
		for (short index = 0; index < length; index ++) {
			dataMap.put(String.valueOf(index + 1), Array.get(arr, index));
		}
		basicDataList.add(dataMap);
		
		createAndShowAbstractDataPanel(getDataHandlerName(), new DataTableModel(getDataHandlerName()), basicDataList);
	}
}
