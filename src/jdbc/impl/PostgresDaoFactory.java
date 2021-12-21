package jdbc.impl;

import jdbc.DaoFactory;
import jdbc.DataSource;
import repository.*;
import repository.impl.*;

public class PostgresDaoFactory extends DaoFactory {

	private static final String URI = "jdbc:postgresql://localhost:5432/Prenotazioni";
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
	public AmministratoreDao getAmministratoreDao() {
		return new AmministratoreDaoImpl(dataSource);
	}

	@Override
	public PrenotazioneDao getPrenotazioneDao() {
		return new PrenotazioneDaoImpl(dataSource);
	}

	@Override
	public SegnalazioneDao getSegnalazioneDao() {
		return new SegnalazioneDaoImpl(dataSource);
	}

	@Override
	public ImpiegatoDao getImpiegatoDao() {
		return new ImpiegatoDaoImpl(dataSource);
	}

	@Override
	public PazienteDao getPazienteDao() { return new PazienteDaoImpl(dataSource); }

	@Override
	public CodiceQRDao getCodiceQRDao() {
		return new CodiceQRDaoImpl(dataSource);
	}

	@Override
	public LoggingDao getLoggingDao() {
		return new LoggingDaoImpl(dataSource);
	}

	@Override
	public AteneoDao getAteneoDao() {
		return new AteneoDaoImpl(dataSource);
	}

	@Override
	public EmailDao getEmailDao() {
		return new EmailDaoImpl(dataSource);
	}

	@Override
	public UtilDao getUtilDao() {
		return new UtilDaoImpl(dataSource);
	}
}
