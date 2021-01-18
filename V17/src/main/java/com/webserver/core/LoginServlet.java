package com.webserver.core;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("LoginServlet:开始处理登录...");
        //1获取登录信息
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        if(username==null||password==null){
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);
            return;
        }
        //2处理登录
        try(
                RandomAccessFile raf = new RandomAccessFile("user.dat","r");
        ){
            for(int i=0;i<raf.length()/100;i++){
                //先将指针移动到该条记录的开始位置
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data,"UTF-8");
                if(name.equals(username)){
                    //找到该用户了，此时再比对密码
                    raf.read(data);
                    String pwd = new String(data,"UTF-8");
                    if(pwd.equals(password)){
                        //登录成功
                        File file = new File("./webapps/myweb/login_success.html");
                        response.setEntity(file);
                        return;
                    }
                    //执行到这里说明用户名对了，但是密码不对
                    break;//停止读取工作，因为没有重复记录
                }
            }
            //设置登录失败页面
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);


        }catch(IOException e){
            e.printStackTrace();
        }




        System.out.println("LoginServlet:处理登录完毕!");
    }
}
