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

import buffer.BufferFactory;
import org.json.JSONException;
import org.json.JSONObject;
import entity.CodiceQR;
import entity.Paziente;
import entity.Prenotazione;
import factory.DataBaseManager;
import repository.CodiceQRDao;
import repository.PazienteDao;
import repository.UniversitaDao;
import repository.PrenotazioneDao;

@SuppressWarnings("serial")
public class EffettuaPrenotazione extends HttpServlet {
	
	private static final int LIMITE_PRENOTAZIONI = 50;
	private static final int TEMPO_EFFETTIVO = 15;
	private static final int TEMPO_VISITA = 10;
	private static final int CONVALIDA = 10;

	private static final String ORARIO_INIZIO = "00:00:00";
	private static final String ORARIO_FINE = "23:59:00";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		final String dateFormat = "HH:mm";
		final String currentTime = new SimpleDateFormat(dateFormat).format(new Date());

		final Calendar start = setTimeToCalendar(dateFormat, ORARIO_INIZIO);
		final Calendar end = setTimeToCalendar(dateFormat, ORARIO_FINE);
		final Calendar now = setTimeToCalendar(dateFormat, currentTime);
		now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + TEMPO_EFFETTIVO);

		final PrenotazioneDao prenotazioneDao = DataBaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
		final int visiteTotali = prenotazioneDao.getTotalVisits();

		final Date current = new Date(now.getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000));
		if (!(current.after(start.getTime()) && current.before(end.getTime()))) {
		    response.getWriter().write("false;Orario non valido per effettuare una prenotazione!");
			return;
		}
		final PrintWriter out = response.getWriter();
		if (visiteTotali >= LIMITE_PRENOTAZIONI) {
			out.println("redirect;Attenzione: Limite Prenotazioni raggiunto");
			return;
		}
		final String probabileVisita = new SimpleDateFormat(dateFormat).format(current);
		out.println("true;" + (visiteTotali + 1) + ";" + probabileVisita);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		final StringBuffer jsonReceived = BufferFactory.getStringBuffer();
		final BufferedReader reader = BufferFactory.getBufferReader(request.getInputStream());
		String line = reader.readLine();

		while (line != null) {
			jsonReceived.append(line);
			line = reader.readLine();
		}		
		
		try {
			final PrintWriter out = response.getWriter();
			final JSONObject json = new JSONObject(jsonReceived.toString());

			final PazienteDao pazienteDao = DataBaseManager.getInstance().getDaoFactory().getPazienteDao();
			final PrenotazioneDao prenotazioneDao = DataBaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
			final CodiceQRDao codiceQRDao = DataBaseManager.getInstance().getDaoFactory().getCodiceQRDao();
			final UniversitaDao universitaDao = DataBaseManager.getInstance().getDaoFactory().getUniversitaDao();
			
			Long matricola = null;
			if (!json.getString("matricola").equals("")) {
				matricola = (Long.parseLong(json.getString("matricola")));
			}
			
			if (pazienteDao.findByPrimaryKey(json.getString("codiceFiscale")) != null) {
				out.println("false;Paziente con codice fiscale '" + json.getString("codiceFiscale") + "' gia' presente");
				return;
			} else {
				if (pazienteDao.exists(matricola)) {
					out.println("false;La Matricola '" + json.getString("matricola") + "' e' stata associata ad un altro Paziente");
					return;
				}
			}
			final Paziente paziente = new Paziente();
			paziente.setCodiceFiscale(json.getString("codiceFiscale"));
			paziente.setNome(json.getString("nome"));
			paziente.setCognome(json.getString("cognome"));
			paziente.setInvalidita(json.getString("invalidita"));

			double imp = 25d;
			if (matricola != null) {
				final Paziente pazienteByPK = universitaDao.findByPrimaryKey(matricola);
				if (pazienteByPK != null) {
					if (pazienteByPK.getNome().equals(paziente.getNome()) && pazienteByPK.getCognome().equals(paziente.getCognome())) {
						imp = 0d;
						pazienteByPK.setMatricola(matricola);
					} else {
						pazienteByPK.setMatricola(null);
					}
				} else {
					paziente.setMatricola(null);
				}
			}
			
			if (!paziente.getInvalidita().equalsIgnoreCase("Nessuna")) {
				imp = 0d;
			}
			final int visiteTotali = prenotazioneDao.getTotalVisits();
			final Calendar now = Calendar.getInstance();
			now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + TEMPO_EFFETTIVO);

			final Date date1 = new Date(now.getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000));
			final Date date2 = new Date(now.getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000) - (CONVALIDA * 60000));

			final String dateFormat = "HH:mm";
			final String visita = new SimpleDateFormat(dateFormat).format(date1);
			final String scadenza = new SimpleDateFormat(dateFormat).format(date2);

			final CodiceQR codiceQR = new CodiceQR();
			codiceQR.setCodice(json.getString("hexcode"));
			codiceQR.setScadenza(scadenza);
			codiceQR.setConvalida(false);
			
			paziente.setCodiceQR(codiceQR.getCodice());

			final Prenotazione prenotazione = new Prenotazione();
			prenotazione.setCodiceVisita(codiceQR.getCodice());
			prenotazione.setNomePaziente(paziente.getNome());
			prenotazione.setCognomePaziente(paziente.getCognome());
			prenotazione.setOrarioVisita(visita);
			prenotazione.setImporto(imp);
			
			codiceQRDao.save(codiceQR);
			pazienteDao.save(paziente);
			prenotazioneDao.save(prenotazione);

			final JSONObject pazienteJson = new JSONObject(paziente);
			out.println("true;" + pazienteJson.toString() + ";" + String.valueOf(prenotazione.getImporto()));
			
		} catch (final JSONException e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
	}
	
	private Calendar setTimeToCalendar(String dateFormat, String date) {
		Date time = null;
		Calendar calendar = null;
		try {
			time = new SimpleDateFormat(dateFormat).parse(date);
			calendar = Calendar.getInstance();
			calendar.setTime(time);

		} catch(ParseException e) {
			e.printStackTrace();
		}
	    return calendar;
	}
}
