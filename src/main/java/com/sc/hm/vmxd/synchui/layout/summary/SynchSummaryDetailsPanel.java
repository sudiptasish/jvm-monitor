package com.sc.hm.vmxd.synchui.layout.summary;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.management.MemoryUsage;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.sc.hm.monitor.util.GraphImageProperties;
import com.sc.hm.vmxd.data.AbstractMBeanDataRepositoryFactory;
import com.sc.hm.vmxd.data.ClassDataRepository;
import com.sc.hm.vmxd.data.CompilationDataRepository;
import com.sc.hm.vmxd.data.GarbageCollectorDataRepository;
import com.sc.hm.vmxd.data.MemoryDataRepository;
import com.sc.hm.vmxd.data.OperatingSystemDataRepository;
import com.sc.hm.vmxd.data.RuntimeDataRepository;
import com.sc.hm.vmxd.data.ThreadDataRepository;
import com.sc.hm.vmxd.data.classes.ClassData;
import com.sc.hm.vmxd.data.gbcollector.GarbageCollectorData;
import com.sc.hm.vmxd.data.os.OperatingSystemData;
import com.sc.hm.vmxd.data.thread.ThreadData;
import com.sc.hm.vmxd.process.lock.AbstractLockRepository;
import com.sc.hm.vmxd.process.lock.ThirdPartySynchronizedLock;
import com.sc.hm.vmxd.synchui.layout.common.SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED;
import com.sc.hm.vmxd.synchui.layout.panel.SynchMonitoringGraphPanel;

public class SynchSummaryDetailsPanel extends SynchMonitoringGraphPanel implements ItemListener {
	
	private static final long serialVersionUID = 1L;
	
	private String applicationId = "";

	private ClassDataRepository cDataRepository = null;
	private GarbageCollectorDataRepository gDataRepository = null;
	private MemoryDataRepository mDataRepository = null;
	private OperatingSystemDataRepository osDataRepository = null;
	private ThreadDataRepository tDataRepository = null;
	private RuntimeDataRepository rtDataRepository = null;
	private CompilationDataRepository cmDataRepository = null;
	
	private ThirdPartySynchronizedLock runtimeLock = null;
	private ThirdPartySynchronizedLock compileLock = null;
	
	
	private static final int WORD_HEIGHT = 18;
    private static final int WORD_V_SPACE = 5;

    private final JPanel summaryPanel = new JPanel();
    private final JPanel memoryPanel = new JPanel();
    private final JPanel threadPanel = new JPanel();
    private final JPanel classPanel = new JPanel();
    private final JPanel osPanel = new JPanel();

    private final JLabel label_s_1 = new JLabel("Uptime:");
    private final JLabel label_s_2 = new JLabel("Process CPU Time:");
    private final JLabel label_s_3 = new JLabel("Compiler Name:");
    private final JLabel label_s_4 = new JLabel("Total Compile Time:");

    private final JLabel label_m_1 = new JLabel("Heap Size (Used):");
    private final JLabel label_m_2 = new JLabel("Heap Size (Max):");
    private final JLabel label_m_3 = new JLabel("Committed Memory:");
    private final JLabel label_m_4 = new JLabel("Finalization Pending (Object):");
    private final JLabel label_m_5 = new JLabel("Garbage Collector:");
    private final JLabel label_m_6 = new JLabel("Garbage Collector:");

    private final JLabel label_t_1 = new JLabel("Live Threads:");
    private final JLabel label_t_2 = new JLabel("Daemon Threads:");
    private final JLabel label_t_3 = new JLabel("Peak Threads:");
    private final JLabel label_t_4 = new JLabel("Started Threads:");
    private final JLabel label_t_5 = new JLabel("Deadlocked Threads:");

    private final JLabel label_c_1 = new JLabel("Current Loaded Class:");
    private final JLabel label_c_2 = new JLabel("Total Loaded Class:");
    private final JLabel label_c_3 = new JLabel("Total Unloaded Class:");

    private final JLabel label_o_1 = new JLabel("OS Name:");
    private final JLabel label_o_2 = new JLabel("OS Version:");
    private final JLabel label_o_3 = new JLabel("Available Processor:");
    private final JLabel label_o_4 = new JLabel("Architecture:");
    private final JLabel label_o_5 = new JLabel("Physical Memory [Total]:");
    private final JLabel label_o_6 = new JLabel("Physical Memory [Available]:");
    private final JLabel label_o_7 = new JLabel("Swap Space [Total]:");
    private final JLabel label_o_8 = new JLabel("Swap Space [Available]:");
    private final JLabel label_o_9 = new JLabel("Virtual memory [Committed]:");

    private final JLabel label_s_1_R = new JLabel("50");
    private final JLabel label_s_2_R = new JLabel("0");
    private final JLabel label_s_3_R = new JLabel("");
    private final JLabel label_s_4_R = new JLabel("0");

    private final JLabel label_m_1_R = new JLabel("0 KB");
    private final JLabel label_m_2_R = new JLabel("0 KB");
    private final JLabel label_m_3_R = new JLabel("0 KB");
    private final JLabel label_m_4_R = new JLabel("0");
    private final JLabel label_m_5_R = new JLabel("");
    private final JLabel label_m_6_R = new JLabel("");

    private final JLabel label_t_1_R = new JLabel("18");
    private final JLabel label_t_2_R = new JLabel("11");
    private final JLabel label_t_3_R = new JLabel("19");
    private final JLabel label_t_4_R = new JLabel("19");
    private final JLabel label_t_5_R = new JLabel("0");

    private final JLabel label_c_1_R = new JLabel("0");
    private final JLabel label_c_2_R = new JLabel("0");
    private final JLabel label_c_3_R = new JLabel("0");

    private final JLabel label_o_1_R = new JLabel("");
    private final JLabel label_o_2_R = new JLabel("");
    private final JLabel label_o_3_R = new JLabel("");
    private final JLabel label_o_4_R = new JLabel("0");
    private final JLabel label_o_5_R = new JLabel("0");
    private final JLabel label_o_6_R = new JLabel("0");
    private final JLabel label_o_7_R = new JLabel("0");
    private final JLabel label_o_8_R = new JLabel("0");
    private final JLabel label_o_9_R = new JLabel("0");
    
    private JLabel label = new JLabel("Choose a Skin");
    private ButtonGroup skinBGroup = new ButtonGroup();
    private JRadioButton rdDefault = new JRadioButton("Default");
    private JRadioButton rdMotif = new JRadioButton("Motif");
    private JRadioButton rdWindows = new JRadioButton("Windows");
    
    private JLabel t_label = new JLabel("Choose Graph Type");
    private ButtonGroup gTypeBGroup = new ButtonGroup();
    private JRadioButton rdSynch = new JRadioButton("Synchronous");
    private JRadioButton rdAsynch = new JRadioButton("ASynchronous");
    
    private final Font font = new Font("Arial", Font.PLAIN, 11);
    
    private final NumberFormat d_format = NumberFormat.getInstance();

	public SynchSummaryDetailsPanel(String application, SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int width, int height) throws Exception {
		this(application, obj, width, height, new TitledBorder(""), null);
	}
	
	public SynchSummaryDetailsPanel(String application, SYNCH_UI_SELECTIONPANEL_GRAPHPANEL_SHARED obj, int panel_width, int panel_height, Border panel_border, LayoutManager layout) throws Exception {
		super(obj, panel_width, panel_height, panel_border, layout);
		applicationId = application;
		initOther();
		initializePrimaryPanel();
		initializeOtherComponent();
		addAllComponent();
	}
	
	private void initOther() {
		try {
			runtimeLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.RUNTIME_LOCK);
			compileLock = AbstractLockRepository.getLockRepository(applicationId).getSynchLockByName(AbstractLockRepository.COMPILATION_LOCK);
			
			AbstractMBeanDataRepositoryFactory mbeanRepositoryFactory = AbstractMBeanDataRepositoryFactory.getDataRepositoryFactory(applicationId);
			
			cDataRepository = (ClassDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_CLASS);
			gDataRepository = (GarbageCollectorDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_GARBAGE_COLLECTOR);
			mDataRepository = (MemoryDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_MEMORY);
			osDataRepository = (OperatingSystemDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_OPERATING_SYSTEM);
			tDataRepository = (ThreadDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_THREAD);
			rtDataRepository = (RuntimeDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_RUNTIME);
			cmDataRepository = (CompilationDataRepository)mbeanRepositoryFactory.getRepositoryByName(AbstractMBeanDataRepositoryFactory.MBEAN_REPOSITORY_COMPILATION);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initializePrimaryPanel() throws Exception {
		setLayout(null);
		d_format.setMinimumFractionDigits(0);
		d_format.setMaximumFractionDigits(2);
		d_format.setGroupingUsed(true);
	}
	
	private void initializeOtherComponent() throws Exception {
		 initSummaryComponent();
         initMemoryComponent();
         initThreadComponent();
         initClassesComponent();
         initOSComponent();
         initSkinComponent();
         initGraphTypeComponent();
         
         summaryPanel.setBounds(4, 10, 780, 70);
         summaryPanel.setLayout(null);
         summaryPanel.setBorder(new TitledBorder(new EtchedBorder(), "Overview", 0, 0, font, Color.BLUE));

         memoryPanel.setBounds(4, 95, 780, 125);
         memoryPanel.setLayout(null);
         memoryPanel.setBorder(new TitledBorder(new EtchedBorder(), "Memory", 0, 0, font, Color.BLUE));

         threadPanel.setBounds(4, 235, 780, 95);
         threadPanel.setLayout(null);
         threadPanel.setBorder(new TitledBorder(new EtchedBorder(), "Thread", 0, 0, font, Color.BLUE));

         classPanel.setBounds(4, 345, 780, 70);
         classPanel.setLayout(null);
         classPanel.setBorder(new TitledBorder(new EtchedBorder(), "Class", 0, 0, font, Color.BLUE));

         osPanel.setBounds(4, 430, 780, 150);
         osPanel.setLayout(null);
         osPanel.setBorder(new TitledBorder(new EtchedBorder(), "Operating System", 0, 0, font, Color.BLUE));

         summaryPanel.add(label_s_1);
         summaryPanel.add(label_s_2);
         summaryPanel.add(label_s_3);
         summaryPanel.add(label_s_4);

         summaryPanel.add(label_s_1_R);
         summaryPanel.add(label_s_2_R);
         summaryPanel.add(label_s_3_R);
         summaryPanel.add(label_s_4_R);

         memoryPanel.add(label_m_1);
         memoryPanel.add(label_m_2);
         memoryPanel.add(label_m_3);
         memoryPanel.add(label_m_4);
         memoryPanel.add(label_m_5);
         memoryPanel.add(label_m_6);

         memoryPanel.add(label_m_1_R);
         memoryPanel.add(label_m_2_R);
         memoryPanel.add(label_m_3_R);
         memoryPanel.add(label_m_4_R);
         memoryPanel.add(label_m_5_R);
         memoryPanel.add(label_m_6_R);

         threadPanel.add(label_t_1);
         threadPanel.add(label_t_2);
         threadPanel.add(label_t_3);
         threadPanel.add(label_t_4);
         threadPanel.add(label_t_5);

         threadPanel.add(label_t_1_R);
         threadPanel.add(label_t_2_R);
         threadPanel.add(label_t_3_R);
         threadPanel.add(label_t_4_R);
         threadPanel.add(label_t_5_R);

         classPanel.add(label_c_1);
         classPanel.add(label_c_2);
         classPanel.add(label_c_3);

         classPanel.add(label_c_1_R);
         classPanel.add(label_c_2_R);
         classPanel.add(label_c_3_R);

         osPanel.add(label_o_1);
         osPanel.add(label_o_2);
         osPanel.add(label_o_3);
         osPanel.add(label_o_4);
         osPanel.add(label_o_5);
         osPanel.add(label_o_6);
         osPanel.add(label_o_7);
         osPanel.add(label_o_8);
         osPanel.add(label_o_9);

         osPanel.add(label_o_1_R);
         osPanel.add(label_o_2_R);
         osPanel.add(label_o_3_R);
         osPanel.add(label_o_4_R);
         osPanel.add(label_o_5_R);
         osPanel.add(label_o_6_R);
         osPanel.add(label_o_7_R);
         osPanel.add(label_o_8_R);
         osPanel.add(label_o_9_R);
	}
	
	private void addAllComponent() throws Exception {
		add(summaryPanel);
        add(memoryPanel);
        add(threadPanel);
        add(classPanel);
        add(osPanel);
        
        label.setFont(font);
        rdDefault.setFont(font);
        rdMotif.setFont(font);
        rdWindows.setFont(font);
        
        add(label);
        add(rdDefault);
        add(rdMotif);
        add(rdWindows);
        
        t_label.setFont(font);
        rdSynch.setFont(font);
        rdAsynch.setFont(font);
        
        add(t_label);
        add(rdSynch);
        add(rdAsynch);
	}
	
	public void startMonitoring() throws Exception {
		try {
			runtimeLock.startGetItem();
			double minutes = rtDataRepository.getRuntimeData().getUptime() / 60000.00;
			label_s_1_R.setText(d_format.format(minutes) + " Minutes");
		}
		finally {
			runtimeLock.endGetItem();
		}
		
        try {
        	compileLock.startGetItem();
        	label_s_3_R.setText(cmDataRepository.getCompilationData().getName());
        	label_s_4_R.setText(cmDataRepository.getCompilationData().getTotalCompilationTime() / 1000.00 + " Seconds");
        }
        finally {
        	compileLock.endGetItem();
        }
        
		_summary_memory();
		_summary_manager();
		_summary_threads();
		_summary_classes();		
		_summary_os();
	}
	
	private void initSummaryComponent() {
        int yIndex = 0;

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_s_1.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_s_1.setFont(font);
        label_s_1_R.setBounds(80 + 130 + 5, yIndex, 130, WORD_HEIGHT);

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_s_2.setBounds(400, yIndex, 130, WORD_HEIGHT);
        label_s_2.setFont(font);
        label_s_2_R.setBounds(400 + 130 + 5, yIndex, 140, WORD_HEIGHT);

        yIndex = 15 + 1 * (WORD_HEIGHT + WORD_V_SPACE);
        label_s_3.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_s_3.setFont(font);
        label_s_3_R.setBounds(80 + 130 + 5, yIndex, 150, WORD_HEIGHT);

        yIndex = 15 + 1 * (WORD_HEIGHT + WORD_V_SPACE);
        label_s_4.setBounds(400, yIndex, 130, WORD_HEIGHT);
        label_s_4.setFont(font);
        label_s_4_R.setBounds(400 + 130 + 5, yIndex, 110, WORD_HEIGHT);

        label_s_1_R.setFont(font);
        label_s_2_R.setFont(font);
        label_s_3_R.setFont(font);
        label_s_4_R.setFont(font);
	}
	
	private void initMemoryComponent() {
        int yIndex = 0;

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_m_1.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_m_1.setFont(font);
        label_m_1_R.setBounds(80 + 130 + 5, yIndex, 90, WORD_HEIGHT);

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_m_2.setBounds(400, yIndex, 170, WORD_HEIGHT);
        label_m_2.setFont(font);
        label_m_2_R.setBounds(400 + 170 + 5, yIndex, 110, WORD_HEIGHT);

        yIndex = 15 + 1 * (WORD_HEIGHT + WORD_V_SPACE);
        label_m_3.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_m_3.setFont(font);
        label_m_3_R.setBounds(80 + 130 + 5, yIndex, 110, WORD_HEIGHT);

        yIndex = 15 + 1 * (WORD_HEIGHT + WORD_V_SPACE);
        label_m_4.setBounds(400, yIndex, 170, WORD_HEIGHT);
        label_m_4.setFont(font);
        label_m_4_R.setBounds(400 + 170 + 5, yIndex, 30, WORD_HEIGHT);

        yIndex = 15 + 2 * (WORD_HEIGHT + WORD_V_SPACE);
        label_m_5.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_m_5.setFont(font);
        label_m_5_R.setBounds(80 + 130 + 5, yIndex, 480, WORD_HEIGHT);

        yIndex = 15 + 3 * (WORD_HEIGHT + WORD_V_SPACE);
        label_m_6.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_m_6.setFont(font);
        label_m_6_R.setBounds(80 + 130 + 5, yIndex, 480, WORD_HEIGHT);

        label_m_1_R.setFont(font);
        label_m_2_R.setFont(font);
        label_m_3_R.setFont(font);
        label_m_4_R.setFont(font);
        label_m_5_R.setFont(font);
        label_m_6_R.setFont(font);
	}
	
	private void initThreadComponent() {
        int yIndex = 0;

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_t_1.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_t_1.setFont(font);
        label_t_1_R.setBounds(80 + 130 + 5, yIndex, 40, WORD_HEIGHT);

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_t_2.setBounds(400, yIndex, 130, WORD_HEIGHT);
        label_t_2.setFont(font);
        label_t_2_R.setBounds(400 + 130 + 5, yIndex, 40, WORD_HEIGHT);

        yIndex = 15 + 1 * (WORD_HEIGHT + WORD_V_SPACE);
        label_t_3.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_t_3.setFont(font);
        label_t_3_R.setBounds(80 + 130 + 5, yIndex, 40, WORD_HEIGHT);

        yIndex = 15 + 1 * (WORD_HEIGHT + WORD_V_SPACE);
        label_t_4.setBounds(400, yIndex, 130, WORD_HEIGHT);
        label_t_4.setFont(font);
        label_t_4_R.setBounds(400 + 130 + 5, yIndex, 40, WORD_HEIGHT);

        yIndex = 15 + 2 * (WORD_HEIGHT + WORD_V_SPACE);
        label_t_5.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_t_5.setFont(font);
        label_t_5_R.setBounds(80 + 130 + 5, yIndex, 40, WORD_HEIGHT);

        label_t_1_R.setFont(font);
        label_t_2_R.setFont(font);
        label_t_3_R.setFont(font);
        label_t_4_R.setFont(font);
        label_t_5_R.setFont(font);
	}
	
	private void initClassesComponent() {
        int yIndex = 0;

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_c_1.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_c_1.setFont(font);
        label_c_1_R.setBounds(80 + 130 + 5, yIndex, 90, WORD_HEIGHT);

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_c_2.setBounds(400, yIndex, 130, WORD_HEIGHT);
        label_c_2.setFont(font);
        label_c_2_R.setBounds(400 + 130 + 5, yIndex, 110, WORD_HEIGHT);

        yIndex = 15 + 1 * (WORD_HEIGHT + WORD_V_SPACE);
        label_c_3.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_c_3.setFont(font);
        label_c_3_R.setBounds(80 + 130 + 5, yIndex, 110, WORD_HEIGHT);

        label_c_1_R.setFont(font);
        label_c_2_R.setFont(font);
        label_c_3_R.setFont(font);
	}
	
	private void initOSComponent() {
        int yIndex = 0;

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_o_1.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_o_1.setFont(font);
        label_o_1_R.setBounds(80 + 130 + 5, yIndex, 160, WORD_HEIGHT);

        yIndex = 15 + 0 * (WORD_HEIGHT + WORD_V_SPACE);
        label_o_2.setBounds(400, yIndex, 130, WORD_HEIGHT);
        label_o_2.setFont(font);
        label_o_2_R.setBounds(400 + 130 + 5, yIndex, 130, WORD_HEIGHT);

        yIndex = 15 + 1 * (WORD_HEIGHT + WORD_V_SPACE);
        label_o_3.setBounds(80, yIndex, 130, WORD_HEIGHT);
        label_o_3.setFont(font);
        label_o_3_R.setBounds(80 + 130 + 5, yIndex, 130, WORD_HEIGHT);

        yIndex = 15 + 1 * (WORD_HEIGHT + WORD_V_SPACE);
        label_o_4.setBounds(400, yIndex, 130, WORD_HEIGHT);
        label_o_4.setFont(font);
        label_o_4_R.setBounds(400 + 130 + 5, yIndex, 130, WORD_HEIGHT);
        
        yIndex = 15 + 2 * (WORD_HEIGHT + WORD_V_SPACE);
        label_o_5.setBounds(80, yIndex, 170, WORD_HEIGHT);
        label_o_5.setFont(font);
        label_o_5_R.setBounds(80 + 170 + 5, yIndex, 130, WORD_HEIGHT);
        
        yIndex = 15 + 2 * (WORD_HEIGHT + WORD_V_SPACE);
        label_o_6.setBounds(400, yIndex, 180, WORD_HEIGHT);
        label_o_6.setFont(font);
        label_o_6_R.setBounds(400 + 180 + 5, yIndex, 130, WORD_HEIGHT);
        
        yIndex = 15 + 3 * (WORD_HEIGHT + WORD_V_SPACE);
        label_o_7.setBounds(80, yIndex, 170, WORD_HEIGHT);
        label_o_7.setFont(font);
        label_o_7_R.setBounds(80 + 170 + 5, yIndex, 130, WORD_HEIGHT);
        
        yIndex = 15 + 3 * (WORD_HEIGHT + WORD_V_SPACE);
        label_o_8.setBounds(400, yIndex, 180, WORD_HEIGHT);
        label_o_8.setFont(font);
        label_o_8_R.setBounds(400 + 180 + 5, yIndex, 130, WORD_HEIGHT);
        
        yIndex = 15 + 4 * (WORD_HEIGHT + WORD_V_SPACE);
        label_o_9.setBounds(80, yIndex, 170, WORD_HEIGHT);
        label_o_9.setFont(font);
        label_o_9_R.setBounds(80 + 170 + 5, yIndex, 130, WORD_HEIGHT);

        label_o_1_R.setFont(font);
        label_o_2_R.setFont(font);
        label_o_3_R.setFont(font);
        label_o_4_R.setFont(font);
        label_o_5_R.setFont(font);
        label_o_6_R.setFont(font);
        label_o_7_R.setFont(font);
        label_o_8_R.setFont(font);
        label_o_9_R.setFont(font);
	}
	
	private void initSkinComponent() {
		label.setBounds(100, 590, 130, WORD_HEIGHT);
		rdDefault.setBounds(250, 590, 130, WORD_HEIGHT);
		rdMotif.setBounds(410, 590, 130, WORD_HEIGHT);
		rdWindows.setBounds(570, 590, 130, WORD_HEIGHT);
		
		/*
		String current_os = System.getProperty("os.name");
		if (current_os.indexOf("Windows") >= 0 || current_os.indexOf("windows") >= 0) {
			rdWindows.setSelected(true);
		}
		else {
			rdDefault.setSelected(true);
		}
		*/
		rdDefault.setSelected(true);
		rdDefault.setEnabled(isAvailableLookAndFeel(GraphImageProperties.DEFAULT_CLASS_NAME));
		rdMotif.setEnabled(isAvailableLookAndFeel(GraphImageProperties.MOTIF_CLASS_NAME));
		rdWindows.setEnabled(isAvailableLookAndFeel(GraphImageProperties.WINDOWS_CLASS_NAME));
		
		skinBGroup.add(rdDefault);
		skinBGroup.add(rdMotif);
		skinBGroup.add(rdWindows);
		
		rdDefault.addItemListener(this);
		rdMotif.addItemListener(this);
		rdWindows.addItemListener(this);
	}
	
	private void initGraphTypeComponent()  {
		t_label.setBounds(100, 620, 130, WORD_HEIGHT);
		rdSynch.setBounds(250, 620, 130, WORD_HEIGHT);
		rdAsynch.setBounds(410, 620, 130, WORD_HEIGHT);
		rdAsynch.setSelected(true);
		
		gTypeBGroup.add(rdSynch);
		gTypeBGroup.add(rdAsynch);
		
		rdSynch.setEnabled(false);
		
		rdSynch.addItemListener(this);
		rdAsynch.addItemListener(this);
	}
	
	private boolean isAvailableLookAndFeel(String LAF_CLASS) {
    	try {
    		Class<?> c = Class.forName(LAF_CLASS);
    		LookAndFeel LnF = (LookAndFeel)c.newInstance();
    		return LnF.isSupportedLookAndFeel();
    	}
    	catch (Exception e) {
    		return false;
    	}
    }
	
	private void _summary_memory() {
		MemoryUsage usage = mDataRepository.getMemoryData(MemoryDataRepository.HEAP_MEMORY).getCurrentUsage();
        label_m_1_R.setText(d_format.format(usage.getUsed() / 1024.0) + " KB");
        label_m_2_R.setText(d_format.format(usage.getMax() / 1024.0) + " KB");
        label_m_3_R.setText(d_format.format(usage.getCommitted() / 1024.0) + " KB");
        label_m_4_R.setText(String.valueOf(mDataRepository.getObjectPendingFinalizationCount()));
    }
    
    private void _summary_manager() {
    	String[] gbCollectorNames = gDataRepository.getGarbageCollectorNames();
        for (byte i = 0; i < gbCollectorNames.length; i ++) {
        	GarbageCollectorData gbCollectorData = gDataRepository.getGarbageCollectorData(gbCollectorNames[i]);
        	if (i == 0) {
        		label_m_5_R.setText(gbCollectorData.toString());
        	}
        	else if (i == 1) {
        		label_m_6_R.setText(gbCollectorData.toString());
        	}
        }
    }
    
    private void _summary_threads() {
        label_s_2_R.setText(osDataRepository.getOsData().getCurrentCpuTime() + " %");
        ThreadData threadData = tDataRepository.getThreadData();
        label_t_1_R.setText(String.valueOf(threadData.getThreadCount()));
        label_t_2_R.setText(String.valueOf(threadData.getDaemonThreadCount()));
        label_t_3_R.setText(String.valueOf(threadData.getPeakThreadCount()));
        label_t_4_R.setText(String.valueOf(threadData.getTotalStartedThreadCount()));
        label_t_5_R.setText(String.valueOf(threadData.getDeadlockThreadCount()));
    }
    
    private void _summary_classes() {
        ClassData classData = cDataRepository.getClassData();
        label_c_1_R.setText(d_format.format(classData.currentLoadedClass()));
        label_c_2_R.setText(d_format.format(classData.totalLoadedClass()));
        label_c_3_R.setText(d_format.format(classData.currentUnLoadedClass()));
    }
    
    private void _summary_os() {
        OperatingSystemData osData = osDataRepository.getOsData();
        
        label_o_1_R.setText(osData.getName());
        label_o_2_R.setText(osData.getVersion());
        label_o_3_R.setText(String.valueOf(osData.getAvailableProcessor()));
        label_o_4_R.setText(osData.getArch());
        label_o_5_R.setText(d_format.format(osData.getTotalPhysicalMemorySize() / 1024.0) + " KB");
        label_o_6_R.setText(d_format.format(osData.getFreePhysicalMemorySize() / 1024.0) + " KB");
        label_o_7_R.setText(d_format.format(osData.getTotalSwapSpaceSize() / 1024.0) + " KB");
        label_o_8_R.setText(d_format.format(osData.getFreeSwapSpaceSize() / 1024.0) + " KB");
        label_o_9_R.setText(d_format.format(osData.getCommittedVirtualMemorySize() / 1024.0) + " KB");
    }
	
	public void itemStateChanged(ItemEvent ie) {
		boolean isSkinItem = false;
    	JComponent component = (JComponent)ie.getSource();
    	if (ie.getStateChange() == ItemEvent.SELECTED) {
    		String lookAndFeelClassName = "";
    		try {
	    		if (component == rdDefault) {
	    			lookAndFeelClassName = GraphImageProperties.DEFAULT_CLASS_NAME;
	    			isSkinItem = true;
	    		}
	    		else if (component == rdMotif) {
	    			lookAndFeelClassName = GraphImageProperties.MOTIF_CLASS_NAME;
	    			isSkinItem = true;
	    		}
	    		else if (component == rdWindows) {
	    			lookAndFeelClassName = GraphImageProperties.WINDOWS_CLASS_NAME;
	    			isSkinItem = true;
	    		}
	    		else if (component == rdSynch) {
	    			appConfig.setGraphType(GraphImageProperties.GRAPH_TYPE_SYNCH);
	    			isSkinItem = false;
	    		}
	    		else if (component == rdAsynch) {
	    			appConfig.setGraphType(GraphImageProperties.GRAPH_TYPE_ASYNCH);
	    			isSkinItem = false;
	    		}
	    		if (isSkinItem) {
	    			appConfig.setLookAndFeel(lookAndFeelClassName);
		    		UIManager.setLookAndFeel(lookAndFeelClassName);
		    		Container parent = getParent().getParent();
		    		SwingUtilities.updateComponentTreeUI(parent);
	    		}
	    		else {	    			
	    		}
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }

	public void startSynchronizedMonitoring() throws Exception {
		startMonitoring();
	}
}
