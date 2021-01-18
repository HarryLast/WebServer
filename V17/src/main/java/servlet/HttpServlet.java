package servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 该类是所有Servlet的超类
 */
public abstract class HttpServlet {
    public abstract void service(HttpRequest request, HttpResponse response);

}
