package Dao;

import java.io.Serializable;
import java.util.List;

/**
 * Unified interface for managing the persistent state of objects
 * @param <T> type of object persistences
 * @param <PK> type of PRIMARY KEY
 */
public interface GenericDao<T extends Identified<PK>, PK extends Serializable> {

    /** Creates a new record and its corresponding object */
    public T create() throws PersistException;

    /** Creates a new record that corresponds to the object */
    public T persist(T object)  throws PersistException;

    /** Returns an object corresponding to a record with a primary key or null */
    public T getByPK(PK key) throws PersistException;

    /** Saves the state of the group object in the database */
    public void update(T object) throws PersistException;

    /** Deletes an entry about an object from the database */
    public void delete(T object) throws PersistException;

    /** Returns a list of objects matching all records in the database */
    public List<T> getAll() throws PersistException;


}
