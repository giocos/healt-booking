package utils;

import exception.PersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//assegna in modo progressivo un id per ogni tabella presente nel nostro DB
public class IdBroker {

	private static final String query = "SELECT nextval('sequenza_id') AS id";

	public static Long getId(Connection connection) throws PersistenceException {
		
		Long id = null;
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			result.next();
			id = result.getLong("id");
			
		} catch(SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		return id;
	}
}
