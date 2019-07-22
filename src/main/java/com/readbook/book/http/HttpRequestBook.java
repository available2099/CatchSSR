package com.readbook.book.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readbook.book.DAO.InsertData;
import com.readbook.book.DbController;
import com.readbook.book.MO.BOOK;
import com.readbook.book.MO.BOOKLIST;
import net.sf.json.xml.XMLSerializer;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpRequestBook {


    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestBook.class.getName());

    public  String sendGet(String url, String param) throws IOException {
        LOGGER.info("request url info : {}", url);
        HttpGet request = new HttpGet(url + "?" + param);
        return send(request);
    }

    public  String sendPost(String url, String param) throws IOException {
        LOGGER.info("request url info : {}", url);
        HttpPost request = new HttpPost(url);
        request.setEntity(
                new StringEntity(param, ContentType.create("application/json;charset=UTF-8"))
        );
        return send(request);
    }

    public String send(HttpRequestBase request) throws IOException {
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
                XMLSerializer xmlSerializer = new XMLSerializer();
                String jsonData = xmlSerializer.read(message).toString();
                System.out.println(jsonData);

                //1、使用JSONObject
               // JSONObject jsonObject=JSONObject.fromObject(jsonData);
                JSONObject jsonObject = JSON.parseObject(jsonData);

                BOOKLIST booklist = JSON.toJavaObject(jsonObject,BOOKLIST.class);
          /* Gson gson = new Gson();

                BOOKLIST event = gson.fromJson(jsonData, BOOKLIST.class);*/
               /* ObjectMapper objectMapper = new ObjectMapper();
                BOOKLIST event = objectMapper.
                        readValue(jsonData, BOOKLIST.class);*/

               for(BOOK book : booklist.getBOOKS()){
                   InsertData insertData =new InsertData();
                   insertData.insertBook(book);
               }
                LOGGER.info(">>>>>>>>>>>>>>>>>response message info : {}", booklist);

                //   JSONObject object = JSONObject.fromObject(message);

                //JSONObject object = XML.toJSONObject(message);
               // String jsonData = object.toString();		//因为gson.formJson(String,class)


            }
        }
        //LOGGER.info(">>>>>>>>>>>>>>>>>response message info : {}", message);
        return message;
    }

    public  static void main(String args[]){
        HttpGet request = new HttpGet("http://f.cpgdp.com/front/book/getBookListByCatagoryCodeForFlipPage?bookShelfId=2&cataId=1&start=10&pageSize=10&orderType=11");
        try {
            HttpRequestBook HttpRequestBook =new HttpRequestBook();
            HttpRequestBook.send(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
