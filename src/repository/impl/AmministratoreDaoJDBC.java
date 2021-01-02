package repository.impl;

import java.sql.*;

import exception.PersistenceException;
import factory.DataSource;
import entity.Amministratore;
import repository.AmministratoreDao;

public class AmministratoreDaoJDBC implements AmministratoreDao {

	private DataSource dataSource;
	
	public AmministratoreDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(Amministratore amministratore) {
		
		Connection connection = dataSource.getConnection();
		try {
			String insert = "INSERT INTO amministratore(username, password) VALUES (?,?)";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, amministratore.getUsername());
			statement.setString(2, amministratore.getPassword());
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
	public Amministratore findByPrimaryKey(String username) {
		
		Connection connection = dataSource.getConnection();
		Amministratore amministratore = null;
		try {
			PreparedStatement statement;
			String query = "SELECT * FROM amministratore WHERE username = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				
				amministratore = new Amministratore();
				amministratore.setUsername(result.getString("username"));				
				amministratore.setPassword(result.getString("password"));
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
		return amministratore;
	}
	
	@Override
	public void update(Amministratore amministratore) {
		
		Connection connection = dataSource.getConnection();
		try {
			String update = "UPDATE amministratore SET password = ? WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(update);
			statement.setString(1, amministratore.getPassword());
			statement.setString(2, amministratore.getUsername());
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
	public void delete(Amministratore amministratore) {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "DELETE FROM amministratore WHERE username = ? ";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, amministratore.getUsername());
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
