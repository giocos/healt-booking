package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entity.Segnalazione;
import factory.DatabaseManager;
import repository.SegnalazioneDao;

@SuppressWarnings("serial")
public class Home extends HttpServlet {
	
	private HttpSession session;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		session = request.getSession();
	
		if(session.getAttribute("loggato") != null && session.getAttribute("loggato").equals(true)) {
			session.setAttribute("numSegnalazioni", contaSegnalazioni());
		} else {
			session.setAttribute("loggato", false);
		}
		
		if(session.getAttribute("wrong") != null) {
			
			if(session.getAttribute("wrong").equals(false)) {
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
		
		if(session != null) { 
			if(session.getAttribute("loggato").equals(true)) {
//				System.out.println("INVALIDATE");
				session.invalidate();
			}
		}
	}
	
	private int contaSegnalazioni() {
		
		SegnalazioneDao dao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
		List<Segnalazione> segnalazioni = dao.findAll();
		
		int cont = 0;
		for(Segnalazione s:segnalazioni)  {
			if(!s.getRisolto()) 
				cont ++;
		}
		return cont;
	}
}
