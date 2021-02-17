package com.iths;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

    String tempFirst;
    String tempLast;

    public void doPost(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("teeeest");

        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("usermame: " +  userName + "password: " + password);

        tempFirst = request.getParameter("username");
        tempLast = request.getParameter("password");
    }
}