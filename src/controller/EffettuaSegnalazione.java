package controller;

import buffer.BufferFactory;
import entity.Segnalazione;
import jdbc.DatabaseManager;
import org.json.JSONException;
import org.json.JSONObject;
import repository.SegnalazioneDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@SuppressWarnings("serial")
public class EffettuaSegnalazione extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			String email = "Nessuna";
			if (!json.getString("email").equals("")) {
				 email = json.getString("email");
			}
			final String nome = json.getString("nome");
			final String cognome = json.getString("cognome");
			final String motivazione = json.getString("motivazione");
			final String commento = json.getString("commento");

			final SegnalazioneDao segnalazioneDao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
			final Segnalazione segnalazione = new Segnalazione();
			segnalazione.setId(segnalazioneDao.getId() + 1);
			segnalazione.setEmail(email);
			segnalazione.setNomeUtente(nome + " " + cognome);
			segnalazione.setMotivazione(motivazione);
			segnalazione.setCommento(commento);

			segnalazioneDao.save(segnalazione);
		
		} catch (final JSONException e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
	}

}
