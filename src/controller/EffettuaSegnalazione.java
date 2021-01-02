package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.util.List;
//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import entity.Segnalazione;
import factory.DatabaseManager;
import repository.SegnalazioneDao;

@SuppressWarnings("serial")
public class EffettuaSegnalazione extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	
			String email = "Nessuna";
			if(!json.getString("email").equals("")) {
				 email = json.getString("email");
			}
			String nome = json.getString("nome");
			String cognome = json.getString("cognome");
			String motivazione = json.getString("motivazione");
			String commento = json.getString("commento");
			
			SegnalazioneDao segnalazioneDao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
		
			Segnalazione segnalazione = new Segnalazione();
			segnalazione.setId(segnalazioneDao.assignId() + 1);
			segnalazione.setEmail(email);
			segnalazione.setNomeUtente(nome + " " + cognome);
			segnalazione.setMotivazione(motivazione);
			segnalazione.setCommento(commento);
			segnalazioneDao.save(segnalazione);
		
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}

}
