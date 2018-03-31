package ru.ssau.daria;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/sampleServlet")
public class SampleServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("Hi!<br>");

        String h = request.getParameter("horizontal");
        String v = request.getParameter("vertical");
        String theme = request.getParameter("theme");

        out.println("horizontal = " +  h);
        out.println("<br>");
        out.println("vertical = " +  v);
        out.println("<br>");
        out.println("theme = " + theme);

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
