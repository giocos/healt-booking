package repository;

import entity.Prenotazione;
import exception.PersistenceException;

import java.util.List;

public interface PrenotazioneDao {

	void save(Prenotazione prenotazione) throws PersistenceException;
	Prenotazione findByPrimaryKey(String codice) throws PersistenceException;
	List<Prenotazione> findAll() throws PersistenceException;
	void update(Prenotazione prenotazione) throws PersistenceException;
	void delete(Prenotazione prenotazione) throws PersistenceException;
	int getTotalVisits() throws PersistenceException;
}
