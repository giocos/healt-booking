package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import model.CodiceQR;
import model.Paziente;
import model.Prenotazione;
import persistence.DatabaseManager;
import persistence.dao.CodiceQRDao;
import persistence.dao.PazienteDao;
import persistence.dao.UniversitaDao;
import persistence.dao.PrenotazioneDao;

@SuppressWarnings("serial")
public class FormPrenotazione extends HttpServlet {
	
	private final int MAX = 50;
	private final int CONVALIDA = 20;
	private final int TEMPO_VISITA = 15;
	private final String ORARIO_INIZIO = "14:00"; 
	private final String ORARIO_FINE = "19:00";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
		
		Calendar now = Calendar.getInstance();
		String current = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
		
		if(current.compareTo(ORARIO_INIZIO) < 0 || current.compareTo(ORARIO_FINE) > 0) {
			response.getWriter().write("false<split>!!!Orario non valido!!!");
			return;
		}
		
		StringBuffer jsonReceived = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));		
		String line = reader.readLine();
		
		while(line != null) {
			jsonReceived.append(line);
			line = reader.readLine();
		}		
//		System.out.println(jsonReceived.toString());//lo studente ricevuto tramite chiamata AJAX
		try {
			
			PazienteDao pazienteDao = DatabaseManager.getInstance().
					getDaoFactory().getPazienteDao();
			PrenotazioneDao prenotazioneDao = DatabaseManager.getInstance().
					getDaoFactory().getPrenotazioneDao();
			CodiceQRDao codiceQRDao = DatabaseManager.getInstance().
					getDaoFactory().getCodiceQRDao();
			UniversitaDao universitaDao = DatabaseManager.getInstance().
					getDaoFactory().getUniversitaDao();
			
			PrintWriter out = response.getWriter();
			int visiteTotali = prenotazioneDao.getTotalVisits();
			
			if(visiteTotali >= MAX) {
				out.println("redirect;Attenzione: Limite Prenotazioni raggiunto");
				return;
			}
			
			JSONObject json = new JSONObject(jsonReceived.toString());
			
			Long matricola = null;
			
			if(!json.getString("matricola").equals("")) {
				matricola = (Long.parseLong(json.getString("matricola")));
			}
			
			if(pazienteDao.findByPrimaryKey(json.getString("codiceFiscale")) != null) {
				out.println("false<split>Paziente con codice fiscale: '" + json.getString("codiceFiscale") + "' gia' presente");
				return;
				
			} else {
				if(pazienteDao.exists(matricola)) {
					out.println("false<split>La Matricola: '" + json.getString("matricola") + "' e' stata associata ad un altro Paziente");
					return;
				}
			}
			
			Paziente paziente = new Paziente();
			paziente.setCodiceFiscale(json.getString("codiceFiscale"));
			paziente.setNome(json.getString("nome"));
			paziente.setCognome(json.getString("cognome"));
			paziente.setMatricola(matricola);
			paziente.setInvalidita(json.getString("invalidita"));
			
			Double imp = new Double(25);
			
			if(paziente.getMatricola() != null) {
				if(universitaDao.findByPrimaryKey(paziente.getMatricola()) == null) { 
					if(!paziente.getInvalidita().equals("Nessuna"))
						imp = new Double(0);
				} else {
					imp = new Double(0);
				}
			} else {
				if(!paziente.getInvalidita().equals("Nessuna"))
					imp = new Double(0);
			}
			
			Date date1 = new Date(Calendar.getInstance().getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000));
			Date date2 = new Date(Calendar.getInstance().getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000) - (CONVALIDA * 60000));
			
			int indexOf = date1.toString().indexOf(":") - 2;
			String visita = date1.toString().substring(indexOf, indexOf + 5);
			String scadenza = date2.toString().substring(indexOf, indexOf + 5);
			
			CodiceQR codiceQR = new CodiceQR();
			codiceQR.setCodice(json.getString("hexcode"));
			codiceQR.setScadenza(scadenza);
			codiceQR.setValido(true);
			paziente.setCodiceQR(codiceQR.getCodice());
			
			Prenotazione prenotazione = new Prenotazione();
			prenotazione.setCodiceVisita(codiceQR.getCodice());
			prenotazione.setNomePaziente(paziente.getNome());
			prenotazione.setCognomePaziente(paziente.getCognome());
			prenotazione.setOrarioVisita(visita);
			prenotazione.setImporto(imp);
			
			codiceQRDao.save(codiceQR);
			pazienteDao.save(paziente);
			prenotazioneDao.save(prenotazione); 
				
			//TODO VISUALIZZARE LA PAGINA IN UN DIALOG
			out.println("true<split>" + (++ visiteTotali) + "<split>" + visita +"<split>");
			
//			response.setContentType("text/html");
			
			out.println("<html>");
			out.println("<head><title>Riepilogo Dati</title>");
//			out.println("<link rel='stylesheet' href='bootstrap-3.3.7-dist/css/bootstrap.min.css'>");
//			out.println("<script src='..js/jquery/jquery-3.2.1.min.js'></script>");
//			out.println("<script src='..js/jquery/jquery.qrcode.js'></script>");
//			out.println("<script src='..js/jquery/html2canvas.js'></script>");
//			out.println("<script src='..js/jquery/jspdf.min.js'></script>");
//			out.println("<script src='..js/qr_code.js'></script>");
//			out.println("<script src='..js/pdf_print.js'></script>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div id='content style='background-color:white;'>");
			out.println("<h1>Riepilogo:</h1>");
			out.println("<h3>Codice Fiscale: " + paziente.getCodiceFiscale() + "</h3>");
			out.println("<h3>Nome: " + paziente.getNome() + "</h3>");
			out.println("<h3>Cognome: " + paziente.getCognome() + "</h3>");
			out.println("<h3>Matricola: " + String.valueOf(paziente.getMatricola()) + "</h3>");
			out.println("<h3>Invalidit&agrave: " + paziente.getInvalidita() + "</h3>");
			out.println("<h3>Importo: " + String.valueOf(prenotazione.getImporto()) + "0 &euro;</h3>");
			out.println("<input id='text' type='hidden' value=" + codiceQR.getCodice() + "/>");
			out.println("<div id='print'>");
			out.println("<div id='qrcode'></div>");
			out.println("<h3>Codice: " + codiceQR.getCodice() + "</h3>");
			out.println("</div>");
			out.println("</div>"); 
			out.println("<h3 style='color:red'>stampa promemoria "
					+ "<button id='cmd' type='button' class='btn btn-default btn-sm'>"
					+ "<span class='glyphicon glyphicon-print'></span> PDF</button></h3>");
			out.println("</body>");
			out.println("</html>");
			
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
}
