package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Logging;
import entity.Amministratore;
import entity.Impiegato;
import entity.Segnalazione;
import factory.DatabaseManager;
import repository.LoggingDao;
import repository.AmministratoreDao;
import repository.ImpiegatoDao;
import repository.SegnalazioneDao;

@SuppressWarnings("serial")
public class Login extends HttpServlet {

	private final int SCADENZA_SESSIONE = 300;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		HttpSession session = request.getSession();
		session.setAttribute("username", null);
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		AmministratoreDao amministratoreDao = DatabaseManager.getInstance().getDaoFactory().getAmministratoreDao();
		Amministratore amministratore = amministratoreDao.findByPrimaryKey(username);
	
		if(amministratore == null) {
			
			ImpiegatoDao impiegatoDao = DatabaseManager.getInstance().getDaoFactory().getImpiegatoDao();
			Impiegato impiegato = impiegatoDao.findByPrimaryKey(username);
		
			if(impiegato == null) {
				
				session.setAttribute("popUp", true);
				session.setAttribute("wrong", true);//attributo che serve solo per non visualizzare il popUp
				session.setAttribute("popUpMessage", "Nessun utente registrato come " + username);
				
			} else { 
				if(password.equals(impiegato.getPassword())) {
				
					session.setAttribute("numSegnalazioni", contaSegnalazioni());
					session.setAttribute("popUp", false);
					session.setAttribute("username", username);				
					session.setAttribute("loggato", true);
					session.setAttribute("loggatoEmployee", true);
					session.setAttribute("username", username);//JSTL
					session.setMaxInactiveInterval(SCADENZA_SESSIONE);//scadenza in secondi
					registraAccesso(username, "login");
					
				} else {
					session.setAttribute("popUp", true);
					session.setAttribute("wrong", true);
					session.setAttribute("popUpMessage", "Spiacente, password non corrispondente per " + username);
				}
			}	
			
		} else { 
			if(password.equals(amministratore.getPassword())) {	
				
				session.setAttribute("numSegnalazioni", contaSegnalazioni());
				session.setAttribute("popUp", false);
				session.setAttribute("username", username);
				session.setAttribute("loggato", true);
				session.setAttribute("loggatoAdmin", true);
				session.setAttribute("username", username);//JSTL
				session.setMaxInactiveInterval(SCADENZA_SESSIONE);
				registraAccesso(username, "login");
								
			} else {
				session.setAttribute("popUp", true);
				session.setAttribute("wrong", true);
				session.setAttribute("popUpMessage", "Spiacente, password non corrispondente per " + username);
			}
		}
		response.sendRedirect("home");
	}
	
	private void registraAccesso(String username, String azione) {
		
		LoggingDao loggingDao = DatabaseManager.getInstance().getDaoFactory().getLoggingDao();
		Logging logging = new Logging();
		logging.setAzione(azione);
		Date date = new Date();
		int id = loggingDao.assignId() + 1;//Chiave Primaria
		logging.setId(id);
		logging.setData(date);
		logging.setOrario(new SimpleDateFormat("hh:MM:ss").format(date));
		logging.setNomeUtente(username);
		loggingDao.save(logging);
	}
	
	private int contaSegnalazioni() {
		
		SegnalazioneDao dao = DatabaseManager.getInstance().getDaoFactory().getSegnalazioneDao();
		List<Segnalazione> segnalazioni = dao.findAll();
		
		int cont = 0;
		for(Segnalazione s:segnalazioni) {
			if(!s.getRisolto())
				cont ++;
		}
		return cont;
	}
}
