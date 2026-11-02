package org.example.filterandintercepter.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class StopWatchFilter implements Filter {


    // Filter 객체가 생성되고 Servlet Container에 등록될 때 호출됨
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[1. Filter] init - 어플리케이션 시작 시 한 번 호출");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        /*chian.doFilter()를 호출하기 전은 Filter의 전처리 영역*/
        /*
        * ServletRequest와 ServletResponse : HTTP 전용 아님
        * -> URI, HEADER, 상태코드(HTTP) 을 사용하기 위해 변환
        * */

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("httpReuqet.getRequsetURI() : " + httpRequest.getRequestURI());
        if(httpRequest.getRequestURI().equals("/filter/block") && httpRequest.getHeader("X-Test") == null) {

            System.out.println("/filter/block 요청이 들어옴...");

            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "X-Test 헤더가 필요합니다.");
            return; // chain.doFilter()를 호출하지 않으므로 이후 과정인 DispatcherServlet, Controller 동작 X
        }

        System.out.println("[2. Filter 동작] 전처리 시간 측정 시작");
        long startTime = System.currentTimeMillis(); // 호출 시작 시간 기록

        chain.doFilter(request, response); // 필터 다음 동작 실행

        System.out.println("[7. Filter 동작] 후처리");
        long endTime = System.currentTimeMillis(); // 로직 종료 후 시간 기록

        System.out.println("[7. Filter] - 전체 체인 수행 시간 : " + (endTime - startTime) + "ms");
    }

    @Override
    public void destroy() {
        System.out.println("99. [Filter] destroy - 어플리케이션 종료 시 한 번 호출");
    }
}
