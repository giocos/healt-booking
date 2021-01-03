package repository;

import java.util.List;
import entity.Email;
import exception.PersistenceException;

public interface EmailDao {
	
	void save(Email email) throws PersistenceException;
	List<Email> findAll() throws PersistenceException;
}
