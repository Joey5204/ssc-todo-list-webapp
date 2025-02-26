package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        securityService.logout(request);
        response.sendRedirect("login");
    }

    @Override
    public String getMapping() {
        return "/logout";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
