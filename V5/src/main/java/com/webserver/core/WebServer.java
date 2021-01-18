package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer主类
 */
public class WebServer {
    private ServerSocket serverSocket;

    /**
    构造器,用于初始化
     */

    public WebServer(){
       try{
           System.out.println("正在启动服务端...");
           serverSocket=new ServerSocket(8088);
           System.out.println("服务端启动完毕!");
       }catch(IOException e){
           e.printStackTrace();
       }
    }

    /**
     * 服务端开始工作的方法
     */
    public void start(){
        try {
            System.out.println("等待客户端链接...");
            Socket socket=serverSocket.accept();
            System.out.println("一个客户端链接了!");

            /*
            如果读取客户端发送过来的内容是乱码,通常是因为浏览器地址栏中
            输入的是https协议!!
            要使用http协议才可以:
            http://localhost:8088
             */



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        WebServer server=new WebServer();
        server.start();

    }

}
