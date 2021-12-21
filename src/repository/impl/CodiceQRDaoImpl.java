package repository.impl;

import entity.CodiceQR;
import exception.PersistenceException;
import jdbc.DataSource;
import repository.CodiceQRDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CodiceQRDaoImpl implements CodiceQRDao {

	private final DataSource dataSource;
	
	public CodiceQRDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(CodiceQR codice) {
		final Connection connection = dataSource.getConnection();
		try {
			final String insert = "INSERT INTO codice_qr(id, orario_scadenza, convalida) VALUES (?,?,?)";
			final PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, codice.getCodice());
			statement.setString(2, codice.getScadenza());
			statement.setBoolean(3, codice.isConvalida());
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
	public CodiceQR findByPrimaryKey(String codice) {
		CodiceQR codiceQr = null;
		final Connection connection = dataSource.getConnection();
		try {
			final String find = "SELECT * FROM codice_qr WHERE id = ?";
			final PreparedStatement statement = connection.prepareStatement(find);
			statement.setString(1, codice);
			final ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				codiceQr = new CodiceQR();
				codiceQr.setCodice(result.getString("id"));
				codiceQr.setScadenza(result.getString("orario_Scadenza"));				
				codiceQr.setConvalida(result.getBoolean("convalida"));
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
		return codiceQr;
	}

	@Override
	public List<CodiceQR> findAll() {
		CodiceQR codiceQR = null;
		List<CodiceQR> codici = new ArrayList<>();
		final Connection connection = dataSource.getConnection();
		try {
			final String find = "SELECT * FROM codice_qr";
			final PreparedStatement statement = connection.prepareStatement(find);
			final ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				codiceQR = new CodiceQR();
				codiceQR.setCodice(result.getString("id"));
				codiceQR.setScadenza(result.getString("orario_Scadenza"));				
				codiceQR.setConvalida(result.getBoolean("convalida"));
				codici.add(codiceQR);
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
		return codici;
	}

	@Override
	public void update(CodiceQR codice) {
		final Connection connection = dataSource.getConnection();
		try {
			final String update = "UPDATE codice_qr SET convalida = ? WHERE id = ?";
			final PreparedStatement statement = connection.prepareStatement(update);
			statement.setBoolean(1, codice.isConvalida());
			statement.setString(2, codice.getCodice());
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
	public void delete(CodiceQR codice) {
		final Connection connection = dataSource.getConnection();
		try {
			final String delete = "DELETE FROM codice_qr WHERE id = ? ";
			final PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, codice.getCodice());
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
}
