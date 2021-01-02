package repository;

import java.util.List;
import entity.Email;

public interface EmailDao {
	
	void save(Email email);
	List<Email> findAll();
}
