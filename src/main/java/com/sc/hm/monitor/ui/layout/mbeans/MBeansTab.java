package com.sc.hm.monitor.ui.layout.mbeans;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.management.ManagementFactory;
import java.util.Enumeration;

import javax.management.ObjectName;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.shared.MBeanSharedObjectRepository;
import com.sc.hm.monitor.ui.layout.mbeans.table.MBeanTreeNode;
import com.sc.hm.monitor.ui.layout.mbeans.table.adapter.MXBeanAdapterDataCache;
import com.sc.hm.monitor.ui.layout.mbeans.table.adapter.MXBeanDomainAdapter;
import com.sc.hm.monitor.ui.layout.mbeans.table.model.common.MBeanTableModel;
import com.sc.hm.monitor.ui.layout.panel.VMStaticTab;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.ManagementUtil;

public class MBeansTab extends VMStaticTab implements TreeSelectionListener, MouseListener, ActionListener, TableModelListener {
	
	public static final long serialVersionUID = 1L;

	private MXBeanDomainAdapter mxBeanAdapter = new MXBeanDomainAdapter();
	
	private int panel_width = 880;
	private int panel_height = 670;
	
	private Dimension dim = new Dimension(panel_width, panel_height);
	
	private JTree mbeansTree = null;
	
	private JSplitPane splitPane = new JSplitPane();
	
	private JPanel null_panel = new JPanel();
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	/*
	private JTabbedPane tabbedPane = new JTabbedPane();
	private AttributeTab attributeTab = new AttributeTab();
	private OperationTab operationTab = new OperationTab();
	private NotificationTab notificationTab = new NotificationTab();
	private InfoTab infoTab = new InfoTab();
	*/	
	public MBeansTab() throws Exception {
		super();
		setSize(dim);
		setLayout(null);
		initMBeanAdapter();
		initializeComponent();
		addComponent();
	}
	
	public Dimension getSize() {
		return dim;
	}
	
	public Dimension getPreferredSize() {
		return dim;
	}
	
	public void initMBeanAdapter() throws Exception {
		ApplicationConfiguration appConfig = ApplicationConfiguration.getInstance();
		if (ManagementUtil.JVM_TYPE_LOCAL.equals(appConfig.getProperty("JVM_TYPE", ManagementUtil.JVM_TYPE_LOCAL))) {
			mxBeanAdapter.initializeLocalMXBeanInfo();
		}
		else {
			String server = appConfig.getProperty(ManagementUtil.HOST);
			String port = appConfig.getProperty(ManagementUtil.PORT);
			String jmxURL = appConfig.getProperty(ManagementUtil.URL);
			
			if (server != null && !"".equals(server) && port != null && !"".equals(port)) {
				mxBeanAdapter.configureMXBeanServerHandler(server, port);
			}
			else if (jmxURL != null && !"".endsWith(jmxURL)) {
				mxBeanAdapter.configureMXBeanServerHandler(jmxURL);
			}
			else {
				throw new Exception ("Error!!! Can not Initialize MBEAN Adapter");
			}
			mxBeanAdapter.initializeRemoteMXBeanInfo();
		}
	}
	
	public void initializeComponent() throws Exception {
		splitPane.setSize(new Dimension(880, 670));
		splitPane.setDividerLocation(180);
		splitPane.setDividerSize(20);
		
		null_panel.setSize(new Dimension(680, 500));
		
		DefaultMutableTreeNode rootNode = createMBeansHierarchy();
		mbeansTree = new JTree(rootNode);
		mbeansTree.setFont(font);
		
		mbeansTree.addMouseListener(this);
		
		JScrollPane scrollPane = new JScrollPane(mbeansTree);
		scrollPane.setBounds(0, 0, 180, 615);
		
		JButton button = new JButton("Refresh");
		button.setBounds(40, 625, 90, 20);
		button.addActionListener(this);
		button.setFont(font);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(180, 670));
		panel.add(scrollPane);
		panel.add(button);
		
		splitPane.setLeftComponent(panel);
		/*
		tabbedPane.addTab("Attributes", attributeTab);
		tabbedPane.addTab("Operation", operationTab);
		tabbedPane.addTab("Notification", notificationTab);
		tabbedPane.addTab("Info", infoTab);
		*/
		
		splitPane.setRightComponent(new JPanel());
	}
	
	public void refreshMBeans() throws Exception {
		mbeansTree.removeMouseListener(this);
		mxBeanAdapter.refreshRemoteMXBeanInfo();
		
		DefaultMutableTreeNode rootNode = createMBeansHierarchy();
		mbeansTree = new JTree(rootNode);
		mbeansTree.setFont(font);
		mbeansTree.addMouseListener(this);
		
		JScrollPane scrollPane = new JScrollPane(mbeansTree);
		scrollPane.setBounds(0, 0, 180, 615);
		
		JButton button = new JButton("Refresh");
		button.setFont(font);
		button.setBounds(40, 625, 90, 20);
		button.addActionListener(this);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(180, 670));
		panel.add(scrollPane);
		panel.add(button);
		
		splitPane.setLeftComponent(panel);
		splitPane.setRightComponent(new JPanel());
	}
	
	public void refreshMBeansByName(ObjectName objectName) throws Exception {
		mbeansTree.removeMouseListener(this);
		mxBeanAdapter.refreshRemoteMXBeanInfoByName(objectName);
		
		DefaultMutableTreeNode rootNode = createMBeansHierarchy();
		mbeansTree = new JTree(rootNode);
		mbeansTree.addMouseListener(this);
		
		JScrollPane scrollPane = new JScrollPane(mbeansTree);
		scrollPane.setBounds(0, 0, 180, 615);
		
		JButton button = new JButton("Refresh");
		button.setBounds(40, 625, 90, 20);
		button.setFont(font);
		button.addActionListener(this);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(180, 670));
		panel.add(scrollPane);
		panel.add(button);
		
		splitPane.setLeftComponent(panel);
		splitPane.setRightComponent(new JPanel());	
	}
	
	public boolean invokeAttributeChanged(ObjectName objectName, Object attributeName, Class attributeValueClass, Object attributeValue) {
		try {
			mxBeanAdapter.invokeAttributeChanged(objectName, (String)attributeName, attributeValueClass, attributeValue);
			return true;
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Can Not Change Value. Attribute May not Supported\nMessage: " + e.getMessage());
		}
		return false;
	}
	
	public void addComponent() {
		add(splitPane);
	}
	
	public DefaultMutableTreeNode createMBeansHierarchy() {
		DefaultMutableTreeNode rootNode = null;
		try {
			rootNode = createChildNodeForStandardMXBean();
		}
		catch (Exception e)  {
			e.printStackTrace();
		}
		return rootNode;
	}
	
	private DefaultMutableTreeNode createChildNodeForStandardMXBean() throws Exception {
		MBeanTreeNode tree = MXBeanAdapterDataCache.getDefaultMBeanTreeStructure();
		
		MBeanNode node = new MBeanNode(tree.getName());
		if (tree.hasChildren()) {
			buildTreeRecursively(node, tree);
		}
		return node;
	}
	
	private void buildTreeRecursively(MBeanNode parentNode, MBeanTreeNode treeNode) {
		for (Enumeration<MBeanTreeNode> enm = treeNode.enumerate(); enm.hasMoreElements();) {
			MBeanTreeNode child = enm.nextElement();
			JComponent nodeComponent = createTreeNodeComponent(child.getData());
			MBeanNode mbeanNode = new MBeanNode(new MBeansNodeData(child.getName(), nodeComponent));
			parentNode.add(mbeanNode);
			
			if (child.hasChildren()) {
				buildTreeRecursively(mbeanNode, child);
			}
		}
	}
	
	private JComponent createTreeNodeComponent (Object data) {
		if (data == null) {
			return null_panel;
		}
		else if (data instanceof Component) {
			return (JComponent)data;
		}
		MBeanTableModel model = (MBeanTableModel)data;
		JTable table = new JTable(model);
		table.setFont(font);
		table.addMouseListener(new TableDataListener());
		/*
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					JTable target = (JTable)me.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					Object value = target.getModel().getValueAt(row, column);
					Logger.log(value.getClass());
					if (value instanceof String[]) {
						String[] val = (String[])value;
						StringBuilder msgBuilder = new StringBuilder(8 * val.length);
						for (short i = 0; i < val.length; i ++) {
							msgBuilder.append(val[i]).append("\n");
						}
						JOptionPane.showMessageDialog(target, msgBuilder.toString());
					}
					else if (value instanceof long[]) {
						long[] val = (long[])value;
						StringBuilder msgBuilder = new StringBuilder(2 * val.length);
						for (short i = 0; i < val.length; i ++) {
							msgBuilder.append(val[i]).append("\n");
						}
						JOptionPane.showMessageDialog(target, msgBuilder.toString());
					}
					else if (value instanceof CompositeData) {
						CompositeData compositeData = (CompositeData)value;
						StringBuilder msgBuilder = new StringBuilder(20);						
						Set<String> keys = compositeData.getCompositeType().keySet();
						
						for (String key : keys) {
							JTextField textField = new JTextField();
							msgBuilder.append(key).append(": ").append(compositeData.get(key));
						}
						JFrame frame = new JFrame("CompositeData Details");
						JOptionPane.showMessageDialog(target, msgBuilder.toString());
					}
					else if (value instanceof sun.management.GcInfoCompositeData) {
						sun.management.GcInfoCompositeData gcInfoCompositeData = (sun.management.GcInfoCompositeData)value;
						StringBuilder msgBuilder = new StringBuilder(20);
						
						Set<String> keys = gcInfoCompositeData.getCompositeType().keySet();
						for (String key : keys) {
							Logger.log(key + " :: " + gcInfoCompositeData.get(key));
						}
					}
				}
			}
		});
		*/
		table.getModel().addTableModelListener(this);
		TableColumn column = table.getColumnModel().getColumn(1);
		column.setCellEditor(new MyTableCellEditor());
		
		JScrollPane tablePane = new JScrollPane(table);
		JButton attrButton = new JButton("Refresh Attributes");
		attrButton.setFont(font);
		if (model.getMbeansObjectName() != null) {
			attrButton.setActionCommand(model.getMbeansObjectName().toString());
		}
		tablePane.setBounds(0, 0, 680, 615);
		attrButton.setBounds(250, 625, 210, 20);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(new Dimension(680, 670));
		panel.add(tablePane);
		panel.add(attrButton);
		
		return panel;
	}
	
	public void displayNodeDetails(Object nodeName) {
		Logger.log("Displaying Node Info: " + nodeName);
		if (((MBeanNode)nodeName).getNodeData() != null) {
			JComponent component = ((MBeanNode)nodeName).getNodeData().getTable();
			SwingUtilities.updateComponentTreeUI(component);
			splitPane.setRightComponent(component);
		}
	}
	
	public void valueChanged(TreeSelectionEvent te) {
		Logger.log(te.getPath());
	}
	
	public void mouseClicked(MouseEvent me) {
		if (mbeansTree.getSelectionPath() != null) {
			Logger.log("Selected Node: " + mbeansTree.getSelectionPath().getLastPathComponent());
			displayNodeDetails(mbeansTree.getSelectionPath().getLastPathComponent());
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equalsIgnoreCase("Refresh")) {
			try {
				refreshMBeans();
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(this, "Error!!! Refreshing MBeans Tree.\nMessage: " + e.getMessage());
			}
		}
		else {
			String name = ae.getActionCommand();
			try {
				refreshMBeansByName(new ObjectName(name));
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(this, "Error!!! Refreshing Attribute List.\nMessage: " + e.getMessage());
			}
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void tableChanged(TableModelEvent tme) {
		TableModel model = (TableModel)tme.getSource();
		Logger.log("Changed....Row: " + tme.getFirstRow() + ". Col: " + tme.getColumn() + ". Type: " + tme.getType());
	}
	
	class MyTableCellEditor extends AbstractCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        
		private int row = -1;
        private int col = 0;
        private JTable changedTable = null;
        // This is the component that will handle the editing of the cell value
        JComponent component = new JTextField();
    
        // This method is called when a cell value is edited by the user.
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex) {
            // 'value' is value contained in the cell located at (rowIndex, vColIndex)			
            if (isSelected) {
            	// cell (and perhaps other cells) are selected
            }
    
            // Configure the component with the specified value
            ((JTextField)component).setText("" + value);
            changedTable = table;
            row = rowIndex;
            
            // Return the configured component
            return component;
        }
    
        // This method is called when editing is completed.
        // It must return the new value to be stored in the cell.
        public Object getCellEditorValue() {
			MBeanTableModel mbeanTableModel = (MBeanTableModel)changedTable.getModel();
			ObjectName objectName = mbeanTableModel.getMbeansObjectName();
			
			if (invokeAttributeChanged(objectName, mbeanTableModel.getValueAt(row, col), mbeanTableModel.getOriginalColumnClass(row, 1), ((JTextField)component).getText())) {
				Logger.log("Attribute Changed.... New Value: " + ((JTextField)component).getText());
				mbeanTableModel.setValueAt(((JTextField)component).getText(), row, 1);
				if (objectName.getCanonicalName().startsWith(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE) || objectName.getKeyProperty("type").equalsIgnoreCase("MemoryPool")) {
					MBeanSharedObjectRepository.getInstance().getMpool_mx_bean().setChangedPoolConfig(objectName.getKeyProperty("name"));
				}
			}
			else {
				((JTextField)component).setText(mbeanTableModel.getValueAt(row, 1).toString());
				mbeanTableModel.fireTableCellUpdated(row, 1);
			}			
            return ((JTextField)component).getText();
        }
    }
}
