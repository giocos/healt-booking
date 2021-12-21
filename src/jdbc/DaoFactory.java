package jdbc;

import jdbc.impl.PostgresDaoFactory;
import repository.*;

public abstract class DaoFactory {

	public static final int HSQLDB = 1;
	public static final int POSTGRESQL = 2;
	
	protected static DaoFactory getDaoFactory(int whichFactory) {
		switch (whichFactory) {
			case HSQLDB:
				return null;
			case POSTGRESQL:
				return new PostgresDaoFactory();
			default:
				throw new IllegalArgumentException("Factory " + whichFactory + " not recognized");
		}
	}
	
	public abstract AmministratoreDao getAmministratoreDao();

	public abstract SegnalazioneDao getSegnalazioneDao();

	public abstract PrenotazioneDao getPrenotazioneDao();

	public abstract ImpiegatoDao getImpiegatoDao();

	public abstract PazienteDao getPazienteDao();

	public abstract CodiceQRDao getCodiceQRDao();

	public abstract LoggingDao getLoggingDao();

	public abstract AteneoDao getAteneoDao();

	public abstract EmailDao getEmailDao();
	
	public abstract UtilDao getUtilDao();
}
