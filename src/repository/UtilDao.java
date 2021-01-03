package repository;

import exception.PersistenceException;

public interface UtilDao {

    void dropDatabase() throws PersistenceException;
    void createDatabase() throws PersistenceException;
    void resetPrenotazioni() throws PersistenceException;
}
