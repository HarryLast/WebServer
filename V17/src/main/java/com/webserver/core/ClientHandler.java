package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpContext;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import servlet.HttpServlet;
import servlet.LoginServlet;
import servlet.RegServlet;
import servlet.ShowAllUserServlet;

import javax.xml.ws.Service;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 每个客户端连接后都会启动一个线程来完成与该客户端的交互.
 * 交互过程遵循HTTP协议的一问一答要求,分三步进行处理.
 * 1.解析请求
 * 2.处理请求
 * 3.响应客户端
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
            String path = request.getRequestURI();
            HttpServlet serevlet=ServerContext.getServlet(path);

            //首先根据请求路径判断是否为请求业务
            if (serevlet!=null) {
                serevlet.service(request,response);
            }else {
                File file = new File("./webapps" + path);
                System.out.println("path:" + path);
                //判断用户请求的资源是否存在并且还要求定位的是文件!
                if (file.exists() && file.isFile()) {
                    response.setEntity(file);
                } else {
                    File notFoundPage = new File("./webapps/root/404.html");
                    response.setStatusCode(404);
                    response.setStatusReason("NotFound");
                    response.setEntity(notFoundPage);
                }
            }
            //告知浏览器我们服务端是谁
            response.putHeader("Server", "WebServer");
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


}
