package repository;

import java.util.List;
import entity.Prenotazione;

public interface PrenotazioneDao {

	void save(Prenotazione prenotazione);
	Prenotazione findByPrimaryKey(String codice);
	List<Prenotazione> findAll();
	void update(Prenotazione prenotazione);
	void delete(Prenotazione prenotazione);
	int getTotalVisits();
}
