package Models;

import Dao.Identified;

import java.sql.Timestamp;

public class Webpage implements Identified<Integer> {

    private Integer id = null;

    private String url;
    private String Html;
    private String text;
    private Timestamp seen;
    private Keyword keyword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return Html;
    }

    public void setHtml(String html) {
        Html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getSeen() {
        return seen;
    }

    public void setSeen(Timestamp seen) {
        this.seen = seen;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }
}
