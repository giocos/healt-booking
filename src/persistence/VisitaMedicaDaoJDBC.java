package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import model.Paziente;
import persistence.dao.VisitaMedicaDao;

public class VisitaMedicaDaoJDBC implements VisitaMedicaDao {

	private DataSource dataSource;
	
	public VisitaMedicaDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(Paziente paziente) {
		
		Connection connection = this.dataSource.getConnection();
		try {
			String query = "insert INTO visitaMedica(id_qr, nome_p, cognome_p) values (?,?,?)";
			PreparedStatement statement = connection.prepareStatement(query);
			if(paziente.getCodice() != null) {
				statement.setString(1, paziente.getCodice().getCodice());
				statement.setString(2, paziente.getNome());
				statement.setString(3, paziente.getCognome());
			}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Paziente> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Paziente paziente) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Paziente paziente) {
		// TODO Auto-generated method stub
		
	}

}
