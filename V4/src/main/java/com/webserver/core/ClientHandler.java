package com.webserver.core;

import com.webserver.http.HttpRquest1;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 每个客户端连接后都会启动一个线程来完成与该客户端的交互.
 * 交互过程遵循HTTP协议的一问一答要求,分三步进行处理.
 * 1.解析请求
 * 2.处理请求
 * 3.相应客户端
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            //1 解析请求
            HttpRquest1 hr=new HttpRquest1(socket);

            //2 处理请求

            //3 响应客户端

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最终交互完毕后与客户端断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readLine() throws IOException {
        /*
        同一个socket对象,无论调用多少次getInputStream方法
        获取到的输入流都是同一个
         */
        InputStream in = socket.getInputStream();
        int l;
        //cur表示本次读取的字符,pre表示上次读取的字符
        char cur = 'a', pre = 'a';
        //记录读取到的一行字符串
        StringBuilder builder = new StringBuilder();
        while ((l = in.read()) != -1) {
            cur = (char) l;


            //如果上次读取的是回车符并且本次读取的是换行符就停止
            if (pre == 13 && cur == 10) {
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }

}
