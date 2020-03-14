package com.sc.hm.vmxd.synchui.layout.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.sc.hm.monitor.util.GraphImageProperties;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.ClassDataRepository;
import com.sc.hm.vmxd.data.GarbageCollectorDataRepository;
import com.sc.hm.vmxd.data.MemoryDataRepository;
import com.sc.hm.vmxd.data.MemoryPoolDataRepository;

public class SynchUISelectionPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	public static final String PANEL_TYPE_MEMORY = "PANEL_TYPE_MEMORY";
	public static final String PANEL_TYPE_MEMORYPOOL = "PANEL_TYPE_MEMORYPOOL";
	public static final String PANEL_TYPE_THREAD = "PANEL_TYPE_THREAD";
	public static final String PANEL_TYPE_CLASS = "PANEL_TYPE_CLASS";
	public static final String PANEL_TYPE_GBCOLLECTOR = "PANEL_TYPE_GBCOLLECTOR";
	
	private String panelName = "";

	private SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED sharedObj = null;

	private int panel_width = 880;
	private int panel_height = 80;

	private Dimension dim = new Dimension(panel_width, panel_height);

	private JLabel m_label = new JLabel("Select");
	private JLabel t_label = new JLabel("Duration");
	private JLabel xc_label = new JLabel("Axis Color");
	private JLabel gc_label = new JLabel("Graph Color");
	private JLabel d_label = new JLabel("Graph Delay");
	private JLabel gb_label = new JLabel("Stroke Level");

	private JComboBox m_comboBox = new JComboBox();
	private JComboBox t_comboBox = new JComboBox();
	private JComboBox xc_comboBox = new JComboBox();
	private JComboBox gc_comboBox = new JComboBox();
	private JComboBox d_comboBox = new JComboBox();
	private JComboBox gb_comboBox = new JComboBox();
	
	private JCheckBox x_checkBox = new JCheckBox("Dynamic Axis");
	private JCheckBox sx_checkBox = new JCheckBox("Show X");
	private JCheckBox sy_checkBox = new JCheckBox("Show Y");
	private JCheckBox fill_checkBox = new JCheckBox("Fill G");
	
	private JCheckBox committedCheck = new JCheckBox("Show C");
		
	private JButton stopButton = new JButton("Pause");
	private JButton reStartButton = new JButton("Resume");
	private JButton gcButton = new JButton("Persist");
	
	private JFileChooser fileChooser = new JFileChooser();

	private String[] availableColors = {"BLACK", "BLUE", "CYAN", "GRAY", "GREEN", "MAGENTA", "ORANGE", "RED", "WHITE", "YELLOW"};
	
	private Font font = new Font("Arial", Font.PLAIN, 11);
	
	private ComboBoxListener comboBoxListener = new ComboBoxListener();
	
	private CheckBoxListener checkboxListener = new CheckBoxListener();
	
	private String applicationId = "";
	
	public SynchUISelectionPanel(String applicationId, String name, SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj) throws Exception {
		this.applicationId = applicationId;
		panelName = name;
		sharedObj = obj;
		initOther();
		setSize(dim);
		setBorder(new BevelBorder(BevelBorder.RAISED));
		setLayout(null);
		initializeComponent();
		addComponent();
	}

	public Dimension getSize() {
		return dim;
	}

	public Dimension getPreferredSize() {
		return dim;
	}

	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
	}
	
	private void initOther() {
		
	}
	
	private void initializeComponent() {
		m_label.setFont(font);
		t_label.setFont(font);
		xc_label.setFont(font);
		gc_label.setFont(font);
		d_label.setFont(font);
		gb_label.setFont(font);
		
		m_comboBox.setFont(font);
		t_comboBox.setFont(font);
		xc_comboBox.setFont(font);
		gc_comboBox.setFont(font);
		d_comboBox.setFont(font);
		gb_comboBox.setFont(font);
		
		x_checkBox.setFont(font);
		sx_checkBox.setFont(font);
		sy_checkBox.setFont(font);
		fill_checkBox.setFont(font);
		committedCheck.setFont(font);
		
		stopButton.setFont(font);
		reStartButton.setFont(font);
		gcButton.setFont(font);
	}

	private void addComponent() throws Exception {
		final FontMetrics fontMetrics = getFontMetrics(font);
		int leftPadding = 15;
		int width = 0;
		
		width = fontMetrics.stringWidth(m_label.getText());
		m_label.setBounds(leftPadding, 15, width, 18);
		leftPadding += width + 5;
		m_comboBox.setBounds(leftPadding, 15, 90, 18);

		leftPadding += 90 + 10;
		width = fontMetrics.stringWidth(t_label.getText());
		t_label.setBounds(leftPadding, 15, width, 18);
		leftPadding += width + 5;
		t_comboBox.setBounds(leftPadding, 15, 80, 18);

		leftPadding += 80 + 10;
		width = fontMetrics.stringWidth(xc_label.getText());
		xc_label.setBounds(leftPadding, 15, width, 18);
		leftPadding += width + 5;
		xc_comboBox.setBounds(leftPadding, 15, 70, 18);

		leftPadding += 70 + 10;
		width = fontMetrics.stringWidth(gc_label.getText());
		gc_label.setBounds(leftPadding, 15, width, 18);
		leftPadding += width + 5;
		gc_comboBox.setBounds(leftPadding, 15, 70, 18);

		leftPadding += 70 + 10;
		width = fontMetrics.stringWidth(d_label.getText());
		d_label.setBounds(leftPadding, 15, width, 18);
		leftPadding += width + 5;
		d_comboBox.setBounds(leftPadding, 15, 80, 18);
		
		leftPadding += 80 + 10;
		width = fontMetrics.stringWidth(gb_label.getText());
		gb_label.setBounds(leftPadding, 15, width, 18);
		leftPadding += width + 5;
		gb_comboBox.setBounds(leftPadding, 15, 80, 18);
		
		x_checkBox.setMnemonic(KeyEvent.VK_D);
		x_checkBox.setBounds(15, 50, 100, 18);
		
		sx_checkBox.setBounds(125, 50, 80, 18);
		sx_checkBox.setMnemonic(KeyEvent.VK_X);
		sx_checkBox.setSelected(true);
		
		sy_checkBox.setBounds(215, 50, 80, 18);
		sy_checkBox.setMnemonic(KeyEvent.VK_Y);
		sy_checkBox.setSelected(true);
		
		fill_checkBox.setBounds(305, 50, 80, 18);
		fill_checkBox.setMnemonic(KeyEvent.VK_G);
		fill_checkBox.setSelected(false);
		
		committedCheck.setBounds(395, 50, 80, 18);
		committedCheck.setMnemonic(KeyEvent.VK_C);
		committedCheck.setToolTipText("Show Committed Memory Usage");
		
		stopButton.setActionCommand("PAUSE");
		reStartButton.setActionCommand("RESUME");
		gcButton.setActionCommand("PERSIST");
		stopButton.setBounds(600, 50, 75, 18);
		reStartButton.setBounds(685, 50, 85, 18);
		gcButton.setBounds(780, 50, 85, 18);

		stopButton.setEnabled(true);
		reStartButton.setEnabled(false);

		m_comboBox.setActionCommand("SELECTION_CHANGE");
		String[] selections = null;
		AbstractMBeanDataRepositoryFactory dataRepositoryFactory = AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId);
		if (panelName.equals(PANEL_TYPE_MEMORYPOOL)) {
			selections = ((MemoryPoolDataRepository)dataRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY_POOL)).getMemoryPoolNames();
		}
		else if (panelName.equals(PANEL_TYPE_MEMORY)) {
			selections = ((MemoryDataRepository)dataRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY)).getMemoryNames();
		}
		else if (panelName.equals(PANEL_TYPE_GBCOLLECTOR)) {
			selections = ((GarbageCollectorDataRepository)dataRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR)).getGarbageCollectorNames();
		}
		else if (panelName.equals(PANEL_TYPE_THREAD)) {
			selections = new String[] {"Main Thread"};
		}
		else if (panelName.equals(PANEL_TYPE_CLASS)) {
			selections = ((ClassDataRepository)dataRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_CLASS)).getAllClassTypes();
		}
		for (String option : selections) {
			m_comboBox.addItem(option);
			if (option.equals(sharedObj.getSelectionName())) {
				Logger.log("Selected Pool: " + option);
				m_comboBox.setSelectedItem(option);
			}
		}

		t_comboBox.setActionCommand("SPAN_CHANGE");
		t_comboBox.addItem("1 [Default]");
		t_comboBox.addItem("2");
		t_comboBox.addItem("4");
		t_comboBox.addItem("8");
		t_comboBox.addItem("10");
		t_comboBox.addItem("20");
		t_comboBox.addItem("40");
		t_comboBox.addItem("80");
		t_comboBox.addItem("100");
		t_comboBox.addItem("200");
		t_comboBox.addItem("400");

		xc_comboBox.setActionCommand("AXIS_COLOR_CHANGE");
		for (String color : availableColors) {
			xc_comboBox.addItem(color);
			if (GraphImageProperties.AXIS_COLOR.equals(color)) {
				xc_comboBox.setSelectedItem(color);
			}
		}

		gc_comboBox.setActionCommand("GRAPH_COLOR_CHANGE");
		for (String color : availableColors) {
			gc_comboBox.addItem(color);
			if (GraphImageProperties.GRAPH_COLOR.equals(color)) {
				gc_comboBox.setSelectedItem(color);
			}
		}

		d_comboBox.setActionCommand("DELAY_CHANGE");
		d_comboBox.addItem("4 [Default]");
		d_comboBox.addItem("5");
		d_comboBox.addItem("6");		
		d_comboBox.addItem("10");
		d_comboBox.addItem("60");
		d_comboBox.addItem("3600");
		
		gb_comboBox.setActionCommand("STYLE_CHANGE");
		gb_comboBox.addItem("1 [Default]");
		gb_comboBox.addItem("2");
		gb_comboBox.addItem("3");
		gb_comboBox.addItem("4");
		
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setDialogTitle("Select A Directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFont(font);
		
		m_comboBox.addItemListener(comboBoxListener);
		t_comboBox.addItemListener(comboBoxListener);
		xc_comboBox.addItemListener(comboBoxListener);
		gc_comboBox.addItemListener(comboBoxListener);
		d_comboBox.addItemListener(comboBoxListener);
		gb_comboBox.addItemListener(comboBoxListener);
		
		m_comboBox.addMouseListener(this);
		t_comboBox.addMouseListener(this);

		ButtonListener listener = new ButtonListener(this);
		stopButton.addActionListener(listener);
		reStartButton.addActionListener(listener);
		gcButton.addActionListener(listener);
		
		x_checkBox.addItemListener(checkboxListener);
		sx_checkBox.addItemListener(checkboxListener);
		sy_checkBox.addItemListener(checkboxListener);
		fill_checkBox.addItemListener(checkboxListener);
		committedCheck.addItemListener(checkboxListener);

		add(m_label);
		add(m_comboBox);
		add(t_label);
		add(t_comboBox);
		add(xc_label);
		add(xc_comboBox);
		add(gc_label);
		add(gc_comboBox);
		add(d_label);
		add(d_comboBox);
		add(gb_label);
		add(gb_comboBox);
		add(x_checkBox);
		add(fill_checkBox);
		add(sx_checkBox);
		add(sy_checkBox);
		if (panelName.equals(PANEL_TYPE_MEMORYPOOL) || panelName.equals(PANEL_TYPE_MEMORY)) {
			add(committedCheck);
		}
		add(stopButton);
		add(reStartButton);
		add(gcButton);
	}

	private Color getColorFromString(String colorString) {
		Color color = null;			
		try	{
			Field colorField = Color.class.getDeclaredField(colorString);
			colorField.setAccessible(true);
			color = (Color)colorField.get(null);
		}
		catch (Exception e) {
			e.printStackTrace();
			color = Color.GREEN;
		}
		return color;
	}
	
	private class ButtonListener implements ActionListener {
		private JPanel panel = null;
		
		public ButtonListener(JPanel p) {
			panel = p;
		}

		public void actionPerformed(ActionEvent ae) {
			if (ae.getActionCommand().equals("PAUSE")) {
				sharedObj.setRun(false);
				stopButton.setEnabled(false);
				reStartButton.setEnabled(true);
			}
			else if (ae.getActionCommand().equals("RESUME")) {
				sharedObj.setRun(true);
				stopButton.setEnabled(true);
				reStartButton.setEnabled(false);
				sharedObj.releaseLockAndResumeProcess();
			}
			else if (ae.getActionCommand().equals("PERSIST")) {
				SwingUtilities.updateComponentTreeUI(fileChooser);
				int val = fileChooser.showSaveDialog(panel);
				if (val == JFileChooser.APPROVE_OPTION) {
					sharedObj.setDirectoryLocation(fileChooser.getSelectedFile());
					sharedObj.setPersist(true);
				}
			}
		}		
	}
	
	private class ComboBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent ie) {
			if (ie.getStateChange() == ItemEvent.SELECTED) {
				Object obj = ie.getItemSelectable();
				if (obj == m_comboBox) {
					sharedObj.setSelectionName(m_comboBox.getSelectedItem().toString());
				}
				else if (obj == t_comboBox) {
					String s = t_comboBox.getSelectedItem().toString();
					if (s.indexOf("[") >= 0) {
						s = s.substring(0, 1);
					}
					if (Integer.parseInt(s) > 40) {
						fill_checkBox.setSelected(false);
						fill_checkBox.setEnabled(false);
						sharedObj.setFillCurve(false);
						
						//gb_comboBox.setSelectedIndex(0);
						gb_comboBox.setEnabled(false);
						sharedObj.setBoldFactor(0.1f);
					}
					else {
						fill_checkBox.setEnabled(true);
						gb_comboBox.setEnabled(true);
						String k = gb_comboBox.getSelectedItem().toString();
						if (k.indexOf("[") >= 0) {
							k = k.substring(0, 1);
						}
						sharedObj.setBoldFactor(Float.parseFloat(k));
					}
					sharedObj.setSpan(s);
				}
				else if (obj == xc_comboBox) {
					sharedObj.setAxisColor(getColorFromString(xc_comboBox.getSelectedItem().toString()));					
				}
				else if (obj == gc_comboBox) {
					sharedObj.setGraphColor(getColorFromString(gc_comboBox.getSelectedItem().toString()));
				}
				else if (obj == d_comboBox) {
					String s = d_comboBox.getSelectedItem().toString();
					if (s.indexOf("[") >= 0) {
						s = s.substring(0, 1);
					}
					sharedObj.setGraphDelay(Integer.parseInt(s));
				}
				else if (obj == gb_comboBox) {
					String s = gb_comboBox.getSelectedItem().toString();
					if (s.indexOf("[") >= 0) {
						s = s.substring(0, 1);
					}
					sharedObj.setBoldFactor(Float.parseFloat(s));
				}
			}
		}
	}
	
	private class CheckBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent ie) {
			Object obj = ie.getItem();
			if (obj == x_checkBox) {
				sharedObj.setDynmaicAxis(x_checkBox.isSelected());
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					if (committedCheck.isSelected()) {
						committedCheck.setSelected(false);
						sharedObj.setShowCommittedUsage(committedCheck.isSelected());
					}
				}
			}
			else if (obj == sx_checkBox) {
				sharedObj.setShowX(sx_checkBox.isSelected());
			}
			else if (obj == sy_checkBox) {
				sharedObj.setShowY(sy_checkBox.isSelected());
			}
			else if (obj == fill_checkBox) {
				sharedObj.setFillCurve(fill_checkBox.isSelected());
			}
			else if (obj == committedCheck) {
				sharedObj.setShowCommittedUsage(committedCheck.isSelected());
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					if (x_checkBox.isSelected()) {
						x_checkBox.setSelected(false);
						sharedObj.setDynmaicAxis(x_checkBox.isSelected());
					}
				}
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}
