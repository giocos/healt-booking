package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import factory.DatabaseManager;
import repository.LoggingDao;

@SuppressWarnings("serial")
public class EliminaAccessi extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LoggingDao loggingDao = DatabaseManager.getInstance().getDaoFactory().getLoggingDao();
	    loggingDao.deleteAll();
	   	request.setAttribute("nessun_accesso", true);
	    
	    RequestDispatcher dispatcher = request.getRequestDispatcher("html/visualizza_accessi.jsp");
	    dispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
