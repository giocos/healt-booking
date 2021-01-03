package factory;

public class DataBaseManager {

	private static DataBaseManager database;

	private final DaoFactory daoFactory;
	
	private DataBaseManager() {
		daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRESQL);
	}

	//Singleton Pattern
	public static DataBaseManager getInstance() {
		if (database == null) {
			database = new DataBaseManager();
		}
		return database;
	}
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}
}
