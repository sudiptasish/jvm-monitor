package com.sc.hm.monitor.mail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sc.hm.monitor.config.ApplicationConfiguration;
import com.sc.hm.monitor.config.cache.EnvironmentConfigObject;
import com.sc.hm.monitor.mail.config.MailAttachment;
import com.sc.hm.monitor.mail.config.MailConfiguration;
import com.sc.hm.monitor.util.Logger;
import com.sc.hm.vmxd.config.SynchApplicationConfiguration;

public class VMMailUtil {
	
	public static void sendMail (MailConfiguration mailConfig) {
		InternetAddress i_address_from = null;
		InternetAddress[] i_address_to = null;
		InternetAddress[] i_address_cc = null;
		InternetAddress[] i_address_bcc = null;
		try {
			if (mailConfig.getFrom() == null || mailConfig.getFrom().trim().length() == 0) {
				throw new Exception("No From Information. Can not Send Mail");
			}
			i_address_from = new InternetAddress(mailConfig.getFrom());
			
			if (mailConfig.getTo() != null) {
				i_address_to = new InternetAddress[mailConfig.getTo().length];
				for (byte i = 0; i < i_address_to.length; i ++) {
					i_address_to[i] = new InternetAddress(mailConfig.getTo()[i]);
				}
			}
			
			if (mailConfig.getCc() != null) {
				i_address_cc = new InternetAddress[mailConfig.getCc().length];
				for (byte i = 0; i < i_address_cc.length; i ++) {
					i_address_cc[i] = new InternetAddress(mailConfig.getCc()[i]);
				}
			}
			
			if (mailConfig.getBcc() != null) {
				i_address_bcc = new InternetAddress[mailConfig.getBcc().length];
				for (byte i = 0; i < i_address_bcc.length; i ++) {
					i_address_bcc[i] = new InternetAddress(mailConfig.getBcc()[i]);
				}
			}
			
			Properties props = new Properties();
			String smtp_server = "";
			EnvironmentConfigObject envConfigObject = ApplicationConfiguration.getInstance().getEnvironmentConfig();
			if (envConfigObject != null) {
				smtp_server = envConfigObject.getSmtp_host();
				if (smtp_server == null || "".equals(smtp_server.trim())) {
					smtp_server = SynchApplicationConfiguration.getSynchInstance().getEnvironmentConfig().getSmtp_host();
				}
			}
			else {
				smtp_server = SynchApplicationConfiguration.getSynchInstance().getEnvironmentConfig().getSmtp_host();
			}			
			props.put("mail.smtp.host", smtp_server);
			Session session = Session.getDefaultInstance(props, null);
			Message message = new MimeMessage(session);
			
			message.setFrom(i_address_from);
			if (i_address_to != null && i_address_to.length > 0) {
				message.setRecipients(Message.RecipientType.TO, i_address_to);
			}
			if (i_address_cc != null && i_address_to.length > 0) {
				message.setRecipients(Message.RecipientType.CC, i_address_cc);
			}
			if (i_address_bcc != null && i_address_bcc.length > 0) {
				message.setRecipients(Message.RecipientType.BCC, i_address_bcc);
			}
			
			message.setSubject(mailConfig.getSubject());
			message.setContent(mailConfig.getMessage(), "text/plain");
			Transport.send(message);
			Logger.log("Sent Message. Subject: " + mailConfig.getMessage());
		}
		catch (Exception e) {
			System.err.println("Error!!! While Sending Mail");
			e.printStackTrace();
		}
	}
	
	public static void sendMail (MailConfiguration mailConfig, MailAttachment mailAttachment) {
		InternetAddress i_address_from = null;
		InternetAddress[] i_address_to = null;
		InternetAddress[] i_address_cc = null;
		InternetAddress[] i_address_bcc = null;
		try {
			if (mailConfig.getFrom() == null || mailConfig.getFrom().trim().length() == 0) {
				throw new Exception("No From Information. Can not Send Mail");
			}
			i_address_from = new InternetAddress(mailConfig.getFrom());
			
			if (mailConfig.getTo() != null) {
				i_address_to = new InternetAddress[mailConfig.getTo().length];
				for (byte i = 0; i < i_address_to.length; i ++) {
					i_address_to[i] = new InternetAddress(mailConfig.getTo()[i]);
				}
			}
			
			if (mailConfig.getCc() != null) {
				i_address_cc = new InternetAddress[mailConfig.getCc().length];
				for (byte i = 0; i < i_address_cc.length; i ++) {
					i_address_cc[i] = new InternetAddress(mailConfig.getCc()[i]);
				}
			}
			
			if (mailConfig.getBcc() != null) {
				i_address_bcc = new InternetAddress[mailConfig.getBcc().length];
				for (byte i = 0; i < i_address_bcc.length; i ++) {
					i_address_bcc[i] = new InternetAddress(mailConfig.getBcc()[i]);
				}
			}
			
			Properties props = new Properties();
			String smtp_server = "";
			EnvironmentConfigObject envConfigObject = ApplicationConfiguration.getInstance().getEnvironmentConfig();
			if (envConfigObject != null) {
				smtp_server = envConfigObject.getSmtp_host();
				if (smtp_server == null || "".equals(smtp_server.trim())) {
					smtp_server = SynchApplicationConfiguration.getSynchInstance().getEnvironmentConfig().getSmtp_host();
				}
			}
			else {
				smtp_server = SynchApplicationConfiguration.getSynchInstance().getEnvironmentConfig().getSmtp_host();
			}			
			props.put("mail.smtp.host", smtp_server);
			Session session = Session.getDefaultInstance(props, null);
			Message message = new MimeMessage(session);
			
			message.setFrom(i_address_from);
			if (i_address_to != null && i_address_to.length > 0) {
				message.setRecipients(Message.RecipientType.TO, i_address_to);
			}
			if (i_address_cc != null && i_address_to.length > 0) {
				message.setRecipients(Message.RecipientType.CC, i_address_cc);
			}
			if (i_address_bcc != null && i_address_bcc.length > 0) {
				message.setRecipients(Message.RecipientType.BCC, i_address_bcc);
			}
			
			message.setSubject(mailConfig.getSubject());
			
			Multipart multiPart = new MimeMultipart();
			
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(mailConfig.getMessage(), "text/plain");			
			multiPart.addBodyPart(messageBodyPart);
			
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(mailAttachment.getAttachment());
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(mailAttachment.getAttachment());
			multiPart.addBodyPart(messageBodyPart);
			
			message.setContent(multiPart);
			
			Transport.send(message);
			Logger.log("Sent Message with Attachment. Subject: " + mailConfig.getMessage());
		}
		catch (Exception e) {
			System.err.println("Error!!! While Sending Mail" + e);
		}
	}
}
