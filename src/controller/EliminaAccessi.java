package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.PersistenceException;
import factory.DataBaseManager;
import repository.LoggingDao;

@SuppressWarnings("serial")
public class EliminaAccessi extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			final LoggingDao loggingDao = DataBaseManager.getInstance().getDaoFactory().getLoggingDao();
			loggingDao.deleteAll();
			request.setAttribute("nessun_accesso", true);

			final RequestDispatcher dispatcher = request.getRequestDispatcher("html/visualizza_accessi.jsp");
			dispatcher.forward(request, response);

		} catch (final PersistenceException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
