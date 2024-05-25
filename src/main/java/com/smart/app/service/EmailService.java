package com.smart.app.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
public boolean sendEmail(String subject,String message,String to) {
	boolean flage =  false;
	String from  =  "jhonjava122@gmail.com";
	String host = "smtp.gmail.com";
	String pw ="oybpskxhjweqfiax12";
	Properties properties =  System.getProperties();
	properties.put("mail.smtp.host", host);
	properties.put("mail.smtp.port", "456");
	properties.put("mail.smtp.ssl.enable", "true");
	properties.put("mail.smtp.auth", "true");
	properties.put("mail.smtp.starttsl.enable", "true");
	
	Session session =  Session.getInstance(properties, new Authenticator() {

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			// TODO Auto-generated method stub
			return new PasswordAuthentication(from,pw);
		}
		
	
	
	});
	session.setDebug(true);
	
	MimeMessage mimeMessage =  new MimeMessage(session);
	try {
		mimeMessage.setFrom(new InternetAddress(from));
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		mimeMessage.setSubject(subject);

		mimeMessage.setContent(message,"text/html");
		
		Transport.send(mimeMessage);
		flage = true;
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	
	return flage;
}
}
