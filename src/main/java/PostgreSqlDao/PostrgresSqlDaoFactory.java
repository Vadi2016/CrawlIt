package PostgreSQL;

import Dao.DaoFactory;
import Dao.GenericDao;
import Dao.PersistException;
import Models.Keyword;
import Models.Webpage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PostrgresSqlDaoFactory implements DaoFactory<Connection> {

    private String user = "postgres";//Логин пользователя
    private String password = "password";//Пароль пользователя
    private String url = "jdbc:postgresql://localhost:5432/javaCrawlerDb";//URL адрес
    private String driver = "org.postgresql.Driver";//Имя драйвера
    private Map<Class, DaoCreator> creators;


    @Override
    public Connection getContext() throws PersistException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return  connection;
    }

    @Override
    public GenericDao getDao(Connection connection, Class dtoClass) throws PersistException {
        DaoCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(connection);
    }

    public PostrgresSqlDaoFactory() {
        try {
            Class.forName(driver);//Регистрируем драйвер
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        creators = new HashMap<Class, DaoCreator>();
        creators.put(Keyword.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new PostgreSqlKeywordDao(PostrgresSqlDaoFactory.this, connection);
            }
        });
        creators.put(Webpage.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new PostgreSqlWebpageDao(PostrgresSqlDaoFactory.this, connection);
            }
        });
    }
}
