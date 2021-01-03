package controller;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import buffer.BufferFactory;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Email;
import factory.DataBaseManager;
import repository.EmailDao;

import javax.mail.*;
import javax.mail.internet.*;

@WebServlet("/inviaEmail")
public class InviaEmail extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String PASSWORD = "prenotazione18";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   this.doPost(request, response);   
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final StringBuffer jsonReceived = BufferFactory.getStringBuffer();
		final BufferedReader reader = BufferFactory.getBufferReader(request.getInputStream());
		String line = reader.readLine();
		
		while (line != null) {
			jsonReceived.append(line);
			line = reader.readLine();
		}		
		
		try {
			final JSONObject json = new JSONObject(jsonReceived.toString());

			final String to = json.getString("to");
			final String from = json.getString("from");
			final String body = json.getString("message");

			final String host = "smtp.gmail.com";
			final Properties properties = System.getProperties();
			
		    properties.setProperty("mail.transport.protocol", "smtp");     
		    properties.setProperty("mail.host", host);
		    properties.put("mail.smtp.auth", "true");
		    properties.put("mail.smtp.port", "465");
//		    properties.put("mail.debug", "true"); 
		    properties.put("mail.smtp.socketFactory.port", "465");  
		    properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
		    properties.put("mail.smtp.socketFactory.fallback", "false");  
		    properties.put("mail.smtp.starttls.enable", "true");

			final Session session = Session.getDefaultInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					   return new PasswordAuthentication(from, PASSWORD);
				}
		    });

		    try {
		    	final MimeMessage message = new MimeMessage(session);
			    message.setFrom(new InternetAddress(from));
			    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			    message.setSubject("[SOLVED] Risposta FAQ");
			    message.setText(body);
			    //send mail
			    Transport.send(message);
		      
		   } catch (final MessagingException me) {
		      me.printStackTrace();
		   }

           final String admin = request.getSession().getAttribute("username").toString();
           final EmailDao emailDao = DataBaseManager.getInstance().getDaoFactory().getEmailDao();
           final Email email = new Email();
		   email.setAdmin(admin);
		   email.setMessaggio(body);
		   email.setEmittente(from);
		   email.setDestinatario(to);
		    
		   emailDao.save(email);

		   response.getWriter().write("Email inviata con successo");
	   
		} catch (final JSONException e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
	}
} 
