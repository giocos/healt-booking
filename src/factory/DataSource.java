package factory;

import exception.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

	private final String URI;
	private final String username;
	private final String password;
	
	public DataSource(String URI, String username, String password) {
		this.URI = URI;
		this.username = username;
		this.password = password;
	}
	 
	/**
	 * Connection: è una connessione (sessione) con uno specifico database.
	 * Le istruzioni SQL vengono eseguite e i risultati vengono restituiti 
	 * nel contesto di una connessione.
	 */
	public Connection getConnection() throws PersistenceException {
		Connection connection = null; 
		try {
			//Tenta di stabilire una connessione con l'URL del database fornito
		    connection = DriverManager.getConnection(URI, username, password);

		} catch (final SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		return connection;
	}
}
