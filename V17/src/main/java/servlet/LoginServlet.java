package servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet extends HttpServlet{

    public void service(HttpRequest request, HttpResponse response) {
        System.out.println("开始处理登录...");

        String username = request.getParameters("username");
        String password = request.getParameters("password");
        if (username == null || password == null) {
            File file = new File("webapps/myweb/login_success.html");
            response.setEntity(file);

            return;

        }

        try (
            RandomAccessFile raf = new RandomAccessFile("user.dat", "r");
        ){
            for (int i = 0; i < raf.length()/100; i ++) {
                raf.seek(i * 100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data, "UTF-8").trim();
                if (name.equals(username)) {
                    raf.read(data);

                    String password1 = new String(data, "UTF-8").trim();
                    if (password1.equals(password)) {
                        File file = new File("webapps/myweb/login_fail.html");//成功
                        response.setEntity(file);
                        return;
                    }
                    break;//走到这里说明用户名对了但是密码不对,停止读取工作

                }

            }
            File file = new File("webapps/myweb/login_success.html");//失败
            response.setEntity(file);


        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
