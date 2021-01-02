package controller;

import java.io.*;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Email;
import factory.DatabaseManager;
import repository.EmailDao;

import javax.mail.*;
import javax.mail.internet.*;

@WebServlet("/inviaEmail")
public class InviaEmail extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final String PASSWORD = "prenotazione18";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   this.doPost(request, response);   
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StringBuffer jsonReceived = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));		
		String line = reader.readLine();
		
		while(line != null) {
			jsonReceived.append(line);
			line = reader.readLine();
		}		
		
		try {
			JSONObject json = new JSONObject(jsonReceived.toString());
		
			String to = json.getString("to");
		    String from = json.getString("from");
		    String mex = json.getString("message");
			
		    String host = "smtp.gmail.com";
		    Properties properties = System.getProperties();
			
		    properties.setProperty("mail.transport.protocol", "smtp");     
		    properties.setProperty("mail.host", host);
		    properties.put("mail.smtp.auth", "true");
		    properties.put("mail.smtp.port", "465");
//		    properties.put("mail.debug", "true"); 
		    properties.put("mail.smtp.socketFactory.port", "465");  
		    properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
		    properties.put("mail.smtp.socketFactory.fallback", "false");  
		    properties.put("mail.smtp.starttls.enable", "true"); 
		    
		    Session session = Session.getDefaultInstance(properties, new Authenticator() {
		    	
			    	@Override
			    	protected PasswordAuthentication getPasswordAuthentication() {  
			    	       return new PasswordAuthentication(from, PASSWORD);  
			    	}  
		    });  

		    try {
		    	MimeMessage message = new MimeMessage(session);
			    message.setFrom(new InternetAddress(from));
			    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			    message.setSubject("[SOLVED] Risposta FAQ");
			    message.setText(mex);
			    Transport.send(message);
		      
		   } catch(MessagingException me) {
		      me.printStackTrace();
		   }
		   
		   String admin = request.getSession().getAttribute("username").toString(); 
		   
		   EmailDao emailDao = DatabaseManager.getInstance().getDaoFactory().getEmailDao();
		   Email email = new Email();
		   email.setAdmin(admin);
		   email.setMessaggio(mex);
		   email.setEmittente(from);
		   email.setDestinatario(to);
		    
		   emailDao.save(email);
		    
		   response.getWriter().write("Email inviata con successo");
	   
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
} 
