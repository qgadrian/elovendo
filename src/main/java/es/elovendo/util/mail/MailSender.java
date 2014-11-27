package es.elovendo.util.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import es.elovendo.util.Constant;

public class MailSender {

	private static Logger logger = null;

	private static MailSender mailSender = null;

	public static MailSender getInstance() {
		if (mailSender == null) {
			mailSender = new MailSender();
			logger = Logger.getLogger(MailSender.class);
		}
		return mailSender;
	}

	/**
	 * Sends an email
	 * @param senderName Sender name
	 * @param senderEmail Sender's email. Email will be sent by "contact@elovendo.com" but will be notified to reply to
	 * this attribute.
	 * @param to Email destination.
	 * @param subject Subject.
	 * @param text Email message.
	 */
	public void sendMail(String senderName, String senderEmail, String to, String subject, String text) {

		logger.debug("Sending from " + senderName + " to " + to);
		logger.debug("Subject: " + subject + " msg: " + text);

		String pass = "988000Adrian";
		String user = Constant.CONTACT_EMAIL;
		String host = "smtp.zoho.com";
		String port = "465";
		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", port);
		properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.socketFactory.port", port);
		properties.put("mail.smtp.startssl.enable", "true");

		Session session = Session.getDefaultInstance(properties);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(text);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, 465, user, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			logger.debug("Sent contact email successfully...");
		} catch (MessagingException mex) {
			logger.error("Error sending contact email...");
			mex.printStackTrace();
		}
	}

}
