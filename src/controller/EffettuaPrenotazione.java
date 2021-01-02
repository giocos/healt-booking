package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import entity.CodiceQR;
import entity.Paziente;
import entity.Prenotazione;
import factory.DatabaseManager;
import repository.CodiceQRDao;
import repository.PazienteDao;
import repository.UniversitaDao;
import repository.PrenotazioneDao;

@SuppressWarnings("serial")
public class EffettuaPrenotazione extends HttpServlet {
	
	private final int LIMITE_PRENOTAZIONI = 50;
	private final int TEMPO_EFFETTIVO = 15;
	
	private final int CONVALIDA = 10;
	private final int TEMPO_VISITA = 10;
	private final String ORARIO_INIZIO = "00:00:00"; 
	private final String ORARIO_FINE = "23:59:00";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		
		String dateFormat = "HH:mm";
		String currentTime = new SimpleDateFormat(dateFormat).format(new Date());
		
		Calendar start = setTimeToCalendar(dateFormat, ORARIO_INIZIO);
		Calendar end = setTimeToCalendar(dateFormat, ORARIO_FINE);
		Calendar now = setTimeToCalendar(dateFormat, currentTime);		
		now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + TEMPO_EFFETTIVO);

		PrenotazioneDao prenotazioneDao = DatabaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
		int visiteTotali = prenotazioneDao.getTotalVisits();
		
		Date current = new Date(now.getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000));
		if(!(current.after(start.getTime()) && current.before(end.getTime()))) {
		    response.getWriter().write("false;Orario non valido per effettuare una prenotazione!");
			return;
		}
		
		PrintWriter out = response.getWriter();
		
		if(visiteTotali >= LIMITE_PRENOTAZIONI) {
			out.println("redirect;Attenzione: Limite Prenotazioni raggiunto");
			return;
		}
		
		String probabileVisita = new SimpleDateFormat(dateFormat).format(current);
		out.println("true;" + (visiteTotali + 1) + ";" + probabileVisita);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
		
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		
		StringBuffer jsonReceived = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));		
		String line = reader.readLine();
		
		while(line != null) {
			jsonReceived.append(line);
			line = reader.readLine();
		}		
		
		try {
			PrintWriter out = response.getWriter();
			JSONObject json = new JSONObject(jsonReceived.toString());
			
			PazienteDao pazienteDao = DatabaseManager.getInstance().getDaoFactory().getPazienteDao();
			PrenotazioneDao prenotazioneDao = DatabaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
			CodiceQRDao codiceQRDao = DatabaseManager.getInstance().getDaoFactory().getCodiceQRDao();
			UniversitaDao universitaDao = DatabaseManager.getInstance().getDaoFactory().getUniversitaDao();
			
			Long matricola = null;
			
			if(!json.getString("matricola").equals("")) {
				matricola = (Long.parseLong(json.getString("matricola")));
			}
			
			if(pazienteDao.findByPrimaryKey(json.getString("codiceFiscale")) != null) {
				out.println("false;Paziente con codice fiscale '" + json.getString("codiceFiscale") + "' gia' presente");
				return;
				
			} else {
				if(pazienteDao.exists(matricola)) {
					out.println("false;La Matricola '" + json.getString("matricola") + "' e' stata associata ad un altro Paziente");
					return;
				}
			}
			
			Paziente paziente = new Paziente();
			paziente.setCodiceFiscale(json.getString("codiceFiscale"));
			paziente.setNome(json.getString("nome"));
			paziente.setCognome(json.getString("cognome"));
			paziente.setInvalidita(json.getString("invalidita"));
			
			Double imp = new Double(25);
			
			if(matricola != null) {
				
				Paziente p = universitaDao.findByPrimaryKey(matricola);
				if(p != null) {
					if(paziente.getNome().equals(p.getNome()) && paziente.getCognome().equals(p.getCognome())) {
						imp = new Double(0);
						paziente.setMatricola(matricola);
					} else {
						paziente.setMatricola(null);
					}
					
				} else {
					paziente.setMatricola(null);
				}
			}
			
			if(!paziente.getInvalidita().equals("Nessuna")) {
				imp = new Double(0);
			}
			
			int visiteTotali = prenotazioneDao.getTotalVisits();
			Calendar now = Calendar.getInstance();
			now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + TEMPO_EFFETTIVO);
			
			
			Date date1 = new Date(now.getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000));
			Date date2 = new Date(now.getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000) - (CONVALIDA * 60000));
			
			String dateFormat = "HH:mm";
			String visita = new SimpleDateFormat(dateFormat).format(date1);
			String scadenza = new SimpleDateFormat(dateFormat).format(date2);
		
			CodiceQR codiceQR = new CodiceQR();
			codiceQR.setCodice(json.getString("hexcode"));
			codiceQR.setScadenza(scadenza);
			codiceQR.setConvalida(false);
			
			paziente.setCodiceQR(codiceQR.getCodice());
			
			Prenotazione prenotazione = new Prenotazione();
			prenotazione.setCodiceVisita(codiceQR.getCodice());
			prenotazione.setNomePaziente(paziente.getNome());
			prenotazione.setCognomePaziente(paziente.getCognome());
			prenotazione.setOrarioVisita(visita);
			prenotazione.setImporto(imp);
			
			codiceQRDao.save(codiceQR);
			pazienteDao.save(paziente);
			prenotazioneDao.save(prenotazione); 
			
			JSONObject pazienteJson = new JSONObject(paziente);
			out.println("true;" + pazienteJson.toString() + ";" + String.valueOf(prenotazione.getImporto()));
			
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	private Calendar setTimeToCalendar(String dateFormat, String date) {
	    
		Date time = null;
		try {
			time = new SimpleDateFormat(dateFormat).parse(date);
		} catch(ParseException e) {
			e.printStackTrace();
		}
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(time);

	    return calendar;
	}
}
