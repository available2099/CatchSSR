package com.readbook.book.Util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Unzip {
    byte doc[]=null;
    String filename=null;
    String unzipath=null;
    public Unzip(String filename,String unzipath)
    {
        this.filename=filename;
        this.unzipath=unzipath;
    }
    public void dounzip()
    {
        try{
            //这里filename是文件名，如xxx.zip
            ZipInputStream zipis=new ZipInputStream(new FileInputStream(filename));
            ZipEntry fentry=null;
            while((fentry=zipis.getNextEntry())!=null)
            {
                //fentry逐个读取zip中的条目，第一个读取的名称为test。
                //test条目是文件夹，因此会创建一个File对象，File对象接收的参数为地址
                //然后就会用exists,判断该参数所指定的路径的文件或者目录是否存在
                //如果不存在，则构建一个文件夹；若存在，跳过
                //如果读到一个zip，也继续创建一个文件夹，然后继续读zip里面的文件，如txt
                if(fentry.isDirectory()){
                    File dir = new File(unzipath+fentry.getName());
                    if(!dir.exists()){
                        dir.mkdirs();
                    }
                }
                else {
                    //fname是文件名,fileoutputstream与该文件名关联
                    String fname=new String(unzipath+fentry.getName());
                    try{
                        //新建一个out,指向fname，fname是输出地址
                        FileOutputStream out = new FileOutputStream(fname);
                        doc=new byte[512];
                        int n;
                        //若没有读到，即读取到末尾，则返回-1
                        while((n=zipis.read(doc,0,512))!=-1)
                        {
                            //这就把读取到的n个字节全部都写入到指定路径了
                            out.write(doc,0,n);
//                            System.out.println(n);
                        }
                        out.close();
                        out=null;
                        doc=null;
                    }catch (Exception ex) {
                        System.out.println("there is a problem");
                    }
                }
            }
            zipis.close();
        }catch (IOException ioex){ System.out.println("io错误："+ioex);}
        System.out.println("finished!");
    }
    public static void main(String[]args)throws IOException,ClassNotFoundException{
        String zipFile=args[0];
        String unzipPath=args[1];
        Unzip myzip=new Unzip(zipFile,unzipPath);
        myzip.dounzip();
    }
}

