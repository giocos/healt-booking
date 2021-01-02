package repository;

import java.util.List;

import entity.Segnalazione;

public interface SegnalazioneDao {

	int assignId();
	void save(Segnalazione segnalazione);
	List<Segnalazione> findAll();
	void update(Segnalazione segnalazione);
	void delete(Segnalazione segnalazione);
}
