package repository.impl;

import entity.Paziente;
import exception.PersistenceException;
import jdbc.DataSource;
import repository.UniversitaDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UniversitaDaoImpl implements UniversitaDao {

	private final DataSource dataSource;
	
	public UniversitaDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(Paziente paziente) {
		final Connection connection = dataSource.getConnection();
		try {
			final String insert = "INSERT INTO università(matricola, nome_paziente, cognome_paziente) VALUES (?,?,?)";
			final PreparedStatement statement = connection.prepareStatement(insert);
			statement.setLong(1, paziente.getMatricola());
			statement.setString(2, paziente.getNome());
			statement.setString(3, paziente.getCognome());
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
	public Paziente findByPrimaryKey(Long id) {
		Paziente paziente = null;
		final Connection connection = dataSource.getConnection();
		try {
			final String find = "SELECT * FROM università WHERE matricola = ?";
			final PreparedStatement statement = connection.prepareStatement(find);
			statement.setLong(1, id);
			final ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				paziente = new Paziente();	
				paziente.setCodiceFiscale(result.getString("matricola"));
				paziente.setNome(result.getString("nome_paziente"));				
				paziente.setCognome(result.getString("cognome_paziente"));
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
		List<Paziente> universitari = new ArrayList<>();
		final Connection connection = dataSource.getConnection();
		try {
			final String find = "SELECT * FROM univeristà";
			final PreparedStatement statement = connection.prepareStatement(find);
			final ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				paziente = new Paziente();
				paziente.setCodiceFiscale(result.getString("matricola"));
				paziente.setNome(result.getString("nome_paziente"));
				paziente.setCognome(result.getString("cognome_paziente"));
				universitari.add(paziente);
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
		return universitari;
	}
}
