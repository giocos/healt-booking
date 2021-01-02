package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exception.PersistenceException;
import factory.DataSource;
import entity.Impiegato;
import repository.ImpiegatoDao;

public class ImpiegatoDaoJDBC implements ImpiegatoDao {

	private DataSource dataSource;
	
	public ImpiegatoDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(Impiegato impiegato) {
		
		Connection connection = dataSource.getConnection();
		try {
			String insert = "INSERT INTO impiegato(username, password, ruolo) VALUES (?,?,?)";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, impiegato.getUsername());
			statement.setString(2, impiegato.getPassword());
			statement.setString(3, impiegato.getRuolo());
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
	public Impiegato findByPrimaryKey(String username) {
		
		Connection connection = dataSource.getConnection();
		Impiegato impiegato = null;
		try {
			PreparedStatement statement;
			String query = "SELECT * FROM impiegato WHERE username = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				
				impiegato = new Impiegato();
				impiegato.setUsername(result.getString("username"));				
				impiegato.setPassword(result.getString("password"));
				impiegato.setRuolo(result.getString("ruolo"));
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
		return impiegato;
	}

	@Override
	public void update(Impiegato impiegato) {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "UPDATE impiegato SET password = ? WHERE username = ? ";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, impiegato.getPassword());
			statement.setString(2, impiegato.getUsername());
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
	public void delete(Impiegato impiegato) {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "DELETE FROM impiegato WHERE username = ? ";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, impiegato.getUsername());
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
