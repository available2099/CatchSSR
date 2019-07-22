package com.readbook.book;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.readbook.book.EO.BookRowMapper;
import com.readbook.book.EO.NewBookRowMapper;
import com.readbook.book.EO.User;
import com.readbook.book.MO.BOOK;
import com.readbook.book.MO.BOOKLIST;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/mydb")
public class DbController {
    @Autowired
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(DbController.class.getName());

    @RequestMapping("/getUsers")
    public List<Map<String, Object>> getDbType() {
        String sql = "select * from user";
        //  List<User> list =  jdbcTemplate.queryForList(sql,User.class);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        return list;
    }

    @RequestMapping("/insertUsers")
    public void insert() {
        User user = new User();
        user.setName("xixi1");
        user.setAdress("huaihelu");
        user.setLelvel("8989");
        String sql = "insert into user(name,adress,lelvel)values(?,?,?)";
        Object args[] = {user.getName(), user.getAdress(), user.getLelvel()};

     /*   int temp = jdbcTemplate.update(sql, args);
        if (temp > 0) {
            System.out.println("user插入成功！");
        }else{
            System.out.println("插入失败");
        }*/
        Map<String, Integer> map = new HashMap<>();
        map.put("6", 450);

     /*   map.put("2",170);
        map.put("3",400);
        map.put("4",450);
        map.put("5",300);
        map.put("7",3000);
        map.put("8",60);
        map.put("27",360);
        map.put("28",130);
        map.put("1",1350);
*/
        for (Map.Entry<String, Integer> s : map.entrySet()) {
            System.out.println("values:" + s);
            String cataId = s.getKey();
            int i = 0;
            String index = "";
            //人文社科 "http://f.cpgdp.com/front/book/getBookListByCatagoryCodeForFlipPage?bookShelfId=2&cataId=1&start=10&pageSize=10&orderType=11
            while (i < s.getValue()) {
                i = i + 10;
                index = "" + i;
                String url = "http://f.cpgdp.com/front/book/getBookListByCatagoryCodeForFlipPage?bookShelfId=2&cataId=" + cataId + "&start=" + index + "&pageSize=10&orderType=11";
                HttpGet request = new HttpGet(url);
                System.out.println("最重要的url:" + url);

                try {
                    // HttpRequestBook userDao = ctx.getBean(HttpRequestBook.class);

                    send(request, cataId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public String send(HttpRequestBase request, String cataId) throws IOException {
        String message = "";
        request.setHeader("Accept", "*/*");
        request.setHeader("Accept-Encoding", "gzip, deflate");
        request.setHeader("Accept-Language", "zh-cn");
        request.setHeader("connection", "close");
        request.setHeader("Host", "f.cpgdp.com");
        request.setHeader("User-Agent", "DajiaBookStore/1 CFNetwork/974.2.1 Darwin/18.0.0");


        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        if (entity != null) {
            long length = entity.getContentLength();
            if (length != -1 && length < 2048) {
                message = EntityUtils.toString(entity);
            } else {
                InputStream in = entity.getContent();
                byte[] data = new byte[4096];
                int count;
                while ((count = in.read(data, 0, 4096)) != -1) {
                    outStream.write(data, 0, count);
                }
                message = new String(outStream.toByteArray(), "UTF-8");
                System.out.println("输出得到的结果" + message);
                message = message.replaceAll("&#x1", "");
                XMLSerializer xmlSerializer = new XMLSerializer();
                String jsonData = xmlSerializer.read(message).toString();
                System.out.println(jsonData);

                //1、使用JSONObject
                // JSONObject jsonObject=JSONObject.fromObject(jsonData);
                JSONObject jsonObject = JSON.parseObject(jsonData);

                BOOKLIST booklist = JSON.toJavaObject(jsonObject, BOOKLIST.class);
          /* Gson gson = new Gson();

                BOOKLIST event = gson.fromJson(jsonData, BOOKLIST.class);*/
               /* ObjectMapper objectMapper = new ObjectMapper();
                BOOKLIST event = objectMapper.
                        readValue(jsonData, BOOKLIST.class);*/

                for (BOOK book : booklist.getBOOKS()) {
                    insertBook(book, cataId, message);
                }

                //   JSONObject object = JSONObject.fromObject(message);

                //JSONObject object = XML.toJSONObject(message);
                // String jsonData = object.toString();		//因为gson.formJson(String,class)


            }
        }
        //LOGGER.info(">>>>>>>>>>>>>>>>>response message info : {}", message);
        return message;
    }

    public void insertBook(BOOK book, String cataId, String message) {

        String sql = "insert into book(bookCode,updater,checkId,BOOKID,VERID,BOOKNAME,BOOKTYPE,PRICE,FREE," +
                "clickCount,BOOKAUTHORS,DESCRIPTION,isbn,PUBLISHERNAME,PAPERPERICE,COVERURL,BOOKURL,probationRate" +
                ",owner,inCart,favorite,promotion,NOWPRICE,categorytype,catalogue,xml)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object args[] = {book.getBookCode(), book.getUpdater(), book.getCheckId(), book.getBOOKID(),
                book.getVERID(), book.getBOOKNAME(), book.getBOOKTYPE(), book.getPRICE(),
                book.getFREE(), book.getClickCount(), book.getBOOKAUTHORS(), book.getDESCRIPTION(),
                book.getIsbn(), book.getPUBLISHERNAME(), book.getPAPERPERICE(), book.getCOVERURL(),
                book.getBOOKURL(), book.getProbationRate(), book.getOwner(), book.getInCart(),
                book.getFavorite(), book.getPromotion(), book.getNOWPRICE(), cataId, book.getCatalogue(), message};

        int temp = jdbcTemplate.update(sql, args);
        if (temp > 0) {
            System.out.println("插入成功！");
        } else {
            System.out.println("插入失败");
        }
    }

    /**
     * @return
     * @throws IOException
     */
    @RequestMapping("/getbooks")

    public List<BOOK> selectbook() {
        List<BOOK> list = null;
        int a = 0;
        while (a < 23000) {
            String sql = "select * from newbook a  where a.downloadtype='0' limit " + a + ",400";
            //  List<User> list =  jdbcTemplate.queryForList(sql,User.class);
            // list = jdbcTemplate.query(sql, new BookRowMapper());
            list = jdbcTemplate.query(sql, new NewBookRowMapper());
            System.out.println("下载到哪了:" + a);
            a = a + 400;
            for (BOOK book : list) {
                try {
                    if (book.getCOVERURL().trim() != "") {
                        sendzip(book.getBOOKURL(), book.getBOOKID(), book.getCategorytype());

                        loadpicture(book.getCOVERURL(), book.getBOOKID(), book.getCategorytype());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
     /*   try {
            sendzip("/9/bdad6b7021134746b71a487e4301a416/bb8fab0e4ece41afb32f18682a5eaefd/book.zip", "bdad6b7021134746b71a487e4301a416", "1000");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return list;
    }

    //@RequestMapping("/downlaodbook")
    public String sendzip(String BOOKURL, String BOOKID, String catagory) throws IOException {
        String url = BOOKURL;
        HttpGet request = new HttpGet(url);
        String message = "";
        request.setHeader("Accept", "*/*");
        request.setHeader("Accept-Encoding", "gzip, deflate");
        request.setHeader("Accept-Language", "zh-cn");
        request.setHeader("connection", "close");
        request.setHeader("Host", "wenjian.dajianet.com");
        request.setHeader("User-Agent", "DajiaBookStore/1 CFNetwork/974.2.1 Darwin/18.0.0");


        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();//获取数据流
        Charset c = Charset.forName("UTF-8");
        ZipInputStream zipis = new ZipInputStream(in, c);//封装成zip输入流
        String filename = "aa";
        String unzipath = "D:/book/";
        byte doc[] = null;

        try {
            //这里filename是文件名，如xxx.zip
            //  ZipInputStream zipis=new ZipInputStream(new FileInputStream(filename));
            ZipEntry fentry = null;
            while ((fentry = zipis.getNextEntry()) != null) {
                //fentry逐个读取zip中的条目，第一个读取的名称为test。
                //test条目是文件夹，因此会创建一个File对象，File对象接收的参数为地址
                //然后就会用exists,判断该参数所指定的路径的文件或者目录是否存在
                //如果不存在，则构建一个文件夹；若存在，跳过
                //如果读到一个zip，也继续创建一个文件夹，然后继续读zip里面的文件，如txt
                if (fentry.isDirectory()) {
                    File dir = new File(unzipath + catagory + "/" + BOOKID + "/book");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                } else {
                    //fname是文件名,fileoutputstream与该文件名关联
                    String fname = new String(unzipath + catagory + "/" + BOOKID + "/" + fentry.getName());
                    try {
                        //新建一个out,指向fname，fname是输出地址
                        FileOutputStream out = new FileOutputStream(fname);
                        doc = new byte[512];
                        int n;
                        //若没有读到，即读取到末尾，则返回-1
                        while ((n = zipis.read(doc, 0, 512)) != -1) {
                            //这就把读取到的n个字节全部都写入到指定路径了
                            out.write(doc, 0, n);
//                            System.out.println(n);
                        }
                        out.close();
                        out = null;
                        doc = null;
                    } catch (Exception ex) {
                        System.out.println("there is a problem");
                    }
                }
            }
            zipis.close();
        } catch (IOException ioex) {
            System.out.println("io错误：" + ioex);
        }
        //LOGGER.info(">>>>>>>>>>>>>>>>>response message info : {}", message);
        return message;
    }

    // @RequestMapping("/downlaodpicture")
    public String loadpicture(String COVERURL, String BOOKID, String catagory) throws IOException {
        String url = COVERURL;
        HttpGet request = new HttpGet(url);
        String message = "";
        request.setHeader("Accept", "image/*");
        request.setHeader("Accept-Encoding", "gzip, deflate");
        request.setHeader("Accept-Language", "zh-cn");
        request.setHeader("connection", "close");
        request.setHeader("Host", "wenjian.dajianet.com");
        request.setHeader("User-Agent", "DajiaBookStore/1 CFNetwork/974.2.1 Darwin/18.0.0");


        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();

        InputStream in = entity.getContent();//获取数据流

        byte[] btImg = readInputStream(in, BOOKID, catagory);


        //LOGGER.info(">>>>>>>>>>>>>>>>>response message info : {}", message);
        return message;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inputStream 输入流
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream, String BOOKID, String catagory) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        //新建文件夹
        File dir = new File("D:/book/picture/" + catagory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //输出文件流
        OutputStream os = new FileOutputStream("D:\\book\\picture\\" + catagory + "\\" + BOOKID + ".png");

        int len = 0;
        //写入
        while ((len = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        //
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        os.close();
        inputStream.close();
        return outputStream.toByteArray();
    }

    @RequestMapping("/insertpost")

    public List<BOOK> insertPost() {
        List<BOOK> list = null;
        int a = 0;
        int cc = 20;
        while (a < 200) {
            String sql = "select * from book   limit " + a + ",100";
            //  List<User> list =  jdbcTemplate.queryForList(sql,User.class);
            list = jdbcTemplate.query(sql, new BookRowMapper());
            System.out.println("下载到哪了:" + a);
            a = a + 100;

            for (BOOK book : list) {


                sql = "insert into wpl_posts(" +
                        "post_author,post_date,post_date_gmt,post_content" +
                        ",post_title,post_excerpt,post_status,comment_status" +
                        ",ping_status,post_password,post_name,to_ping" +
                        ",pinged,post_modified,post_modified_gmt,post_content_filtered" +
                        ",post_parent,guid,menu_order,post_type,post_mime_type" +
                        ",comment_count" +
                        ") values" +
                        "(?,?,?,?" +
                        ",?,?,?,?" +
                        ",?,?,?,?" +
                        ",?,?,?,?" +
                        ",?,?,?,?,?" +
                        ",?" +
                        ")";
                String guid = "https://z17107.adman.cloud/?p=" + cc;
                String picture = "/picture/" + book.getCategorytype() + "/" + book.getBOOKID() + ".png";
                String content = "<img class=\"alignnone size-medium wp-image-15\" src=\"" + picture + "\"/>"
                        + "作者:" + book.getBOOKAUTHORS() + "介绍:" + book.getDESCRIPTION() + book.getCatalogue();
                Object args[] = {1, new Date(), new Date(), content,
                        book.getBOOKNAME(), "", "publish", "open"
                        , "open", "", book.getBOOKNAME(), ""
                        , "", new Date(), new Date(), ""
                        , "0", guid, 0, "post", "", 0};

                int temp = jdbcTemplate.update(sql, args);
                if (temp > 0) {
                    System.out.println("11插入成功！");
                } else {
                    System.out.println("11插入失败");
                }
                cc = cc + 1;


            }
        }
        return list;
    }

    @RequestMapping("/insernewbook")
    public String send() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        // map.put("1", 339);
      /*  map.put("2", 81);
        map.put("3", 157);
        map.put("4", 111);
        map.put("5", 77);
        map.put("6", 149);*/
        map.put("7", 842);
        map.put("8", 103);
       /* map.put("27", 65);
        map.put("28", 26);*/
        for (Map.Entry<String, Integer> s : map.entrySet()) {
            System.out.println("values:" + s);
            String cataId = s.getKey();
            int i = 1;
            String index = "";
            //人文社科 "http://f.cpgdp.com/front/book/getBookListByCatagoryCodeForFlipPage?bookShelfId=2&cataId=1&start=10&pageSize=10&orderType=11
            while (i <= s.getValue()) {
                index = "" + i;
                String url = "http://v.dajianet.com/category/item/" + s.getKey() + ".html?type=" + s.getKey() + "&pageNo=" + i + "&validated=true";
                System.out.println("最重要的url:" + url);
                //  String url = "http://v.dajianet.com/category/item/1.html?type=1&pageNo=339&validated=true";
                HttpGet request = new HttpGet(url);
                String message = "";
                request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                request.setHeader("Accept-Encoding", "gzip, deflate");
                request.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                request.setHeader("connection", "keep-alive");
                request.setHeader("Host", "v.dajianet.com");
                request.setHeader("Referer", "http://login.dajianet.com/cas/remoteLogin?service=http%3A%2F%2Fv.dajianet.com%2Fcategory%2Fitem%2F1.html%3Ftype%3D1%26pageNo%3D2");
                request.setHeader("Upgrade-Insecure-Requests", "1");
                request.setHeader("Cookie", "UM_distinctid=16817eb580b3f7-08e4904876f0ec-3c604504-1fa400-16817eb580c306; __guid=201821782.3490714357298167000.1548666827026.6187; __utmc=53454903; Hm_lvt_b379093210417f4472ddc827f27027cc=1546589526,1546589547,1548643157,1548742942; JSESSIONID=8A02FADBE2E466FB9BCC9CEC874F1EF7; monitor_count=10; CNZZDATA3366226=cnzz_eid%3D1061180351-1548662948-http%253A%252F%252Flogin.dajianet.com%252F%26ntime%3D1548754358; Hm_lpvt_b379093210417f4472ddc827f27027cc=1548758128; __utma=53454903.294853489.1546589526.1548742942.1548758128.5; __utmz=53454903.1548758128.5.4.utmcsr=login.dajianet.com|utmccn=(referral)|utmcmd=referral|utmcct=/cas/remoteLogin; __utmt=1; __utmb=53454903.1.10.1548758128");

                request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
                CloseableHttpClient httpclient = HttpClients.createDefault();
                CloseableHttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                if (entity != null) {
                    long length = entity.getContentLength();
                    if (length != -1 && length < 2048) {
                        message = EntityUtils.toString(entity);
                    } else {
                        InputStream in = entity.getContent();
                        byte[] data = new byte[4096];
                        int count;
                        while ((count = in.read(data, 0, 4096)) != -1) {
                            outStream.write(data, 0, count);
                        }
                        message = new String(outStream.toByteArray(), "UTF-8");
                        //        System.out.println("输出得到的结果" + message);
                        Document doc = Jsoup.parse(message);
                        Element body = doc.body();
                        Elements list = doc.getElementsByClass("dl2");
                        if (list != null && list.size() > 0) {
                            Element dd = list.get(0);
                            Elements childdd = dd.children();
                            //   System.out.println("输出得到的结果" + childdd);
                            for (Element ele : childdd) {
                                Elements links = ele.getElementsByTag("a");
                                String detailurl = "";
                                String linkText = "";
                                String BOOKNAME = "";
                                String BOOKID = "";

                                if (links != null && links.size() >= 2) {
                                    detailurl = links.get(0).attr("href");
                                    //  linkText = links.get(0).text();
                                    BOOKNAME = links.get(1).text();
                                    String BOOKIDll = StringUtils.substringBeforeLast(detailurl, ".");
                                    String[] bookidl = BOOKIDll.split("/");
                                    BOOKID = bookidl[2];

                                }

                                Elements COVERURLs = ele.getElementsByTag("img");
                                String COVERURL = "";
                                String BOOKURL = "";
                                String VERID = "";
                                if (COVERURLs != null && COVERURLs.size() > 0) {
                                    COVERURL = COVERURLs.get(0).attr("src");
                                    BOOKURL = StringUtils.substringBeforeLast(COVERURL, "/") + "/book.zip";
                                    String[] bookidl = COVERURL.split("/");
                                    VERID = bookidl[6];
                                }

                                Elements prices = ele.getElementsByTag("p");
                                String PRICE = "";
                                String NOWPRICE = "";
                                if (prices != null && prices.size() >= 2) {
                                    PRICE = prices.get(0).text();
                                    NOWPRICE = prices.get(1).text();
                                } else if (prices != null && prices.size() == 1) {
                                    PRICE = prices.get(0).text();
                                }

                                Elements book_names = ele.getElementsByClass("book_name");
                                Elements book_infos = ele.getElementsByClass("book_info");
                                String bookauthors = "";
                                if (book_names != null && book_names.size() > 0) {
                                    bookauthors = book_names.get(0).text();

                                }
                                String description = "";
                                if (book_infos != null && book_infos.size() > 0) {
                                    description = book_infos.get(0).text();
                                }


                                String sqlz = "select * from newbook a where a.BOOKID='" + BOOKID + "'";
                                //  List<User> list =  jdbcTemplate.queryForList(sql,User.class);
                                List<Map<String, Object>> listuu = jdbcTemplate.queryForList(sqlz);

                                if (listuu != null && listuu.size() > 0) {

                                } else {
                                    //   System.out.println("输出得到的结果" + description);
                                    String sql = "insert into newbook" +
                                            "(BOOKID,VERID,BOOKNAME,PRICE,BOOKAUTHORS" +
                                            ",DESCRIPTION,PUBLISHERNAME,COVERURL,BOOKURL,NOWPRICE" +
                                            ",categorytype,detailurl)" +
                                            "values" +
                                            "(?,?,?,?,?" +
                                            ",?,?,?,?,?,?,?)";
                                    Object args[] = {BOOKID, VERID, BOOKNAME, PRICE, bookauthors,
                                            description, "", COVERURL, BOOKURL, NOWPRICE, s.getKey(), detailurl
                                    };

                                    int temp = jdbcTemplate.update(sql, args);
                                    if (temp > 0) {
                                        System.out.println("newbook插入成功！");
                                    } else {
                                        System.out.println("newbook插入失败");
                                    }
                                }

                            }

                          /*  try {
                                Thread.sleep(1000);//休眠一分钟
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/


                        }

                    }
                }
                i++;
            }
            i = 1;
        }


        //LOGGER.info(">>>>>>>>>>>>>>>>>response message info : {}", message);
        return null;
    }


    /**
     * 循环文件夹
     */
    @RequestMapping("/updatedownloadtype")

    public void wenjianjia() {
        File file = new File("D:/book/");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        File[] fileslist = file2.listFiles();
                        if (!"picture".equals(file2.getName())) {
                            for (File file3 : fileslist) {
                                String bookid = file3.getName();
                                //    System.out.println("文件夹kk:" + bookid);
                                String sql = "update newbook set downloadtype='1' where BOOKID=?";
                                Object args[] = {bookid};

                                int temp = jdbcTemplate.update(sql, args);
                                if (temp > 0) {
                                    //    System.out.println("更新成功！");
                                } else {
                                    System.out.println(bookid + ":更新失败");
                                }
                            }
                        }

                        //   traverseFolder2(file2.getAbsolutePath());
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

    public static void main(String args[]) {
        try {
            wenjianjiaReName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //wenjianjia();
        //  send();

    }


    /**
     * 循环文件夹更新文件名
     */
    public static void wenjianjiaReName() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 339);
        map.put("2", 81);
        map.put("3", 157);
        map.put("4", 111);
        map.put("5", 77);
        map.put("6", 149);
        map.put("7", 842);
        map.put("8", 103);
        map.put("27", 65);
        map.put("28", 26);
        for (Map.Entry<String, Integer> s : map.entrySet()) {
        File file = new File("D:/Books/book/"+s.getKey()+"/");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                      //  System.out.println("文件夹:" + file2.getAbsolutePath());
                        File[] fileslist = file2.listFiles();
                        String bookid = file2.getName();
                        String encoding = "utf-8";
                        File filel = new File("D:/Books/book/"+s.getKey()+"/" + bookid + "/book/book.dat");
                        if (filel.isFile()&&filel.exists()) { //判断文件是否存在
                            InputStreamReader read = null;//考虑到编码格式
                            try {
                                read = new InputStreamReader(
                                        new FileInputStream(filel), encoding);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            BufferedReader bufferedReader = new BufferedReader(read);
                            String lineTxt = null;
                            while ((lineTxt = bufferedReader.readLine()) != null) {
                              //  System.out.println(lineTxt);
                                int first1 = lineTxt.indexOf("<Name>");//参数为字符的ascii码
                                int first2 = lineTxt.indexOf("</Name>");
                                String bookname="";
                                //查找指定字符串第一次出现的位置
                                if(first1!=0&& first1+6<lineTxt.length()){
                                     bookname = lineTxt.substring(first1 + 6, first2);


                             //   System.out.println("最终:" + bookname);
                                file = new File("D:/Books/book/"+s.getKey()+"/" + bookid + "/book/book.epub"); //指定文件名及路径
                                file2 = new File("D:/Books/book/"+s.getKey()+"/" + bookid + "/book/book.pdf"); //指定文件名及路径

                                if (file.exists()) {
                                    if (file.renameTo(new File("D:/Books/book/"+s.getKey()+"/" + bookname + "#q.epub"))) {
                                       // System.out.println("修改成功!");
                                    } else {
                                        System.out.println("修改失败:"+bookid);
                                    }
                                } else if (file2.exists()) {
                                    if (file2.renameTo(new File("D:/Books/book/"+s.getKey()+"/" + bookname + "#q.pdf"))) {
                                     //   System.out.println("修改成功!");
                                    } else {
                                        System.out.println("修改失败:"+bookid);
                                    }
                                }
                                }



                            }
                            read.close();

                        } else {
                        //    System.out.println("找不到指定的文件");
                            //   return new Success<>(false);
                        }

                        //    System.out.println("文件夹kk:" + bookid);


                        //   traverseFolder2(file2.getAbsolutePath());
                    } else {
                    //    System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }
    }

    /**
     * 2019-02-15
     * 查询其他小分类的书籍信息
     * @return
     * @throws IOException
     */
    @RequestMapping("/insertcategorybook")
    public String insertCategoryBook() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        map.put("21",19);
        map.put("22",7);
        map.put("23",7);
        map.put("25",35);
        map.put("26",0);
        map.put("122",0);
        map.put("123",0);
        map.put("124",5);
        map.put("125",0);
        map.put("126",0);
        map.put("127",5);
        for (Map.Entry<String, Integer> s : map.entrySet()) {
            System.out.println("values:" + s);
            String cataId = s.getKey();
            int i = 1;
            String index = "";
            //人文社科 "http://f.cpgdp.com/front/book/getBookListByCatagoryCodeForFlipPage?bookShelfId=2&cataId=1&start=10&pageSize=10&orderType=11
            while (i <= s.getValue()) {
                index = "" + i;
                String url = "http://v.dajianet.com/category/item/" + s.getKey() + ".html?type=" + s.getKey() + "&pageNo=" + i + "&validated=true";
                System.out.println("最重要的url:" + url);
                //  String url = "http://v.dajianet.com/category/item/1.html?type=1&pageNo=339&validated=true";
                HttpGet request = new HttpGet(url);
                String message = "";
                request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                request.setHeader("Accept-Encoding", "gzip, deflate");
                request.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                request.setHeader("connection", "keep-alive");
                request.setHeader("Host", "v.dajianet.com");
                request.setHeader("Referer", "http://login.dajianet.com/cas/remoteLogin?service=http%3A%2F%2Fv.dajianet.com%2Fcategory%2Fitem%2F61.html%3Ftype%3D1%26pageNo%3D2");
                request.setHeader("Upgrade-Insecure-Requests", "1");
                request.setHeader("Cookie", "UM_distinctid=168ec53fa13ca-02cc12a3426598-3c604504-1fa400-168ec53fa1411a; Hm_lvt_b379093210417f4472ddc827f27027cc=1550153153; __utmc=53454903; JSESSIONID=2ACDEB67A0EC04E001A00CE24FBF8739; CNZZDATA3366226=cnzz_eid%3D1541643383-1550152555-http%253A%252F%252Fwww.so.com%252F%26ntime%3D1550218647; __utma=53454903.1229332524.1550153153.1550212459.1550220855.4; __utmz=53454903.1550220855.4.4.utmcsr=login.dajianet.com|utmccn=(referral)|utmcmd=referral|utmcct=/cas/remoteLogin; __utmt=1; __utmb=53454903.2.10.1550220855; Hm_lpvt_b379093210417f4472ddc827f27027cc=1550220864");

                request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
                CloseableHttpClient httpclient = HttpClients.createDefault();
                CloseableHttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                if (entity != null) {
                    long length = entity.getContentLength();
                    if (length != -1 && length < 2048) {
                        message = EntityUtils.toString(entity);
                    } else {
                        InputStream in = entity.getContent();
                        byte[] data = new byte[4096];
                        int count;
                        while ((count = in.read(data, 0, 4096)) != -1) {
                            outStream.write(data, 0, count);
                        }
                        message = new String(outStream.toByteArray(), "UTF-8");
                        //        System.out.println("输出得到的结果" + message);
                        Document doc = Jsoup.parse(message);
                        Element body = doc.body();
                        Elements list = doc.getElementsByClass("dl2");
                        if (list != null && list.size() > 0) {
                            Element dd = list.get(0);
                            Elements childdd = dd.children();
                            //   System.out.println("输出得到的结果" + childdd);
                            for (Element ele : childdd) {
                                Elements links = ele.getElementsByTag("a");
                                String detailurl = "";
                                String linkText = "";
                                String BOOKNAME = "";
                                String BOOKID = "";

                                if (links != null && links.size() >= 2) {
                                    detailurl = links.get(0).attr("href");
                                    //  linkText = links.get(0).text();
                                    BOOKNAME = links.get(1).text();
                                    String BOOKIDll = StringUtils.substringBeforeLast(detailurl, ".");
                                    String[] bookidl = BOOKIDll.split("/");
                                    BOOKID = bookidl[2];

                                }

                                Elements COVERURLs = ele.getElementsByTag("img");
                                String COVERURL = "";
                                String BOOKURL = "";
                                String VERID = "";
                                if (COVERURLs != null && COVERURLs.size() > 0) {
                                    COVERURL = COVERURLs.get(0).attr("src");
                                    BOOKURL = StringUtils.substringBeforeLast(COVERURL, "/") + "/book.zip";
                                    String[] bookidl = COVERURL.split("/");
                                    VERID = bookidl[6];
                                }

                                Elements prices = ele.getElementsByTag("p");
                                String PRICE = "";
                                String NOWPRICE = "";
                                if (prices != null && prices.size() >= 2) {
                                    PRICE = prices.get(0).text();
                                    NOWPRICE = prices.get(1).text();
                                } else if (prices != null && prices.size() == 1) {
                                    PRICE = prices.get(0).text();
                                }

                                Elements book_names = ele.getElementsByClass("book_name");
                                Elements book_infos = ele.getElementsByClass("book_info");
                                String bookauthors = "";
                                if (book_names != null && book_names.size() > 0) {
                                    bookauthors = book_names.get(0).text();

                                }
                                String description = "";
                                if (book_infos != null && book_infos.size() > 0) {
                                    description = book_infos.get(0).text();
                                }


                               /* String sqlz = "select * from newbook a where a.BOOKID='" + BOOKID + "'";
                                //  List<User> list =  jdbcTemplate.queryForList(sql,User.class);
                                List<Map<String, Object>> listuu = jdbcTemplate.queryForList(sqlz);

                                if (listuu != null && listuu.size() > 0) {

                                } else {*/
                                    //   System.out.println("输出得到的结果" + description);
                                    String sql = "insert into categorybook" +
                                            "(BOOKID,VERID,BOOKNAME,PRICE,BOOKAUTHORS" +
                                            ",DESCRIPTION,PUBLISHERNAME,COVERURL,BOOKURL,NOWPRICE" +
                                            ",categorytype,detailurl,parenttag)" +
                                            "values" +
                                            "(?,?,?,?,?" +
                                            ",?,?,?,?,?,?,?,?)";
                                    Object args[] = {BOOKID, VERID, BOOKNAME, PRICE, bookauthors,
                                            description, "", COVERURL, BOOKURL, NOWPRICE, s.getKey(), detailurl,"2"
                                    };

                                    int temp = jdbcTemplate.update(sql, args);
                                    if (temp > 0) {
                                        System.out.println("newbook插入成功！");
                                    } else {
                                        System.out.println("newbook插入失败");
                                    }
                               // }

                            }

                          /*  try {
                                Thread.sleep(1000);//休眠一分钟
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/


                        }

                    }
                }
                i++;
            }
            i = 1;
        }


        //LOGGER.info(">>>>>>>>>>>>>>>>>response message info : {}", message);
        return null;
    }


    @RequestMapping("/getpost")
    public List<Map<String, Object>> getPost() {
        String sql = "select * from wpl_posts limit   0 ,400";
        //  List<User> list =  jdbcTemplate.queryForList(sql,User.class);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        return list;
    }
}
