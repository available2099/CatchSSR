package com.readbook.book.EO;

import com.readbook.book.MO.BOOK;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewBookRowMapper implements RowMapper<BOOK> {

    @Override
    public BOOK mapRow(ResultSet result, int i) throws SQLException {
        //        获取结果集中的数据
        String  BOOKID       =result.getString("BOOKID");
        String  VERID        =result.getString("VERID");
        String  BOOKNAME     =result.getString("BOOKNAME");
        String  PRICE        =result.getString("PRICE");
        String  BOOKAUTHORS  =result.getString("BOOKAUTHORS");
        String  DESCRIPTION  =result.getString("DESCRIPTION");
        String  PUBLISHERNAME=result.getString("PUBLISHERNAME");
        String  COVERURL     =result.getString("COVERURL");
        String  BOOKURL      =result.getString("BOOKURL");

        String  NOWPRICE     =result.getString("NOWPRICE");
        String  categorytype =result.getString("categorytype");
         //        把数据封装成User对象
        BOOK book = new BOOK();

        book.setBOOKID(BOOKID);
        book.setVERID(VERID);
        book.setBOOKNAME(BOOKNAME);
        book.setPRICE(PRICE);

        book.setBOOKAUTHORS(BOOKAUTHORS);
        book.setDESCRIPTION(DESCRIPTION);
        book.setPUBLISHERNAME(PUBLISHERNAME);
        book.setCOVERURL(COVERURL);
        book.setBOOKURL(BOOKURL);

        book.setNOWPRICE(NOWPRICE);
        book.setCategorytype(categorytype);
              return book;
    }
}
