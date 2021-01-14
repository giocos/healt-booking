package listener;

import exception.PersistenceException;
import jdbc.DatabaseManager;
import repository.UtilDao;

public class ResetPrenotazioni implements Runnable {
	
	private UtilDao dao;

	protected ResetPrenotazioni() {}
	
	@Override
	public void run() {
		try {
			dao = DatabaseManager.getInstance().getDaoFactory().getUtilDao();
			dao.resetPrenotazioni();

		} catch (final PersistenceException e) {
			e.printStackTrace();
		}
	}
}
