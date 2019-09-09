package Dao;

import java.sql.Connection;
import java.sql.SQLException;

/** Factory of object for working with the database */
public interface DaoFactory<Context> {

    public interface DaoCreator<Context> {
        public GenericDao create(Context context);
    }

    /** Returns the connection to the database */
    public Context getContext() throws PersistException;

    /** Returns an object to control the persistent state of an object */
    public GenericDao getDao(Context context, Class dtoClass) throws PersistException;
}


