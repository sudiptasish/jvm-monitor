package com.sc.hm.monitor.ui.layout.mbeans.tab;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.sc.hm.monitor.ui.layout.mbeans.table.model.DataTableModel;
import com.sc.hm.monitor.util.Logger;

public class DataTreePanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private int panel_width = 0;
	private int panel_height = 0;
	
	private Dimension dim = null;
	
	private AbstractTableModel model = null;
	private JTable table = null;
	private JScrollPane scrollPane = null;
	
	private JButton leftArrow2 = new JButton("<");
	private JLabel label2 = new JLabel("Tabular Navigation [Top]");
	private JButton rightArrow2 = new JButton(">");
	
	private JButton leftArrow = new JButton("<");
	private JLabel label = new JLabel("Tabular Navigation");
	private JButton rightArrow = new JButton(">");
	
	private List<Map<String, Object>> tabularDataMap = null;
	private Stack<List<Map<String, Object>>> stack = new Stack<List<Map<String,Object>>>();
	private int current_index = 0;
	
	private Font font = new Font("Arial", Font.PLAIN, 11);

	public DataTreePanel(int width, int height) {
		super();
		this.panel_width = width;
		this.panel_height = height;
		dim = new Dimension(panel_width, panel_height);
		configurePanel();		
	}
	
	private void configurePanel() {
		setSize(dim);
		setLayout(null);
	}
	
	public Dimension getSize() {
		return dim;
	}
	
	public Dimension getPreferredSize() {
		return dim;
	}
	
	public int getWidth() {
		return panel_width;
	}
	
	public int getHeight() {
		return panel_height;
	}

	public TableModel getModel() {
		return model;
	}

	public void setModel(AbstractTableModel model) {
		this.model = model;
		this.model.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent te) {
				Logger.log(te.getFirstRow());
			}			
		});
	}
	
	public List<Map<String, Object>> getTabularDataMap() {
		return tabularDataMap;
	}

	public void setTabularDataMap(List<Map<String, Object>> tabularDataMap) {
		this.tabularDataMap = tabularDataMap;
	}

	public void configureAndAddComponent() {
		leftArrow2.setActionCommand("LEFT_ARROW_2");
		leftArrow2.setBounds(45, 10, 50, 18);
		label2.setBounds(130, 10, 200, 18);
		rightArrow2.setActionCommand("RIGHT_ARROW_2");
		rightArrow2.setBounds(280, 10, 50, 18);
		
		leftArrow.setActionCommand("LEFT_ARROW");
		leftArrow.setBounds(45, 40, 50, 18);
		label.setBounds(130, 40, 200, 18);
		rightArrow.setActionCommand("RIGHT_ARROW");
		rightArrow.setBounds(280, 40, 50, 18);
		
		leftArrow2.setFont(font);
		label2.setFont(font);
		rightArrow.setFont(font);
		leftArrow.setFont(font);
		label.setFont(font);
		rightArrow.setFont(font);
		
		
		((DataTableModel)model).initializeTableRowData(tabularDataMap.get(current_index));
		table = new JTable(model);
		table.setFont(font);
		//table.setBorder(new TitledBorder(new EtchedBorder(), "Composite Data"));
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mev) {
				boolean changed = false;
				if (mev.getClickCount() == 2) {
					JTable table = (JTable)mev.getSource();
					int colIndex = table.getSelectedColumn();
					if (colIndex == 0) {
						return;
					}
					List<Map<String, Object>> dataList = null;
					Object cellData = table.getModel().getValueAt(table.getSelectedRow(), colIndex);
					
					if (cellData instanceof CompositeData) {
						current_index = 0;
						dataList = new ArrayList<Map<String,Object>>(1);		
						CompositeData compositeData = (CompositeData)cellData;
						Set<String> keys = compositeData.getCompositeType().keySet();
						Map<String, Object> tableMap = new HashMap<String, Object>();
						for (String key : keys) {
							tableMap.put(key, compositeData.get(key));
						}
						dataList.add(tableMap);
						changed = true;
					}
					else if (cellData instanceof CompositeData[]) {
						CompositeData[] compositeDataArr = (CompositeData[])cellData;
						if (compositeDataArr != null && compositeDataArr.length > 0) {
							current_index = 0;
							dataList = new ArrayList<Map<String,Object>>(compositeDataArr.length);		
							for (CompositeData compositeData : compositeDataArr) {
								Set<String> keys = compositeData.getCompositeType().keySet();
								Map<String, Object> tableMap = new HashMap<String, Object>();
								for (String key : keys) {
									tableMap.put(key, compositeData.get(key));
								}
								dataList.add(tableMap);
							}						
							changed = true;
						}
					}
					else if (cellData instanceof TabularData) {
						current_index = 0;
						TabularData tabularData = (TabularData)cellData;
						Set<?> keySet = tabularData.keySet();
						
						dataList = new ArrayList<Map<String,Object>>(keySet.size());		
						for (Object keys : keySet) {
							Object[] keyValues = ((List<?>)keys).toArray();
							CompositeData compositeData = tabularData.get(keyValues);
							Set<String> set = compositeData.getCompositeType().keySet();
							Map<String, Object> tableMap = new HashMap<String, Object>();
							for (String str : set) {
								tableMap.put(str, compositeData.get(str));
							}
							dataList.add(tableMap);
						}
						changed = true;
					}
					else if (cellData instanceof Map) {
						Map<String, Object> map = (Map<String, Object>)cellData;
						dataList = new ArrayList<Map<String,Object>>(1);
						changed = true;						
					}
					if (changed) {
						stack.push(tabularDataMap);
						tabularDataMap = dataList;
						((DataTableModel)model).initializeTableRowData(tabularDataMap.get(current_index));
						checkButtonStatus();
						model.fireTableDataChanged();						
					}
				}
			}
		});
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(5, 80, 375, 180);
		checkButtonStatus();
				
		leftArrow.addActionListener(this);
		rightArrow.addActionListener(this);
		leftArrow2.addActionListener(this);		
		rightArrow2.addActionListener(this);
		
		addAllComponent();
	}
	
	private void checkButtonStatus() {
		leftArrow2.setEnabled(false);
		rightArrow2.setEnabled(false);
		leftArrow.setEnabled(false);
		rightArrow.setEnabled(true);
		if (current_index == tabularDataMap.size() - 1) {
			rightArrow.setEnabled(false);
		}
		if (stack.size() > 0) {
			leftArrow2.setEnabled(true);
		}
	}
	
	private void addAllComponent() {
		add(leftArrow);
		add(label);
		add(rightArrow);
		add(leftArrow2);
		add(label2);
		add(rightArrow2);
		add(scrollPane);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equalsIgnoreCase("LEFT_ARROW")) {
			current_index --;
			Map<String, Object> temp = tabularDataMap.get(current_index);
			int rowIndex = 0, colIndex = 0;
			for (Map.Entry<String, Object> me : temp.entrySet()) {
				model.setValueAt(me.getKey(), rowIndex, colIndex);
				model.setValueAt(me.getValue(), rowIndex ++, colIndex + 1);
			}
			model.fireTableDataChanged();
			if (current_index == 0) {
				leftArrow.setEnabled(false);
			}
			if (current_index < tabularDataMap.size() - 1) {
				rightArrow.setEnabled(true);
			}
		}
		else if (ae.getActionCommand().equalsIgnoreCase("RIGHT_ARROW")) {
			current_index ++;
			Map<String, Object> temp = tabularDataMap.get(current_index);
			int rowIndex = 0, colIndex = 0;
			for (Map.Entry<String, Object> me : temp.entrySet()) {
				model.setValueAt(me.getKey(), rowIndex, colIndex);
				model.setValueAt(me.getValue(), rowIndex ++, colIndex + 1);
			}
			model.fireTableDataChanged();
			if (current_index == tabularDataMap.size() - 1) {
				rightArrow.setEnabled(false);
			}
			if (current_index > 0) {
				leftArrow.setEnabled(true);
			}
		}
		else if (ae.getActionCommand().equalsIgnoreCase("LEFT_ARROW_2")) {
			current_index = 0;
			tabularDataMap = stack.pop();
			((DataTableModel)model).initializeTableRowData(tabularDataMap.get(current_index));
			model.fireTableDataChanged();
			if (stack.size() == 0) {
				leftArrow2.setEnabled(false);
			}
			if (current_index == 0) {
				leftArrow.setEnabled(false);
			}
			if (current_index == tabularDataMap.size() - 1) {
				rightArrow.setEnabled(false);
			}
		}
	}
}
