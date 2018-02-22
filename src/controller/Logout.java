package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Accesso;
import persistence.DatabaseManager;
import persistence.dao.AccessoDao;

@SuppressWarnings("serial")
public class Logout extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		session.setAttribute("popUp", false);
		session.setAttribute("username", null);
		session.setAttribute("loggato", false);
			
		if(session.getAttribute("loggatoAdmin") != null && session.getAttribute("loggatoAdmin").equals(true)) {
			session.setAttribute("loggatoAdmin", false);
		} else {
			session.setAttribute("loggatoEmployee", false);
		}
		registraDisconnessione(request.getQueryString(), "logout");
		response.sendRedirect("home");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	private void registraDisconnessione(String username, String azione) {
		
		AccessoDao accessoDao = DatabaseManager.getInstance().getDaoFactory().getAccessoDao();
		Accesso accesso = new Accesso();
		accesso.setAzione(azione);
		Date date = new Date();
		accesso.setData(date);
		accesso.setOrario(new SimpleDateFormat("hh:MM:ss").format(date));
		accesso.setNomeUtente(username);
		accessoDao.save(accesso);
	}
}
