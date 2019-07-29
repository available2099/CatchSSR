package com.readbook.book.http;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequest.class.getName());

    public static String sendGet(String url, String param) throws IOException {
        LOGGER.info("request url info : {}", url);
        HttpGet request = new HttpGet(url + "?" + param);
        return send(request);
    }

    public static String sendPost(String url, String param) throws IOException {
        LOGGER.info("request url info : {}", url);
        HttpPost request = new HttpPost(url);
        request.setEntity(
                new StringEntity(param, ContentType.create("application/json;charset=UTF-8"))
        );
        return send(request);
    }

    private static String send(HttpRequestBase request) throws IOException {
        String message = "";
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) ...");
        request.setHeader("accept", "*/*");
        request.setHeader("connection", "Keep-Alive");
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
            }
        }
        LOGGER.info(">>>>>>>>>>>>>>>>>response message info : {}", message);
        return message;
    }
}
