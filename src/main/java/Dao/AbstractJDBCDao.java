package Dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class providing the basic implementation of CRUD operations using JDBC.
 *
 * @param <T>  Type of persistance
 * @param <PK> type of PRIMARY KEY
 */
public abstract class AbstractJDBCDao<T extends Identified<PK>, PK extends Integer> implements GenericDao<T, PK> {

    private Connection connection;
    private DaoFactory<Connection> parentFactory;

    /**
     * Returns the sql query to retrieve all records.
     * <p/>
     * SELECT * FROM [Table]
     */
    public abstract String getSelectQuery();

    /**
     * Returns a sql query to insert a new record into the database.
     * <p/>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    public abstract String getCreateQuery();

    /**
     * Returns a sql query to update the record.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();

    /**
     * Returns a sql query to delete the record from the database.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract String getDeleteQuery();

    /**
     * Disassembles the ResultSet and returns a list of objects corresponding to the contents of the ResultSet.
     */
    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    /**
     * Sets the insert arguments of the request according to the value of the fields of the object.
     */
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;

    /**
     *Sets the query update arguments according to the field value of the object object.
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;



    private Set<ManyToOne> relations = new HashSet<ManyToOne>();


    @Override
    public T persist(T object) throws PersistException {
        if (object.getId() != null) {
            throw new PersistException("Object is already persist.");
        }
        // Сохраняем зависимости
        saveDependences(object);

        T persistInstance;
        // Добавляем запись
        String sql = getCreateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("On persist modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        // Получаем только что вставленную запись
        sql = getSelectQuery() + " WHERE id = last_insert_id();";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {
                throw new PersistException("Exception on findByPK new persist data.");
            }
            persistInstance = list.iterator().next();
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return persistInstance;
    }



    @Override
    public T getByPK(Integer key) throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistException("Record with PK = " + key + " not found.");
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public void update(T object) throws PersistException {
        // Сохраняем зависимости
        saveDependences(object);

        String sql = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            prepareStatementForUpdate(statement, object); // filling out the arguments of the request is left to the conscience of the descendants
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public void delete(T object) throws PersistException {
        String sql = getDeleteQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try {
                statement.setObject(1, object.getId());
            } catch (Exception e) {
                throw new PersistException(e);
            }
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("On delete modify more then 1 record: " + count);
            }
            statement.close();
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public List<T> getAll() throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return list;
    }

    public AbstractJDBCDao(DaoFactory<Connection> parentFactory, Connection connection) {
        this.parentFactory = parentFactory;
        this.connection = connection;
    }

    protected Identified getDependence(Class<? extends Identified> dtoClass, Serializable pk) throws PersistException {
        return parentFactory.getDao(connection, dtoClass).getByPK(pk);
    }

    protected boolean addRelation(Class<? extends Identified> ownerClass, String field) {
        try {
            return relations.add(new ManyToOne(ownerClass, parentFactory, field));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveDependences(Identified owner) throws PersistException {
        for (ManyToOne m : relations) {
            try {
                if (m.getDependence(owner) == null) {
                    continue;
                }
                if (m.getDependence(owner).getId() == null) {
                    Identified depend = m.persistDependence(owner, connection);
                    m.setDependence(owner, depend);
                } else {
                    m.updateDependence(owner, connection);
                }
            } catch (Exception e) {
                throw new PersistException("Exception on save dependence in relation " + m + ".", e);
            }
        }
    }




}