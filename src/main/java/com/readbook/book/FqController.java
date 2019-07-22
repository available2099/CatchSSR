package com.readbook.book;

import com.readbook.book.ssr.FreeSSR;
import com.readbook.book.ssr.bean.SSRNode;
import com.readbook.book.ssr.util.DoSearchSSR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/mydb")
public class FqController {
    @Autowired
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @RequestMapping(value = "/getssr", method = {RequestMethod.GET})
    @ResponseBody
    public void  getssr(){
        List<SSRNode> oklist = null;
        try {
             oklist= DoSearchSSR.getssrdata();
        } catch (IOException e) {
            e.printStackTrace();
        }

      System.out.println("准备插入：总共多少："+oklist.size());
        ExecutorService inserttable =  Executors.newFixedThreadPool(60);

        for (SSRNode sn : oklist) {

                            try {
                                String sql = "insert into ssr_data(downloadurl,SSRURL,server,server_port)values(?,?,?,?)";
                                Object args[] = {sn.getDownloadurl(),sn.getSSRURL(),sn.getServer(),sn.getServer_port()};
                                int temp = jdbcTemplate.update(sql, args);
                                if (temp > 0) {
                                    //    System.out.println("user插入成功！");
                                }else{
                                    System.out.println("插入失败");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("失败连接："+sn.getSSRURL());

                            }


        }
        System.out.println("插入完成");
        }






}
