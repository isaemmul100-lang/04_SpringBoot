package org.example.intro.chap03;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/chap03/forward")
public class ForwardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 쿼리스트링 담을 수 있음
        String message = req.getParameter("message");

        req.setAttribute("moveType", "FORWARD");
        req.setAttribute("message", message);

        req.getRequestDispatcher("/chap03/result").forward(req, resp);
    }
}
