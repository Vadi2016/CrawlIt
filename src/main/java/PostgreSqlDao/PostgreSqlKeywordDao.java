package PostgreSQL;

import Dao.AbstractJDBCDao;
import Dao.DaoFactory;
import Dao.PersistException;
import Models.Keyword;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class PostgreSqlKeywordDao extends AbstractJDBCDao<Keyword, Integer> {


    private class PersistKeyword extends Keyword {
        public void setId(int id) {
            super.setId(id);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT id, keyword FROM keywords";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO keywords (keyword) \n" +
                "VALUES (?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE keywords SET  keyword = ? WHERE id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM keywords WHERE id= ?;";
    }

    @Override
    public Keyword create() throws PersistException {
        Keyword k = new Keyword();
        return persist(k);
    }

    public PostgreSqlKeywordDao(DaoFactory<Connection> parentFactory, Connection connection) {

        super(parentFactory, connection);
    }

    @Override
    protected List<Keyword> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Keyword> result = new LinkedList<Keyword>();
        try {
            while (rs.next()) {
                PersistKeyword keyword = new PersistKeyword();
                keyword.setId(rs.getInt("id"));
                keyword.setKeyword(rs.getString("keyword"));
                result.add(keyword);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Keyword object) throws PersistException {
        try {
            statement.setString(1, object.getKeyword());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Keyword object) throws PersistException {
        try {
            statement.setString(1, object.getKeyword());
            statement.setInt(2, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

}
