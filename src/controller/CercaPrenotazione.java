package controller;

import entity.CodiceQR;
import entity.Prenotazione;
import jdbc.DatabaseManager;
import repository.CodiceQRDao;
import repository.PrenotazioneDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressWarnings("serial")
public class CercaPrenotazione extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		
		final String hexcode = request.getParameter("hexcode");
		final CodiceQRDao codiceQRDao = DatabaseManager.getInstance().getDaoFactory().getCodiceQRDao();
		final PrenotazioneDao prenotazioneDao = DatabaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
		final CodiceQR codiceQR = codiceQRDao.findByPrimaryKey(hexcode);
		
		if (codiceQR != null) {
			final Calendar scadenza = Calendar.getInstance();
			final String[] orario = codiceQR.getScadenza().split(":");
			
			scadenza.set(Calendar.HOUR_OF_DAY, Integer.parseInt(orario[0]));
			scadenza.set(Calendar.MINUTE, Integer.parseInt(orario[1]));
			
			if (new Date().after(scadenza.getTime())) {
				response.getWriter().write("false;Prenotazione scaduta");
			} else {
				final Prenotazione prenotazione = prenotazioneDao.findByPrimaryKey(codiceQR.getCodice());
				response.getWriter().write("true;" + codiceQR.getScadenza() + ";" + prenotazione.getOrarioVisita() + ";" + hexcode + ";" + codiceQR.isConvalida());
			}
		} else {
			response.getWriter().write("false;Codice non trovato");
		} 
	}

}
