package br.sahydi.crudspring.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
	
	private String my_email = "teste15502891@gmail.com";
	private String my_password= "hxwgkhwgbmaacbjx";   //my_password para APP

	private String my_name = "Projeto Crud Agenda de Contatos";

	public void sendEmail(String email_addressee, String email_subject, String email_text)throws Exception{

       Properties properties = new Properties();
		
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");/*Autorização*/
		properties.put("mail.smtp.auth", "true");/*Autorização*/
		properties.put("mail.smtp.starttls", "true"); /*Autenticação*/
		properties.put("mail.smtp.host", "smtp.gmail.com"); /*Sercidor gmail Google*/
		properties.put("mail.smtp.port", "465");/*Porta do servidor*/
		properties.put("mail.smtp.socketFactory.port", "465");/*Expecifica a porta a ser conectada pelo socket*/
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");/*Classe socket de conexão ao SMTP*/
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(my_email, my_password);
			}
		});
		
		Address[] toUser = InternetAddress.parse(email_addressee);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(my_email, my_name)); /*Quem está enviano*/
		message.setRecipients(Message.RecipientType.TO, toUser);/*Email de destino*/
		message.setSubject(email_subject);/*Assunto do e-mail*/
		
		message.setText(email_text);
		
		Transport.send(message);
	}
	

}
