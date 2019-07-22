package com.readbook.book.EO;

import com.readbook.book.MO.BOOK;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<BOOK> {

    @Override
    public BOOK mapRow(ResultSet result, int i) throws SQLException {
        //        获取结果集中的数据
        String  bookCode     =result.getString("bookCode");
        String  updater      =result.getString("updater");
        String  checkId      =result.getString("checkId");
        String  BOOKID       =result.getString("BOOKID");
        String  VERID        =result.getString("VERID");
        String  BOOKNAME     =result.getString("BOOKNAME");
        String  BOOKTYPE     =result.getString("BOOKTYPE");
        String  PRICE        =result.getString("PRICE");
        String  FREE         =result.getString("FREE");
        String  clickCount   =result.getString("clickCount");
        String  BOOKAUTHORS  =result.getString("BOOKAUTHORS");
        String  DESCRIPTION  =result.getString("DESCRIPTION");
        String  isbn         =result.getString("isbn");
        String  PUBLISHERNAME=result.getString("PUBLISHERNAME");
        String  PAPERPERICE  =result.getString("PAPERPERICE");
        String  COVERURL     =result.getString("COVERURL");
        String  BOOKURL      =result.getString("BOOKURL");
        String  probationRate=result.getString("probationRate");
        String  owner        =result.getString("owner");
        String  inCart       =result.getString("inCart");
        String  favorite     =result.getString("favorite");
        String  promotion    =result.getString("promotion");
        String  NOWPRICE     =result.getString("NOWPRICE");
        String  categorytype =result.getString("categorytype");
         //        把数据封装成User对象
        BOOK book = new BOOK();
        book.setBookCode(bookCode);
        book.setUpdater(updater);
        book.setCheckId(checkId);
        book.setBOOKID(BOOKID);
        book.setVERID(VERID);
        book.setBOOKNAME(BOOKNAME);
        book.setBOOKTYPE(BOOKTYPE);
        book.setPRICE(PRICE);
        book.setFREE(FREE);
        book.setClickCount(clickCount);
        book.setBOOKAUTHORS(BOOKAUTHORS);
        book.setDESCRIPTION(DESCRIPTION);
        book.setIsbn(isbn);
        book.setPUBLISHERNAME(PUBLISHERNAME);
        book.setPAPERPERICE(PAPERPERICE);
        book.setCOVERURL(COVERURL);
        book.setBOOKURL(BOOKURL);
        book.setProbationRate(probationRate);
        book.setOwner(owner);
        book.setInCart(inCart);
        book.setFavorite(favorite);
        book.setPromotion(promotion);
        book.setNOWPRICE(NOWPRICE);
        book.setCategorytype(categorytype);
              return book;
    }
}
