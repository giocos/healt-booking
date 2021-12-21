package repository.impl;

import entity.Email;
import exception.PersistenceException;
import jdbc.DataSource;
import repository.EmailDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmailDaoImpl implements EmailDao {

	private DataSource dataSource;
	
	public EmailDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(Email email) {
		Connection connection = dataSource.getConnection();
		try {
			String insert = "INSERT INTO email(admin, messaggio, emittente, destinatario) VALUES (?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(insert);
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
		Connection connection = dataSource.getConnection();
		try {
			String find = "SELECT * FROM email";
			PreparedStatement statement = connection.prepareStatement(find);
			ResultSet result = statement.executeQuery();
			
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
