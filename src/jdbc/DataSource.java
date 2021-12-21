package jdbc;

import exception.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

	private final String uri;
	private final String username;
	private final String password;
	
	public DataSource(String uri, String username, String password) {
		this.uri = uri;
		this.username = username;
		this.password = password;
	}
	 
	/**
	 * Restituisce una connessione (sessione) verso uno specifico database.
	 * Le istruzioni SQL vengono eseguite e i risultati vengono restituiti 
	 * nel contesto di una connessione.
	 *
	 * @return connection
	 */
	public Connection getConnection() throws PersistenceException {
		Connection connection = null; 
		try {
			// Try to create a connection with database with given URI
		    connection = DriverManager.getConnection(uri, username, password);

		} catch (final SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		return connection;
	}
}
