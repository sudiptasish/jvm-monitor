package com.sc.hm.monitor.ui.layout.mbeans.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sc.hm.monitor.ui.layout.mbeans.table.model.DataTableModel;

public class MapDataHandler extends BasicDataHandler {

	public MapDataHandler(String dataHandlerName, String dataTypeName, AbstractData data) {
		super(dataHandlerName, dataTypeName, data);
	}
	
	public void handleData() throws Exception {
		Map<String, Object> map = (Map<String, Object>)getData().getCelldata();
		
		List<Map<String, Object>> mapDataList = new ArrayList<Map<String,Object>>(1);
		mapDataList.add(map);
		
		createAndShowAbstractDataPanel(getDataHandlerName(), new DataTableModel(getDataHandlerName()), mapDataList);		
	}
}
