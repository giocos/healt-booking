package repository;

import java.util.List;

import entity.Logging;
import exception.PersistenceException;

public interface LoggingDao {

	int getId() throws PersistenceException;
	void save(Logging logging) throws PersistenceException;
	List<Logging> findAll() throws PersistenceException;
	void deleteAll() throws PersistenceException;
}
