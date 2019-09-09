pakage Crawler.CrawlerLogic;

import Crawler.db.CrawlerDb;
import edu.uci.ics.crawler4j.crawler.CrawlController;

public class CrawlerFactory implements CrawlController.WebCrawlerFactory<Crawler>   {

    private final String dbUrl;
    private final String dbUser;
    private final String dbPw;

    public CrawlerFactory(String dbUrl, String dbUser, String dbPw) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPw = dbPw;
    }

    @Override
    public Crawler newInstance() throws Exception {
        return new Crawler(new CrawlerDb(dbUrl,dbUser,dbPw,"org.postgresql.Driver"));
    }
}
