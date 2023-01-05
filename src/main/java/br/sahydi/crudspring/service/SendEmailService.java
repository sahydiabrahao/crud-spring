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
	
	private String userName = "teste15502891@gmail.com";
	private String senha= "hxwgkhwgbmaacbjx";   //Senha para APP

	private String nomeRemetente = "Sahydi Abrahão";

	public void sendEmail(String destinoEmail, String assuntoEmail, String textoEmail)throws Exception{

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
				return new PasswordAuthentication(userName, senha);
			}
		});
		
		Address[] toUser = InternetAddress.parse(destinoEmail);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName, nomeRemetente)); /*Quem está enviano*/
		message.setRecipients(Message.RecipientType.TO, toUser);/*Email de destino*/
		message.setSubject(assuntoEmail);/*Assunto do e-mail*/
		
		message.setText(textoEmail);
		
		Transport.send(message);
	}
	

}
