package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示HTTp的一个请求
 * 每个请求有三部分构成:请求行,消息头,消息正文
 */
public class HttpRequest {
    //请求行相关信息
    private String methood;                                 //请求方式
    private String url;                                     //抽象路径
    private String protocol;                                //协议版本
    private Socket socket;
    private String requestURI;                              //保存uri中的请求部分("?"左侧内容)
    private String queryString;                             //保存uri中的请求部分("?"右侧内容)
    private Map<String, String> parameters = new HashMap<>();    //保存每一组参数,key为参数名,value为参数值


    private Map<String, String> headers = new HashMap<>();  //消息头相关信息

    /*
        通过构造器给请求方式(String)、抽象路径(String)、协议版本(String)以及
        消息头相关信息(Map)进行赋值
     */
    public HttpRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        parseRequsetLine();
        parseHeaders();
        parseContent();

    }

    private void parseRequsetLine() throws EmptyRequestException {
        try {
            String line = readLine();
            //如果读取请求行内容返回时空串,说明本次为空请求!
            if (line.isEmpty()) {
                throw new EmptyRequestException();
            }
            methood = line.split(" ")[0];
            url = line.split(" ")[1];
            protocol = line.split(" ")[2];

            //进一步解析uri
            parseUri();

            System.out.println("methood:" + methood);
            System.out.println("url:" + url);
            System.out.println("protocol:" + protocol);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }                         //解析请求行

    private void parseHeaders() {
        try {
            String line;
            while (!(line = readLine()).isEmpty()) {
                String[] str = line.split(": ");
                headers.put(str[0], str[1]);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进一步对uri进行拆分解析,因为uri可能包含参数.
     */
    private void parseUri() {
        System.out.println("HttpRequest:进一步解析uri...");
        /*
            对抽象路径解码(解决传递中文问题,将%xx的内容还原对应的文字)
         */

        try {
            /*
                URLDecoder提供的静态方法:
                static String decode(String str,String csn)
                将给定的字符串(第一个参数)中%xx这样的内容按照给定的字符集(第二个参数)还原为
                对应的文字并替换原有的%xx部分.将替换后的字符串返回.
             */
            url= URLDecoder.decode(url,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*
            uri可能存在两种情况:含有参数,不含有参数
            含有参数(uri中包含"?"):
            首先按照"?"将uri拆分为两部分,第一部分赋值给requestURI这个属性.第二部分赋值给
            queryString这个属性.
            '然后再将queryString(uri中的参数部分)进行进一步拆分,来得到每一组参数.
            首先将queryString按照"&"拆分每一组参数,然后每组参数在按照"="拆分为参数名为参数值
            之后将参数名作为key,参数值作为value保存到parameters这个Map中保存即可.

            不含有参数(uri中不包含"?")
            则直接将uri的值赋值给requestURI即可
         */
        if (url.contains("?")) {
            String[] str1 = url.split("\\?");
            requestURI = str1[0];
            if (str1.length > 1) {
                queryString = str1[1];
                String[] arr = queryString.split("&");
                for (String s : arr) {
                    String[] str2 = s.split("=");
                    if (str2.length > 1) {
                        parameters.put(str2[0], str2[1]);
                    } else {
                        parameters.put(str2[0], null);
                    }
                }

            }


        } else {
            requestURI = url;
        }


        System.out.println("HttpRequest:进一步解析uri玩毕!");
    }


    //解析消息头

    private void parseContent() {
    }                               //解析正文

    public String getHeader(String name) {
        return headers.get(name);
    }

    private String readLine() throws IOException {
        InputStream is = socket.getInputStream();
        int d;
        char cur = 'a', pre = 'a';
        StringBuilder builder = new StringBuilder();
        while ((d = is.read()) != -1) {
            cur = (char) d;
            if (cur == 10 && pre == 13) {
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        System.out.println("请求行:" + builder);
        return builder.toString().trim();

    }

    public String getMethood() {
        return methood;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameters(String name) {
        return parameters.get(name);
    }
}
