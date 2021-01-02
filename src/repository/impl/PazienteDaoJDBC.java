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
import entity.Paziente;
import repository.PazienteDao;

public class PazienteDaoJDBC implements PazienteDao {

	private DataSource dataSource;

	public PazienteDaoJDBC(DataSource dataSource) {
			this.dataSource = dataSource;
	}

	@Override
	public void save(Paziente paziente) {
			
		Connection connection = dataSource.getConnection();
		try {
			String insert = "INSERT INTO paziente(codice_fiscale, nome, cognome, matricola, invalidità, id_codice) VALUES (?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, paziente.getCodiceFiscale());
			statement.setString(2, paziente.getNome());
			statement.setString(3, paziente.getCognome());
			if(paziente.getMatricola() != null)
				statement.setLong(4, paziente.getMatricola());
			else
				statement.setNull(4, Types.BIGINT);
			statement.setString(5, paziente.getInvalidita());
			statement.setString(6, paziente.getCodiceQR());
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
	public Paziente findByPrimaryKey(String codiceFiscale) {
		
		Connection connection = dataSource.getConnection();
		Paziente paziente = null;
		try {
			PreparedStatement statement;
			String query = "SELECT * FROM paziente WHERE codice_fiscale = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, codiceFiscale);
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				
				paziente = new Paziente();
				paziente.setCodiceFiscale(result.getString("codice_fiscale"));
				paziente.setNome(result.getString("nome"));				
				paziente.setCognome(result.getString("cognome"));
				paziente.setMatricola(result.getLong("matricola"));
				paziente.setInvalidita(result.getString("invalidit�"));
				paziente.setCodiceQR(result.getString("id_codice"));
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
		return paziente;
	}
	
	@Override
	public Paziente findByForeignKey(String codiceQR) {
		
		Connection connection = dataSource.getConnection();
		Paziente paziente = null;
		try {
			PreparedStatement statement;
			String query = "SELECT * FROM paziente WHERE id_codice = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, codiceQR);
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				
				paziente = new Paziente();
				paziente.setCodiceFiscale(result.getString("codice_fiscale"));
				paziente.setNome(result.getString("nome"));				
				paziente.setCognome(result.getString("cognome"));
				paziente.setMatricola(result.getLong("matricola"));
				paziente.setInvalidita(result.getString("invalidit�"));
				paziente.setCodiceQR(result.getString("id_codice"));
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
		return paziente;
	}

	@Override
	public List<Paziente> findAll() {
		
		Connection connection = dataSource.getConnection();
		List<Paziente> pazienti = new ArrayList<>();
		Paziente paziente = null;
		try {
			String query = "SELECT * FROM paziente";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				
				paziente = new Paziente();
				paziente.setCodiceFiscale(result.getString("codice_fiscale"));
				paziente.setNome(result.getString("nome"));				
				paziente.setCognome(result.getString("cognome"));
				paziente.setMatricola(result.getLong("matricola"));
				paziente.setInvalidita(result.getString("invalidit�"));
				paziente.setCodiceQR(result.getString("id_codice"));
				pazienti.add(paziente);
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
		return pazienti;
	}

	@Override
	public void update(Paziente paziente) {
		
		Connection connection = dataSource.getConnection();
		try {
			String update = "UPDATE paziente SET  nome = ?, cognome = ?, matricola = ?, invalidità = ?, id_codice = ? WHERE codice_fiscale = ?";
			PreparedStatement statement = connection.prepareStatement(update);
			statement.setString(1, paziente.getNome());
			statement.setString(2, paziente.getCognome());
			statement.setLong(3, paziente.getMatricola());
			statement.setString(4, paziente.getInvalidita());
			statement.setString(5, paziente.getCodiceQR());
			statement.setString(6,  paziente.getCodiceFiscale());
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
	public void delete(Paziente paziente) {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "DELETE FROM paziente WHERE codice_fiscale = ? ";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, paziente.getCodiceFiscale());
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
	public boolean exists(Long matricola) {
		
		if(matricola == null)
			return false;
		
		Connection connection = dataSource.getConnection();
		try {
			PreparedStatement statement;
			String query = "SELECT * FROM paziente WHERE matricola = ?";
			statement = connection.prepareStatement(query);
			statement.setLong(1, matricola);
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				return true;
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
		return false;
	}
}
