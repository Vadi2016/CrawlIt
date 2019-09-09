package Crawler.db;

import Dao.*;
import Models.Keyword;
import Models.Webpage;
import PostgreSQL.PostgreSqlKeywordDao;
import PostgreSQL.PostrgresSqlDaoFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.slf4j.Logger;


import java.sql.*;
import java.util.*;


public class CrawlerDb implements CrawlerDbService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CrawlerDb.class);
    private String dbUrl;
    private String dbUser;
    private String dbPw;
    private String driver;

    private static final DaoFactory<Connection> factory = new PostrgresSqlDaoFactory();

    public Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/javaCrawlerDb", "postgres", "password");



    public CrawlerDb(String dbUrl, String dbUser, String dbPw, String driver) throws
             SQLException, ClassNotFoundException {
            this.driver = driver;

               this.dbUrl = dbUrl;
               this.dbUser = dbUser;
               this.dbPw = dbPw;
               Class.forName(this.driver);
               if (con != null) {
                   System.out.println("You made it, take control your database now!");
               }
               else {
                   System.out.println("Failed to make connection!");
               }
               assert con != null;
    }



     public void store(Page page) {
         
        PreparedStatement insertKeyStatement = con.prepareStatement(w.getCreateQuery());
        PostgreSqlWebpageDao w = new PostgreSqlWebpageDao(factory, con);
            
        Map<Integer, String> key = new HashMap<Integer, String>();
        PostgreSqlKeywordDao k = new PostgreSqlKeywordDao(factory, con);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(k.getSelectQuery());
        while(rs.next()){
            key.put(rs.getInt("id"), rs.getString("keyword"));
        }
            Webpage webpage = new Webpage();
        if (key.containsValue(htmlParseData.getTitle()) || key.containsValue(htmlParseData.getText()) || key.containsValue(htmlParseData.getMetaTags())){
                    insertKeyStatement.setInt(1, webpage.getKeyword().getId());
                    insertKeyStatement.setString(2, htmlParseData.getHtml());
                    insertKeyStatement.setString(3, htmlParseData.getText());
                    insertKeyStatement.setString(4, page.getWebURL().getURL());
                    insertKeyStatement.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
                    insertKeyStatement.executeUpdate();
                    }
         rs.close();
         stmt.close();
          
        }
}

