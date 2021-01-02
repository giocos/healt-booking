package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.Prenotazione;
import factory.DatabaseManager;
import repository.PrenotazioneDao;

@SuppressWarnings("serial")
public class RestituisciPrenotazioni extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		PrenotazioneDao prenotazioneDao = DatabaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
		List<Prenotazione> prenotazioni = prenotazioneDao.findAll();		
		
		if(prenotazioni.size() > 0)
			request.setAttribute("prenotazioni", prenotazioni);
		else
			request.setAttribute("vuoto", true);
	
		RequestDispatcher dispatcher = request.getRequestDispatcher("html/visualizza_prenotazioni.jsp");
		dispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
