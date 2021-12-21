package jdbc;

public class DatabaseManager {

	private static DatabaseManager databaseManager = null;

	private final DaoFactory daoFactory;
	
	private DatabaseManager() {
		daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRESQL);
	}

	// Singleton Pattern
	public static DatabaseManager getInstance() {
		if (databaseManager == null) {
			databaseManager = new DatabaseManager();
		}
		return databaseManager;
	}
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}
}
