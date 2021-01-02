package factory;

import exception.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

	private final String dbURI;
	private final String userName;
	private final String password;
	
	public DataSource(String dbURI, String userName, String password) {
		
		this.dbURI = dbURI;
		this.userName = userName;
		this.password = password;
	}
	 
	/*
	 * Connection: è una connessione (sessione) con uno specifico database.
	 * Le istruzioni SQL vengono eseguite e i risultati vengono restituiti 
	 * nel contesto di una connessione.
	 */
	public Connection getConnection() throws PersistenceException {
		
		Connection connection = null; 
		try {
			//Tenta di stabilire una connessione con l'URL del database fornito
		    connection = DriverManager.getConnection(dbURI, userName, password);
			
		} catch(SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		return connection;
	}
}
