package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
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
            if (line.isEmpty()){
                throw new EmptyRequestException();
            }
            methood = line.split(" ")[0];
            url = line.split(" ")[1];
            protocol = line.split(" ")[2];
            System.out.println("methood:"+methood);
            System.out.println("url:"+url);
            System.out.println("protocol:"+protocol);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }                         //解析请求行

    private void parseHeaders() {
        try {
            String line;
            while (!(line = readLine()).isEmpty()) {
                headers.put(line.split(": ")[0], line.split(": ")[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }                            //解析消息头

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
        System.out.println("请求行:"+builder);
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
}
