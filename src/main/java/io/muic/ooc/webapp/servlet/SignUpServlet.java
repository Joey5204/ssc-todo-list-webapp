package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;
import io.muic.ooc.webapp.service.UserServiceException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet implements Routable {

    private UserService userService = UserService.getInstance();

    private SecurityService securityService; // Store SecurityService if needed

    @Override
    public String getMapping() {
        return "/signup";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService; // Optional, might be useful later
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/signup.jsp");
        rd.include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String displayName = request.getParameter("displayName");

        if (username == null || password == null || displayName == null || username.isEmpty() || password.isEmpty() || displayName.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/signup.jsp");
            rd.include(request, response);
            return;
        }

        if (userService.findUser(username) != null) {
            request.setAttribute("error", "Username already exists.");
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/signup.jsp");
            rd.include(request, response);
            return;
        }

        try {
            userService.createUser(username, password, displayName);
            response.sendRedirect("login");
        } catch (UserServiceException e) {
            request.setAttribute("error", "Error creating account: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/signup.jsp");
            rd.include(request, response);
        }
    }
}
