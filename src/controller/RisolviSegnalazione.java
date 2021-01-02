package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import entity.Segnalazione;
import factory.DatabaseManager;
import repository.SegnalazioneDao;

@SuppressWarnings("serial")
public class RisolviSegnalazione extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loggato") != null) { 
			
			if(session.getAttribute("loggato").equals(true)) {
		
				String risposta = request.getParameter("risposta");
			    List<Segnalazione> s = risolviSegnalazione("", risposta);
			    
			    if(s.size() > 0)
				     request.setAttribute("segnalazioni", s);
				 else
					 request.setAttribute("vuoto", true);
		    	    
			    RequestDispatcher dispatcher = request.getRequestDispatcher("html/segnalazioni.jsp");
			    dispatcher.forward(request, response);
			    return;
			}
		}
		response.sendError(404, "Effettuare l'accesso come admin o come employee per visualizzare quest'area");
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
			
			String risposta = json.getString("risposta");
		    String id = json.getString("id");
		    
		    risolviSegnalazione(id, risposta);
		    
//		    response.setContentType("text/html");
			
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	private List<Segnalazione> risolviSegnalazione(String id, String risposta) {
		SegnalazioneDao segnalazioneDao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
		List<Segnalazione> segnalazioni = segnalazioneDao.findAll();
		  
		if(!id.equals("")) {
		    for(Segnalazione s:segnalazioni) {
		    	
		    	if(s.getId().equals(Integer.parseInt(id))) {
		    		
		    		s.setRisposta(risposta);
		            s.setRisolto(true);
		            segnalazioneDao.update(s);
		            break; 
		    	}
		    }
		}
	    return segnalazioni;
	}
	
}
