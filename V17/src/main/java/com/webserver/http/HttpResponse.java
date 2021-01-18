package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象,当前类的每一个实例用于表示服务端发送给客户端的一个标准的
 * HTTP响应内容
 * 每个响应由三部分构成:状态行,响应头,响应正文
 */
public class HttpResponse {
    //状态行相关信息
    private int statusCode = 200;//状态代码,默认值200
    private String statusReason = "OK";//状态描述,默认值OK

    //响应头相关信息
    private Map<String, String> headers = new HashMap<>();


    //响应正文相关信息
    private File entity;
    private byte[] contentData;//直接将一组字节作为正文内容

    private Socket socket;

    public HttpResponse(Socket socket) {
        this.socket = socket;


    }

    /**
     * 将当前响应对象内容以标准的HTTP响应格式发送给客户端
     */

    public void flush() {
        //发送状态行
        sendStatusLine();
        //发送响应头
        sendHeaders();
        //发送响应正文
        sendContent();
    }

    /**
     * 发送状态行
     */
    private void sendStatusLine() {
        System.out.println("HttpResponse:开始发送状态行...");
        try {
            OutputStream osw = socket.getOutputStream();
            String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
            osw.write(line.getBytes("ISO8859-1"));
            osw.write(13);
            osw.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpResponse:状态行发送完毕!");
    }

    /**
     * 发送响应头
     */
    private void sendHeaders() {
        System.out.println("HttpResponse:开始发送响应头...");
        try {
            OutputStream osw = socket.getOutputStream();

            //发送响应头
//            String line="Content-Type:text/html";
//            osw.write(line.getBytes("ISO8859-1"));
//            osw.write(13);
//            osw.write(10);
//
//            line="Content-Length:"+entity.length();
//            osw.write(line.getBytes("ISO8859-1"));
//            osw.write(13);
//            osw.write(10);

            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Map.Entry<String, String> e : set) {
                String name = e.getKey();
                String value = e.getValue();
                String line = name + ": " + value;
                osw.write(line.getBytes("ISO8859-1"));
                osw.write(13);
                osw.write(10);
                System.out.println("响应头:" + line);
            }

            //单独发送CRLF表示响应头发送完毕
            osw.write(13);
            osw.write(10);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpResponse:响应头发送完毕!");
    }

    /**
     * 发送响应正文
     */
    private void sendContent() {
        System.out.println("HttpResponse:开始发送响应正文...");
        if (contentData != null) {
            try {
                OutputStream os = socket.getOutputStream();
                os.write(contentData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (entity != null) {
            try (
                    FileInputStream fis = new FileInputStream(entity);
            ) {
                OutputStream osw = socket.getOutputStream();

                int len;
                byte[] data = new byte[1024 * 10];
                while ((len = fis.read(data)) != -1) {
                    osw.write(data,0,len);


                }
                System.out.println("响应发送完毕!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("HttpResponse:响应正文发送完毕!");
    }

    public File getEntity() {
        return entity;
    }

    /**
     * 设置响应正文的文件,设置的同时会根据该文件添加两个响应头:Content-Type和Content-Length
     *
     * @param entity
     */

    public void setEntity(File entity) {
        this.entity = entity;
        //logo.png
        //获取到文件名中最后一个点之后第一个字符的位置
        int index = entity.getName().lastIndexOf(".") + 1;
        //截取文件后缀名
        String ext = entity.getName().substring(index);
        String mime = HttpContext.getMimeType(ext);
        putHeader("Content-Type", mime);
        putHeader("Content-Length", entity.length() + "");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public byte[] getContentData() {
        return contentData;
    }

    /**
     * 将一组字节作为响应正文,设置的同时会自动包含Content-length头
     *
     * @param contentData
     */

    public void setContentData(byte[] contentData) {
        this.contentData = contentData;
        putHeader("Content-Length", contentData.length + "");
    }

    /**
     * 添加一个响应头
     *
     * @param name  响应头的名字
     * @param value 响应头的值
     */
    public void putHeader(String name, String value) {
        this.headers.put(name, value);
    }

    ;


}