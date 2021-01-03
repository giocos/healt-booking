package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exception.PersistenceException;
import factory.DataSource;
import entity.Impiegato;
import repository.ImpiegatoDao;

public class ImpiegatoDaoJdbc implements ImpiegatoDao {

	private final DataSource dataSource;
	
	public ImpiegatoDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(Impiegato impiegato) {
		final Connection connection = dataSource.getConnection();
		try {
			final String insert = "INSERT INTO impiegato(username, password, ruolo) VALUES (?,?,?)";
			final PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, impiegato.getUsername());
			statement.setString(2, impiegato.getPassword());
			statement.setString(3, impiegato.getRuolo());
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
	public Impiegato findByPrimaryKey(String username) {
		Impiegato impiegato = null;
		final Connection connection = dataSource.getConnection();
		try {
			final String query = "SELECT * FROM impiegato WHERE username = ?";
			final PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			final ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				impiegato = new Impiegato();
				impiegato.setUsername(result.getString("username"));				
				impiegato.setPassword(result.getString("password"));
				impiegato.setRuolo(result.getString("ruolo"));
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
		return impiegato;
	}

	@Override
	public void update(Impiegato impiegato) {
		final Connection connection = dataSource.getConnection();
		try {
			final String delete = "UPDATE impiegato SET password = ? WHERE username = ? ";
			final PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, impiegato.getPassword());
			statement.setString(2, impiegato.getUsername());
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
	public void delete(Impiegato impiegato) {
		final Connection connection = dataSource.getConnection();
		try {
			final String delete = "DELETE FROM impiegato WHERE username = ? ";
			final PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, impiegato.getUsername());
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
