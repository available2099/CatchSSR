package com.readbook.book;

import com.readbook.book.ssr.FreeSSR;
import com.readbook.book.ssr.bean.SSRNode;
import com.readbook.book.ssr.util.DoSearchSSR;
import com.readbook.book.ssr.util.TraceIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.readbook.book.ssr.util.DoSearchSSR.base64Decode;
import static com.readbook.book.ssr.util.DoSearchSSR.createFile;

@RestController
@RequestMapping("/mydb")
public class FqController {
    @Autowired
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getsubscription", method = {RequestMethod.GET})
    @ResponseBody
    public String getsubscription() {
        
        String result = "";
        try {
            String encoding = "UTF-8";
            //指定文件地址
            File file = new File("C:/sst/20190721_195218.text");
            //存储文件内容
            StringBuilder sb = new StringBuilder();
            //判断文件是否存在
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String ssrurl = null;
                while ((ssrurl = bufferedReader.readLine()) != null) {
                    sb.append(ssrurl + "\n");
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
             result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String encodedString = Base64.getEncoder().encodeToString(result.getBytes());
        return encodedString;
    }

    @RequestMapping(value = "/getssr", method = {RequestMethod.GET})
    @ResponseBody
    public void getssr() {
        List<SSRNode> oklist = null;
        try {
            oklist = DoSearchSSR.getssrdata();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("准备插入：总共多少：" + oklist.size());
        ExecutorService inserttable = Executors.newFixedThreadPool(60);

        for (SSRNode sn : oklist) {

            try {
                String sql = "insert into ssr_data(downloadurl,SSRURL,server,server_port)values(?,?,?,?)";
                Object args[] = {sn.getDownloadurl(), sn.getSSRURL(), sn.getServer(), sn.getServer_port()};
                int temp = jdbcTemplate.update(sql, args);
                if (temp > 0) {
                    //    System.out.println("user插入成功！");
                } else {
                    System.out.println("插入失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("失败连接：" + sn.getSSRURL());

            }


        }
        System.out.println("插入完成");
    }


    @RequestMapping(value = "/do4039", method = {RequestMethod.GET})
    @ResponseBody
    public void do4039() {

        try {
            List<SSRNode> okNodeList = new ArrayList<>();
            List<SSRNode> pingokNodeList = new ArrayList<>();

            //去重专用
            List<SSRNode> personList = new ArrayList<>();

            List<String> specialURL = new ArrayList<>();
            //文件编码
            String encoding = "UTF-8";
            //指定文件地址
            File file = new File("C:/sst/20190728_151810.text");
            //存储文件内容
            StringBuilder sb = new StringBuilder();
            //判断文件是否存在
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String ssrurl = null;
                while ((ssrurl = bufferedReader.readLine()) != null) {

                    System.out.println("文件:" + ssrurl);
                    String[] urlArray;

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
                                            ssrNode.setDownloadurl("");
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
                                        } else if (len >= 4) {

                                            //下载链接
                                            ssrNode.setDownloadurl("");
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
                            String urlString = ssrurl;
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
                                        ssrNode.setDownloadurl("");
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

                                        //下载链接
                                        ssrNode.setDownloadurl("");
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

                        String urlString = ssrurl;
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
                                ssrNode.setDownloadurl("");

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
                    //  sb.append(lineTxt + "\n");
                }
                System.out.println(okNodeList);
                ExecutorService executorServiceCN2 = Executors.newFixedThreadPool(500);

                //判断是否cn2
                Set<SSRNode> isCN2set = new HashSet<>();

                for (SSRNode sn : okNodeList) {
                   executorServiceCN2.submit(
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    Boolean isCN2 = TraceIP.traceip(sn.getServer());
                                    if (isCN2) {
                                       // isCN2set.add(sn);
                                    insertyidong(sn.getSSRURL(),sn.getServer(),String.valueOf(sn.getServer_port()));
                                    }
                               }
                            })
                    );

                }
               // executorServiceCN2.shutdown();

                //等待所有任务都执行结束
        // if (executorServiceCN2.isTerminated()) {
                 /*   System.out.println("cn2判断完毕");
                    String shuchu = "";
                    for (SSRNode sn : isCN2set) {
                        shuchu = shuchu + sn.getSSRURL() + "\n";

                    }
                    createFile("isCN2", shuchu);*/

        //}
        read.close();
    } else {
        System.out.println("找不到指定的文件");
    }
    String result = sb.toString();
} catch (IOException e) {
        e.printStackTrace();
        }

        }


public void insertyidong(String SSRURL,String server,String server_port) {



            try {
                String sql = "insert into china_yidong (downloadurl,SSRURL,server,server_port)values(?,?,?,?)";
                Object args[] = {"", SSRURL, server, server_port};
                int temp = jdbcTemplate.update(sql, args);
                if (temp > 0) {
                    //    System.out.println("user插入成功！");
                } else {
                    System.out.println("插入失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("失败连接：" +SSRURL);

            }



        System.out.println("插入完成");
    }


}
