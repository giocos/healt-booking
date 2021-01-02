package repository;

import java.util.List;
import entity.Paziente;

public interface UniversitaDao {

	void save(Paziente paziente);
	Paziente findByPrimaryKey(Long matricola);
	List<Paziente> findAll();
}
