package factory;

import repository.*;
import utils.UtilDao;

public abstract class DaoFactory {

	public static final int HSQLDB = 1;
	public static final int POSTGRESQL = 2;
	
	public static DaoFactory getDaoFactory(int whichFactory) {
		
		switch(whichFactory) {
			case HSQLDB:
				return null;
			case POSTGRESQL:
				return new PostgresDaoFactory();
			default:
				throw new IllegalArgumentException();
		}
	}
	
	public abstract PazienteDao getPazienteDao();
	
	public abstract AmministratoreDao getAmministratoreDao();
	
	public abstract UniversitaDao getUniversitaDao();
	
	public abstract CodiceQRDao getCodiceQRDao();
	
	public abstract PrenotazioneDao getPrenotazioneDao();
	
	public abstract ImpiegatoDao getImpiegatoDao();
	
	public abstract SegnalazioneDao getSegnalazioneDao();
	
	public abstract LoggingDao getLoggingDao();
	
	public abstract EmailDao getEmailDao();
	
	public abstract UtilDao getUtilDao();
}
