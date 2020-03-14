package com.sc.hm.vmxd.synchui.layout.panel;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * A basic panel to display monitoring details.
 * It sets the width and height of the underlying panel, along with the border.
 * A panel may or may not have a layout manager.
 * 
 * @author Sudiptasish Chanda
 */
public abstract class SynchBasicGraphPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int panel_width = 0;
	private int panel_height = 0;
	private Dimension dim = null;
	
	public SynchBasicGraphPanel(int panel_width, int panel_height) {
		this(panel_width, panel_height, new TitledBorder(""), null);
	}
	
	public SynchBasicGraphPanel(int panel_width
        , int panel_height
        , Border panel_border
        , LayoutManager layout) {
        
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
	
    @Override
	public int getWidth() {
		return panel_width;
	}
	
    @Override
	public int getHeight() {
		return panel_height;
	}
	
    @Override
	public Dimension getSize() {
		return dim;
	}
	
    @Override
	public Dimension getPreferredSize() {
		return dim;
	}
}
