package controller;

import entity.Segnalazione;
import jdbc.DatabaseManager;
import repository.SegnalazioneDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class RestituisciSegnalazioni extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final SegnalazioneDao segnalazioneDao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
		final List<Segnalazione> segnalazioni = segnalazioneDao.findAll();
		  
	    if (segnalazioni.size() > 0) {
			request.setAttribute("segnalazioni", segnalazioni);
		} else {
			request.setAttribute("vuoto", true);
		}
	    RequestDispatcher dispatcher = request.getRequestDispatcher("html/assistenza.jsp");
	    dispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
