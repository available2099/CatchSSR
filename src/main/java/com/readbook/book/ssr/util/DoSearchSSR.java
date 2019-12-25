package com.readbook.book.ssr.util;

import com.readbook.book.ssr.bean.SSRNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoSearchSSR {

    //1. 爬取地址/逗比根据地免费账号地址
    static String SourceUrltest[] = {"https://github.com/available2099/subscrible_tool_ceshi/blob/master/nodex.txt"};

    static String[] SourceUrlArry = {
            "https://github.com/abcdefuxk/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/difone/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/feixun800/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/Jacky342/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/BLUE22R/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/Dep0s1t/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/dx2ly/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/HiUSB/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/Jacky-Scp/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/jacky-0502/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/JanzenZhang/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/kariynerson/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/Kauroth/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/kevoncheung/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/Kwok1am/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/leasual/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/MalcolmCHO/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/muma16/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/muma16fx/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/myweb1991/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/oliverpoon/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/piaoyer1988/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/poplm/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/guduren9089/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/jgchengxin/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/KwokLam/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/lizihua5215/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/ljkk-li/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/Loliye/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/luanluanxu/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/lyhrtb/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/lzihxin/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/ManuZhu/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/minnesote/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/pansongjun/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/s2270661782/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/silencechenliang/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/singing9907/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/wubounce/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/young1119/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/yulmin15/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/Steve-ShadowsocksR/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/wangjianwx/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/xianword/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/xihuizhao/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/yataome1/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/yenkj/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/yl287440269/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/yhhu2049/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/zhanyu666/ssr_subscrible_tool/blob/master/node.txt",
            "https://github.com/zswd89/ssr_subscrible_tool/blob/master/node.txt"
    };

    //2. SSR可执行程序所在位置,可执行程序名称必须是ShadowsocksR-dotnet4.0.exe
    static String InstallFolder = "C:\\GreenSoftware\\ShadowsocksR-4.7.0\\ShadowsocksR-dotnet4.0.exe";

    //3. SSR的json配置文件路径
    static String filePath = "C:\\GreenSoftware\\ShadowsocksR-4.7.0\\gui-config.json";

    //4. 节点状态json地址
    static String NodeStatusUrl = "http://sstz.toyoo.ml/json/stats.json";

    static String vmess = "";

    /*
     * base64解码
     */
    public static String base64Decode(String string) throws UnsupportedEncodingException {
        byte[] asBytes = Base64.getUrlDecoder().decode(string);
        String result = new String(asBytes, "UTF-8");
        return result;
    }

    /*
     * 根据正则表达式提取字符串
     */
    public static List<String> getStringByRegex(String regex, String source) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /*
     * 获取节点可用状态
     */
    public static List<Boolean> getNodeStatus() throws IOException {
        Document doc = Jsoup.connect(NodeStatusUrl)
                .ignoreContentType(true)
                .get();
        String json = doc.select("body").text();

        //直接用正则表达式取状态数据
        List<String> statusList = new ArrayList<>();
        statusList = getStringByRegex("(\"status\": ){1}[a-z]*", json);
        //转换为布尔值
        List<Boolean> status = new ArrayList<>();
        for (String string : statusList) {
            string = string.substring(10);
            status.add(Boolean.valueOf(string));
        }
        return status;
    }

    /*
     * 执行ping操作,判断节点是否可用,及延时时间
     */
    public static String getPingTime(String ip) throws IOException {
        //执行ping命令
        Process p = Runtime.getRuntime().exec("ping " + ip);
        //接受返回的数据
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            //System.out.println(line);
            sb.append(line);
        }
        //使用正则表达式ping结果
        List<String> result = DoSearchSSR.getStringByRegex("[0-9]*ms$", sb.toString());
        //长度为零,及结果中不包含"平均 = XXXms"的即ping不通
        if (result.size() == 0) {
            return "请求超时";
        } else {
            return result.get(0);
        }
    }

    /*
     * 读取json配置文件
     * 参数:文件路径
     */
    public static String readJSON(String filePath) throws IOException {
        //文件编码
        String encoding = "UTF-8";
        //指定文件地址
        File file = new File(filePath);
        //存储文件内容
        StringBuilder sb = new StringBuilder();
        //判断文件是否存在
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                sb.append(lineTxt + "\n");
            }
            read.close();
        } else {
            System.out.println("找不到指定的文件");
        }
        String result = sb.toString();
        return result;
    }

    /*
     * 将更新的内容写入json配置文件
     */
    public static boolean writeJSON(String result, String filePath) {
        File file = new File(filePath);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));
            writer.write(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /*启动ShadowsocksR-dotnet4.0.exe
     * 判断SSR程序是否运行,运行就杀掉进程
     * cmd命令
     * 杀掉程序:taskkill /f /im ShadowsocksR-dotnet4.0.exe
     * 判断程序是否运行:tasklist|find /i "ShadowsocksR-dotnet4.0.exe"
     * 启动程序:C:\GreenSoftware\ShadowsocksR-4.7.0\ShadowsocksR-dotnet4.0.exe
     */
    public static void startSSR() throws IOException {
        //启动命令
        String startCommand = InstallFolder;
        //判断,查找有没有ssr进程,不加"cmd /c"会报错
        String findCommand = "cmd /c tasklist|findstr /i \"ShadowsocksR-dotnet4.0\"";
        //杀死进程命令
        String killCommand = "taskkill /f /im ShadowsocksR-dotnet4.0.exe";

        Runtime run = Runtime.getRuntime();
        //先判断SSR是否已运行
        Process process = run.exec(findCommand);
        //接受返回信息
        BufferedReader bufferedReader = new BufferedReader
                (new InputStreamReader(process.getInputStream()));
        String line = null;
        //存储返回信息
        StringBuilder sb = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line + "\n");
        }
        //System.out.println(sb);
        //从反馈的信息中进行判断,包含"ShadowsocksR-dotnet4.0"代表SSR程序已启动
        String regex = "ShadowsocksR-dotnet4.0";
        int count = DoSearchSSR.getStringByRegex(regex, sb.toString()).size();
        if (count == 2) {
            //重启,先杀再启
            System.out.println("SSR客户端已启动,将进行重启");
            run.exec(killCommand);
            //延时,等待程序退出再重新启动
            try {
                System.out.println("请稍候......");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            run.exec(startCommand);
            System.out.println("重启完成");
        } else {
            //否则直接启动SSR客户端
            System.out.println("启动SSR客户端");
            run.exec(startCommand);
        }
    }

    /*
     * 退出程序
     */
    public static void exit() {
        //退出
        System.out.println("\n按回车键退出");
        Scanner input = new Scanner(System.in);
        String isExit = input.nextLine();
        if (isExit.length() == 0) {
            //关闭当前进程
            input.close();
            System.exit(0);
        }
    }

    public static void geturl() throws IOException {
        SourceUrlArry = new String[]{};
        Set<String> urllist = new HashSet<>();
        //   https://github.com/available2099/ssr_subscrible_tool/network/members
        HttpGet request = new HttpGet("https://github.com/available2099/ssr_subscrible_tool/network/members");
        String message = "";
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        request.setHeader("Accept-Encoding", "gzip, deflate, br");
        request.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        request.setHeader("Cache-Control", "max-age=0");
        request.setHeader("connection", "keep-alive");
        request.setHeader("Host", "github.com");
        //	request.setHeader("Referer", "http://login.dajianet.com/cas/remoteLogin?service=http%3A%2F%2Fv.dajianet.com%2Fcategory%2Fitem%2F1.html%3Ftype%3D1%26pageNo%3D2");
        //	request.setHeader("Upgrade-Insecure-Requests", "1");
        //	request.setHeader("Cookie", "UM_distinctid=16817eb580b3f7-08e4904876f0ec-3c604504-1fa400-16817eb580c306; __guid=201821782.3490714357298167000.1548666827026.6187; __utmc=53454903; Hm_lvt_b379093210417f4472ddc827f27027cc=1546589526,1546589547,1548643157,1548742942; JSESSIONID=8A02FADBE2E466FB9BCC9CEC874F1EF7; monitor_count=10; CNZZDATA3366226=cnzz_eid%3D1061180351-1548662948-http%253A%252F%252Flogin.dajianet.com%252F%26ntime%3D1548754358; Hm_lpvt_b379093210417f4472ddc827f27027cc=1548758128; __utma=53454903.294853489.1546589526.1548742942.1548758128.5; __utmz=53454903.1548758128.5.4.utmcsr=login.dajianet.com|utmccn=(referral)|utmcmd=referral|utmcct=/cas/remoteLogin; __utmt=1; __utmb=53454903.1.10.1548758128");

        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
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
                Elements list = doc.getElementsByClass("repo");

                Elements hrh = doc.getElementsByAttributeValueContaining("href", "ssr_subscrible_tool");
                if (list != null && list.size() > 0) {
                    //开始时间
                    long startTime = System.currentTimeMillis();
                    for (Element ele : list) {
                        Element chid = ele.child((ele.childNodeSize() / 2) - 1);
                        Attributes jj = chid.attributes();

                        String yu = jj.asList().get(0).getValue();
                        System.out.println("jkj" + yu);
                        //https://github.com/abcdefuxk/ssr_subscrible_tool/blob/master/node.txt
                        String url = "https://github.com" + yu + "/blob/master/node.txt";

                        urllist.add(url);

                    }


                }

            }
        }
        SourceUrlArry = urllist.toArray(new String[]{});

    }

    public static void getmulturl() throws IOException {
        SourceUrlArry = new String[]{};
        List<String> urllist = new ArrayList<>();
        //   https://github.com/available2099/ssr_subscrible_tool/network/members
        String baseUrl = "https://github.com/available2099/ssr_subscrible_tool/network/members";
        HttpGet request = new HttpGet(baseUrl);
        String message = "";
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        request.setHeader("Accept-Encoding", "gzip, deflate, br");
        request.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        request.setHeader("Cache-Control", "max-age=0");
        request.setHeader("connection", "keep-alive");
        request.setHeader("Host", "github.com");
        //	request.setHeader("Referer", "http://login.dajianet.com/cas/remoteLogin?service=http%3A%2F%2Fv.dajianet.com%2Fcategory%2Fitem%2F1.html%3Ftype%3D1%26pageNo%3D2");
        //	request.setHeader("Upgrade-Insecure-Requests", "1");
        //	request.setHeader("Cookie", "UM_distinctid=16817eb580b3f7-08e4904876f0ec-3c604504-1fa400-16817eb580c306; __guid=201821782.3490714357298167000.1548666827026.6187; __utmc=53454903; Hm_lvt_b379093210417f4472ddc827f27027cc=1546589526,1546589547,1548643157,1548742942; JSESSIONID=8A02FADBE2E466FB9BCC9CEC874F1EF7; monitor_count=10; CNZZDATA3366226=cnzz_eid%3D1061180351-1548662948-http%253A%252F%252Flogin.dajianet.com%252F%26ntime%3D1548754358; Hm_lpvt_b379093210417f4472ddc827f27027cc=1548758128; __utma=53454903.294853489.1546589526.1548742942.1548758128.5; __utmz=53454903.1548758128.5.4.utmcsr=login.dajianet.com|utmccn=(referral)|utmcmd=referral|utmcct=/cas/remoteLogin; __utmt=1; __utmb=53454903.1.10.1548758128");

        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
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
                Elements list = doc.getElementsByClass("repo");

                Elements hrh = doc.getElementsByAttributeValueContaining("href", "ssr_subscrible_tool");
                if (list != null && list.size() > 0) {
                    //开始时间
                    long startTime = System.currentTimeMillis();
                    for (Element ele : list) {
                        Element chid = ele.child((ele.childNodeSize() / 2) - 1);
                        Attributes jj = chid.attributes();

                        String yu = jj.asList().get(0).getValue();
                        System.out.println("jkj" + yu);
                        //https://github.com/abcdefuxk/ssr_subscrible_tool/blob/master/node.txt
                        String url = "https://github.com" + yu + "/blob/master/node.txt";
                        try {
                            baseUrl = "https://github.com/" + yu.split("/")[1] + "?tab=repositories";

                            HttpGet request1 = new HttpGet(baseUrl);
                            String message1 = "";
                            request1.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
                            request1.setHeader("Accept-Encoding", "gzip, deflate, br");
                            request1.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                            request1.setHeader("Cache-Control", "max-age=0");
                            request1.setHeader("connection", "keep-alive");
                            request1.setHeader("Host", "github.com");
                            //	request.setHeader("Referer", "http://login.dajianet.com/cas/remoteLogin?service=http%3A%2F%2Fv.dajianet.com%2Fcategory%2Fitem%2F1.html%3Ftype%3D1%26pageNo%3D2");
                            //	request.setHeader("Upgrade-Insecure-Requests", "1");
                            //	request.setHeader("Cookie", "UM_distinctid=16817eb580b3f7-08e4904876f0ec-3c604504-1fa400-16817eb580c306; __guid=201821782.3490714357298167000.1548666827026.6187; __utmc=53454903; Hm_lvt_b379093210417f4472ddc827f27027cc=1546589526,1546589547,1548643157,1548742942; JSESSIONID=8A02FADBE2E466FB9BCC9CEC874F1EF7; monitor_count=10; CNZZDATA3366226=cnzz_eid%3D1061180351-1548662948-http%253A%252F%252Flogin.dajianet.com%252F%26ntime%3D1548754358; Hm_lpvt_b379093210417f4472ddc827f27027cc=1548758128; __utma=53454903.294853489.1546589526.1548742942.1548758128.5; __utmz=53454903.1548758128.5.4.utmcsr=login.dajianet.com|utmccn=(referral)|utmcmd=referral|utmcct=/cas/remoteLogin; __utmt=1; __utmb=53454903.1.10.1548758128");

                            request1.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
                            CloseableHttpClient httpclient1 = HttpClients.createDefault();
                            CloseableHttpResponse response1 = httpclient1.execute(request1);
                            HttpEntity entity1 = response1.getEntity();
                            ByteArrayOutputStream outStream1 = new ByteArrayOutputStream();
                            if (entity1 != null) {
                                length = entity1.getContentLength();
                                if (length != -1 && length < 2048) {
                                    message1 = EntityUtils.toString(entity1);
                                } else {
                                    InputStream      in1 = entity1.getContent();
                                    byte[] data1 = new byte[4096];
                                    int count1;
                                    while ((count1 = in1.read(data1, 0, 4096)) != -1) {
                                        outStream1.write(data1, 0, count1);
                                    }
                                    message1 = new String(outStream1.toByteArray(), "UTF-8");
                                    //        System.out.println("输出得到的结果" + message);
                                    Document doc1 = Jsoup.parse(message1);
                                    //      body = doc1.body();
                                    Elements list1 = doc1.getElementsByAttributeValue("itemprop", "name codeRepository");
                                    for (Element elel : list1) {
                                        Attributes heiheiurl = elel.attributes();
                                        for (Attribute ji : heiheiurl) {
                                            if (ji.getKey().equals("href")) {
                                                String urlkk = "https://github.com" + ji.getValue() + "/blob/master/node.txt";
                                                urllist.add(urlkk);
                                            }


                                        }

                                    }

                                }

                            }
                            urllist.add(url);

                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                    System.out.println("duoshaao:"+urllist.size());

                }


            }
        }
        // SourceUrlArry = urllist.toArray(new String[]{});
        //urllist.toArray(new String[0]);
        SourceUrlArry = urllist.toArray(new String[]{});
    }

    public static List<String> getSSRnode(String url) throws IOException {

        List<String> SSRList = new ArrayList<>();


        String message = null;
        CloseableHttpClient httpclient = null;
        HttpEntity entity = null;
        ByteArrayOutputStream outStream = null;
        try {
            HttpGet request = new HttpGet(url);
            message = "";
            request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            request.setHeader("Accept-Encoding", "gzip, deflate, br");
            request.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            request.setHeader("Cache-Control", "max-age=0");
            request.setHeader("connection", "keep-alive");
            request.setHeader("Host", "github.com");
            //	request.setHeader("Referer", "http://login.dajianet.com/cas/remoteLogin?service=http%3A%2F%2Fv.dajianet.com%2Fcategory%2Fitem%2F1.html%3Ftype%3D1%26pageNo%3D2");
            //	request.setHeader("Upgrade-Insecure-Requests", "1");
            //	request.setHeader("Cookie", "UM_distinctid=16817eb580b3f7-08e4904876f0ec-3c604504-1fa400-16817eb580c306; __guid=201821782.3490714357298167000.1548666827026.6187; __utmc=53454903; Hm_lvt_b379093210417f4472ddc827f27027cc=1546589526,1546589547,1548643157,1548742942; JSESSIONID=8A02FADBE2E466FB9BCC9CEC874F1EF7; monitor_count=10; CNZZDATA3366226=cnzz_eid%3D1061180351-1548662948-http%253A%252F%252Flogin.dajianet.com%252F%26ntime%3D1548754358; Hm_lpvt_b379093210417f4472ddc827f27027cc=1548758128; __utma=53454903.294853489.1546589526.1548742942.1548758128.5; __utmz=53454903.1548758128.5.4.utmcsr=login.dajianet.com|utmccn=(referral)|utmcmd=referral|utmcct=/cas/remoteLogin; __utmt=1; __utmb=53454903.1.10.1548758128");

            request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(request);
            entity = response.getEntity();
            outStream = new ByteArrayOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                httpclient.close();

                Elements list = doc.getElementsByClass("highlight tab-size js-file-line-container");
                if (list != null && list.size() > 0) {
                    Element dd = list.get(0);
                    Elements childdd = dd.children();
                    Elements heh = dd.getElementsByClass("blob-code blob-code-inner js-file-line");
                    //   System.out.println("输出得到的结果" + childdd);

                    for (Element ele : heh) {
                        String SSRurl = ele.text();

                        System.out.println("连接：" + SSRurl);
                        if ((SSRurl.indexOf("ssr://") != -1 || SSRurl.indexOf("ss://") != -1

                        ) && !"#".equals(SSRurl.substring(0, 1))

                                ) {
                            SSRList.add(SSRurl);
                        }else if( SSRurl.indexOf("vmess://") != -1){
                            vmess = vmess + SSRurl + "\n";


                        }
                    }


                }

            }
        }
        return SSRList;
    }

    public static boolean createFile(String filename ,String filecontent) {
        Boolean bool = false;
        String filenameTemp =  "C:/ssrfile/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date())+"/"+new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒").format(new Date())+filename + ".text";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //如果文件不存在，则创建新的文件
            if (!file.exists()) {
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is " + filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    public static boolean writeFileContent(String filepath, String newstr) throws IOException {
        Boolean bool = false;
        String filein = newstr + "\r\n";//新写入的行，换行
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名称
     * @return
     */
    public static boolean delFile(String fileName) {
        Boolean bool = false;
        String filenameTemp = "C:/ssrfile/" + "SSR:" + System.currentTimeMillis() + ".text";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            if (file.exists()) {
                file.delete();
                bool = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }


    // public static void main(String[] args) throws IOException {
    public static List<SSRNode> getssrdata() throws IOException {
        //开始时间
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        //geturl();
        getmulturl();
        List<SSRNode> okNodeList = new ArrayList<>();
        List<SSRNode> pingokNodeList = new ArrayList<>();

        //去重专用
        List<SSRNode> personList = new ArrayList<>();

        List<String> specialURL = new ArrayList<>();
        System.out.println(">>>>>>>> 开始连接......");
        //SourceUrlArry
        //SourceUrltest
        for (String url : SourceUrlArry) {
            executorService.submit(
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            //SourceUrltest
                            List<String> SSRList = null;
                            try {
                                SSRList = getSSRnode(url);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //Document doc = Jsoup.connect(SourceUrl).get();

                            System.out.println("连接成功!url:" + url);

    /*    //更新日期
        Elements updateTimeEle = doc.select("span[style='color: #ff6464;']");
        String updateTime = updateTimeEle.eq(1).text();
        System.out.println("更新日期: " + updateTime);

        //账号列表元素
        Elements tableEle = doc.select("table[width=100%]");

        //System.out.println(tableEle.html());

        //将元素转换字符串
        String tableStr = tableEle.toString();

        //账号列表页面的连接多了几位无用的字符,导致解码的时候出错,是为了防爬????
        //提取连接,到二级页面去获取正确的ssr连接
        String regexLink = "(http://doub.pw/qr/qr.php\\?text=ssr://){1}[a-zA-Z0-9_!]{60,}";
        List<String> linkList = new ArrayList<>();
        List<String> SSRList = new ArrayList<>();
        linkList = getStringByRegex(regexLink, tableStr);

        System.out.println(">>>>>>>> 获取SSR地址......");

        for (String string : linkList) {
            //测试输出
            //System.out.println("二级页面连接:"+string);
            Document html = Jsoup.connect(string).get();
            Elements ssrEle = html.select("#biao1");
            //测试输出
            System.out.println(ssrEle.text());
            SSRList.add(ssrEle.text());
        }*/
                            //节点数
                            int ssrNodeNum = SSRList.size();
                            System.out.println("共获得" + ssrNodeNum + "个账号");
                            //如果没有可用节点,直接停止程序
                            if (SSRList.size() > 0) {
                                System.out.println(">>>>>>>> 对地址进行base64解码......");

                                String urlString = "";
                                String[] urlArray;
                                //存储节点
                                List<SSRNode> nodeList = new ArrayList<>();
                                //获取节点名
                                //  int trNum = tableEle.select("tr").size();
                                //表格tr行数
                                //System.out.println(trNum);
                                for (String ssrurl : SSRList) {
                                    if (ssrurl.indexOf("ssr://") != -1) {
                                        String serverName = "";//tableEle.select("tr").get(trNum - ssrNodeNum + i - 1).select("td").get(0).text();
                                        //System.out.println(serverName);

                                        int loc = ssrurl.indexOf("#");//首先获取字符的位置
                                        //然后调用字符串截取
                                        if (loc != -1) {
                                            ssrurl = ssrurl.substring(0, loc);//再对字符串进行截取，获得想要得到的字符串
                                        }
                                        //处理多个ssr一行的情况
                                        //处理空格问题
                                        ssrurl = ssrurl.trim().replaceAll(" ", "");
                                        String[] ssrurlr = ssrurl.split("ssr://");
                                        if (ssrurlr != null && ssrurlr.length > 1) {
                                            for (String urlh : ssrurlr) {

                                                if (urlh.indexOf("@") == -1) {
                                                    try {
                                                        urlh = base64Decode(urlh);
                                                        urlArray = urlh.split(":");

                                                        //SSRNode节点
                                                        SSRNode ssrNode = new SSRNode();
                                                        ssrNode.setSSRURL(ssrurl);
                                                        //如果数组长度为6以上的IP地址是IPV6,长度为6的是IPV4
                                                        int len = urlArray.length;
                                                        if (len > 6) {
                                                            String ip = "";
                                                            for (int n = 0; n < len - 5; n++) {
                                                                if (n == len - 6) {
                                                                    ip = urlArray[n];
                                                                } else {
                                                                    ip = urlArray[n] + ":";
                                                                }
                                                            }
                                                            //下载链接
                                                            ssrNode.setDownloadurl(url);
                                                            //备注
                                                            ssrNode.setRemarks(serverName);
                                                            //ip
                                                            ssrNode.setServer(ip);
                                                            //端口
                                                            ssrNode.setServer_port(Integer.valueOf(urlArray[len - 5]));
                                                            //协议
                                                            ssrNode.setProtocol(urlArray[len - 4]);
                                                            //加密方式
                                                            ssrNode.setMethod(urlArray[len - 3]);
                                                            //混淆
                                                            ssrNode.setObfs(urlArray[len - 2]);
                                                            //密码
                                                            //  String pdStr = getStringByRegex("[a-zA-Z0-9]*", urlArray[len - 1]).get(0);
                                                            //对密码进行base64二次解码
                                                            //  pdStr = base64Decode(pdStr);
                                                            //   ssrNode.setPassword(pdStr);
                                                            //remarks
                                                            // String remStr = getStringByRegex("(=){1}[a-zA-Z0-9-]*", urlArray[len - 1]).get(0);
                                                            // remStr = remStr.substring(1);
                                                            //  ssrNode.setRemarks_base64(remStr);
                                                            okNodeList.add(ssrNode);
                                                        } else if(len >= 4){
                                                            if ("us-lax-a.fastssrr.me".equals(urlArray[0])) {
                                                                createFile("ip_url_is_important", url);
                                                            }
                                                            //下载链接
                                                            ssrNode.setDownloadurl(url);
                                                            String ip = urlArray[0];
                                                            //备注
                                                            ssrNode.setRemarks(serverName);
                                                            //ip
                                                            ssrNode.setServer(ip);
                                                            if("39.107.136.219".equals(ip)||"47.96.87.22".equals(ip)){
                                                                createFile("liad", url);
                                                            }
                                                            if("154.95.1.4".equals(ip)){
                                                                createFile("cn2", url);
                                                            }
                                                            //端口
                                                            ssrNode.setServer_port(Integer.valueOf(urlArray[1]));
                                                            //协议
                                                            ssrNode.setProtocol(urlArray[2]);
                                                            //加密方式
                                                            ssrNode.setMethod(urlArray[3]);
                                                            //混淆
                                                            ssrNode.setObfs(urlArray[4]);
                                                            //密码
                                                            //     String pdStr = getStringByRegex("[a-zA-Z0-9]*", urlArray[5]).get(0);
                                                            //对密码进行base64二次解码
                                                            //      pdStr = base64Decode(pdStr);
                                                            //     ssrNode.setPassword(pdStr);
                                                            //remarks
                                                            //  String remStr = getStringByRegex("(=){1}[a-zA-Z0-9-]*", urlArray[5]).get(0);
                                                            //remStr = remStr.substring(1);
                                                            //  ssrNode.setRemarks_base64(remStr);
                                                            okNodeList.add(ssrNode);
                                                        }
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace();
                                                    } catch (IllegalArgumentException e) {
                                                        System.out.println("有问题：ssr://" + urlh);
                                                        specialURL.add("ssr://" + urlh);
                                                        e.printStackTrace();
                                                    } catch (IndexOutOfBoundsException e) {
                                                        System.out.println("有问题：ssr://" + urlh);
                                                        specialURL.add("ssr://" + urlh);
                                                        e.printStackTrace();

                                                    } catch (Exception e) {
                                                        System.out.println("有问题：ssr://" + urlh);
                                                        specialURL.add("ssr://" + urlh);
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    if (urlh != "") {
                                                        specialURL.add("ssr://" + urlh);
                                                    }
                                                }
                                            }

                                        } else if (ssrurlr != null && ssrurlr.length == 1) {

                                            //去除ssr://
                                            urlString = ssrurl;
                                            urlString = urlString.substring(6);
                                            //网页源代码的ssr连接有个感叹号,点进去又没有感叹号???有感叹号解码就会报错......
                                            //urlString = urlString.replaceAll("!", "");
                                            //BASE64解码
                                            if (urlString.indexOf("@") == -1) {
                                                try {
                                                    urlString = base64Decode(urlString);
                                                    urlArray = urlString.split(":");

                                                    //SSRNode节点
                                                    SSRNode ssrNode = new SSRNode();
                                                    ssrNode.setSSRURL(ssrurl);
                                                    //如果数组长度为6以上的IP地址是IPV6,长度为6的是IPV4
                                                    int len = urlArray.length;
                                                    if (len > 6) {
                                                        String ip = "";
                                                        for (int n = 0; n < len - 5; n++) {
                                                            if (n == len - 6) {
                                                                ip = urlArray[n];
                                                            } else {
                                                                ip = urlArray[n] + ":";
                                                            }
                                                        }
                                                        //下载链接
                                                        ssrNode.setDownloadurl(url);
                                                        //备注
                                                        ssrNode.setRemarks(serverName);
                                                        //ip
                                                        ssrNode.setServer(ip);
                                                        //端口
                                                        ssrNode.setServer_port(Integer.valueOf(urlArray[len - 5]));
                                                        //协议
                                                        ssrNode.setProtocol(urlArray[len - 4]);
                                                        //加密方式
                                                        ssrNode.setMethod(urlArray[len - 3]);
                                                        //混淆
                                                        ssrNode.setObfs(urlArray[len - 2]);
                                                        //密码
                                                        //  String pdStr = getStringByRegex("[a-zA-Z0-9]*", urlArray[len - 1]).get(0);
                                                        //对密码进行base64二次解码
                                                        //  pdStr = base64Decode(pdStr);
                                                        //   ssrNode.setPassword(pdStr);
                                                        //remarks
                                                        // String remStr = getStringByRegex("(=){1}[a-zA-Z0-9-]*", urlArray[len - 1]).get(0);
                                                        // remStr = remStr.substring(1);
                                                        //  ssrNode.setRemarks_base64(remStr);
                                                        okNodeList.add(ssrNode);
                                                    } else {
                                                        if ("us-lax-a.fastssrr.me".equals(urlArray[0])) {
                                                            createFile("ip_url_is_important", url);
                                                        }
                                                        //下载链接
                                                        ssrNode.setDownloadurl(url);
                                                        String ip = urlArray[0];
                                                        //备注
                                                        ssrNode.setRemarks(serverName);
                                                        //ip
                                                        ssrNode.setServer(ip);
                                                        //端口
                                                        ssrNode.setServer_port(Integer.valueOf(urlArray[1]));
                                                        //协议
                                                        ssrNode.setProtocol(urlArray[2]);
                                                        //加密方式
                                                        ssrNode.setMethod(urlArray[3]);
                                                        //混淆
                                                        ssrNode.setObfs(urlArray[4]);
                                                        //密码
                                                        //     String pdStr = getStringByRegex("[a-zA-Z0-9]*", urlArray[5]).get(0);
                                                        //对密码进行base64二次解码
                                                        //      pdStr = base64Decode(pdStr);
                                                        //     ssrNode.setPassword(pdStr);
                                                        //remarks
                                                        //  String remStr = getStringByRegex("(=){1}[a-zA-Z0-9-]*", urlArray[5]).get(0);
                                                        //remStr = remStr.substring(1);
                                                        //  ssrNode.setRemarks_base64(remStr);
                                                        okNodeList.add(ssrNode);
                                                    }
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                } catch (NumberFormatException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalArgumentException e) {
                                                    System.out.println("有问题：ssr://" + urlString);
                                                    specialURL.add("ssr://" + urlString);
                                                    e.printStackTrace();
                                                } catch (IndexOutOfBoundsException e) {
                                                    System.out.println("有问题：ssr://" + urlString);
                                                    specialURL.add("ssr://" + urlString);
                                                    e.printStackTrace();

                                                } catch (Exception e) {
                                                    System.out.println("有问题：ssr://" + urlString);
                                                    specialURL.add("ssr://" + urlString);
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                specialURL.add("ssr://" + urlString);
                                            }
                                        }


                                    } else if (ssrurl.indexOf("ss://") != -1) {
                                        String serverName = "";//tableEle.select("tr").get(trNum - ssrNodeNum + i - 1).select("td").get(0).text();
                                        //System.out.println(serverName);
                                        //去除ssr://
                                        int loc = ssrurl.indexOf("#");//首先获取字符的位置
                                        //然后调用字符串截取
                                        if (loc != -1) {
                                            ssrurl = ssrurl.substring(0, loc);//再对字符串进行截取，获得想要得到的字符串
                                        }

                                        urlString = ssrurl;
                                        urlString = urlString.substring(5);
                                        //网页源代码的ssr连接有个感叹号,点进去又没有感叹号???有感叹号解码就会报错......
                                        //urlString = urlString.replaceAll("!", "");
                                        //BASE64解码
                                        if (urlString.indexOf("@") == -1) {

                                            try {
                                                urlString = base64Decode(urlString);
                                                String pwd = urlString.split("@")[0];
                                                String ipPort = urlString.split("@")[1];
                                                urlArray = urlString.split(":");

                                                //SSRNode节点
                                                SSRNode ssrNode = new SSRNode();
                                                //下载链接
                                                ssrNode.setDownloadurl(url);

                                                ssrNode.setSSRURL(ssrurl);

                                                ssrNode.setRemarks(serverName);
                                                //ip
                                                ssrNode.setServer(ipPort.split(":")[0]);
                                                //端口
                                                ssrNode.setServer_port(Integer.valueOf(ipPort.split(":")[1]));
                                                //协议
                                                //ssrNode.setProtocol(urlArray[2]);
                                                //加密方式
                                                //           ssrNode.setMethod(pwd.split(":")[0]);
                                                //混淆
                                                // ssrNode.setObfs(urlArray[4]);
                                                //密码
                                                //           ssrNode.setPassword(pwd.split(":")[1]);

                                                //remarks
                                                okNodeList.add(ssrNode);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            } catch (IllegalArgumentException e) {
                                                specialURL.add("ss://" + urlString);
                                                e.printStackTrace();
                                            } catch (IndexOutOfBoundsException e) {
                                                specialURL.add("ss://" + urlString);
                                                e.printStackTrace();
                                            } catch (Exception e) {
                                                System.out.println("有问题：ss://" + urlString);
                                                specialURL.add("ss://" + urlString);
                                                e.printStackTrace();
                                            }
                                        } else {
                                            specialURL.add("ss://" + urlString);

                                        }
                                    } else if (ssrurl.indexOf("vmess://") != -1) {
                                        specialURL.add(ssrurl);
                                    }


                                }
                                System.out.println("base64解码完成");

                                //查询节点状态
       /* List<Boolean> statusList = getNodeStatus();
        for (int i = 0; i < statusList.size(); i++) {
            nodeList.get(i).setStatus(statusList.get(i));
        }*/


     /*       for (SSRNode nl : nodeList) {
                //     if (nl.isStatus()) {
                System.out.println("ping " + nl.getServer() + "......");
                String result = getPingTime(nl.getServer());
                if(!"请求超时".equals(result)){
                    nl.setAvgPingTime(result);
                    okNodeList.add(nl);
                }

                //  }
            }*/

                            }


                        }
                    })
            );


        }

        //2019-11-04 vmess 连接文本创建
        createFile("vmess连接：", vmess);

        executorService.shutdown();

        while (true) {
            //等待所有任务都执行结束
            if (executorService.isTerminated()) {
/*              ExecutorService executorServiceCN2 = Executors.newFixedThreadPool(500);

        //判断是否cn2
        Set<SSRNode>  isCN2set = new HashSet<>();

        for (SSRNode sn : okNodeList) {
            executorServiceCN2.submit(
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Boolean  isCN2= TraceIP.traceip(sn.getServer());
                            if(isCN2){
                                isCN2set.add(sn);
                            }
                        }
                    })
            );

        }
        executorServiceCN2.shutdown();

        //等待所有任务都执行结束
        if (executorServiceCN2.isTerminated()) {
            String shuchu = "";
            for(SSRNode sn : isCN2set){
                shuchu = shuchu + sn.getSSRURL() + "\n";

            }
            createFile("isCN2", shuchu);

        }*/
            String    shuchu = "";
                for (SSRNode sn : okNodeList) {
                    shuchu = shuchu + sn.getSSRURL() + "\n";

                }
                createFile("去重前的数量：", shuchu);

                //okNodeList去重
                Map<String, SSRNode> ssrNodeMapUrl = new HashMap<>();
                for (SSRNode url : okNodeList) {
                    ssrNodeMapUrl.put(url.getSSRURL(), url);
                }

                shuchu = "";
                for (SSRNode sn : ssrNodeMapUrl.values()) {
                    shuchu = shuchu + sn.getSSRURL() + "\n";

                }
                createFile("去重后的数量：", shuchu);

                //所有的子线程都结束了
                System.out.println(">>>>>>>> 执行ping操作中......");
                ExecutorService executorServiceping = Executors.newFixedThreadPool(600);


                //SSRNode nodeListArryy[] = okNodeList.toArray(new SSRNode[]{});

                for (SSRNode ssnode : ssrNodeMapUrl.values()) {
                    executorServiceping.submit(
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        System.out.println("ping " + ssnode.getServer() + " :耗时:" + (System.currentTimeMillis() - startTime) * 1.0 / 1000 + " 秒");
                                        String result = null;
                                        try {
                                            result = getPingTime(ssnode.getServer());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if (!"请求超时".equals(result)) {
                                            ssnode.setAvgPingTime(result);
                                            pingokNodeList.add(ssnode);
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();

                                    }
                                }
                            })
                    );

                }


                //进行ping操作
                System.out.println(">>>>>>>> 执行ping操作结束......");

                executorServiceping.shutdown();
                while (true) {
                    //等待所有任务都执行结束
                    if (executorServiceping.isTerminated()) {
                        //所有的子线程都结束了
                        System.out.println("共耗时:" + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
                        //根据延时进行排序
                        // Collections.sort(okNodeList);
                        System.out.println(pingokNodeList.size() + "个可用节点:");

                        //输出80端口
                        shuchu = "";
                        for (SSRNode sn : pingokNodeList) {
                            shuchu = shuchu + sn.getSSRURL() + "\n";

                        }
                        createFile("quchu超时:", shuchu);
                        //节点集合
                        List<String> ssrurlList = new ArrayList<>();
                         shuchu = "";
                        Set<SSRNode> ssrNodeSet = new HashSet<>();
                        Map<String, SSRNode> ssrNodeMap = new HashMap<>();
                        Map<String, SSRNode> ssrNodeMap80 = new HashMap<>();
                        //  System.out.println("okNodeList一共有多少："+okNodeList.size());

                        // 去重
                /*       okNodeList.stream().forEach(
                               p -> {
                                   if (!personList.contains(p)) {
                                       personList.add(p);
                                   }
                               }
                       );*/
                        //   System.out.println("personList一共有多少："+personList.size());
                        //     okNodeList= personList;
                        for (SSRNode sn : pingokNodeList) {
                            ssrurlList.add(sn.getSSRURL());
                            System.out.println(sn.getSSRURL());
                            shuchu = shuchu + sn.getSSRURL() + "\n";
                            ssrNodeSet.add(sn);
                            ssrNodeMap.put(sn.getServer(), sn);
                            if (80 == sn.getServer_port() || 8080 == sn.getServer_port()) {
                                ssrNodeMap80.put(sn.getServer(), sn);

                            }
                        }
                        createFile("SSR", shuchu);
                        //输出80端口
                        shuchu = "";
                        for (SSRNode sn : ssrNodeMap80.values()) {
                            shuchu = shuchu + sn.getSSRURL() + "\n";

                        }
                        createFile("80map  quchongfu", shuchu);


                        shuchu = "";
                        for (SSRNode sn : ssrNodeSet) {
                            shuchu = shuchu + sn.getSSRURL() + "\n";

                        }
                        createFile("setquchongfu", shuchu);


                        shuchu = "";
                        for (SSRNode sn : ssrNodeMap.values()) {
                            shuchu = shuchu + sn.getSSRURL() + "\n";

                        }
                        createFile("mapquchongfu", shuchu);

                        String speilurl = "";
                        for (String urlp : specialURL) {
                            speilurl = speilurl + urlp + "\n";
                        }
                        if (specialURL.size() > 0) {
                            createFile("SpecialSSR", speilurl);
                        }
                        return pingokNodeList;

                    }

                }
            }

        }


        //读取配置文件
       /* String result = readJSON(filePath);
        //利用GSON解析json
        Gson gson = new Gson();
        GUIConfig guiConfig = gson.fromJson(result, GUIConfig.class);
        //服务器列表
        List<Server> configList = new ArrayList<>();
        //信息显示
        System.out.println(okNodeList.size() + "个可用节点:");
        int nodeCount = 0;
        for (SSRNode sn : okNodeList) {
            System.out.println("======== 第" + ++nodeCount + "个节点 ========");
            System.out.println(sn.toString());
            //服务器
            Server config = new Server();
            //设置 备注  ip  端口  协议  加密方式  混淆  密码  remarks_base 分组
            config.setServer(sn.getServer());
            config.setServer_port(sn.getServer_port());
            config.setProtocol(sn.getProtocol());
            config.setMethod(sn.getMethod());
            config.setObfs(sn.getObfs());
            config.setPassword(sn.getPassword());
            config.setRemarks_base64("");
            config.setGroup(sn.getRemarks());
            //用平均延时做备注
            config.setRemarks(sn.getAvgPingTime());
            configList.add(config);
        }
        //更新服务器列表
        guiConfig.setConfigs(configList);
        //将更新后的信息装换为json
        String updateConfig = gson.toJson(guiConfig);

        //System.out.println(updateConfig);

        System.out.println(">>>>>>>> 更新配置文件......");

        //写入配置文件
        writeJSON(updateConfig, filePath);

        System.out.println("完成更新");

        //启动或重启以应用新的配置文件
        startSSR();

        System.out.println();*/
        //结束时间
        //  long endTime = System.currentTimeMillis();
        //  System.out.println("耗时:" + (System.currentTimeMillis() - startTime) * 1.0 / 1000 + " 秒");

        //退出程序
        //   exit();

    }
}

