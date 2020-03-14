package com.sc.hm.monitor.ui.layout.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;

public class CoordinatePanel extends AbstractCoordinatePanel {
	
	private JLabel label = new JLabel();
	
	private long currentMinX = 0L;
	private long currentMaxX = 0L;

	public CoordinatePanel(int width, int height) {
		super(width, height);
		configuraPanel();
	}
	
	public void configuraPanel() {
		setLayout(null);
		setSize(dimension);		
		setBounds(10, 10, width, height);
		setOpaque(false);
		label.setBounds(3, 3, 150, 20);
		label.setFont(new Font("Verdana", Font.PLAIN, 11));
		label.setForeground(Color.WHITE);
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoeved(MouseEvent me) {
				int interval = (int)(axisInterval * 100);
				if ((100 * me.getX()) % interval == 0) {
					label.setText(String.valueOf(calculateEquivalentYValue(me.getY())));
				}
			}
		});
	}
	
	public void addComponent() {
		add(label);		
	}
	
	public double calculateEquivalentYValue(long yCoordinate) {
		double currentValue = (double)currentMinX + ((double)height - yCoordinate) * (currentMaxX - currentMinX) / (double)height;		
		return currentValue;
	}
}
