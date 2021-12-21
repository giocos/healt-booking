package listener;

import entity.CodiceQR;
import entity.Paziente;
import entity.Prenotazione;
import jdbc.DaoFactory;
import jdbc.DatabaseManager;
import repository.CodiceQRDao;
import repository.PazienteDao;
import repository.PrenotazioneDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EliminaPrenotazione implements Runnable {

	private static final String COLUMN = ":";

	private final DaoFactory factory;

	protected EliminaPrenotazione() {
		factory = DatabaseManager.getInstance().getDaoFactory();
	}

	@Override
	public void run() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		CodiceQRDao codiceQRDao = factory.getCodiceQRDao();
		List<CodiceQR> codici = codiceQRDao.findAll();
		
		if (!codici.isEmpty()) {
			PazienteDao pazienteDao = factory.getPazienteDao();
			PrenotazioneDao prenotazioneDao = factory.getPrenotazioneDao();
			for (CodiceQR it : codici) {
				if (scaduta(it.getScadenza()) && !it.isConvalida()) {
					Prenotazione prenotazione = prenotazioneDao.findByPrimaryKey(it.getCodice());
					prenotazioneDao.delete(prenotazione);

					Paziente paziente = pazienteDao.findByForeignKey(it.getCodice());
					pazienteDao.delete(paziente);
					
					codiceQRDao.delete(it);
					System.out.println("Prenotazione " + prenotazione.getCodiceVisita() + " eliminata");
				}
			}
		}
	}
	
	private boolean scaduta(String orarioVisita) {
		if (orarioVisita == null || orarioVisita.isEmpty()) {
			throw new IllegalArgumentException("Parametro 'orarioVisita' non può essere null");
		}
		final Calendar scadenza = Calendar.getInstance();
		final String[] orario = orarioVisita.split(COLUMN);
		scadenza.set(Calendar.HOUR_OF_DAY, Integer.parseInt(orario[0]));
		scadenza.set(Calendar.MINUTE, Integer.parseInt(orario[1]));

		return new Date().after(scadenza.getTime());
	}
}
