package repository;

import entity.Impiegato;
import exception.PersistenceException;

public interface ImpiegatoDao {

	void save(Impiegato impiegato) throws PersistenceException;
	Impiegato findByPrimaryKey(String username) throws PersistenceException;
	void update(Impiegato impiegato) throws PersistenceException;
	void delete(Impiegato impiegato) throws PersistenceException;
}
