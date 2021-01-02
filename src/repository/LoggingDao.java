package repository;

import java.util.List;

import entity.Logging;

public interface LoggingDao {

	int assignId();
	void save(Logging accesso);
	List<Logging> findAll();
	void deleteAll();
}
