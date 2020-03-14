package com.sc.hm.vmxd.data.compile;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class CompilationData {
	
	private String name = "";
	private long totalCompilationTime = 0L;

	public CompilationData() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTotalCompilationTime() {
		return totalCompilationTime;
	}

	public void setTotalCompilationTime(long totalCompilationTime) {
		this.totalCompilationTime = totalCompilationTime;
	}
}
