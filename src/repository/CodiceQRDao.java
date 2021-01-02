package repository;

import java.util.List;
import entity.CodiceQR;

public interface CodiceQRDao {

	void save(CodiceQR codice);
	CodiceQR findByPrimaryKey(String codice);
	List<CodiceQR> findAll();
	void update(CodiceQR codice);
	void delete(CodiceQR codice);
}
