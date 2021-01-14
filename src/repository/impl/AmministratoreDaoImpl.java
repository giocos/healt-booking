package repository.impl;

import entity.Amministratore;
import exception.PersistenceException;
import jdbc.DataSource;
import repository.AmministratoreDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AmministratoreDaoImpl implements AmministratoreDao {

	private final DataSource dataSource;
	
	public AmministratoreDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(Amministratore amministratore) {
		final Connection connection = dataSource.getConnection();
		try {
			final String insert = "INSERT INTO amministratore(username, password) VALUES (?,?)";
			final PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, amministratore.getUsername());
			statement.setString(2, amministratore.getPassword());
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
	public Amministratore findByPrimaryKey(String username) {
		Amministratore amministratore = null;
		final Connection connection = dataSource.getConnection();
		try {
			final String find = "SELECT * FROM amministratore WHERE username = ?";
			final PreparedStatement statement = connection.prepareStatement(find);
			statement.setString(1, username);
			final ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				amministratore = new Amministratore();
				amministratore.setUsername(result.getString("username"));				
				amministratore.setPassword(result.getString("password"));
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
		return amministratore;
	}
	
	@Override
	public void update(Amministratore amministratore) {
		final Connection connection = dataSource.getConnection();
		try {
			final String update = "UPDATE amministratore SET password = ? WHERE username = ?";
			final PreparedStatement statement = connection.prepareStatement(update);
			statement.setString(1, amministratore.getPassword());
			statement.setString(2, amministratore.getUsername());
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
	public void delete(Amministratore amministratore) {

		final Connection connection = dataSource.getConnection();
		try {
			final String delete = "DELETE FROM amministratore WHERE username = ? ";
			final PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, amministratore.getUsername());
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
