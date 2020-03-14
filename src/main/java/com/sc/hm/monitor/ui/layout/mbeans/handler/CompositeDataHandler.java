package com.sc.hm.monitor.ui.layout.mbeans.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.openmbean.CompositeData;

import com.sc.hm.monitor.ui.layout.mbeans.table.model.DataTableModel;

public class CompositeDataHandler extends ComplexDataHandler {
	
	public CompositeDataHandler(String dataHandlerName) {
		this(dataHandlerName, null, null);
	}

	public CompositeDataHandler(String dataHandlerName, String dataTypeName, AbstractData data) {
		super(dataHandlerName, dataTypeName, data);
	}
	
	public void handleData() throws Exception {
		handleData(new CompositeData[] {(CompositeData)getData().getCelldata()});
	}
	
	public void handleData(CompositeData[] compositeData) throws Exception {
		List<Map<String, Object>> tabularDataList = new ArrayList<Map<String,Object>>(compositeData.length);
		
		for (int i = 0; i < compositeData.length; i ++) {
			if (compositeData[i] != null) {
				Set<String> keys = compositeData[i].getCompositeType().keySet();		
				Map<String, Object> dataMap = new LinkedHashMap<String, Object>(keys.size());
				
				for (String key : keys) {
					dataMap.put(key, compositeData[i].get(key));
				}
				tabularDataList.add(dataMap);
			}
		}
		if (tabularDataList.size() > 0) {
			createAndShowAbstractDataPanel(getDataHandlerName(), new DataTableModel(getDataHandlerName()), tabularDataList);
		}
	}
}
