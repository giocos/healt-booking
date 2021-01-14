package repository;

import entity.CodiceQR;
import exception.PersistenceException;

import java.util.List;

public interface CodiceQRDao {

	void save(CodiceQR codice) throws PersistenceException;
	CodiceQR findByPrimaryKey(String codice) throws PersistenceException;
	List<CodiceQR> findAll() throws PersistenceException;
	void update(CodiceQR codice) throws PersistenceException;
	void delete(CodiceQR codice) throws PersistenceException;
}
