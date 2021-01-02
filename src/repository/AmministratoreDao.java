package repository;

import entity.Amministratore;

public interface AmministratoreDao {

	void save(Amministratore amministratore);
	Amministratore findByPrimaryKey(String username);
	void update(Amministratore amministratore);
	void delete(Amministratore amministratore);
}
