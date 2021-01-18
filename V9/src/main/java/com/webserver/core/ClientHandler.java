package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);
            //2 处理请求
            String path = request.getUrl();
            File file = new File("./webapps" + path);
            System.out.println("path:" + path);
            //判断用户请求的资源是否存在并且还要求定位的是文件!
            if (file.exists() && file.isFile()) {
                response.setEntity(file);
                /*
                如何根据用户请求的资源文件(file对象表示的文件)来设置
                Content-Type对应的值?
                提示:要根据用户请求的资源文件的后缀来设置这个值,参考http.txt
                文件最后提供的6种
                 */
                Map<String, String> map = new HashMap<>();

                map.put("html", "text/html");
                map.put("css", "text/css");
                map.put("js", "application/javascript");
                map.put("png", "image/png");
                map.put("gif", "image/gif");
                map.put("jpg", "image/jpeg");
                int index = file.getName().lastIndexOf(".")+1;
                String str = file.getName().substring(index);
                String mime = map.get(str);

                response.putHeader("Content-Type", mime);
                response.putHeader("Content-Length", file.length() + "");

            } else {
                File notFoundPage = new File("./webapps/root/404.html");
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                response.setEntity(notFoundPage);
                response.putHeader("Content-Type", "text/html");
                response.putHeader("Content-Length", notFoundPage.length() + "");


            }
            //响应客户端
            response.flush();


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


            //单独捕获空请求异常,不需要做任何处理,目的仅是忽略处理工作
        } catch (EmptyRequestException e) {

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

//    private String readLine() throws IOException {
//        /*
//        同一个socket对象,无论调用多少次getInputStream方法
//        获取到的输入流都是同一个
//         */
//        InputStream in = socket.getInputStream();
//        int l;
//        //cur表示本次读取的字符,pre表示上次读取的字符
//        char cur = 'a', pre = 'a';
//        //记录读取到的一行字符串
//        StringBuilder builder = new StringBuilder();
//        while ((l = in.read()) != -1) {
//            cur = (char) l;
//
//
//            //如果上次读取的是回车符并且本次读取的是换行符就停止
//            if (pre == 13 && cur == 10) {
//                break;
//            }
//            builder.append(cur);
//            pre = cur;
//        }
//        return builder.toString().trim();
//    }

}
