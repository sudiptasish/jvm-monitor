/* $Header: SynchTopThreadTableModel.java Jan 21, 2017 schanda  Exp $ */

/* Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved. */

/*
   DESCRIPTION
    <short description of component this file declares/defines>

   PRIVATE CLASSES
    <list of private classes defined - with one-line descriptions>

   NOTES
    <other useful comments, qualifications, etc.>

   MODIFIED    (MM/DD/YY)
    schanda     Jan 21, 2017 - Creation
 */

/**
 * @version $Header: SynchTopThreadTableModel.java Jan 21, 2017 schanda  Exp $
 * @author  schanda
 * @since   release specific (what release of product did this appear in)
 */

package com.sc.hm.vmxd.synchui.layout.thread.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.sc.hm.vmxd.data.thread.ThreadUsageData;

public class SynchTopThreadTableModel extends AbstractTableModel {
    
    private String label = "";
    
    protected String[] columns = {"Sl. No", "Thread Id", "Thread Name", "Cpu Time", "User Time", "Percentage", "State"};
    protected Object[][] rows = new Object[0][7];
    
    private final Comparator<ThreadUsageData> comparator = new UsageComparator();
    
    public SynchTopThreadTableModel(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }

    /**
     * Set the new table data.
     * This API will trigger a change in table state event, which will result
     * the entire table to be repainted.
     * 
     * @param values
     */
    public void setTableModelData(List<ThreadUsageData> values) {
        Collections.sort(values, comparator);        
        Object[][] rows = new Object[values.size()][];
        
        int i = 0;
        for (Iterator<ThreadUsageData> itr = values.iterator(); itr.hasNext(); ) {
            ThreadUsageData usageData = itr.next();
            
            rows[i] = new Object[columns.length];
            rows[i][0] = i + 1;
            rows[i][1] = usageData.getId();
            rows[i][2] = usageData.getName();
            rows[i][3] = usageData.getLastCpuTime();
            rows[i][4] = usageData.getLastUserTime();
            rows[i][5] = "";
            rows[i][6] = "";
            
            i ++;
        }
        this.rows = rows;
        fireTableDataChanged();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return rows.length;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows[rowIndex][columnIndex];
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Object val = rows[0][columnIndex];
        return val.getClass();
    }

    private static class UsageComparator implements Comparator<ThreadUsageData> {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(ThreadUsageData o1, ThreadUsageData o2) {
            if (o2.getLastCpuTime() > o1.getLastCpuTime()) {
                return 1;
            }
            else if (o2.getLastCpuTime() < o1.getLastCpuTime()) {
                return -1;
            }
            return 0;
        }        
    }
}
