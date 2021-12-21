package listener;

import exception.PersistenceException;
import jdbc.DatabaseManager;
import repository.UtilDao;

public class ResetPrenotazioni implements Runnable {

	protected ResetPrenotazioni() {}
	
	@Override
	public void run() {
		try {
			UtilDao dao = DatabaseManager.getInstance().getDaoFactory().getUtilDao();
			dao.resetPrenotazioni();

		} catch (final PersistenceException e) {
			e.printStackTrace();
		}
	}
}
