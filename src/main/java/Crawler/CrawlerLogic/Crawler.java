pakage Crawler.CrawlerLogic;

import Dao.PersistException;
import Crawler.db.CrawlerDb;
import Crawler.db.CrawlerDbService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.Set;
import java.util.regex.Pattern;

public class Crawler extends WebCrawler {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Crawler.class);

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(" +
            "css|js" +
            "|bmp|gif|jpeg|JPEG|png|tiff|ico|nef|raw" +
            "|mid|mp2|mp3|mp4|wav|wma|flv|mpeg" +
            "|avi|mov|mpeg|ram|m4v|wmv|rm|smil" +
            "|pdf|doc|docx|pub|xls|xlsx|vsd|ppt|pptx" +
            "|swf" +
            "|zip|rar|gz|bz2|7z|bin" +
            "|xml|txt|java|c|cpp|exe" +
            "))$");

    private final CrawlerDbService crawlerDbService;

    public Crawler(CrawlerDbService crawlerDbService) {
        this.crawlerDbService = crawlerDbService;
    }


    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith("https://www.linux.org.ru/");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        logger.info("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());

            try {
                crawlerDbService.store(page);
            } catch (Exception e) {
                logger.error("Storing failed", e);
            }
        }
    }
}