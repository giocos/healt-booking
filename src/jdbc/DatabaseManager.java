package jdbc;

public class DatabaseManager {

	private static DatabaseManager manager;

	private final DaoFactory daoFactory;
	
	private DatabaseManager() {
		daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRESQL);
	}

	// Singleton Pattern
	public static DatabaseManager getInstance() {
		if (manager == null) {
			manager = new DatabaseManager();
		}
		return manager;
	}
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}
}
