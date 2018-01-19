package persistence;

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
			String delete = "drop SEQUENCE if EXISTS id;"
					+ "drop table if EXISTS paziente;"
					+ "drop table if EXISTS amministratore;"
					+ "drop table if EXISTS visitaMedica;"
					+ "drop table if EXISTS codiceQr;"
					+ "drop table if EXISTS universita;";
				
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
			String delete = "create SEQUENCE id;"
					+ "create table codiceQr(\"id\" VARCHAR(255) primary key, data_scadenza DATE, valido boolean);"
					+ "create table paziente(\"matricola\" bigint primary key, nome VARCHAR(255),"
					+ "cognome VARCHAR(255), invalidit� VARCHAR(255), id_codiceQr VARCHAR(255));"
					+ "create table amministratore(\"username\" VARCHAR(255) primary key, password VARCHAR(255));";
//					+ "create table universita(matricola_p bigint REFERENCES paziente(\"matricola\"), "
//					+ "nome_p VARCHAR(255) REFERENCES paziente(nome), cognome_p VARCHAR(255) REFERENCES paziente(cognome));"
//					+ "create table visitaMedica(id_qr bigint REFERENCES codiceQr(\"id\"), "
//					+ "nome_p VARCHAR(255) REFERENCES paziente(nome), cognome_p VARCHAR(255) REFERENCES paziente(cognome));";
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
				
				delete = "delete FROM universita";
				statement = connection.prepareStatement(delete);
				statement.executeUpdate();
				
				delete = "delete FROM visitaMedica";
				statement = connection.prepareStatement(delete);
				statement.executeUpdate();
				
				delete = "delete FROM codiceQr";
				statement = connection.prepareStatement(delete);
				statement.executeUpdate();
				
				delete = "delete FROM amministratore";
				statement = connection.prepareStatement(delete);
				statement.executeUpdate();
				
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
