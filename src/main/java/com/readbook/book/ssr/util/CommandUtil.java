package com.readbook.book.ssr.util;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import java.util.Scanner;

public class CommandUtil {
    String tracert = "tracert -h 30 "; //模拟tracert命令

    String ping = "ping";//模拟 ping 命令

    String routePrint = "route print -4";//模拟route print命令

    /* 注意：这边我是用while循环主要实现tracert以及ping命令要是读者需要
     完全重写dos命令行模式则需要使用多线程并采用同步原理，虽然工程量大
     但是实现原理并不难，读者可自行尝试。
 */
    public static void main(String args[]) {

        String input = null;

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        //利用while循环接收输入的命令行参数
        while (true) {

            System.out.println("Please input destination server IP address ：\n");

            input = scanner.next();

            CommandUtil host = new CommandUtil();

            host.tracert = host.tracert + " " + input;

            host.ping = host.ping + " " + input;


            try {
                host.command(host.routePrint);


                host.command(host.tracert);


                host.command(host.ping);


                InetAddress ipAddress;


                ipAddress = InetAddress.getByName(input);

                System.out.println("IP address : " + ipAddress);


                URL url;


                url = new URL("http", input, 80, "index.html");

                System.out.println();//输出服务器地址

                System.out.println("Get the Server-Name# : " + url.getHost());

                System.out.println();//输出首页文件

                System.out.println("Get the default file# : " + url.getFile());

                System.out.println();//输出首页协议和端口

                System.out.println("Get the protocol# : " + url.getProtocol() + " " + url.getPort());

                System.out.println();


                System.out.println();

                System.out.println("Get serverName & IPAddress：" + InetAddress.getByName(input));


                long freeMemory = Runtime.getRuntime().freeMemory();

                System.out.println("Surplus memory of JVM: " + freeMemory + "B");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    //模拟 tracert 命令

   /* 注意这边将跃点数定为10个，也可以不在内部定义，而在命令输入时确定
    为了方便起见，直接将ping命令和tracert命令封装在一起，
    这样子做的好处就是既能够显示每一个数据报经过的路由，也能显示是否到达以及耗时
    可以在ping命令中自己设置TTL值以及要发送的数据包的数量，读者自己选择即可
*/


    StringBuffer commandResult = null;

    private void command(String tracerCommand) throws IOException {
        //第一步：创建进程(是接口不必初始化)

        //1.通过Runtime类的getRuntime().exec()传入需要运行的命令参数

        System.out.println();

        System.out.println(InetAddress.getByName("localhost") + " is tracking the destination server...");

        Process process = Runtime.getRuntime().exec(tracerCommand);

      /*  BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String answer = "";

        try {
            System.out.println("inputstream ready: "+input.ready());
            answer+=input.readLine();
            System.out.println("process answer:  "+answer);
           // input.close();

        } catch(Exception e) {
            System.out.print(e.getMessage());
        }*/
        try
        {
            //执行命令
         //   p = Runtime.getRuntime().exec(cmd);
            //取得命令结果的输出流
            InputStream fis=process.getInputStream();
            //用一个读输出流类去读
          //  InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            //将字节流转为字符流并建立读缓存区
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,"GBK"));

         //   BufferedReader br=new BufferedReader(isr,"UTF-8");
            String line=null;
            //直到读完为止
            while((line=br.readLine())!=null)
            {
                System.out.println(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    //  readResult(process.getInputStream());

        process.destroy();
    }
    //第二步：通过输入流来将命令执行结果输出到控制台

    private void readResult(InputStream inputStream) throws IOException {


        commandResult = new StringBuffer();  //初始化命令行

        String commandInfo = null; //定义用于接收命令行执行结果的字符串

     /*   if (inputStream != null) {
// ByteArrayOutputStream好处：边读，边缓冲数据
// 可以捕获内存缓冲区的数据，转换成字节数组。
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int temp = -1;
            while ((temp = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, temp);
            }
       //     inputStream.close();
        //    baos.close();
            commandInfo = baos.toString();
        }

        System.out.println("heh:"+commandInfo);*/


        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(inputStream));

        while ((commandInfo = bufferedReader.readLine()) != null) {

            System.out.println(commandInfo);
        }
        bufferedReader.close();
    }
}
