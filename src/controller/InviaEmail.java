package controller;

import buffer.BufferFactory;
import entity.Email;
import jdbc.DatabaseManager;
import org.json.JSONException;
import org.json.JSONObject;
import repository.EmailDao;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;

import static controller.constants.EmailProperty.*;

@WebServlet("/inviaEmail")
public class InviaEmail extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String PASSWORD = "prenotazione18";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   this.doPost(request, response);   
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer jsonReceived = BufferFactory.getStringBuffer();
		BufferedReader reader = BufferFactory.getBufferReader(request.getInputStream());
		String line = reader.readLine();
		
		while (line != null) {
			jsonReceived.append(line);
			line = reader.readLine();
		}		
		
		try {
			JSONObject json = new JSONObject(jsonReceived.toString());
			final String to = json.getString("to");
			final String from = json.getString("from");
			final String body = json.getString("message");
			final Properties properties = System.getProperties();
			properties.setProperty("mail.transport.protocol", PROTOCOL);

			properties.setProperty("mail.host", HOST);
		    properties.put("mail.smtp.auth", AUTH);
		    properties.put("mail.smtp.port", PORT);
//		    properties.put("mail.debug", DEBUG);
		    properties.put("mail.smtp.socketFactory.port", PORT);
			properties.put("mail.smtp.starttls.enable", TLS_ENABLE);
		    properties.put("mail.smtp.socketFactory.class",SSL_FACTORY);
		    properties.put("mail.smtp.socketFactory.fallback", FALLBACK);

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
           final EmailDao emailDao = DatabaseManager.getInstance().getDaoFactory().getEmailDao();
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
