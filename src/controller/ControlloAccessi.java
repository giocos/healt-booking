package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Accesso;
import persistence.DatabaseManager;
import persistence.dao.AccessoDao;

@SuppressWarnings("serial")
public class ControlloAccessi extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();

		if(session.getAttribute("loggato") != null) { 
			
			if(session.getAttribute("loggato").equals(true)) {
		
			AccessoDao accessoDao = DatabaseManager.getInstance().getDaoFactory().getAccessoDao();
			List<Accesso> accessi = accessoDao.findAll();
			
			request.setAttribute("accessi", accessi);
		
	    	RequestDispatcher dispatcher = request.getRequestDispatcher("html/visualizza_accessi.jsp");
	    	dispatcher.forward(request, response);
			}
			return;
		}
		response.sendError(404, "Effettuare l'accesso come admin o come employee per visualizzare quest'area");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}