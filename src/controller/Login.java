package controller;

import entity.Amministratore;
import entity.Impiegato;
import entity.Segnalazione;
import jdbc.DatabaseManager;
import repository.AmministratoreDao;
import repository.ImpiegatoDao;
import repository.SegnalazioneDao;
import utility.LoggingUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class Login extends HttpServlet {

	private static final int SCADENZA_SESSIONE = 300;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final HttpSession session = request.getSession();
		session.setAttribute("username", null);
		
		final String username = request.getParameter("username");
		final String password = request.getParameter("password");
		final AmministratoreDao amministratoreDao = DatabaseManager.getInstance().getDaoFactory().getAmministratoreDao();
		final Amministratore amministratore = amministratoreDao.findByPrimaryKey(username);
	
		if (amministratore == null) {
			final ImpiegatoDao impiegatoDao = DatabaseManager.getInstance().getDaoFactory().getImpiegatoDao();
			final Impiegato impiegato = impiegatoDao.findByPrimaryKey(username);
		
			if (impiegato == null) {
				session.setAttribute("popUp", true);
				session.setAttribute("wrong", true);//attributo che serve solo per non visualizzare il popUp
				session.setAttribute("popUpMessage", "Nessun utente registrato come " + username);
				
			} else { 
				if (password.equals(impiegato.getPassword())) {
					session.setAttribute("numSegnalazioni", contaSegnalazioni());
					session.setAttribute("popUp", false);
					session.setAttribute("username", username);				
					session.setAttribute("loggato", true);
					session.setAttribute("loggatoEmployee", true);
					session.setAttribute("username", username);//JSTL
					session.setMaxInactiveInterval(SCADENZA_SESSIONE);//scadenza in secondi
					LoggingUtil.registraAccesso(username, "login");
					
				} else {
					session.setAttribute("popUp", true);
					session.setAttribute("wrong", true);
					session.setAttribute("popUpMessage", "Spiacente, password non corrispondente per " + username);
				}
			}
		} else { 
			if (password.equals(amministratore.getPassword())) {
				session.setAttribute("numSegnalazioni", contaSegnalazioni());
				session.setAttribute("popUp", false);
				session.setAttribute("username", username);
				session.setAttribute("loggato", true);
				session.setAttribute("loggatoAdmin", true);
				session.setAttribute("username", username);//JSTL
				session.setMaxInactiveInterval(SCADENZA_SESSIONE);
				LoggingUtil.registraAccesso(username, "login");
								
			} else {
				session.setAttribute("popUp", true);
				session.setAttribute("wrong", true);
				session.setAttribute("popUpMessage", "Spiacente, password non corrispondente per " + username);
			}
		}
		response.sendRedirect("home");
	}

	private int contaSegnalazioni() {
		final SegnalazioneDao dao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
		final List<Segnalazione> segnalazioni = dao.findAll();

		return (int) segnalazioni.stream().filter(s -> !s.getRisolto()).count();
	}
}
