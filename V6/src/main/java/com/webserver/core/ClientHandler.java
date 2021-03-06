package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;

import java.io.*;
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
            HttpRequest request=new HttpRequest(socket);
            //2 处理请求
            String path=request.getUrl();
            File file=new File("./webapps"+path);
            if(file.exists()){
                OutputStream osw=socket.getOutputStream();
                //发送状态行
                String line="HTTP/1.1 200 OK";
                osw.write(line.getBytes("ISO8859-1"));
                osw.write(13);
                osw.write(10);
                //发送响应头
                line="Content-Type:text/html";
                osw.write(line.getBytes("ISO8859-1"));
                osw.write(13);
                osw.write(10);

                line="Content-Length:"+file.length();
                osw.write(line.getBytes("ISO8859-1"));
                osw.write(13);
                osw.write(10);
                //单独发送CRLF表示响应头发送完毕
                osw.write(13);
                osw.write(10);
                //发送响应正文
                FileInputStream fis=new FileInputStream(file);
                int len;
                byte[] data=new byte[1024*10];
                while((len=fis.read(data))!=-1){
                    osw.write(data,0,len);

                }
                System.out.println("响应发送完毕!");

            }else{
                OutputStream osw=socket.getOutputStream();
                file=new File("./webapps/root/404.html");

                String line="HTTP/1.1 404 NotFound";
                osw.write(line.getBytes("ISO8859-1"));
                osw.write(13);
                osw.write(10);

                line="Content-Type:text/html";
                osw.write(line.getBytes("ISO8859-1"));
                osw.write(13);
                osw.write(10);

                line="Content-Length:"+file.length();
                osw.write(line.getBytes("ISO8859-1"));
                osw.write(13);
                osw.write(10);

                osw.write(13);
                osw.write(10);

                FileInputStream fis=new FileInputStream(file);
                int len;
                byte[] data=new byte[1024*10];
                while((len=fis.read(data))!=-1){
                    osw.write(data,0,len);

                }
                System.out.println("响应发送完毕!");

            }





            //3 响应客户端
            /*
            发送一个标准的HTTP响应,将刚才写好的页面:
            ./webapps/myweb/index.html

            响应的格式
            HTTP/1.1 200 OK(CRLF)
            Content-Type:text/html(CRLF)
            Content-Length:2546(CRLF)(CRLF)
            1011101010101011.....

             */
            OutputStream osw=socket.getOutputStream();

            String line="HTTP/1.1 200 OK(CRLF)";
            osw.write(line.getBytes("ISO8859-1"));
            osw.write(13);
            osw.write(10);

            line="Content-Type:text/html(CRLF)";
            osw.write(line.getBytes("ISO8859-1"));
            osw.write(13);
            osw.write(10);

            line="Content-Length:2546(CRLF)(CRLF)";
            osw.write(line.getBytes("ISO8859-1"));
            osw.write(13);
            osw.write(10);

            osw.write(13);
            osw.write(10);

            FileInputStream fis=new FileInputStream(file);
            int len;
            byte[] data=new byte[1024*10];
            while((len=fis.read(data))!=-1){
                osw.write(data,0,len);

            }
            System.out.println("响应发送完毕!");



        //单独捕获空请求异常,不需要做任何处理,目的仅是忽略处理工作
        }catch(EmptyRequestException e){

        } catch(Exception e) {
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
