package repository.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.PersistenceException;
import factory.DataSource;
import entity.Logging;
import repository.LoggingDao;

public class LoggingDaoJdbc implements LoggingDao {

	private final DataSource dataSource;
	
	public LoggingDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	synchronized public int getId() {
		final Connection connection = dataSource.getConnection();
		try {
			final String count = "SELECT COUNT(*) AS count FROM logging";
			final PreparedStatement statement = connection.prepareStatement(count);
			final ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				return result.getInt(1);
			}

		} catch (final SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (final SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return 0;
	}
	
	@Override
	public void save(Logging logging) {
		final Connection connection = dataSource.getConnection();
		try {
			final String insert = "INSERT INTO logging(id, azione, data, orario, nome_utente) VALUES (?,?,?,?,?)";
			final PreparedStatement statement = connection.prepareStatement(insert);
			statement.setInt(1, logging.getId());
			statement.setString(2, logging.getAzione());
			long millis = logging.getData().getTime();
			statement.setDate(3, new Date(millis));
			statement.setString(4, logging.getOrario());
			statement.setString(5, logging.getNomeUtente());
			statement.executeUpdate();

		} catch (final SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (final SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	@Override
	public List<Logging> findAll() {
		Logging logging = null;
		List<Logging> logs = new ArrayList<>();
		final Connection connection = dataSource.getConnection();
		try {
			final String find = "SELECT * FROM logging";
			final PreparedStatement statement = connection.prepareStatement(find);
			final ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				logging = new Logging();
				logging.setId(result.getInt("id"));
				logging.setAzione(result.getString("azione"));
				logging.setData(result.getDate("data"));				
				logging.setOrario(result.getString("orario"));
				logging.setNomeUtente(result.getString("nome_utente"));
				logs.add(logging);
			}

		} catch (final SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (final SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return logs;
	}

	@Override
	public void deleteAll() {
		final Connection connection = dataSource.getConnection();
		try {
			final String delete = "DELETE FROM logging";
			final PreparedStatement statement = connection.prepareStatement(delete);
			statement.executeUpdate();

		} catch (final SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (final SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

}
