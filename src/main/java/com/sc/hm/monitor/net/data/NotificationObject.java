package com.sc.hm.monitor.net.data;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

public class NotificationObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String jobId = "";
	private String notification = "";
	private String exception = "";
	private String status = "";
	
	public NotificationObject(String jobId, String status) {
		this.jobId = jobId;
		this.status = status;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public void setException(Exception e) {
		try {
			ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
			PrintWriter pw = new PrintWriter(bOutStream);
			e.printStackTrace(pw);
			pw.close();
			bOutStream.close();
			this.exception = new String(bOutStream.toByteArray());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString() {
		return new StringBuilder().append("[").append(jobId).append(" : ").append(status).append(" : ").append(exception).append("]").toString();
	}
}
