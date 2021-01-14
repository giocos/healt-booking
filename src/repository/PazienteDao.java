package repository;

import entity.Paziente;
import exception.PersistenceException;

import java.util.List;

public interface PazienteDao {

	void save(Paziente paziente) throws PersistenceException;
	Paziente findByPrimaryKey(String codiceFiscale) throws PersistenceException;
	Paziente findByForeignKey(String codiceQR) throws PersistenceException;
	List<Paziente> findAll() throws PersistenceException;
	void update(Paziente paziente) throws PersistenceException;
	void delete(Paziente paziente) throws PersistenceException;
	boolean exists(Long matricola) throws PersistenceException;
}
