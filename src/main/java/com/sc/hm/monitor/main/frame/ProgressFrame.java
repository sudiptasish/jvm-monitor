package com.sc.hm.monitor.main.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Dimension screen_dimension = Toolkit.getDefaultToolkit().getScreenSize();
	
	private ProgressPane progressPane = null;
	private JLabel progressLabel = new JLabel("Loading, please wait....");
	private JProgressBar progressBar = new JProgressBar();
	private Dimension labelSize = new Dimension(480, 10);
	
	private Font font = new Font("Arial", Font.PLAIN, 10);

	public ProgressFrame() {
		progressPane = new ProgressPane("images/MonitorWelcome.jpg");
		initializeProgressPaneComponent();
		initializeFrame();
		addComponent();
	}
	
	private void initializeProgressPaneComponent() {
		progressLabel.setFont(font);
		progressBar.setFont(font);
		progressLabel.setMaximumSize(labelSize);
        progressLabel.setPreferredSize(labelSize);
        progressLabel.setForeground(Color.WHITE);
        
        progressBar.setStringPainted(true);
        progressLabel.setLabelFor(progressBar);
        progressBar.setMinimum(0);
        progressBar.setValue(0);
        progressBar.getAccessibleContext().setAccessibleName("Starting VM Monitor....");
        progressBar.setMaximum(15);
        
        progressPane.add(progressLabel);
        progressPane.add(Box.createRigidArea(new Dimension(1,7)));
        progressPane.add(progressBar);        
	}
	
	private void initializeFrame() {
		setSize(new Dimension(520, 307));
        setUndecorated(true);        
        setLocation((int)(screen_dimension.getWidth() / 2 - getSize().getWidth() / 2), (int)(screen_dimension.getHeight() / 2 - getSize().getHeight() / 2));        
	}
	
	private void addComponent() {
		getContentPane().add(progressPane, BorderLayout.CENTER);
	}
    
    public JLabel getProgressLabel() {
		return progressLabel;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}
	
	public void setProgressValue(int val) {
		progressBar.setValue(val);
	}
	
	public int getProgressValue() {
		return progressBar.getValue();
	}
	
	public void setProgressText(String txt) {
		progressLabel.setText(txt);
	}

	private class ProgressPane extends JPanel {
    	private static final long serialVersionUID = 1L;
		
    	private String imagePath = "";
    	private Image backgroundImage = null;
    	
    	public ProgressPane(String imagePath) {
    		super();
    		this.imagePath = imagePath;
    		initializePanel();
    		initializeStream();
    	}
    	
    	public Insets getInsets() {
            return new Insets(200, 25, 40, 25);
        }
    	
    	private void initializePanel() {
    		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	}
    	
    	public void initializeStream() {
    		try {
    			InputStream iStream = getClass().getClassLoader().getResourceAsStream(imagePath);
    			backgroundImage = ImageIO.read(iStream);
    		}
    		catch (Exception e) {
    			System.err.println("Unable to Load Image. " + e.getMessage());
    		}
    	}
    	
        public void paintComponent(Graphics g) {
        	if (backgroundImage != null) {
    	    	try {
    	    		g.drawImage(backgroundImage, 0, 0, this);
    	    	}
    	    	catch (Exception e) {
    	    		e.printStackTrace();
    	    	}
        	}
        }
    }
}
