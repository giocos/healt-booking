package repository;

import java.util.List;
import entity.Paziente;

public interface PazienteDao {

	void save(Paziente paziente);
	Paziente findByPrimaryKey(String codiceFiscale);
	Paziente findByForeignKey(String codiceQR);
	List<Paziente> findAll();
	void update(Paziente paziente);
	void delete(Paziente paziente);
	boolean exists(Long matricola);
}
