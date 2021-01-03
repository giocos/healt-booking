package repository;

import java.util.List;

import entity.Segnalazione;
import exception.PersistenceException;

public interface SegnalazioneDao {

	int getId() throws PersistenceException;
	void save(Segnalazione segnalazione) throws PersistenceException;
	List<Segnalazione> findAll() throws PersistenceException;
	void update(Segnalazione segnalazione) throws PersistenceException;
	void delete(Segnalazione segnalazione) throws PersistenceException;
}
