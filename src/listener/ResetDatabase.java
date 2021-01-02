package listener;

import exception.PersistenceException;
import factory.DatabaseManager;
import utils.UtilDao;

public class ResetDatabase implements Runnable {
	
	private UtilDao dao;
	
	@Override
	public void run() {

		try {
			dao = DatabaseManager.getInstance().getDaoFactory().getUtilDao();
			dao.resetDatabase();

		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}
}
