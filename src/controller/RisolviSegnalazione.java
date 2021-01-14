package controller;

import buffer.BufferFactory;
import entity.Segnalazione;
import jdbc.DatabaseManager;
import org.json.JSONException;
import org.json.JSONObject;
import repository.SegnalazioneDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class RisolviSegnalazione extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final HttpSession session = request.getSession();
		if (session.getAttribute("loggato") != null) {
			if (session.getAttribute("loggato").equals(true)) {
				final String risposta = request.getParameter("risposta");
				final List<Segnalazione> s = risolviSegnalazione("", risposta);
			    
			    if (s.size() > 0) {
					request.setAttribute("segnalazioni", s);
				} else {
					request.setAttribute("vuoto", true);
				}
				final RequestDispatcher dispatcher = request.getRequestDispatcher("html/segnalazioni.jsp");
			    dispatcher.forward(request, response);

			    return;
			}
		}
		response.sendError(404, "Effettuare l'accesso come admin o come employee per visualizzare quest'area");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final StringBuffer jsonReceived = BufferFactory.getStringBuffer();
		final BufferedReader reader = BufferFactory.getBufferReader(request.getInputStream());
		String line = reader.readLine();
		
		while(line != null) {
			jsonReceived.append(line);
			line = reader.readLine();
		}		
		
		try {
			final JSONObject json = new JSONObject(jsonReceived.toString());
			final String risposta = json.getString("risposta");
			final String id = json.getString("id");
		    
		    risolviSegnalazione(id, risposta);
			response.setContentType("text/html");
			
		} catch (final JSONException e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
	}
	
	private List<Segnalazione> risolviSegnalazione(String id, String risposta) {
		final SegnalazioneDao segnalazioneDao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
		final List<Segnalazione> segnalazioni = segnalazioneDao.findAll();
		  
		if (!id.equals("")) {
		    for (final Segnalazione s : segnalazioni) {
		    	if (s.getId().equals(Integer.parseInt(id))) {
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
