package es.elovendo.util.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

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

	public void sendMail(String from, String to, String subject, String text) {

		logger.warn("Sending from " + from + " to " + to);
		logger.warn("Subject: " + subject + " msg: " + text);

		String para = to;
		 
	      // La dirección de la cuenta de envío (from)
	      String de = from;
	 
	      // El servidor (host). En este caso usamos localhost
	      String host = "localhost";
	 
	      // Obtenemos las propiedades del sistema
	      Properties propiedades = System.getProperties();
	 
	      // Configuramos el servidor de correo
	      propiedades.setProperty("mail.smtp.host", host);
	 
	      // Obtenemos la sesión por defecto
	      Session sesion = Session.getDefaultInstance(propiedades);
	 
	      try{
	         // Creamos un objeto mensaje tipo MimeMessage por defecto.
	         MimeMessage mensaje = new MimeMessage(sesion);
	 
	         // Asignamos el “de o from” al header del correo.
	         mensaje.setFrom(new InternetAddress(de));
	 
	         // Asignamos el “para o to” al header del correo.
	         mensaje.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(para));
	 
	         // Asignamos el asunto
	         mensaje.setSubject("Primer correo sencillo");
	 
	         // Asignamos el mensaje como tal
	         mensaje.setText("El mensaje de nuestro primer correo");
	 
	         // Enviamos el correo
	         Transport.send(mensaje);
	         System.out.println("Mensaje enviado");
	      }catch (MessagingException e) {
	         e.printStackTrace();
	      }
	}

}
