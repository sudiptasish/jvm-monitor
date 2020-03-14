package com.sc.hm.monitor.util;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;

public class SwingUtil {

	public static void setComponentFontRecursively(JComponent c, Font font) {
		if (c.getClass().getName().equals("javax.swing.JPanel")) {
			return;
		}
		else if (c.getClass().getName().equals("javax.swing.JLabel") || c.getClass().getName().equals("javax.swing.JTextField")
				|| c.getClass().getName().equals("javax.swing.JButton") || c.getClass().getName().equals("javax.swing.JComboBox")
					|| c.getClass().getName().equals("javax.swing.JCheckBox") || c.getClass().getName().equals("javax.swing.JTextArea")) {
			c.setFont(font);
			return;
		}
		else {			
			Component[] children = c.getComponents();
			for (Component component : children) {
				if (component instanceof JComponent) {
					setComponentFontRecursively((JComponent)c, font);
				}
			}
		}
	}
}
