package com.readbook.book.ssr.util;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReadLocalFile {
    public final static Map<String,String> trainSeatPriceMapping = new HashMap<>();
    static {
        trainSeatPriceMapping.put("一等座", "zy");
        trainSeatPriceMapping.put("二等座", "ze");
    }
    public void readLocalFile(){
        File file = new File("D:\\book\\train.json");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        //从json文件中读取数据
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line;
            while((line=bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //开始解析

        JSONObject nluResult = JSONObject.parseObject(stringBuffer.toString());
        JSONObject results = nluResult.getJSONObject("response_content").getJSONObject("results");
    }

}
