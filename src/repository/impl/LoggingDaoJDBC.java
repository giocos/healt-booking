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

public class LoggingDaoJDBC implements LoggingDao {

	private DataSource dataSource;
	
	public LoggingDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public int assignId() {
		
		Connection connection = dataSource.getConnection();
		try {
			String query = "SELECT COUNT(*) AS count FROM logging";
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
	public void save(Logging accesso) {
		
		Connection connection = dataSource.getConnection();
		try {
			String insert = "INSERT INTO logging(id, azione, data, orario, nome_utente) VALUES (?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setInt(1, accesso.getId());
			statement.setString(2, accesso.getAzione());
			long millis = accesso.getData().getTime();
			statement.setDate(3, new Date(millis));
			statement.setString(4, accesso.getOrario());
			statement.setString(5, accesso.getNomeUtente());
			statement.executeUpdate();
			
		} catch (SQLException e) {
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
	public List<Logging> findAll() {
		
		Connection connection = dataSource.getConnection();
		List<Logging> log = new ArrayList<>();
		Logging logging = null;
		try {
			PreparedStatement statement;
			String query = "SELECT * FROM logging";
			statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				
				logging = new Logging();
				logging.setId(result.getInt("id"));
				logging.setAzione(result.getString("azione"));
				logging.setData(result.getDate("data"));				
				logging.setOrario(result.getString("orario"));
				logging.setNomeUtente(result.getString("nome_utente"));
				log.add(logging);
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
		return log;
	}

	@Override
	public void deleteAll() {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "DELETE FROM logging";
			PreparedStatement statement = connection.prepareStatement(delete);
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
