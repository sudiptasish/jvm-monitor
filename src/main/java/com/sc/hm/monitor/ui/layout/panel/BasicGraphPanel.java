package com.sc.hm.monitor.ui.layout.panel;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class BasicGraphPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int panel_width = 0;
	private int panel_height = 0;
	private Dimension dim = null;
	
	public BasicGraphPanel() {
		setLayout(null);
	}
	
	public BasicGraphPanel(int panel_width, int panel_height) {
		this(panel_width, panel_height, new TitledBorder(""), null);
	}
	
	public BasicGraphPanel(int panel_width, int panel_height, Border panel_border, LayoutManager layout) {
		this.panel_width = panel_width;
		this.panel_height = panel_height;
		configurePanel(panel_border, layout);
	}
	
	private void configurePanel(Border panel_border, LayoutManager layout) {
		dim = new Dimension(this.panel_width, this.panel_height);
		setSize(dim);
		setBorder(panel_border);
		setLayout(layout);
	}
	
	public int getWidth() {
		return panel_width;
	}
	
	public int getHeight() {
		return panel_height;
	}
	
	public Dimension getSize() {
		return dim;
	}
	
	public Dimension getPreferredSize() {
		return dim;
	}
}
