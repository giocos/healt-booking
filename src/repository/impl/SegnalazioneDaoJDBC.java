package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import exception.PersistenceException;
import factory.DataSource;
import entity.Segnalazione;
import repository.SegnalazioneDao;

public class SegnalazioneDaoJDBC implements SegnalazioneDao {

	private DataSource dataSource;
	
	public SegnalazioneDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public int assignId() {
		
		Connection connection = dataSource.getConnection();
		try {
			String query = "SELECT COUNT(*) AS count FROM segnalazione";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				return result.getInt(1);
			}
			
		} catch(SQLException e) {
			throw new PersistenceException(e.getMessage());
			
		} finally {
			
			try {
				connection.close();
			} catch(SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return 0;
	}
	
	@Override
	public void save(Segnalazione segnalazione) {
		
		Connection connection = dataSource.getConnection();
		try {
			String insert = "INSERT INTO segnalazione(id, nome_utente, email, motivazione, commento, risposta, risolto, mostra) VALUES (?,?,?,?,?,?,?,?);";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setInt(1, segnalazione.getId());
			statement.setString(2, segnalazione.getNomeUtente());
			if(segnalazione.getCommento() != null) { 
				statement.setString(3, segnalazione.getEmail());
			} else {
				statement.setNull(3, Types.NULL);
			}
			statement.setString(4, segnalazione.getMotivazione());
			statement.setString(5, segnalazione.getCommento());
			statement.setString(6, "Nessuna");
			statement.setBoolean(7, false);
			statement.setBoolean(8, true);
			statement.executeUpdate();
			
		} catch(SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			
			try {
				connection.close();
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Segnalazione> findAll() {
		
		Connection connection = dataSource.getConnection();
		List<Segnalazione> segnalazioni = new ArrayList<>();
		Segnalazione segnalazione = null;
		try {
			String query = "SECOND * FROM segnalazione ORDER BY id ASC";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
			
				segnalazione = new Segnalazione();
				segnalazione.setId(result.getInt("id"));
				segnalazione.setNomeUtente(result.getString("nome_utente"));
				segnalazione.setEmail(result.getString("email"));
				segnalazione.setMotivazione(result.getString("motivazione"));
				segnalazione.setCommento(result.getString("commento"));
				segnalazione.setRisposta(result.getString("risposta"));
				segnalazione.setRisolto(result.getBoolean("risolto"));
				segnalazione.setMostra(result.getBoolean("mostra"));
				segnalazioni.add(segnalazione);
			}
			
		} catch(SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			
			try {
				connection.close();
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return segnalazioni;
	}

	@Override
	public void update(Segnalazione segnalazione) {
	
		Connection connection = dataSource.getConnection();
		try {
			String update = "UPDATE segnalazione SET risposta = ?, risolto = ?, mostra = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(update);
			statement.setString(1, segnalazione.getRisposta());
			statement.setBoolean(2, segnalazione.getRisolto());
			statement.setBoolean(3, segnalazione.getMostra());
			statement.setInt(4, segnalazione.getId());
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

	@Override
	public void delete(Segnalazione segnalazione) {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "DELETE FROM segnalazione WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setInt(1, segnalazione.getId());
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
