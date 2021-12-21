package repository.impl;

import entity.Paziente;
import exception.PersistenceException;
import jdbc.DataSource;
import repository.PazienteDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PazienteDaoImpl implements PazienteDao {

	private final DataSource dataSource;

	public PazienteDaoImpl(DataSource dataSource) {
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
			if (paziente.getMatricola() != null) {
				statement.setLong(4, paziente.getMatricola());
			} else {
				statement.setNull(4, Types.BIGINT);
			}
			statement.setString(5, paziente.getInvalidita());
			statement.setString(6, paziente.getCodiceQR());
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
	public Paziente findByPrimaryKey(String codiceFiscale) {
		Paziente paziente = null;
		final Connection connection = dataSource.getConnection();
		try {
			final String query = "SELECT * FROM paziente WHERE codice_fiscale = ?";
			final PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, codiceFiscale);
			final ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				paziente = new Paziente();
				paziente.setCodiceFiscale(result.getString("codice_fiscale"));
				paziente.setNome(result.getString("nome"));				
				paziente.setCognome(result.getString("cognome"));
				paziente.setMatricola(result.getLong("matricola"));
				paziente.setInvalidita(result.getString("invalidit�"));
				paziente.setCodiceQR(result.getString("id_codice"));
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
		return paziente;
	}
	
	@Override
	public Paziente findByForeignKey(String codiceQR) {
		Paziente paziente = null;
		final Connection connection = dataSource.getConnection();
		try {
			final String find = "SELECT * FROM paziente WHERE id_codice = ?";
			final PreparedStatement statement = connection.prepareStatement(find);
			statement.setString(1, codiceQR);
			final ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				paziente = new Paziente();
				paziente.setCodiceFiscale(result.getString("codice_fiscale"));
				paziente.setNome(result.getString("nome"));				
				paziente.setCognome(result.getString("cognome"));
				paziente.setMatricola(result.getLong("matricola"));
				paziente.setInvalidita(result.getString("invalidit�"));
				paziente.setCodiceQR(result.getString("id_codice"));
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
		return paziente;
	}

	@Override
	public List<Paziente> findAll() {
		Paziente paziente = null;
		List<Paziente> pazienti = new ArrayList<>();
		final Connection connection = dataSource.getConnection();
		try {
			final String find = "SELECT * FROM paziente";
			final PreparedStatement statement = connection.prepareStatement(find);
			final ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				paziente = new Paziente();
				paziente.setCodiceFiscale(result.getString("codice_fiscale"));
				paziente.setNome(result.getString("nome"));				
				paziente.setCognome(result.getString("cognome"));
				paziente.setMatricola(result.getLong("matricola"));
				paziente.setInvalidita(result.getString("invalidit�"));
				paziente.setCodiceQR(result.getString("id_codice"));
				pazienti.add(paziente);
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
		return pazienti;
	}

	@Override
	public void update(Paziente paziente) {
		final Connection connection = dataSource.getConnection();
		try {
			final String update = "UPDATE paziente SET  nome = ?, cognome = ?, matricola = ?, invalidità = ?, id_codice = ? WHERE codice_fiscale = ?";
			final PreparedStatement statement = connection.prepareStatement(update);
			statement.setString(1, paziente.getNome());
			statement.setString(2, paziente.getCognome());
			statement.setLong(3, paziente.getMatricola());
			statement.setString(4, paziente.getInvalidita());
			statement.setString(5, paziente.getCodiceQR());
			statement.setString(6,  paziente.getCodiceFiscale());
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
	public void delete(Paziente paziente) {
		final Connection connection = dataSource.getConnection();
		try {
			final String delete = "DELETE FROM paziente WHERE codice_fiscale = ? ";
			final PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, paziente.getCodiceFiscale());
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
	public boolean exists(Long matricola) {
		if (matricola == null || matricola < 0) {
			return false;
		}

		final Connection connection = dataSource.getConnection();
		try {
			final String exists = "SELECT * FROM paziente WHERE matricola = ?";
			final PreparedStatement statement = connection.prepareStatement(exists);
			statement.setLong(1, matricola);
			final ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				return true;
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
		return false;
	}
}
