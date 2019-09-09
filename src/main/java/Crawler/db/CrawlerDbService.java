package Crawler.db;

import Dao.PersistException;
import edu.uci.ics.crawler4j.crawler.Page;

import java.sql.SQLException;

public interface CrawlerDbService  {

    void store(Page webPage) ;

}
