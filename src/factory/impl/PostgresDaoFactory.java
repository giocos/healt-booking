package factory.impl;

import factory.DaoFactory;
import factory.DataSource;
import repository.*;
import repository.impl.*;

public class PostgresDaoFactory extends DaoFactory {

	private static final String URI = "jdbc:postgresql://localhost:5432/Prenotazione";
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "postgres";

	private static DataSource dataSource = null;
	
	static {
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			dataSource = new DataSource(URI, USERNAME, PASSWORD);
			//dataSource = new DataSource("jdbc:postgresql://prenotazione.c8me4gj6vybs.eu-west-1.rds.amazonaws.com:5432/Prenotazione","postgres","postgres");
			
		} catch (final Exception e) {
			System.err.println("PostgresDAOFactory.class: failed to load MySQL JDBC driver\n" + e);
		}
	}

	@Override
	public PazienteDao getPazienteDao() { return new PazienteDaoJdbc(dataSource); }
	
	@Override
	public AmministratoreDao getAmministratoreDao() {
		return new AmministratoreDaoJdbc(dataSource);
	}

	@Override
	public UniversitaDao getUniversitaDao() {
		return new UniversitaDaoJdbc(dataSource);
	}

	@Override
	public CodiceQRDao getCodiceQRDao() {
		return new CodiceQRDaoJdbc(dataSource);
	}

	@Override
	public PrenotazioneDao getPrenotazioneDao() {
		return new PrenotazioneDaoJdbc(dataSource);
	}
	
	@Override
	public ImpiegatoDao getImpiegatoDao() {
		return new ImpiegatoDaoJdbc(dataSource);
	}
	
	@Override
	public SegnalazioneDao getSegnalazioneDao() {
		return new SegnalazioneDaoJdbc(dataSource);
	}
	
	@Override
	public LoggingDao getLoggingDao() {
		return new LoggingDaoJdbc(dataSource);
	}
	
	@Override
	public EmailDao getEmailDao() {
		return new EmailDaoJdbc(dataSource);
	}
	
	@Override
	public UtilDao getUtilDao() {
		return new UtilDaoJdbc(dataSource);
	}
}
