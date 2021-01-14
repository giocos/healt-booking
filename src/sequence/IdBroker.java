package sequence;

import exception.PersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class IdBroker {

	private static final String SELECT = "SELECT nextval('sequence_id') AS id";
	
	public static synchronized Long getId(Connection connection) throws PersistenceException {
		Long id = null;
		try {
			PreparedStatement statement = connection.prepareStatement(SELECT);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				id = result.getLong("id");
			}

		} catch (final SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		return id;
	}
}
