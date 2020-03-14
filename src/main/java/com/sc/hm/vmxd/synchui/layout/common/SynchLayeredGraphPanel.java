package com.sc.hm.vmxd.synchui.layout.common;

import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class SynchLayeredGraphPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int width = 0;
	private int height = 0;
	private Dimension dimension = null;
	
	private JLayeredPane layeredPane = new JLayeredPane();
	
	private AbstractSynchGraphPlotterPanel bottomPanel = null;
	private AbstractSynchCoordinatePanel topPanel = null;
	
	public SynchLayeredGraphPanel(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.dimension = new Dimension(this.width, this.height);
		configuraPanel();
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
		this.width = (int)this.dimension.getWidth();
		this.height = (int)this.dimension.getHeight();
	}
	
	public Dimension getSize() {
		return dimension;
	}
	
	public Dimension getPreferredSize() {
		return dimension;
	}
	
	public void configuraPanel() {
		setSize(dimension);
		setLayout(null);
		layeredPane.setSize(dimension);
		layeredPane.setBounds(0, 0, width, height);		
	}
	
	public void setUnderlyingPanel(AbstractSynchGraphPlotterPanel plotterPanel) {
		bottomPanel = plotterPanel;
		topPanel = new SynchCoordinatePanel(width, height);
	}
	
	public void addLayeredComponent() {
		layeredPane.add(bottomPanel, new Integer(1));
		layeredPane.add(topPanel, new Integer(2));
		add(layeredPane);
	}
}
