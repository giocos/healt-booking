package listener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import entity.CodiceQR;
import entity.Paziente;
import entity.Prenotazione;
import factory.DatabaseManager;
import repository.CodiceQRDao;
import repository.PazienteDao;
import repository.PrenotazioneDao;

public class EliminaPrenotazione implements Runnable {
	
	@Override
	public void run() {
		
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		CodiceQRDao codiceQRDao = DatabaseManager.getInstance().getDaoFactory().getCodiceQRDao();
		List<CodiceQR> codici = codiceQRDao.findAll();
		
		if(!codici.isEmpty()) {

			PazienteDao pazienteDao = DatabaseManager.getInstance().getDaoFactory().getPazienteDao();
			PrenotazioneDao prenotazioneDao = DatabaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
			
			for(CodiceQR it:codici) {
				
				if(scaduta(it.getScadenza()) && !it.isConvalida()) {
					
					Prenotazione prenotazione = prenotazioneDao.findByPrimaryKey(it.getCodice());
					prenotazioneDao.delete(prenotazione);
					
					Paziente paziente = pazienteDao.findByForeignKey(it.getCodice());
					pazienteDao.delete(paziente);
					
					codiceQRDao.delete(it);
//					System.out.println("deleted");
				}
			}
		}
	}
	
	private boolean scaduta(String orarioVisita) {
		
		Calendar scadenza = Calendar.getInstance();
		String[] orario = orarioVisita.split(":");
		scadenza.set(Calendar.HOUR_OF_DAY, Integer.parseInt(orario[0]));
		scadenza.set(Calendar.MINUTE, Integer.parseInt(orario[1]));
		
		return new Date().after(scadenza.getTime());
	}
}
