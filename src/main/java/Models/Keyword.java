package Models;

import Dao.Identified;

public class Keyword implements Identified<Integer> {

    private Integer id = null;
    private String keyword;


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
