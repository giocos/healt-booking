package utils;

import exception.PersistenceException;
import factory.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UtilDao {

	private DataSource dataSource;

	public UtilDao(DataSource dataSource) {
			this.dataSource=dataSource;
	}

	public void dropDatabase() throws PersistenceException {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "drop SEQUENCE if exists id;"
					+ "drop table if exists logging;"
					+ "drop table if exists paziente cascade;"
					+ "drop table if exists università;"
					+ "drop table if exists amministratore cascade;"
					+ "drop table if exists impiegato;"
					+ "drop table if exists codice_qr cascade;"
					+ "drop table if exists prenotazione;"
					+ "drop table if exists segnalazione;"
					+ "drop table if exists email;";
				
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.executeUpdate();
			System.out.println("Executed drop database");
			
		} catch(SQLException e) {	
			throw new PersistenceException(e.getMessage());
			
		} finally {
			
			try {
				connection.close();
			} catch(SQLException e) {	
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public void createDatabase() throws PersistenceException {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "CREATE sequence id;"
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
			
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.executeUpdate();		
			System.out.println("Executed create database");
			
		} catch(SQLException e) {	
			throw new PersistenceException(e.getMessage());
			
		} finally {
			
			try {
				connection.close();
			} catch(SQLException e) {	
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public void resetDatabase() throws PersistenceException {
			
			Connection connection = dataSource.getConnection();
			try {
				String delete = "delete FROM paziente";
				PreparedStatement statement = connection.prepareStatement(delete);
				statement.executeUpdate();
				
				delete = "delete FROM prenotazione";
				statement = connection.prepareStatement(delete);
				statement.executeUpdate();
				
				delete = "delete FROM codice_qr";
				statement = connection.prepareStatement(delete);
				statement.executeUpdate();
				
				System.out.println("Executed reset database");
				
			} catch(SQLException e) {	
				throw new PersistenceException(e.getMessage());
				
			} finally {
				
				try {
					connection.close();
				} catch(SQLException e) {	
					throw new PersistenceException(e.getMessage());
				}
			}
		}
	}
