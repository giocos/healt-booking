package repository;

import entity.Paziente;
import exception.PersistenceException;

import java.util.List;

public interface UniversitaDao {

	void save(Paziente paziente) throws PersistenceException;
	Paziente findByPrimaryKey(Long matricola) throws PersistenceException;
	List<Paziente> findAll() throws PersistenceException;
}
