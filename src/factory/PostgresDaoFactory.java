package factory;

import repository.*;
import repository.impl.*;
import utils.UtilDao;

public class PostgresDaoFactory extends DaoFactory {

	private static DataSource dataSource = null;
	
	static {
		try {
			Class.forName("org.postgresql.Driver").newInstance();
//			dataSource = new DataSource("jdbc:postgresql://prenotazione.c8me4gj6vybs.eu-west-1.rds.amazonaws.com:5432/Prenotazione","postgres","postgres");
			dataSource = new DataSource("jdbc:postgresql://localhost:5432/Prenotazione","postgres","postgres");
			
		} catch(Exception e) {
			System.err.println("PostgresDAOFactory.class: failed to load MySQL JDBC driver\n" + e);
		}
	}

	@Override
	public PazienteDao getPazienteDao() {
		return new PazienteDaoJDBC(dataSource);
	}
	
	@Override
	public AmministratoreDao getAmministratoreDao() {
		return new AmministratoreDaoJDBC(dataSource);
	}

	@Override
	public UniversitaDao getUniversitaDao() {
		return new UniversitaDaoJDBC(dataSource);
	}

	@Override
	public CodiceQRDao getCodiceQRDao() {
		return new CodiceQRDaoJDBC(dataSource);
	}

	@Override
	public PrenotazioneDao getPrenotazioneDao() {
		return new PrenotazioneDaoJDBC(dataSource);
	}
	
	@Override
	public ImpiegatoDao getImpiegatoDao() {
		return new ImpiegatoDaoJDBC(dataSource);
	}
	
	@Override
	public SegnalazioneDao getSegnalazioneDao() {
		return new SegnalazioneDaoJDBC(dataSource);
	}
	
	@Override
	public LoggingDao getLoggingDao() {
		return new LoggingDaoJDBC(dataSource);
	}
	
	@Override
	public EmailDao getEmailDao() {
		return new EmailDaoJDBC(dataSource);
	}
	
	@Override
	public UtilDao getUtilDao() {
		return new UtilDao(dataSource);
	}
}
