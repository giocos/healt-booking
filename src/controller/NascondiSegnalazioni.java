package controller;

import entity.Segnalazione;
import jdbc.DatabaseManager;
import repository.SegnalazioneDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class NascondiSegnalazioni extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SegnalazioneDao segnalazioneDao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
		List<Segnalazione> segnalazioni = segnalazioneDao.findAll();
		HttpSession session = request.getSession();
	    
	    boolean tmp = true;
	    boolean first = true;
	    for (final Segnalazione segnalazione : segnalazioni) {
	    	if (segnalazione.getRisolto()) {
	    		if (first) {
					first = false;
					tmp = segnalazione.getMostra();
	    		}
	    		if (tmp) {
	    			segnalazione.setMostra(false);
				}
	    		else {
	    			segnalazione.setMostra(true);
				}
				segnalazioneDao.update(segnalazione);
			}
	    }
  
	    if (segnalazioni.size() > 0) {
			session.setAttribute("segnalazioni", segnalazioni);
		} else {
			session.setAttribute("vuoto", true);
		}
	    response.sendRedirect("risolviSegnalazione");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
