package repository;

import java.util.List;
import entity.Paziente;
import exception.PersistenceException;

public interface UniversitaDao {

	void save(Paziente paziente) throws PersistenceException;
	Paziente findByPrimaryKey(Long matricola) throws PersistenceException;
	List<Paziente> findAll() throws PersistenceException;
}
