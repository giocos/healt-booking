package repository.impl;

import exception.PersistenceException;
import factory.DataSource;
import repository.UtilDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UtilDaoJdbc implements UtilDao {

	private final DataSource dataSource;

	public UtilDaoJdbc(DataSource dataSource) {
			this.dataSource = dataSource;
	}

	public void dropDatabase() throws PersistenceException {
		final Connection connection = dataSource.getConnection();
		try {
			final String drop = "DROP SEQUENCE IF EXISTS id;"
					+ "DROP TABLE IF EXISTS logging;"
					+ "DROP TABLE IF EXISTS paziente CASCADE;"
					+ "DROP TABLE IF EXISTS università;"
					+ "DROP TABLE IF EXISTS amministratore CASCADE;"
					+ "DROP TABLE IF EXISTS impiegato;"
					+ "DROP TABLE IF EXISTS codice_qr CASCADE;"
					+ "DROP TABLE IF EXISTS prenotazione;"
					+ "DROP TABLE IF EXISTS segnalazione;"
					+ "DROP TABLE IF EXISTS email;";
				
			final PreparedStatement statement = connection.prepareStatement(drop);
			statement.executeUpdate();
			System.out.println("Executed DROP database");

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

	public void createDatabase() throws PersistenceException {
		final Connection connection = dataSource.getConnection();
		try {
			final String create = "CREATE SEQUENCE id;"
					+ "CREATE TABLE codice_qr(id VARCHAR(12) PRIMARY KEY, orario_scadenza VARCHAR(5), convalida BOOLEAN);"
					+ "CREATE TABLE università(matricola BIGINT PRIMARY KEY, nome_paziente VARCHAR(16), cognome_paziente VARCHAR(16));"
					+ "CREATE TABLE paziente(\"codice_fiscale\" VARCHAR(16) PRIMARY KEY, nome VARCHAR(16),"
					+ "cognome VARCHAR(16), matricola BIGINT, invalidità VARCHAR(20), "
					+ "id_codice VARCHAR(12) REFERENCES codice_qr(\"id\"));"
					+ "CREATE TABLE amministratore(username VARCHAR(16) PRIMARY KEY, password VARCHAR(16));"
					+ "CREATE TABLE prenotazione(id_prenotazione VARCHAR(12) PRIMARY KEY REFERENCES codice_qr(\"id\"),"
					+ "nome_paziente VARCHAR(16), cognome_paziente VARCHAR(16), orario_visita VARCHAR(5), importo BIGINT);"
					+ "CREATE TABLE impiegato(username VARCHAR(16) PRIMARY KEY, password VARCHAR(16), ruolo VARCHAR(16));"
					+ "CREATE TABLE segnalazione(id INT PRIMARY KEY, nome_utente VARCHAR(20), email VARCHAR(50),"
					+ "motivazione VARCHAR(32), commento VARCHAR(255), risposta VARCHAR(255), risolto BOOLEAN, mostra BOOLEAN);"
					+ "CREATE TABLE logging(id INT PRIMARY KEY, azione VARCHAR(16), data DATE, orario VARCHAR(8), nome_utente VARCHAR(16));"
					+ "CREATE TABLE email(admin VARCHAR(16) REFERENCES amministratore(\"username\"), messaggio VARCHAR(255), emittente VARCHAR(50), destinatario VARCHAR(50));";

			final PreparedStatement statement = connection.prepareStatement(create);
			statement.executeUpdate();
			System.out.println("Executed create database");

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

	public void resetPrenotazioni() throws PersistenceException {
		final Connection connection = dataSource.getConnection();
		try {
			String delete = "DELETE FROM paziente";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.executeUpdate();

			delete = "DELETE FROM prenotazione";
			statement = connection.prepareStatement(delete);
			statement.executeUpdate();

			delete = "DELETE FROM codice_qr";
			statement = connection.prepareStatement(delete);
			statement.executeUpdate();

			System.out.println("Executed reset database");

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
