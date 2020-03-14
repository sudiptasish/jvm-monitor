package com.sc.hm.monitor.mail.config;

public class MailAttachment {

	private String attachment = "";

	public MailAttachment() {}

	public MailAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
}
