package com.sc.hm.monitor.ui.layout.mbeans.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import com.sc.hm.monitor.ui.layout.mbeans.table.model.DataTableModel;

public class TabularDataHandler extends ComplexDataHandler {

	public TabularDataHandler(String dataHandlerName, String dataTypeName, AbstractData data) {
		super(dataHandlerName, dataTypeName, data);
	}
	
	public void handleData() throws Exception {
		TabularData tabularData = (TabularData)getData().getCelldata();		
		Set<?> keySet = tabularData.keySet();
				
		List<Map<String, Object>> tabularDataList = new ArrayList<Map<String,Object>>(keySet.size());		
		for (Object keys : keySet) {
			Object[] keyValues = ((List<?>)keys).toArray();
			CompositeData compositeData = tabularData.get(keyValues);
			
			Set<String> set = compositeData.getCompositeType().keySet();
			Map<String, Object> tableMap = new LinkedHashMap<String, Object>();
			for (String str : set) {
				tableMap.put(str, compositeData.get(str));
			}
			tabularDataList.add(tableMap);
		}		
		createAndShowAbstractDataPanel(getDataHandlerName(), new DataTableModel(getDataHandlerName()), tabularDataList);
	}
}
