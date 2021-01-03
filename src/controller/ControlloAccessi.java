package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entity.Logging;
import factory.DataBaseManager;
import repository.LoggingDao;

@SuppressWarnings("serial")
public class ControlloAccessi extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final HttpSession session = request.getSession();
		if (session.getAttribute("loggatoAdmin") != null) {
			if (session.getAttribute("loggatoAdmin").equals(true)) {
				final LoggingDao loggingDao = DataBaseManager.getInstance().getDaoFactory().getLoggingDao();
				final List<Logging> logs = loggingDao.findAll();
				
				if (logs.size() > 0) {
					request.setAttribute("accessi", logs);
				} else {
					request.setAttribute("nessun_accesso", true);
				}
				final RequestDispatcher dispatcher = request.getRequestDispatcher("html/visualizza_accessi.jsp");
		    	dispatcher.forward(request, response);
			}
			return;
		}
		response.sendError(404, "Effettuare l'accesso come admin per visualizzare quest'area");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
