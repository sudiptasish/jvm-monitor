package com.sc.hm.vmxd.data.thread;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class ThreadUsageData {
    
    private static final int MAX_ITEM = 75;
    
    private Long id;
    private String name;
    private Queue<Long> cpuTimes = new LinkedList<>();
    private Queue<Long> userTimes = new LinkedList<>();
    private String state;
    
    public ThreadUsageData() {}

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the cpuTimes
     */
    public Queue<Long> getCpuTimes() {
        return cpuTimes;
    }

    /**
     * @param cpuTimes the cpuTimes to set
     */
    public void addCpuTime(Long cpuTime) {
        this.cpuTimes.add(cpuTime);
        if (cpuTimes.size() > MAX_ITEM) {
            cpuTimes.remove();
        }
    }

    /**
     * @return the userTimes
     */
    public Queue<Long> getUserTimes() {
        return userTimes;
    }

    /**
     * @param userTimes the userTimes to set
     */
    public void addUserTime(Long userTime) {
        this.userTimes.add(userTime);
        if (userTimes.size() > MAX_ITEM) {
            userTimes.remove();
        }
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }
    
    /**
     * Get the latest CPU time of the thread.
     * @return Long
     */
    public Long getLastCpuTime() {
        try {
            return (Long)((LinkedList)cpuTimes).getLast();
        }
        catch (NoSuchElementException e) {
            return 0L;
        }
    }
    
    /**
     * Get the latest User time of the thread.
     * @return Long
     */
    public Long getLastUserTime() {
        try {
            return (Long)((LinkedList)userTimes).getLast();
        }
        catch (NoSuchElementException e) {
            return 0L;
        }
    }
}
