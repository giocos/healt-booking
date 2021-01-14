package controller;

import entity.CodiceQR;
import jdbc.DatabaseManager;
import repository.CodiceQRDao;
import repository.PrenotazioneDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@SuppressWarnings("serial")
public class ConvalidaPrenotazione extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final HttpSession session = request.getSession();
		if (session.getAttribute("loggato") != null) {
			if (session.getAttribute("loggato").equals(true)) {
				final String hexcode = request.getParameter("hexcode");
				final CodiceQRDao codiceQRDao = DatabaseManager.getInstance().getDaoFactory().getCodiceQRDao();

				final CodiceQR codiceQR = codiceQRDao.findByPrimaryKey(hexcode);
				final PrenotazioneDao prenotazioneDao = DatabaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
				
				if (codiceQR == null) {
					response.getWriter().write("Non &egrave; stata trovata alcuna prenotazione con il codice: " + hexcode);
				} else if(codiceQR.isConvalida()) {
						response.getWriter().write("Prenotazione gi&agrave; convalidata");
					} else {
						final String importo = String.valueOf(prenotazioneDao.findByPrimaryKey(hexcode).getImporto());
						codiceQR.setConvalida(true);
						codiceQRDao.update(codiceQR);
						response.getWriter().write("true;Prenotazione convalidata con successo;" + importo);		
					}
					return;
			}
		}
		response.sendError(404, "Effettuare l'accesso come admin o come employee per visualizzare quest'area");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
