package repository;

import entity.Logging;
import exception.PersistenceException;

import java.util.List;

public interface LoggingDao {

	int getId() throws PersistenceException;
	void save(Logging logging) throws PersistenceException;
	List<Logging> findAll() throws PersistenceException;
	void deleteAll() throws PersistenceException;
}
