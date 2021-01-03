package repository;

import entity.Amministratore;
import exception.PersistenceException;

public interface AmministratoreDao {

	void save(Amministratore amministratore) throws PersistenceException;
	Amministratore findByPrimaryKey(String username) throws PersistenceException;
	void update(Amministratore amministratore) throws PersistenceException;
	void delete(Amministratore amministratore) throws PersistenceException;
}
