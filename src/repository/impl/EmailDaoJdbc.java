package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.PersistenceException;
import factory.DataSource;
import entity.Email;
import repository.EmailDao;

public class EmailDaoJdbc implements EmailDao {

	private final DataSource dataSource;
	
	public EmailDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(Email email) {
		final Connection connection = dataSource.getConnection();
		try {
			final String insert = "INSERT INTO email(admin, messaggio, emittente, destinatario) VALUES (?,?,?,?)";
			final PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, email.getAdmin());
			statement.setString(2, email.getMessaggio());
			statement.setString(3, email.getEmittente());
			statement.setString(4, email.getDestinatario());
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
	public List<Email> findAll() {
		Email email = null;
		List<Email> emails = new ArrayList<>();
		final Connection connection = dataSource.getConnection();
		try {
			final String find = "SELECT * FROM email";
			final PreparedStatement statement = connection.prepareStatement(find);
			final ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				email = new Email();
				email.setAdmin(result.getString(1));
				email.setMessaggio(result.getString(2));
				email.setEmittente(result.getString(3));
				email.setDestinatario(result.getString(4));
				emails.add(email);
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
		return emails;
	}
}
