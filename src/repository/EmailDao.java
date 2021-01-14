package repository;

import entity.Email;
import exception.PersistenceException;

import java.util.List;

public interface EmailDao {
	
	void save(Email email) throws PersistenceException;
	List<Email> findAll() throws PersistenceException;
}
