package org.example.intro.chap01;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/life-cycle") // localhost/life-chcle 에 요청이 왔을 때 실행
public class LifeCycleTest extends HttpServlet {

    private int initCount = 0;
    private int serviceCount = 0;
    private int destroyCount = 0;

    // init() : 최초 서블릿 요청 시 호출되는 메소드
    @Override
    public void init(ServletConfig config) throws ServletException {
        initCount += 1;
        System.out.println("init() => " + initCount);
    }

    // service() : 서블릿 컨테이너에 의해 호출되고 최초 요청 시 init() 다음에 동작
    // 그리고 이후 요청을 받을 때마다 service() 동작
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        serviceCount += 1;
        System.out.println("service() => " + serviceCount);
    }

    // destroy() : 서블릿 컨테이너가 종료될 때 호출되는 메소드
    @Override
    public void destroy() {
        destroyCount += 1;
        System.out.println("destroy() => " + destroyCount);
    }
}
