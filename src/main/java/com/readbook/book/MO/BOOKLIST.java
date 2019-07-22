package com.readbook.book.MO;

import java.util.ArrayList;
import java.util.List;

public class BOOKLIST {
    public String BOOKSHELFID;

    public String START;


    public String END;


    public String BOOKSUM;


    public List<BOOK> BOOKS =new ArrayList<>();

    public String getBOOKSHELFID() {
        return BOOKSHELFID;
    }

    public void setBOOKSHELFID(String BOOKSHELFID) {
        this.BOOKSHELFID = BOOKSHELFID;
    }

    public String getSTART() {
        return START;
    }

    public void setSTART(String START) {
        this.START = START;
    }

    public String getEND() {
        return END;
    }

    public void setEND(String END) {
        this.END = END;
    }

    public String getBOOKSUM() {
        return BOOKSUM;
    }

    public void setBOOKSUM(String BOOKSUM) {
        this.BOOKSUM = BOOKSUM;
    }

    public List<BOOK> getBOOKS() {
        return BOOKS;
    }

    public void setBOOKS(List<BOOK> BOOKS) {
        this.BOOKS = BOOKS;
    }
}
