package listener;

import exception.PersistenceException;
import factory.DataBaseManager;
import repository.impl.UtilDao;

public class ResetPrenotazioni implements Runnable {
	
	private UtilDao dao;

	protected ResetPrenotazioni() {}
	
	@Override
	public void run() {
		try {
			dao = DataBaseManager.getInstance().getDaoFactory().getUtilDao();
			dao.resetPrenotazioni();

		} catch (final PersistenceException e) {
			e.printStackTrace();
		}
	}
}
