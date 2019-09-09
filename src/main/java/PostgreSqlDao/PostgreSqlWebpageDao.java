package PostgreSQL;

import Dao.AbstractJDBCDao;
import Dao.DaoFactory;
import Dao.PersistException;
import Models.Keyword;
import Models.Webpage;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class PostgreSqlWebpageDao extends AbstractJDBCDao<Webpage, Integer> {

    private class PersistWebpage extends Webpage {
        public void setId(int id) {
            super.setId(id);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT id, keyword_id, html, text1, url, seen FROM webpage ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO webpage (keyword_id, html, text1, url, seen) \n" +
                "VALUES (?, ?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE webpage \n" +
                "SET keyword_id = ?, html  = ?, text1 = ?, url = ?, seen = ? \n" +
                "WHERE id = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM webpage WHERE id= ?;";
    }

    @Override
    public Webpage create() throws PersistException {
        Webpage w = new Webpage();
        return persist(w);
    }

    public PostgreSqlWebpageDao(DaoFactory<Connection> parentFactory, Connection connection) {
        super(parentFactory, connection);
        addRelation(Webpage.class, "keyword");
    }

    @Override
    protected List<Webpage> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Webpage> result = new LinkedList<Webpage>();
        try {
            while (rs.next()) {
                PersistWebpage webpage = new PersistWebpage();
                webpage.setId(rs.getInt("id"));
                webpage.setKeyword((Keyword) getDependence(Keyword.class, rs.getInt("keyword_id")));
                webpage.setHtml(rs.getString("html"));
                webpage.setText(rs.getString("text1"));
                webpage.setUrl(rs.getString("url"));
                webpage.setSeen(rs.getTimestamp("seen"));
                result.add(webpage);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Webpage object) throws PersistException {



        try {
            int keyword_id = (object.getKeyword() == null || object.getKeyword().getId() == null) ? -1
                    : object.getKeyword().getId();

            statement.setString(1, object.getHtml());
            statement.setString(2, object.getText());
            statement.setString(3, object.getUrl());
            statement.setTimestamp(4, object.getSeen());
            statement.setInt(5, object.getId());
            statement.setInt(6, keyword_id);
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }




    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Webpage object) throws PersistException {
        try {

            int keyword_id = (object.getKeyword() == null || object.getKeyword().getId() == null) ? -1
                    : object.getKeyword().getId();

            statement.setString(1, object.getHtml());
            statement.setString(2, object.getText());
            statement.setString(3, object.getUrl());
            statement.setTimestamp(4, object.getSeen());
            statement.setInt(5, keyword_id);
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }





}
