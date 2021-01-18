package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示HTTP的一个请求
 * 每个请求由三部分构成:请求行,消息头,消息正文
 */
public class HttpRquest1 {


    //请求行相关信息
    //请求行中的请求方式
    private String method;
    //抽象路径部分
    private String uri;
    //协议版本
    private String protocol;

    //消息头相关信息
    Map<String, String> headers = new HashMap<>();

    //消息正文相关信息
    private Socket socket;

    /**
     * 构造方法,在实例化HttpRequest的同时完成了解析请求的工作
     */

    public HttpRquest1(Socket socket) throws EmptyRequestException {
        this.socket = socket;
        /*
        1:解析请求行
        2:解析消息头
        3:解析消息正文
         */
        parseRequestLine();
        parseHeaders();
        parseContent();

    }

    private void parseRequestLine() throws EmptyRequestException {//请求行
        try {
            System.out.println("HttpRequest:解析请求行...");
            String str1 = readLine();
            System.out.println("请求行"+str1);
    //        如果读取请求行内容返回时空串,说明本次为空请求!
            if(str1.isEmpty()){
                throw new EmptyRequestException();
            }
            String[] arr1 = str1.split(" ");
            method = arr1[0];
            uri = arr1[1];
            protocol = arr1[2];
            System.out.println("method:" + method);
            System.out.println("uri:" + uri);
            System.out.println("protocol:" + protocol);
            System.out.println("请求行解析完毕!");

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void parseHeaders() {//消息头
        System.out.println("HttpRequest:解析消息头...");
        Map<String, String> map = new HashMap<>();
        while (true) {
            String str2 = null;
            try {
                str2 = readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (str2.isEmpty()) {
                break;
            }
            System.out.println("消息头:" + str2);
            String[] arr2 = str2.split("/ ");
            map.put(arr2[0], arr2[1]);
            System.out.println("消息头:" + str2);

        }

        System.out.println("HttpRequest:消息头解析完毕!");
    }

    private void parseContent() {
        System.out.println("HttpRequest:解析消息正文...");


        System.out.println("HttpRequest:消息正文解析完毕!");


    }


    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String readLine() throws IOException {

        InputStream is = socket.getInputStream();
        int l;
        char cur = 'a';
        char pre = 'a';
        StringBuilder sb = new StringBuilder();
        while ((l = is.read()) != -1) {
            cur = (char) l;
            if (pre == 13 && cur == 10) {
                break;

            }
            sb.append(cur);
            cur = pre;

        }
        return sb.toString().trim();


    }

}







