package controller;

import entity.Segnalazione;
import jdbc.DatabaseManager;
import repository.SegnalazioneDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class Home extends HttpServlet {

	@Override
	public void init(ServletConfig config) {
		// ...
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("loggato") != null && session.getAttribute("loggato").equals(true)) {
			session.setAttribute("numSegnalazioni", contaSegnalazioni());
		} else {
			session.setAttribute("loggato", false);
		}
		
		if (session.getAttribute("wrong") != null) {
			if (session.getAttribute("wrong").equals(false)) {
				session.setAttribute("popUp", false);
			} else {
				session.setAttribute("wrong", false);
			}
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
		dispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	@Override
	public void destroy() {
		Object loggato = getServletContext().getAttribute("loggato");
		if (loggato != null && ((boolean) loggato)) {
			getServletContext().setAttribute("loggato", null);
		}
	}
	
	private int contaSegnalazioni() {
		SegnalazioneDao dao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
		List<Segnalazione> segnalazioni = dao.findAll();
		return (int) segnalazioni.stream().filter(s -> !s.getRisolto()).count();
	}
}
