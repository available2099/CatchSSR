package com.readbook.book.ssr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class TraceIP {
    private static String tracert = "tracert -h 30 "; //模拟tracert命令

    public static Boolean traceip(String ip) {
        tracert = tracert + " " + ip;
        try {
            return   command(tracert);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static Boolean command(String tracerCommand) throws IOException {
        //第一步：创建进程(是接口不必初始化)
        boolean ipcheckcn2 = false;

        //1.通过Runtime类的getRuntime().exec()传入需要运行的命令参数

        Process process = Runtime.getRuntime().exec(tracerCommand);

        try {
            //执行命令
            //   p = Runtime.getRuntime().exec(cmd);
            //取得命令结果的输出流
            InputStream fis = process.getInputStream();
            //用一个读输出流类去读
            //  InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            //将字节流转为字符流并建立读缓存区
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));

            //   BufferedReader br=new BufferedReader(isr,"UTF-8");
            String line = null;
            Set<String> ipset = new HashSet<>();
            //直到读完为止
            while ((line = br.readLine()) != null) {
                String[] ip = line.split(" ");
                if (ip[ip.length - 1].contains(".")) {
                    ipset.add(ip[ip.length - 1]);
                }
                System.out.println(line);

            }
            for (String ips : ipset) {
                if (ips.indexOf("59.43.") != -1) {
                    ipcheckcn2 = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        process.destroy();
        return ipcheckcn2;
    }

    public static void main(String args[]) {
        Boolean heh=  traceip("demobbs.top");
        System.out.println(heh);
    }
}
