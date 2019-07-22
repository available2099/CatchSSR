package com.readbook.book.DAO;

import com.readbook.book.MO.BOOK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.UUID;

public class InsertData {
    @Autowired
    @Resource(name = "jdbcTemplate")

    private  JdbcTemplate jdbcTemplate;

    public  void insertBook(BOOK book){

        String sql = "insert into book(bookCode,updater,checkId,BOOKID,VERID,BOOKNAME,BOOKTYPE,PRICE,FREE," +
                "clickCount,BOOKAUTHORS,DESCRIPTION,isbn,PUBLISHERNAME,PAPERPERICE,COVERURL,BOOKURL,probationRate" +
                ",owner,inCart,favorite,promotion,NOWPRICE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object args[] = {book.getBookCode(),book.getUpdater(),book.getCheckId(),book.getBOOKID(),
                book.getVERID(),book.getBOOKNAME(),book.getBOOKTYPE(),book.getPRICE(),
                book.getFREE(),book.getClickCount(),book.getBOOKAUTHORS(),book.getDESCRIPTION(),
                book.getIsbn(),book.getPUBLISHERNAME(),book.getPAPERPERICE(),book.getCOVERURL(),
                book.getBOOKURL(),book.getProbationRate(),book.getOwner(),book.getInCart(),
                book.getFavorite(),book.getPromotion(),book.getNOWPRICE()};

        int temp = jdbcTemplate.update(sql, args);
        if (temp > 0) {
            System.out.println("插入成功！");
        }else{
            System.out.println("插入失败");
        }
    }

    public void heh() {
     String uuid=  UUID.randomUUID().toString();
        System.out.println("uuid:"+uuid);
    }

    public static void main(String args[]){
        String uuid=  UUID.randomUUID().toString().replaceAll("-","");
        System.out.println("uuid:"+uuid);
    }

}
