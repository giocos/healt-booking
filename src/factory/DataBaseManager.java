package factory;

public class DataBaseManager {

	private static DataBaseManager manager;

	private final DaoFactory daoFactory;
	
	private DataBaseManager() {
		daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRESQL);
	}

	//Singleton Pattern
	public static DataBaseManager getInstance() {
		if (manager == null) {
			manager = new DataBaseManager();
		}
		return manager;
	}
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}
}
