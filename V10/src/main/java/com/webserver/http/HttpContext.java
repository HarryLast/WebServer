package com.webserver.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类维护所有HTTP协议中固定不变的内容
 */
public class HttpContext {
    //保存所有Content-Type头的值与资源后缀的对应关系
    private static Map<String,String> mimeMapping=new HashMap<>();

    static{
        //初始化所有静态资源
        initMimeMapping();
    }

    private static void initMimeMapping(){
        mimeMapping.put("html", "text/html");
        mimeMapping.put("css", "text/css");
        mimeMapping.put("js", "application/javascript");
        mimeMapping.put("png", "image/png");
        mimeMapping.put("gif", "image/gif");
        mimeMapping.put("jpg", "image/jpeg");
    }

    /**
     * 根据资源后缀名获取对应的Content-Type的值
     * @param ext   资源的后缀名
     * @return      Content-Type头的值
     */
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }



}
